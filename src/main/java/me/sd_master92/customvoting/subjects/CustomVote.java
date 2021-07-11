package me.sd_master92.customvoting.subjects;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Data;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.constants.Settings;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.*;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class CustomVote extends Vote
{
    private static boolean isAwaitingBroadcast = false;

    private final Main plugin;

    public CustomVote(Main plugin, Vote vote)
    {
        super();
        this.plugin = plugin;
        setUsername(vote.getUsername());
        setServiceName(vote.getServiceName());
        setAddress(vote.getAddress());
        setTimeStamp(vote.getTimeStamp());

        forwardVote();
    }

    public static void shootFirework(Main plugin, Location loc)
    {
        if (plugin.getConfig().getBoolean(Settings.FIREWORK))
        {
            World world = loc.getWorld();
            if (world != null)
            {
                Firework firework = (Firework) world.spawnEntity(loc, EntityType.FIREWORK);
                FireworkMeta fireworkMeta = firework.getFireworkMeta();
                Random random = new Random();
                Color[] colors = {Color.AQUA, Color.BLUE, Color.FUCHSIA, Color.GREEN, Color.LIME, Color.MAROON,
                        Color.NAVY,
                        Color.OLIVE, Color.ORANGE, Color.PURPLE, Color.RED, Color.TEAL,};
                FireworkEffect.Type[] fireworkEffects = {FireworkEffect.Type.BALL, FireworkEffect.Type.BALL_LARGE,
                        FireworkEffect.Type.BURST, FireworkEffect.Type.STAR};
                FireworkEffect effect =
                        FireworkEffect.builder()
                                .flicker(random.nextBoolean())
                                .withColor(colors[random.nextInt(colors.length)])
                                .withFade(colors[random.nextInt(colors.length)])
                                .with(fireworkEffects[random.nextInt(fireworkEffects.length)])
                                .trail(random.nextBoolean()).build();
                fireworkMeta.addEffect(effect);
                fireworkMeta.setPower(random.nextInt(2) + 1);
                firework.setFireworkMeta(fireworkMeta);
            }
        }
    }

    public static void create(Main plugin, String name, String service)
    {
        Vote vote = new Vote();
        vote.setUsername(name);
        vote.setServiceName(service);
        vote.setAddress("0.0.0.0");
        Date date = new Date();
        vote.setTimeStamp(String.valueOf(date.getTime()));
        plugin.getServer().getPluginManager().callEvent(new VotifierEvent(vote));
    }

    private void forwardVote()
    {
        Player player = Bukkit.getPlayer(getUsername());
        if (player == null)
        {
            queue();
        } else
        {
            new VoteFile(player.getUniqueId().toString(), plugin).addVote(true);
            broadcast();
            shootFirework(plugin, player.getLocation());
            giveRewards(player);
            if (plugin.getConfig().getBoolean(Settings.VOTE_PARTY))
            {
                subtractVotesUntilVoteParty();
            }
        }
    }

    private void queue()
    {
        PlayerFile playerFile = PlayerFile.getByName(getUsername(), plugin);
        if (playerFile != null)
        {
            new VoteFile(playerFile.getUuid(), plugin).addQueue(getServiceName());
        }
    }

    private void broadcast()
    {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%PLAYER%", getUsername());
        placeholders.put("%SERVICE%", getServiceName());
        String message = Messages.VOTE_BROADCAST.getMessage(plugin, placeholders);
        plugin.getServer().broadcastMessage(message);
    }

    private void subtractVotesUntilVoteParty()
    {
        if (plugin.getData().getLocations(Data.VOTE_PARTY).size() > 0)
        {
            int votesRequired = plugin.getConfig().getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY);
            int votesUntil = votesRequired - plugin.getData().getNumber(Data.CURRENT_VOTES);
            if (votesUntil <= 1)
            {
                plugin.getData().setNumber(Data.CURRENT_VOTES, 0);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        new VoteParty(plugin).start();
                    }
                }.runTaskLater(plugin, 40);
            } else
            {
                plugin.getData().addNumber(Data.CURRENT_VOTES);
                if (!isAwaitingBroadcast)
                {
                    isAwaitingBroadcast = true;
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            int updatedVotesUntil = votesRequired - plugin.getData().getNumber(Data.CURRENT_VOTES);
                            if (updatedVotesUntil != votesRequired)
                            {
                                Map<String, String> placeholders = new HashMap<>();
                                placeholders.put("%VOTES%", "" + updatedVotesUntil);
                                placeholders.put("%s%", updatedVotesUntil == 1 ? "" : "s");
                                plugin.getServer().broadcastMessage(Messages.VOTE_PARTY_UNTIL.getMessage(plugin, placeholders));
                            }
                            isAwaitingBroadcast = false;
                        }
                    }.runTaskLater(plugin, 40);
                }
            }
        }
    }

    private void giveRewards(Player player)
    {
        giveItems(player);
        giveLuckyReward(player);
        executeCommands(player);
        String rewardMessage = "";
        double money = giveMoney(player);
        if (Main.economy != null && money > 0)
        {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%MONEY%", new DecimalFormat("#.##").format(money));
            rewardMessage += Messages.VOTE_REWARD_MONEY.getMessage(plugin, placeholders);
        }
        int xp = giveExperience(player);
        if (xp > 0)
        {
            Map<String, String> placeholders = new HashMap<>();
            placeholders.put("%XP%", "" + xp);
            placeholders.put("%s%", xp == 1 ? "" : "s");
            rewardMessage += rewardMessage.isEmpty() ? "" :
                    Messages.VOTE_REWARD_DIVIDER.getMessage(plugin);
            rewardMessage += Messages.VOTE_REWARD_XP.getMessage(plugin, placeholders);
        }
        String message = rewardMessage;
        if (!message.isEmpty())
        {
            new BukkitRunnable()
            {
                int i = 40;

                @Override
                public void run()
                {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                            new TextComponent(Messages.VOTE_REWARD_PREFIX.getMessage(plugin) + message));
                    if (i == 0)
                    {
                        cancel();
                    }
                    i--;
                }
            }.runTaskTimer(plugin, 0, 1);
        }
    }

    private void giveItems(Player player)
    {
        for (ItemStack reward : plugin.getData().getItems(Data.ITEM_REWARDS))
        {
            for (ItemStack item : player.getInventory().addItem(reward).values())
            {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }
    }

    private double giveMoney(Player player)
    {
        Economy economy = Main.economy;
        if (economy != null && economy.hasAccount(player))
        {
            double amount = plugin.getConfig().getDouble(Settings.VOTE_REWARD_MONEY);
            economy.depositPlayer(player, amount);
            return amount;
        }
        return 0;
    }

    private int giveExperience(Player player)
    {
        int amount = plugin.getConfig().getNumber(Settings.VOTE_REWARD_EXPERIENCE);
        player.setLevel(player.getLevel() + amount);
        return amount;
    }

    private void giveLuckyReward(Player player)
    {
        Random random = new Random();
        int i = random.nextInt(100);
        if (i < plugin.getConfig().getNumber(Settings.LUCKY_VOTE_CHANCE))
        {
            ItemStack[] luckyRewards = plugin.getData().getItems(Data.LUCKY_REWARDS);
            if (luckyRewards.length > 0)
            {
                i = random.nextInt(luckyRewards.length);
                for (ItemStack item : player.getInventory().addItem(luckyRewards[i]).values())
                {
                    player.getWorld().dropItemNaturally(player.getLocation(), item);
                }
                player.sendMessage(Messages.VOTE_LUCKY.getMessage(plugin));
            }
        }
    }

    private void executeCommands(Player player)
    {
        for (String command : plugin.getData().getStringList(Data.VOTE_COMMANDS))
        {
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command.replace("%PLAYER%",
                    player.getName()));
        }
    }
}
