package srdqrk.me.sansalvadorevents.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
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
        glowingTask = new BukkitRunnable() {
            private boolean glowing = true;

            @Override
            public void run() {
                if (glowing) {
                    for (Player player : players) {
                        if (player.isOnline()) {
                            player.setGlowing(true);
                        }
                    }
                    glowing = false;
                } else {
                    for (Player player : players) {
                        if (player.isOnline()) {
                            player.setGlowing(false);
                        }
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
                    player.setGlowing(false);
                }
            }
        }
    }

}
