package srdqrk.me.sansalvadorevents.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import srdqrk.me.sansalvadorevents.util.Util;

import java.util.ArrayList;
import java.util.List;

public class GlobalScenarios {

    private List<Player> players;

    private BukkitRunnable glowingTask;

    private JavaPlugin plugin;

    private BukkitTask fogTask;
    private boolean isFogActive;


    public GlobalScenarios(JavaPlugin plugin) {
        this.plugin = plugin;

        this.isFogActive = false;
        this.players = new ArrayList<>(Bukkit.getOnlinePlayers()); // TODO: remove for customized players list
    }

    public void startGlowingCycle() {
        this.players = new ArrayList<>(Bukkit.getOnlinePlayers()); // TODO REMOVE THIS
        Util.titleMessage("\uE010 \uE011 \uE012");
        Util.broadcastMessage("\uE004 &r &f¡Aprovecha y ten cuidado con la revelación de ubicaciones!");
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

    public void startFogEvent() {
        if (isFogActive) {
            Bukkit.getLogger().info("El evento de neblina ya está activo.");
            return;
        }

        isFogActive = true;
        Bukkit.getLogger().info("¡Evento de neblina iniciado!");
        Util.titleMessage("\uE010 \uE011 \uE012");
        Util.broadcastMessage("\uE004 &r &f¡Ten mucho cuidado con la visibilidad!");
        fogTask = new BukkitRunnable() {
            private int timeElapsed = 0;

            @Override
            public void run() {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    Location playerLocation = player.getLocation();
                    player.getWorld().spawnParticle(Particle.SMOKE_LARGE, playerLocation, 500, 0.5, 0.5, 0.5, 0.02);
                }

                timeElapsed += 1;

                if (timeElapsed >= 120) {
                    stopFogEvent();
                }
            }
        }.runTaskTimer(plugin, 0, 5);
    }


    public void stopFogEvent() {
        if (fogTask != null && !fogTask.isCancelled()) {
            fogTask.cancel();
        }
        isFogActive = false;
        Bukkit.getLogger().info("El evento de neblina ha terminado.");
    }

    public boolean isFogActive() {
        return isFogActive;
    }



}
