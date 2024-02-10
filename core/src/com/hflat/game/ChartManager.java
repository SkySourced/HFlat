package com.hflat.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class ChartManager {
    private ArrayList<File> chartFiles;
    private ArrayList<Chart> charts;
    private String currentTask;

    public ChartManager(File chartDirectory, Game game) {
        System.out.println(Arrays.toString(chartDirectory.listFiles()));
        chartFiles = new ArrayList<File>();
        try {
            for (File file : chartDirectory.listFiles()) {
                if (file.getName().endsWith(".sm")) {
                    chartFiles.add(file);
                    System.out.println("Found chart: " + file.getName());
                }
            }
        } catch (NullPointerException e) {
            System.out.println("No files found");
        }
        charts = new ArrayList<>();
        for (File chart : chartFiles) {
            currentTask = "Parsing " + chart.getName();
            try {
                charts.add(Chart.parseChart(chart));
                System.out.println("Parsed chart: " + chart.getName());
            } catch (Exception e) {
                System.out.println("Error parsing chart: " + chart.getName() + " - " + e.getMessage());
            }
        }
        game.setState(GameState.SONG_SELECT);
        currentTask = "";
    }
    public String getCurrentTask() {
        return currentTask;
    }

    public Chart getChart(int index) {
        return charts.get(index);
    }

    public int getChartCount() {
        return charts.size();
    }
}
