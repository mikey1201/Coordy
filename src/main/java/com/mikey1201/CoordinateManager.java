package com.mikey1201;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CoordinateManager {
    private static final String FILE_NAME = "coordinates.json";
    private Map<String, List<Coordinate>> coordinatesMap;
    private final Gson gson;

    public CoordinateManager() {
        coordinatesMap = new HashMap<>();
        gson = new GsonBuilder().setPrettyPrinting().create();
        loadCoordinatesFromFile();
    }

    public void saveCoordinate(String player, Coordinate coord) {
        coordinatesMap.computeIfAbsent(player, k -> new ArrayList<>()).add(coord);
        saveCoordinatesToFile();
    }

    public List<Coordinate> getCoordinatesList(String player) {
        return coordinatesMap.getOrDefault(player, new ArrayList<>());
    }

    public Coordinate getCoordinates(String player, String label) {
        for (Coordinate coord : getCoordinatesList(player)) {
            if (coord.getName().equals(label)) {
                return coord;
            }
        }
        return null;
    }

    public boolean checkExists(String player, String label) {
        for (Coordinate coord : getCoordinatesList(player)) {
            if (coord.getName().equals(label)) {
                return true;
            }
        }
        return false;
    }

    public boolean removeCoordinate(String player, String label) {
        List<Coordinate> coordinates = coordinatesMap.get(player);
        if (coordinates != null) {
            boolean removed = coordinates.removeIf(coord -> coord.getName().equals(label));
            if (removed) {
                saveCoordinatesToFile();
            }
            return removed;
        }
        return false;
    }

    private void saveCoordinatesToFile() {
        try (Writer writer = new FileWriter(new File(Coordy.getPlugin().getDataFolder(), FILE_NAME))) {
            gson.toJson(coordinatesMap, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCoordinatesFromFile() {
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