package srdqrk.me.sansalvadorevents;

import dev.jorel.commandapi.CommandAPI;
import dev.jorel.commandapi.CommandAPIBukkitConfig;
import dev.jorel.commandapi.CommandAPICommand;
import org.bukkit.plugin.java.JavaPlugin;
import srdqrk.me.sansalvadorevents.commands.GeneralCommands;

public final class SanSalvadorEvents extends JavaPlugin {

    public static SanSalvadorEvents core;

    private GeneralCommands generalCommands;
    @Override
    public void onEnable() {
        core = this;

        CommandAPI.onEnable();
        generalCommands = new GeneralCommands();
        // Register commands, listeners etc.
    }

    @Override
    public void onDisable() {
        CommandAPI.onDisable();
    }
}
