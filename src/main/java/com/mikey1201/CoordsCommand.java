package com.mikey1201;

import dev.jorel.commandapi.CommandAPICommand;
import dev.jorel.commandapi.SuggestionInfo;
import dev.jorel.commandapi.arguments.ArgumentSuggestions;
import dev.jorel.commandapi.arguments.EntitySelectorArgument;
import dev.jorel.commandapi.arguments.GreedyStringArgument;
import dev.jorel.commandapi.executors.CommandArguments;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CoordsCommand {
    public static void registerCoordsCommand() {
        //List add remove
        new CommandAPICommand("coords")
                .executesPlayer(CoordsCommand::coordsList)
                .withSubcommand(new CommandAPICommand("add")
                        .withArguments(new GreedyStringArgument("label"))
                        .executesPlayer(CoordsCommand::coordsAdd))
                .withSubcommand(new CommandAPICommand("remove")
                        .withArguments(new GreedyStringArgument("label")
                                .replaceSuggestions(ArgumentSuggestions.strings(CoordsCommand::labelSuggestions)))
                        .executesPlayer(CoordsCommand::coordsRemove))
                .register();
        //Share current coordinates
        new CommandAPICommand("coords")
                .withSubcommand(new CommandAPICommand("share")
                        .withArguments(new EntitySelectorArgument.ManyPlayers("player")
                                .replaceSuggestions(ArgumentSuggestions.strings(CoordsCommand::playerSuggestions)))
                        .executesPlayer(CoordsCommand::coordsShareCurrent))
                .register();
        //Share specific coordinates
        new CommandAPICommand("coords")
                .withSubcommand(new CommandAPICommand("share")
                        .withArguments(new EntitySelectorArgument.ManyPlayers("player")
                                .replaceSuggestions(ArgumentSuggestions.strings(CoordsCommand::playerSuggestions)))
                        .withArguments(new GreedyStringArgument("label")
                                .replaceSuggestions(ArgumentSuggestions.strings(CoordsCommand::labelSuggestions)))
                        .executesPlayer(CoordsCommand::coordsShare))
                .register();
    }

    private static void coordsShareCurrent(Object sender, CommandArguments args) {
        Player player = (Player) sender;
        String rawArg = args.getRaw("player");
        if (invalidRawArg(Objects.requireNonNull(rawArg), player)) {
            player.sendMessage(ComponentManager.noPermission(rawArg));
            return;
        }
        Coordinate coordinate = new Coordinate(player.getName()+"'s coordinates", player);
        share(player, rawArg, coordinate);
    }

    private static void coordsShare(Object sender, CommandArguments args) {
        Player player = (Player) sender;
        String rawArg = args.getRaw("player");
        if (invalidRawArg(Objects.requireNonNull(rawArg), player)) {
            player.sendMessage(ComponentManager.noPermission(rawArg));
            return;
        }
        String label = args.getRaw("label");
        Coordinate coordinate = CoordinateManager.getCoordinate(player, label);
        if (coordinate != null) {
            share(player, rawArg, coordinate);
        }
    }

    private static void share(Player player, String rawArg, Coordinate coord) {
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

    private static void coordsRemove(Object sender, CommandArguments args) {
        Player player = (Player) sender;
        String label = (String) args.get("label");
        if (CoordinateManager.removeCoordinate(label, player)) {
            player.sendMessage(ComponentManager.removeSuccess(label));
        } else {
            player.sendMessage(ComponentManager.removeFailure(label));
        }
    }

    private static void coordsAdd(Object sender, CommandArguments args) {
        Player player = (Player) sender;
        String label = (String) args.get("label");
        if (CoordinateManager.addCoordinate(label, player)) {
            player.sendMessage(ComponentManager.addSuccess(label));
        } else {
            player.sendMessage(ComponentManager.addFailure(label));
        }
    }

    private static void coordsList(Object sender, CommandArguments args) {
        Player player = (Player) sender;
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

    private static String[] labelSuggestions(SuggestionInfo<CommandSender> info) {
        List<Coordinate> coords = CoordinateManager.getCoordinatesList((Player) info.sender());
        if (coords == null) {
            return new String[0];
        }
        return coords.stream().map(Coordinate::getLabel).toArray(String[]::new);
    }

    private static String[] playerSuggestions(SuggestionInfo<CommandSender> info) {
        List<String> suggestions = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
        Player p = (Player) info.sender();
        if (p.hasPermission("coordy.commands.share.@a")) {
            suggestions.add("@a");
        }
        return suggestions.toArray(new String[0]);
    }

    private static boolean hasPermission(String rawArg, Player player) {
        return Objects.equals(rawArg, "@a") && player.hasPermission("coordy.commands.share.@a");
    }

    private static boolean invalidRawArg(String rawArg, Player player) {
        boolean isRestrictedArg = rawArg.equals("@e") || rawArg.equals("@r") || rawArg.equals("@s") || rawArg.equals("@p");
        return (rawArg.equals("@a") && !player.hasPermission("coordy.commands.share.@a")) || isRestrictedArg;
    }
}