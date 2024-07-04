package com.mikey1201;

import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.SuggestionInfo;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CoordsCommand {
    public void registerCoordsCommand() {
        new CommandAPICommand("coords")
                .executes(this::coordsList)
                .withSubcommand(new CommandAPICommand("add")
                        .withArguments(new GreedyStringArgument("label"))
                        .executes(this::coordsAdd)
                )
                .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(new GreedyStringArgument("label")
                                .replaceSuggestions(this::getLabels))
                        .executes(this::coordsRemove)
                )
                .withSubcommand(new CommandAPICommand("broadcast")
                        .withArguments(new GreedyStringArgument("label")
                                .replaceSuggestions(this::getLabels))
                        .executes(this::coordsBroadcastSpecific)
                )
                .withSubcommand(new CommandAPICommand("send")
                        .withArguments(new PlayerArgument("player"))
                        .withArguments(new GreedyStringArgument("label")
                                .replaceSuggestions(this::getLabels))
                        .executes(this::coordsSendSpecific)
                )
                .register();
        new CommandAPICommand("coords")
                .withSubcommand(new CommandAPICommand("broadcast")
                        .executes(this::coordsBroadcastCurrent)
                )
                .register();
        new CommandAPICommand("coords")
                .withSubcommand(new CommandAPICommand("send")
                        .withArguments(new PlayerArgument("player"))
                        .executes(this::coordsSendCurrent)
                )
                .register();

    }

    private CompletableFuture<Suggestions> getLabels(SuggestionInfo<CommandSender> sender, SuggestionsBuilder suggestions) {
        CoordinateManager c = new CoordinateManager();
        for (Coordinate coords : c.getCoordinatesList(sender.sender().getName())) {
            suggestions.suggest(coords.getName());
        }
        return suggestions.buildFuture();
    }


    private void coordsList(Object sender, Object args) {
        if (sender instanceof Player player) {
            CoordinateManager c = new CoordinateManager();
            List<Coordinate> temp = c.getCoordinatesList(player.getName());
            player.sendMessage(Component.text(player.getName() + "'s coordinates:"));
            for (Coordinate coords : temp) {
                player.sendMessage(Component.text(coords.toString()));
            }
        }
    }
    private void coordsAdd(Object sender, CommandArguments args) {
        if (sender instanceof Player player) {
            String label = (String) args.get("label");
            CoordinateManager c = new CoordinateManager();
            Location l = player.getLocation();
            if (c.checkExists(player.getName(), label)) {
                player.sendMessage(Component.text(label + " already exists."));
                return;
            }
            c.saveCoordinate(player.getName(), new Coordinate(l.getX(), l.getY(), l.getZ(), label));
            player.sendMessage(Component.text(label + " added."));
        }
    }

    private void coordsRemove(Object sender, CommandArguments args) {
        if (sender instanceof Player player) {
            String label = (String) args.get("label");
            CoordinateManager c = new CoordinateManager();
            if (c.removeCoordinate(player.getName(), label)) {
                player.sendMessage(Component.text(label + " removed."));
            } else {
                player.sendMessage(Component.text(label + " not found."));
            }
        }
    }

    private void coordsBroadcastSpecific(Object sender, CommandArguments args) {
        if (sender instanceof Player player) {
            String label = (String) args.get("label");
            CoordinateManager c = new CoordinateManager();
            if (c.checkExists(player.getName(), label)) {
                Bukkit.broadcast(Component.text(c.getCoordinates(player.getName(),label).toString()));
            } else {
                player.sendMessage(Component.text(label + " not found."));
            }
        }
    }

    private void coordsSendSpecific(Object sender, CommandArguments args) {
        if (sender instanceof Player player) {
            String label = (String) args.get("label");
            Player recipient = (Player) args.get("player");
            CoordinateManager c = new CoordinateManager();
            if (Bukkit.getOnlinePlayers().contains(recipient)) {
                if (c.checkExists(player.getName(), label)) {
                    assert recipient != null;
                    recipient.sendMessage(Component.text(c.getCoordinates(player.getName(),label).toString()));
                } else {
                    player.sendMessage(Component.text("No such coordinate."));
                }
            } else {
                player.sendMessage(Component.text("Player not found or not online."));
            }
        }
    }

    private void coordsBroadcastCurrent(Object sender, CommandArguments args) {
        if (sender instanceof Player player) {
            Location l = player.getLocation();
            Bukkit.broadcast(Component.text(new Coordinate(l.getX(), l.getY(), l.getZ(), player.getName() + "'s coordinates:").toString()));
        }
    }

    private void coordsSendCurrent(Object sender, CommandArguments args) {
        if (sender instanceof Player player) {
            Player recipient = (Player) args.get("player");
            Location l = player.getLocation();
            if (Bukkit.getOnlinePlayers().contains(recipient)) {
                assert recipient != null;
                recipient.sendMessage(Component.text(new Coordinate(l.getX(), l.getY(), l.getZ(), player.getName() + "'s coordinates:").toString()));
            }
        }
    }
}