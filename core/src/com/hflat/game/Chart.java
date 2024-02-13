package com.hflat.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Chart {
    private final String name;
    private final String subtitle;
    private final String artist;
    private final String soundPath;
    private final String bgPath;
    private final String path;
    private final float offset;
    private final Texture banner;
    private final int difficulty;
    private final String difficultyString;
    private final String stepAuthor;
    private final float bpm;
    private final Color backgroundColour;
    private final Color difficultyColour;

    private ArrayList<Note> notes;

    public Chart(String name, String subtitle, String artist, String soundPath, String bgPath, String path, int difficulty, String difficultyString, String stepAuthor, float bpm, float offset, String bannerPath) {
        if (bannerPath == null || bannerPath.equals("charts/")) bannerPath = "defaultbanner.png";

        this.name = name;
        this.subtitle = subtitle;
        this.artist = artist;
        this.difficulty = difficulty;
        this.soundPath = soundPath;
        this.bgPath = bgPath;
        this.path = path;
        this.difficultyString = difficultyString;
        this.stepAuthor = stepAuthor;
        this.bpm = bpm;
        this.offset = offset;
        this.notes = new ArrayList<>();
        this.banner = new Texture(bannerPath);
        this.backgroundColour = hashColor(name + artist);
        this.difficultyColour = hashColor(difficultyString.toLowerCase());
    }

    public static Chart parseChart(File chart) throws FileNotFoundException {
        Scanner fileReader = new Scanner(chart);
        StringBuilder file = new StringBuilder();
        while (fileReader.hasNextLine()) {
            String data = fileReader.nextLine();
            file.append(data);
        }
        fileReader.close();
        String[] fileLines = file.toString().split(";");

        String name = "";
        String subtitle = "";
        String artist = "";
        String soundPath = "";
        String path = chart.getPath();
        int difficulty = 0;
        float bpm = 0;
        float offset = 0;
        String difficultyString = "";
        String stepAuthor = "";
        String bgPath = "";
        String bannerPath = "";

        for (String line : fileLines) {
            while (!line.startsWith("#")){
                line = line.substring(1);
            }
            if (line.startsWith("#TITLE:")) {
                name = line.substring(7);
            } else if (line.startsWith("#SUBTITLE:")) {
                subtitle = line.substring(10);
            } else if (line.startsWith("#ARTIST:")) {
                artist = line.substring(8);
            } else if (line.startsWith("#BANNER:")) {
                bannerPath = "charts/" + line.substring(8);
            } else if (line.startsWith("#MUSIC:")) {
                soundPath = line.substring(7);
            } else if (line.startsWith("#OFFSET:")){
                offset = Float.parseFloat(line.substring(8));
            } else if (line.startsWith("#BACKGROUND:")){
                bgPath = line.substring(12);
            } else if (line.startsWith("#NOTES:")) {
                String[] rawNoteInfo = line.split(":");

                for (int i = 0; i < rawNoteInfo.length; i++) rawNoteInfo[i] = rawNoteInfo[i].trim();

                stepAuthor = rawNoteInfo[2];
                difficultyString = rawNoteInfo[3];
                difficulty = Integer.parseInt(rawNoteInfo[4]);
                String noteData = rawNoteInfo[6];

                // Note parsing (could this be done later when the chart is played?)
                String[] bars = noteData.split(","); // Split the note data into bars
                for (String s : bars) {
                    System.out.println(s);
                        String[] beats = s.split(""); // TODO: Split the line into individual beats
                        for (String b : beats) {

                            // TODO: Split the beats into lanes & types and calculate quantisation

                            System.out.println(b);
                        }
                }


            } else if (line.startsWith("#BPMS:")) {
                bpm = Float.parseFloat(line.split("=")[1]);
            }
        }

        return new Chart(name, subtitle, artist, soundPath, bgPath, path, difficulty, difficultyString, stepAuthor, bpm, offset, bannerPath);
    }

    public String getName() {
        return name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getArtist() {
        return artist;
    }


    public int getDifficulty() {
        return difficulty;
    }

    public String getPath() {
        return path;
    }

    public String getDifficultyString() {
        return difficultyString;
    }

    public String getStepAuthor() {
        return stepAuthor;
    }

    public float getBpm() {
        return bpm;
    }

    public Texture getTexture() {
        return banner;
    }

    public Color getBackgroundColour() {
        return backgroundColour;
    }

    public Color getDifficultyColour() {
        return difficultyColour;
    }

    private static Color hashColor(String s) {
        int hash = s.hashCode();
        float r = (hash & 0xFF0000) >> 16;
        float g = (hash & 0x00FF00) >> 8;
        float b = hash & 0x0000FF;
        return new Color(r / 255, g / 255, b / 255, 0.3f);
    }

    private static String hexToAscii(String hexStr) {
        StringBuilder output = new StringBuilder();

        for (int i = 0; i < hexStr.length(); i += 2) {
            String str = hexStr.substring(i, i + 2);
            output.append((char) Integer.parseInt(str, 16));
        }

        return output.toString();
    }

}
