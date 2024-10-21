package srdqrk.me.sansalvadorevents.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import srdqrk.me.sansalvadorevents.SanSalvadorEvents;
import srdqrk.me.sansalvadorevents.scenarios.GlobalScenarios;
import srdqrk.me.sansalvadorevents.scenarios.VolcanicEvent;

public class GeneralCommands {
    private final GlobalScenarios globalScenarios;
    private final VolcanicEvent volcanicEvent;
    private boolean isGlowingActive;

    private boolean isVolcanicEventActive;

    public GeneralCommands() {
        this.globalScenarios = new GlobalScenarios(SanSalvadorEvents.core);
        this.volcanicEvent = new VolcanicEvent(SanSalvadorEvents.core);
        this.isGlowingActive = false;
        this.isVolcanicEventActive = false;
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

            new CommandAPICommand("toggleVolcanicEvent")
                    .executes((CommandSender sender, CommandArguments args) -> {
                        if (isVolcanicEventActive) {
                            sender.sendMessage("El evento volcánico ya está activo.");
                        } else {
                            sender.sendMessage("¡OH, NO! El volcán de San Salvador ha entrado en erupción.");
                            volcanicEvent.startEvent();
                            isVolcanicEventActive = true;
                        }
                    })
                    .register();
    }
}

