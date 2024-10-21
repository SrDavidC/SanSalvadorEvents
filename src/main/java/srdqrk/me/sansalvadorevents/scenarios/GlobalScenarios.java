package srdqrk.me.sansalvadorevents.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class GlobalScenarios {

    private List<Player> players;

    private BukkitRunnable glowingTask;

    private JavaPlugin plugin;

    public GlobalScenarios(JavaPlugin plugin) {
        this.plugin = plugin;
        this.players = new ArrayList<>(Bukkit.getOnlinePlayers()); // TODO: remove for customized players list
    }

    public void startGlowingCycle() {
        this.players = new ArrayList<>(Bukkit.getOnlinePlayers()); // TODO REMOVE THIS
        glowingTask = new BukkitRunnable() {
            private boolean glowing = false;

            @Override
            public void run() {
                if (glowing) {
                    for (Player player : players) {
                        System.out.println(player.getName());
                        player.removePotionEffect(PotionEffectType.GLOWING);
                    }
                    glowing = false;
                } else {
                    for (Player player : players) {
                        System.out.println(player.getName());
                        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200,0));
                    }
                    glowing = true;
                }
            }
        };
        glowingTask.runTaskTimer(plugin, 0, 200L);
    }
    public void cancelGlowingCycle() {
        if (glowingTask != null && !glowingTask.isCancelled()) {
            glowingTask.cancel();
            for (Player player : players) {
                if (player.isOnline()) {
                    player.removePotionEffect(PotionEffectType.GLOWING);
                }
            }
        }
    }

}
