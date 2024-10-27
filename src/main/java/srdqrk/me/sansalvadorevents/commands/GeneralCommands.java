package srdqrk.me.sansalvadorevents.commands;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import srdqrk.me.sansalvadorevents.SanSalvadorEvents;
import srdqrk.me.sansalvadorevents.scenarios.GlobalScenarios;
import srdqrk.me.sansalvadorevents.scenarios.VolcanicEvent;
import srdqrk.me.sansalvadorevents.util.Util;

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
                        sender.sendMessage("\uE004 El glowing ya está activo.");
                    } else {
                        sender.sendMessage("\uE004 Activando glowing para todos los jugadores.");
                        globalScenarios.startGlowingCycle();
                        Util.broadcastSound("hexacreators:xocolatl.audio_4");
                        isGlowingActive = true;
                        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("\uE004 El glowing ha sido activado."));
                    }
                })
                .register();

        new CommandAPICommand("glowingOff")
                .withPermission("SanSalvadorAdmin")
                .executes((CommandSender sender, CommandArguments args) -> {
                    if (!isGlowingActive) {
                        sender.sendMessage("\uE004 El glowing ya está desactivado.");
                    } else {
                        sender.sendMessage("\uE004 Desactivando glowing para todos los jugadores.");
                        globalScenarios.cancelGlowingCycle();
                        isGlowingActive = false;
                        Bukkit.getOnlinePlayers().forEach(player -> player.sendMessage("El glowing ha sido desactivado."));
                    }
                })
                .register();

        new CommandAPICommand("volcanicEventOn")
                .withPermission("SanSalvadorAdmin")
                .executes((CommandSender sender, CommandArguments args) -> {
                    if (this.volcanicEvent.isActive()) {
                        sender.sendMessage("El evento volcánico ya está activo.");
                    } else {
                        Util.broadcastMessage("\uE004 ¡OH, NO! El volcán de San Salvador ha entrado en erupción.");
                        Util.broadcastSound("hexacreators:xocolatl.audio_2");
                        volcanicEvent.startEvent();
                        isVolcanicEventActive = true;
                    }
                })
                .register();

        new CommandAPICommand("volcanicEventOff")
                .withPermission("SanSalvadorAdmin")
                .executes((CommandSender sender, CommandArguments args) -> {
                    if (!isVolcanicEventActive) {
                        sender.sendMessage("\uE004 El evento volcánico ya está desactivado.");
                    } else {
                        sender.sendMessage("\uE004 Deteniendo el evento volcánico.");
                        volcanicEvent.stopEvent();
                        isVolcanicEventActive = false;
                    }
                })
                .register();

        new CommandAPICommand("fogEventOn")
                .withPermission("SanSalvadorAdmin")
                .executes((CommandSender sender, CommandArguments args) -> {
                    if (globalScenarios.isFogActive()) {
                        sender.sendMessage("\uE004 El evento de neblina ya está activo.");
                    } else {
                        sender.sendMessage("\uE004 Iniciando el evento de neblina...");
                        Util.broadcastSound("hexacreators:xocolatl.audio_3");
                        globalScenarios.startFogEvent();
                    }
                })
                .register();

        new CommandAPICommand("fogEventOff")
                .withPermission("SanSalvadorAdmin")
                .executes((CommandSender sender, CommandArguments args) -> {
                    if (!globalScenarios.isFogActive()) {
                        sender.sendMessage("\uE004 El evento de neblina ya está desactivado.");
                    } else {
                        sender.sendMessage("\uE004 Deteniendo el evento de neblina...");
                        globalScenarios.stopFogEvent();
                    }
                })
                .register();
    }

    public void onDisable() {
        globalScenarios.cancelGlowingCycle();
        globalScenarios.stopFogEvent();
        volcanicEvent.stopEvent();
    }




}

