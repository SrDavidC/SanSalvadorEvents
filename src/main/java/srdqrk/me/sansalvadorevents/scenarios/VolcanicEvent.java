package srdqrk.me.sansalvadorevents.scenarios;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import srdqrk.me.sansalvadorevents.util.Util;

import java.util.Random;

public class VolcanicEvent {
    private final JavaPlugin plugin;
    private final Random random = new Random();
    private boolean isActive = false;
    private final int DURATION = 120; // seconds
    private final String WORLD_NAME = "world";
    final int BOUND1 = 25;
    final int BOUND2 = 25;
    final int Y_SPAWN_LAYER = 200;


    public VolcanicEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startEvent() {
        if (isActive) {
            return;
        }

        isActive = true;
        Util.titleMessage("\uE010 \uE011 \uE012");
        Util.broadcastMessage("\uE004 &r &f¡Alejate de las zonas peligrosas para protegerte de las rocas volcánicas!");
        new BukkitRunnable() {
            private int elapsedTime = 0;

            @Override
            public void run() {
                if (elapsedTime >= DURATION) {
                    stopEvent();
                    cancel();
                    return;
                }
                Location randomLocation = new Location(Bukkit.getWorld(WORLD_NAME),
                        random.nextInt(BOUND1) - 50,
                        Y_SPAWN_LAYER,
                        random.nextInt(BOUND2) - 50);

                launchMeteorite(randomLocation);

                elapsedTime += 5;
            }
        }.runTaskTimer(plugin, 0, 100L);
    }

    public void stopEvent() {
        isActive = false;
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendMessage("¡El evento volcánico ha terminado!")
        );
    }

    private void launchMeteorite(Location location) {
        System.out.println("Se ha lanzado un meteorito en las coordenadas: " + location.toString());
        ArmorStand meteorite = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 20, 0), EntityType.ARMOR_STAND);
        meteorite.setInvisible(true);
        meteorite.setGravity(false);

        ItemStack helmet = new ItemStack(Material.PAPER);
        helmet.getItemMeta().setCustomModelData(new Random().nextInt(2) == 0 ? 1 : 2);
        meteorite.setItem(EquipmentSlot.HEAD, helmet);
        meteorite.setMarker(true);

        new BukkitRunnable() {
            @Override
            public void run() {
                if (meteorite.getLocation().getY() <= -200) {
                    meteorite.remove();
                    return;
                }

                boolean explosionTriggered = false;

                for (int x = -5; x <= 5; x++) {
                    for (int y = -5; y <= 5; y++) {
                        for (int z = -5; z <= 5; z++) {
                            Location checkLocation = meteorite.getLocation().add(x, y, z);
                            if (checkLocation.getBlock().getType().isSolid() && checkLocation.distance(meteorite.getLocation()) < 5) {
                                explosionTriggered = true;
                                break;
                            }
                        }
                        if (explosionTriggered) break;
                    }
                    if (explosionTriggered) break;
                }

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getLocation().distance(meteorite.getLocation()) < 5) {
                        explosionTriggered = true;
                        break;
                    }
                }

                if (explosionTriggered) {
                    meteorite.getWorld().createExplosion(meteorite.getLocation(), 3.0F);
                    meteorite.remove();
                    this.cancel();
                    return;
                }

                meteorite.teleport(meteorite.getLocation().subtract(0, 1, 0));
            }
        }.runTaskTimer(plugin, 0, 1);
    }


}
