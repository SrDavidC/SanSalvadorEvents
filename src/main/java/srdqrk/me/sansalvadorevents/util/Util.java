package srdqrk.me.sansalvadorevents.util;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

public class Util {

    public static void broadcastMessage(String message) {
        // Broadcasts a message to all players
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(message);
        }
    }

    public static void titleMessage(String title) {
        // Broadcasts a title message to all players
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(title,"", 10, 70, 20);
        }
    }

    public static void playSoundForAll(Sound sound, float volume, float pitch) {
        // Plays a sound for all players
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, volume, pitch);
        }
    }

    public static void broadcastSound(String sound) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player,sound, SoundCategory.MASTER, 1.0f, 1.0f);
        }
    }

}
