package com.hflat.game;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;

public class ChartManager {
    private ArrayList<File> chartFiles;
    private ArrayList<Chart> charts;

    public ChartManager(File chartDirectory) {
        chartFiles = new ArrayList<File>();
        try {
            for (File file : Objects.requireNonNull(chartDirectory.listFiles())) {
                if (file.getName().endsWith(".sm")) {
                    chartFiles.add(file);
                }
            }
        } catch (NullPointerException e) {
            System.out.println("No files found");
        }
        charts = new ArrayList<>();
        for (File chart : chartFiles) {
            try {
                charts.add(Chart.parseChart(chart));
            } catch (Exception e) {
                System.out.println("Error parsing chart: " + chart.getName());
            }
        }
    }

}
