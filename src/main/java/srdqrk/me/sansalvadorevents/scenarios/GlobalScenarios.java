package srdqrk.me.sansalvadorevents.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import srdqrk.me.sansalvadorevents.util.Util;

import java.util.List;
import java.util.stream.Collectors;

public class GlobalScenarios {

    private List<Player> players;
    private BukkitRunnable glowingTask;
    private JavaPlugin plugin;
    private BukkitTask fogTask;
    private boolean isFogActive;

    public GlobalScenarios(JavaPlugin plugin) {
        this.plugin = plugin;
        this.isFogActive = false;
        updatePlayersList();
    }

    private void updatePlayersList() {
        // Actualizar la lista de jugadores excluyendo a los operadores
        this.players = Bukkit.getOnlinePlayers().stream()
                .filter(player -> !player.isOp())
                .collect(Collectors.toList());
    }

    public void startGlowingCycle() {
        Util.playSoundForAll(Sound.BLOCK_BEACON_ACTIVATE, 1.0f, 1.0f);
        updatePlayersList();  // Actualizar la lista de jugadores antes de comenzar el ciclo
        Util.titleMessage("\uE012");
        Util.broadcastMessage("\uE004 ¡Aprovecha y ten cuidado con la revelación de ubicaciones!");

        glowingTask = new BukkitRunnable() {
            private boolean glowing = false;

            @Override
            public void run() {
                if (glowing) {
                    for (Player player : players) {
                        player.removePotionEffect(PotionEffectType.GLOWING);
                    }
                    glowing = false;
                } else {
                    for (Player player : players) {
                        player.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 200, 0));
                    }
                    glowing = true;
                }
            }
        };
        glowingTask.runTaskTimer(plugin, 0, 200L);

        new BukkitRunnable() {
            @Override
            public void run() {
                cancelGlowingCycle();
            }
        }.runTaskLater(plugin, 3600L);
    }

    public void cancelGlowingCycle() {
        Util.playSoundForAll(Sound.BLOCK_BEACON_DEACTIVATE, 1.0f, 1.0f);
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
        Util.playSoundForAll(Sound.ENTITY_BLAZE_BURN, 1.0f, 1.0f);
        Util.playSoundForAll(Sound.ENTITY_BLAZE_DEATH, 1.0f, 1.0f);
        isFogActive = true;
        updatePlayersList();  // Actualizar la lista de jugadores antes del evento
        Bukkit.getLogger().info("¡Evento de neblina iniciado!");
        Util.titleMessage("\uE011");
        Util.broadcastMessage("\uE004 Ten mucho cuidado con la visibilidad!");

        fogTask = new BukkitRunnable() {
            private int timeElapsed = 0;

            @Override
            public void run() {
                for (Player player : players) {
                    Location playerLocation = player.getLocation();
                    player.getWorld().spawnParticle(Particle.SMOKE_LARGE, playerLocation, 1500, 0.5, 0.5, 0.5, 0.02);
                }
                timeElapsed += 1;
                if (timeElapsed >= 180) {
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
        Util.playSoundForAll(Sound.ENTITY_BLAZE_BURN, 1.0f, 1.0f);
        Util.playSoundForAll(Sound.ENTITY_BLAZE_DEATH, 1.0f, 1.0f);
    }

    public boolean isFogActive() {
        return isFogActive;
    }
}
