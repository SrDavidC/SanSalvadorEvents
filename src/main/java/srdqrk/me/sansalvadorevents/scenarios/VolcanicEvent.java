package srdqrk.me.sansalvadorevents.scenarios;

import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Transformation;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import srdqrk.me.sansalvadorevents.util.Util;

import java.util.Random;

public class VolcanicEvent {
    private final JavaPlugin plugin;
    private final Random random = new Random();
    private boolean isActive = false;
    private final int DURATION = 300; // seconds
    private final String WORLD_NAME = "world";
    final int BOUND1 = 250;
    final int BOUND2 = 250;
    final int Y_SPAWN_LAYER = 100;

    int idTask;


    public VolcanicEvent(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void startEvent() {
        if (isActive) {
            return;
        }
        Util.playSoundForAll(Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
        Util.playSoundForAll(Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.0f);
        Util.playSoundForAll(Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.0f);
        Util.playSoundForAll(Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.0f);

        isActive = true;
        Util.titleMessage("\uE010");
        Util.broadcastMessage("\uE004 ¡Alejate de las zonas peligrosas para protegerte de las rocas volcánicas!");
        this.idTask = new BukkitRunnable() {
            private int elapsedTime = 0;

            @Override
            public void run() {
                if (elapsedTime >= DURATION) {
                    stopEvent();
                    cancel();
                    return;
                }
                Location randomLocation = new Location(Bukkit.getWorld(WORLD_NAME),
                        random.nextInt(BOUND1 *-1, BOUND1),
                        Y_SPAWN_LAYER,
                        random.nextInt(BOUND2 *-1, BOUND2));

                launchMeteorite(randomLocation);

                elapsedTime += 2;
            }
        }.runTaskTimer(plugin, 0, 40L).getTaskId();
    }

    public void stopEvent() {
        isActive = false;
        Bukkit.getOnlinePlayers().forEach(player ->
                player.sendMessage("\uE004 ¡El evento volcánico ha terminado!")
        );

        for (World world : Bukkit.getWorlds()) {
            for (Entity entity : world.getEntities()) {
                if (entity.hasMetadata("METEORITE_TAG")) {
                    entity.remove(); // Remover el meteorito si tiene el metadato distintivo
                }
            }
        }

        Bukkit.getScheduler().cancelTask(this.idTask);
        Bukkit.getLogger().info("Evento de meteoritos finalizado y meteoritos restantes eliminados.");

        Util.playSoundForAll(Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);
        Util.playSoundForAll(Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.0f);
        Util.playSoundForAll(Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.0f);
        Util.playSoundForAll(Sound.ENTITY_DRAGON_FIREBALL_EXPLODE, 1.0f, 1.0f);


    }

    private void launchMeteorite(Location location) {
        System.out.println("Se ha lanzado un meteorito en las coordenadas: " + location.toString());

        Location impactLocation = findImpactLocation(location.clone());
        ItemDisplay warningDisplay = createItemDisplay(impactLocation);

        ArmorStand meteorite = (ArmorStand) location.getWorld().spawnEntity(location.add(0, 20, 0), EntityType.ARMOR_STAND);
        meteorite.setInvisible(true);
        meteorite.setGravity(false);
        meteorite.setFireTicks(Integer.MAX_VALUE);
        meteorite.setMetadata("METEORITE_TAG", new FixedMetadataValue(plugin, true));
        warningDisplay.setMetadata("METEORITE_TAG", new FixedMetadataValue(plugin, true));

        ItemStack helmet = new ItemStack(Material.PAPER);
        helmet.getItemMeta().setCustomModelData(1);
        meteorite.setItem(EquipmentSlot.HEAD, helmet);
        meteorite.setMarker(true);

        final int descensoPorCiclo = 3;
        final int frecuencia = 1;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (meteorite.getLocation().getY() <= -200) {
                    meteorite.remove();
                    if (!warningDisplay.isDead()) {
                        warningDisplay.remove();
                    }
                    this.cancel();
                    return;
                }

                meteorite.getWorld().spawnParticle(Particle.FLAME, meteorite.getLocation(), 20, 0.5, 0.5, 0.5, 0.05);

                boolean explosionTriggered = false;

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getLocation().distance(meteorite.getLocation()) < 5) {
                        explosionTriggered = true;
                        break;
                    }
                }

                if (impactLocation.clone().subtract(0,2,0).distance(meteorite.getLocation()) < 5) {
                    explosionTriggered = true;
                }

                if (explosionTriggered) {
                    meteorite.getWorld().createExplosion(meteorite.getLocation(), 5.0F);
                    meteorite.remove();
                    if (!warningDisplay.isDead()) {
                        warningDisplay.remove();
                    }
                    this.cancel();
                    return;
                }

                // Caída acelerada del meteorito
                meteorite.teleport(meteorite.getLocation().subtract(0, descensoPorCiclo, 0));
            }
        }.runTaskTimer(plugin, 0, frecuencia);
    }



    // Método para encontrar la ubicación de impacto (donde se generará el ItemDisplay)
    private Location findImpactLocation(Location startLocation) {
        Location impactLocation = startLocation.clone();

        for (int i = 200; i > -200; i--) {
            impactLocation.subtract(0, 1, 0);
            if (impactLocation.getBlock().getType().isSolid()) {
                System.out.println(impactLocation.getBlock().getType());
                break;
            }
        }
        impactLocation.add(0, 2, 0);
        return impactLocation;
    }

    // Método para crear el ItemDisplay en la ubicación de impacto
    private ItemDisplay createItemDisplay(Location location) {
        World world = location.getWorld();

        // Crear un ItemDisplay en la ubicación de impacto
        ItemDisplay itemDisplay = (ItemDisplay) world.spawnEntity(location, EntityType.ITEM_DISPLAY);

        // Configurar el item (en este caso, papel con CustomModelData 2)
        ItemStack item = new ItemStack(Material.PAPER);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            meta.setCustomModelData(2);
            item.setItemMeta(meta);
        }
        itemDisplay.setItemStack(item); // Asignar el ítem al display

        // Ajustes adicionales
        itemDisplay.setInvisible(false); // El display debe ser visible para advertir
        itemDisplay.setGravity(false);   // No debe caer
        Matrix4f matrix = new Matrix4f()
                .scaling(4.0f, 4.0f, 4.0f); // Escala (X = 10, Y = 1, Z = 10)

        itemDisplay.setTransformationMatrix(matrix); // Aplicar la escala
        //itemDisplay.setRotation(0, 90);  // Rotar el ítem si es necesario
        //itemDisplay.setBrightness(15);   // Hacerlo brillar si lo deseas

        return itemDisplay;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
