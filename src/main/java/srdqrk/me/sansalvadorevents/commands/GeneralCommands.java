package srdqrk.me.sansalvadorevents.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import srdqrk.me.sansalvadorevents.SanSalvadorEvents;
import srdqrk.me.sansalvadorevents.scenarios.GlobalScenarios;

public class GeneralCommands {
    private final GlobalScenarios globalScenarios;
    private boolean isGlowingActive;

    public GeneralCommands() {
        this.globalScenarios = new GlobalScenarios(SanSalvadorEvents.core);
        this.isGlowingActive = false;
        this.registerCommands();
    }

        private void registerCommands() {
            new CommandAPICommand("toggleEventoSeguridad")
                    .executes((CommandSender sender, CommandArguments args) -> {
                        if (isGlowingActive) {
                            sender.sendMessage("Desactivando glowing para todos los jugadores.");
                            globalScenarios.cancelGlowingCycle();
                        } else {
                            sender.sendMessage("Activando glowing para todos los jugadores.");
                            globalScenarios.startGlowingCycle();
                        }

                        isGlowingActive = !isGlowingActive;

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            player.sendMessage(isGlowingActive ? "El glowing ha sido activado." : "El glowing ha sido desactivado.");
                        });
                    })
                    .register();
    }
}

