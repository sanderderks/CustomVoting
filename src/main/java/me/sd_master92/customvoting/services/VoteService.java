package me.sd_master92.customvoting.services;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;
import me.sd_master92.customfile.PlayerFile;
import me.sd_master92.customvoting.Main;
import me.sd_master92.customvoting.VoteFile;
import me.sd_master92.customvoting.constants.Messages;
import me.sd_master92.customvoting.constants.Settings;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class VoteService
{
    private final Main plugin;
    private final VotePartyService votePartyService;

    private boolean isAwaitingBroadcast = false;

    public VoteService(Main plugin)
    {
        this.plugin = plugin;
        votePartyService = new VotePartyService(plugin);
    }

    public static void shootFirework(Main plugin, Location loc)
    {
        if (plugin.getSettings().getBoolean(Settings.FIREWORK))
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

    public void fakeVote(String name, String service)
    {
        Vote vote = new Vote();
        vote.setUsername(name);
        vote.setServiceName(service);
        vote.setAddress("0.0.0.0");
        Date date = new Date();
        vote.setTimeStamp(String.valueOf(date.getTime()));
        plugin.getServer().getPluginManager().callEvent(new VotifierEvent(vote));
    }

    public void fakeVote(String name)
    {
        fakeVote(name, "fakevote.com");
    }

    public void queueVote(Vote vote)
    {
        PlayerFile playerFile = PlayerFile.getByName(vote.getUsername(), plugin);
        if (playerFile != null)
        {
            new VoteFile(playerFile.getUuid(), plugin).addQueue(vote.getServiceName());
        }
    }

    public void forwardVote(Player player, Vote vote)
    {
        new VoteFile(player.getUniqueId().toString(), plugin).addVote(true);
        broadcastVote(vote);
        shootFirework(plugin, player.getLocation());
        giveRewards(player);
        subtractVotesUntilVoteParty();
    }

    public void broadcastVote(Vote vote)
    {
        HashMap<String, String> placeholders = new HashMap<>();
        placeholders.put("%PLAYER%", vote.getUsername());
        placeholders.put("%SERVICE%", vote.getServiceName());
        String message = plugin.getMessages().getMessage(Messages.BROADCAST, placeholders);
        plugin.getServer().broadcastMessage(message);
    }

    public void subtractVotesUntilVoteParty()
    {
        if (plugin.getData().getLocations("voteparty").size() > 0)
        {
            int votesRequired = plugin.getSettings().getNumber(Settings.VOTES_REQUIRED_FOR_VOTE_PARTY);
            int votesUntil = votesRequired - plugin.getData().getNumber("current_votes");
            if (votesUntil <= 1)
            {
                plugin.getData().setNumber("current_votes", 0);
                new BukkitRunnable()
                {
                    @Override
                    public void run()
                    {
                        votePartyService.start();
                    }
                }.runTaskLater(plugin, 40);
            } else
            {
                plugin.getData().addNumber("current_votes");
                if (!isAwaitingBroadcast)
                {
                    isAwaitingBroadcast = true;
                    new BukkitRunnable()
                    {
                        @Override
                        public void run()
                        {
                            int updatedVotesUntil = votesRequired - plugin.getData().getNumber("current_votes");
                            if(updatedVotesUntil != votesRequired)
                            {
                                Map<String, String> placeholders = new HashMap<>();
                                placeholders.put("%VOTES%", "" + updatedVotesUntil);
                                plugin.getServer().broadcastMessage(plugin.getMessages().getMessage(Messages.VOTE_PARTY_UNTIL,
                                        placeholders));
                                isAwaitingBroadcast = false;
                            }
                        }
                    }.runTaskLater(plugin, 40);
                }
            }
        }
    }

    public void giveRewards(Player player)
    {
        for (ItemStack reward : plugin.getData().getItems("rewards"))
        {
            for (ItemStack item : player.getInventory().addItem(reward).values())
            {
                player.getWorld().dropItemNaturally(player.getLocation(), item);
            }
        }
    }
}
