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

        new CommandAPICommand("glowingOn")
                .withPermission("SanSalvadorAdmin")
                .executes((CommandSender sender, CommandArguments args) -> {
                    if (isGlowingActive) {
                        sender.sendMessage("El glowing ya está activo.");
                    } else {
                        sender.sendMessage("Activando glowing para todos los jugadores.");
                        globalScenarios.startGlowingCycle();
                        isGlowingActive = true;
                        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("El glowing ha sido activado."));
                    }
                })
                .register();

        new CommandAPICommand("glowingOff")
                .withPermission("SanSalvadorAdmin")
                .executes((CommandSender sender, CommandArguments args) -> {
                    if (!isGlowingActive) {
                        sender.sendMessage("El glowing ya está desactivado.");
                    } else {
                        sender.sendMessage("Desactivando glowing para todos los jugadores.");
                        globalScenarios.cancelGlowingCycle();
                        isGlowingActive = false;
                        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("El glowing ha sido desactivado."));
                    }
                })
                .register();

        new CommandAPICommand("volcanicEventOn")
                .withPermission("SanSalvadorAdmin")
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

        new CommandAPICommand("volcanicEventOff")
                .withPermission("SanSalvadorAdmin")
                .executes((CommandSender sender, CommandArguments args) -> {
                    if (!isVolcanicEventActive) {
                        sender.sendMessage("El evento volcánico ya está desactivado.");
                    } else {
                        sender.sendMessage("Deteniendo el evento volcánico.");
                        volcanicEvent.stopEvent();
                        isVolcanicEventActive = false;
                    }
                })
                .register();

        new CommandAPICommand("fogEventOn")
                .withPermission("SanSalvadorAdmin")
                .executes((CommandSender sender, CommandArguments args) -> {
                    if (globalScenarios.isFogActive()) {
                        sender.sendMessage("El evento de neblina ya está activo.");
                    } else {
                        sender.sendMessage("Iniciando el evento de neblina...");
                        globalScenarios.startFogEvent();
                    }
                })
                .register();

        new CommandAPICommand("fogEventOff")
                .withPermission("SanSalvadorAdmin")
                .executes((CommandSender sender, CommandArguments args) -> {
                    if (!globalScenarios.isFogActive()) {
                        sender.sendMessage("El evento de neblina ya está desactivado.");
                    } else {
                        sender.sendMessage("Deteniendo el evento de neblina...");
                        globalScenarios.stopFogEvent();
                    }
                })
                .register();
    }




}

