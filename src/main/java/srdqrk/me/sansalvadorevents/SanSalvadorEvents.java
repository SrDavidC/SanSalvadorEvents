package srdqrk.me.sansalvadorevents;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class SanSalvadorEvents extends JavaPlugin {

public static SanSalvadorEvents core;

    @Override
    public void onLoad() {

    }

    @Override
    public void onEnable() {
        CommandAPI.onEnable();



        core = this;

        // Register commands, listeners etc.
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}
