package com.mikey1201;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import org.bukkit.entity.Player;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

public class CoordinateManager {
    private static final String FILE_NAME = "coordinates.json";
    private Map<String, List<Coordinate>> coordinatesMap;
    private final Gson gson;

    public CoordinateManager() {
        coordinatesMap = new HashMap<>();
        gson = new GsonBuilder().setPrettyPrinting().create();
        loadCoordinatesFromFile();
    }

    public static boolean addCoordinate(String label, Player player) {
        CoordinateManager c = new CoordinateManager();
        c.coordinatesMap.putIfAbsent(player.getName(), new ArrayList<>());
        List<Coordinate> coordinates = c.coordinatesMap.get(player.getName());
        if (coordinates.stream().noneMatch(coordinate -> coordinate.getLabel().equals(label))) {
            coordinates.add(new Coordinate(label, player));
            c.saveCoordinatesToFile();
            return true;
        }
        return false;
    }

    public static boolean removeCoordinate(String label, Player player) {
        CoordinateManager c = new CoordinateManager();
        List<Coordinate> coordinates = c.coordinatesMap.get(player.getName());
        if (coordinates != null && coordinates.stream().anyMatch(coordinate -> coordinate.getLabel().equals(label))) {
            coordinates.remove(coordinates.stream().filter(coordinate -> coordinate.getLabel().equals(label)).findFirst().get());
            c.saveCoordinatesToFile();
            return true;
        }
        return false;
    }

    public static List<Coordinate> getCoordinatesList(Player player) {
        return new CoordinateManager().coordinatesMap.getOrDefault(player.getName(), Collections.emptyList());
    }

    public static Coordinate getCoordinate(Player player, String label) {
        return getCoordinatesList(player).stream().filter(coordinate -> coordinate.getLabel().equals(label)).findFirst().orElse(null);
    }

    private void saveCoordinatesToFile() {
        try (Writer writer = new FileWriter(new File(Coordy.getPlugin().getDataFolder(), FILE_NAME))) {
            gson.toJson(coordinatesMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadCoordinatesFromFile() {
        try (Reader reader = new FileReader(new File(Coordy.getPlugin().getDataFolder(), FILE_NAME))) {
            Type type = new TypeToken<Map<String, List<Coordinate>>>() {}.getType();
            coordinatesMap = gson.fromJson(reader, type);
        } catch (FileNotFoundException e) {
            coordinatesMap = new HashMap<>();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}