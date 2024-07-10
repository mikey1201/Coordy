package com.mikey1201;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.SuggestionInfo;
import dev.jorel.commandapi.arguments.*;
import dev.jorel.commandapi.executors.CommandArguments;

import net.kyori.adventure.text.Component;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CoordsCommand {
    public void registerCoordsCommand() {
        new CommandAPICommand("coords")
                .executes(this::coordsList)
                .withSubcommand(new CommandAPICommand("add")
                        .withArguments(new GreedyStringArgument("label"))
                        .executes(this::coordsAdd))
                .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(new GreedyStringArgument("label")
                                .replaceSuggestions(ArgumentSuggestions.strings(this::labelSuggestions)))
                        .executes(this::coordsRemove))
                .register();

        new CommandAPICommand("coords")
                .withSubcommand(new CommandAPICommand("share")
                        .withArguments(new EntitySelectorArgument.ManyPlayers("player")
                                .replaceSuggestions(ArgumentSuggestions.strings(this::playerSuggestions)))
                        .executes(this::coordsShareCurrent))
                .register();

        new CommandAPICommand("coords")
                .withSubcommand(new CommandAPICommand("share")
                        .withArguments(new EntitySelectorArgument.ManyPlayers("player")
                                .replaceSuggestions(ArgumentSuggestions.strings(this::playerSuggestions)))
                        .withArguments(new GreedyStringArgument("label")
                                .replaceSuggestions(ArgumentSuggestions.strings(this::labelSuggestions)))
                        .executes(this::coordsShare))
                .register();
    }

    private void coordsShareCurrent(Object sender, CommandArguments args) {
        if (sender instanceof Player player) {
            String rawArg = args.getRaw("player");
            if (invalidRawArg(Objects.requireNonNull(rawArg), player)) {
                player.sendMessage(ComponentManager.noPermission(rawArg));
                return;
            }
            Coordinate coord = new Coordinate(player.getName()+"'s coordinates", player);
            share(player, rawArg, coord);
        }
    }

    private void coordsShare(Object sender, CommandArguments args) {
        if (sender instanceof Player player) {
            String rawArg = args.getRaw("player");
            if (invalidRawArg(Objects.requireNonNull(rawArg), player)) {
                player.sendMessage(ComponentManager.noPermission(rawArg));
                return;
            }
            String label = args.getRaw("label");
            Coordinate coord = CoordinateManager.getCoordinate(player, label);
            if (coord != null) {
                share(player, rawArg, coord);
            }
        }
    }

    private void share(Player player, String rawArg, Coordinate coord) {
        if (hasPermission(Objects.requireNonNull(rawArg), player)) {
            Bukkit.getOnlinePlayers().forEach(pla -> pla.sendMessage(ComponentManager.coordinate(coord)));
            return;
        }
        Player recipient = Bukkit.getPlayer(rawArg);
        if (recipient == null) {
            player.sendMessage(ComponentManager.playerNotFound());
            return;
        }
        recipient.sendMessage(ComponentManager.coordinate(coord));
    }

    private void coordsRemove(Object sender, CommandArguments args) {
        if (sender instanceof Player player) {
            String label = (String) args.get("label");
            if (CoordinateManager.removeCoordinate(label, player)) {
                player.sendMessage(ComponentManager.removeSuccess(label));
            } else {
                player.sendMessage(ComponentManager.removeFailure(label));
            }
        }
    }

    private void coordsAdd(Object sender, CommandArguments args) {
        if (sender instanceof Player player) {
            String label = (String) args.get("label");
            if (CoordinateManager.addCoordinate(label, player)) {
                player.sendMessage(ComponentManager.addSuccess(label));
            } else {
                player.sendMessage(ComponentManager.addFailure(label));
            }
        }
    }

    private void coordsList(Object sender, CommandArguments args) {
        if (sender instanceof Player player) {
            List<Coordinate> coords = CoordinateManager.getCoordinatesList(player);
            if (coords == null) {
                player.sendMessage(ComponentManager.noCoords());
            } else if (coords.isEmpty()) {
                player.sendMessage(ComponentManager.noCoords());
            } else {
                player.sendMessage(ComponentManager.coordListHeading(player));
                coords.forEach(coord -> player.sendMessage(ComponentManager.coordinate(coord)));
            }
        }
    }

    private String[] labelSuggestions(SuggestionInfo<CommandSender> info) {
        List<Coordinate> coords = CoordinateManager.getCoordinatesList((Player) info.sender());
        if (coords == null) {
            return new String[0];
        }
        return coords.stream().map(Coordinate::getLabel).toArray(String[]::new);
    }

    private String[] playerSuggestions(SuggestionInfo<CommandSender> info) {
        List<String> suggestions = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        Player p = (Player) info.sender();
        if (p.hasPermission("coordy.commands.share.@a")) {
            suggestions.add("@a");
        }
        return suggestions.toArray(new String[0]);
    }

    private boolean hasPermission(String rawArg, Player player) {
        return Objects.equals(rawArg, "@a") && player.hasPermission("coordy.commands.share.@a");
    }

    private boolean invalidRawArg(String rawArg, Player player) {
        boolean isRestrictedArg = rawArg.equals("@e") || rawArg.equals("@r") || rawArg.equals("@s") || rawArg.equals("@p");
        return (rawArg.equals("@a") && !player.hasPermission("coordy.commands.share.@a")) || isRestrictedArg;
    }
}