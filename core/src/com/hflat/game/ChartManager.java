package com.hflat.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import com.badlogic.gdx.Gdx;
import com.hflat.game.Game.GameState;

/**
 * You're not going to believe this, it manages charts
 * When instantiated it searches its directory for charts
 * It will then parse charts and start the game from there
 * */
public class ChartManager {
    private ArrayList<Chart> charts;
    private String currentTask;

    /**
     * Constructor for chart manager
     *
     * */
    public ChartManager(File chartDirectory, Game game) {
        Gdx.app.debug("ChartManager", Arrays.toString(chartDirectory.listFiles()));
        ArrayList<File> chartFiles = new ArrayList<>();
        try {
            for (File file : chartDirectory.listFiles()) {
                if (file.getName().endsWith(".sm")) {
                    chartFiles.add(file);
                    Gdx.app.log("ChartManager", String.format("Found chart: %s",file.getName()));
                }
            }
        } catch (NullPointerException e) { // i think it will say its an error anyway
            Gdx.app.error("ChartManager","No files found",e);
        }
        charts = new ArrayList<>();
        for (File chart : chartFiles) {
            currentTask = "Parsing " + chart.getName();
            try {
                charts.add(Chart.parseChart(chart));
                Gdx.app.log("ChartManager", "Parsed chart: " + chart.getName());
            } catch (Exception e) {
                Gdx.app.error("ChartManager", "Error parsing chart: " + chart.getName(), e);
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
