package srdqrk.me.sansalvadorevents.util;

import org.bukkit.Bukkit;
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

}
