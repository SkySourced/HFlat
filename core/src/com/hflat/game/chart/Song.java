package com.hflat.game.chart;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.hflat.game.note.Note;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;

public class Song {
    private final String name;
    private final String subtitle;
    private final String artist;
    private final String soundPath;
    private final String bgPath;
    private final String path;
    private final float offset;
    private final Texture banner;
    private final Color backgroundColour;
    private final float bpm;
    public ArrayList<Chart> charts; // Novice, Easy, Medium, Hard, Expert, Challenge

    public Song(String name, String subtitle, String artist, String soundPath, String bgPath, String path, float offset, String bannerPath, float bpm, ArrayList<Chart> charts) {
        if (bannerPath == null || bannerPath.equals("charts/")) bannerPath = "defaultbanner.png";

        this.name = name;
        this.subtitle = subtitle;
        this.artist = artist;
        this.soundPath = soundPath;
        this.bgPath = bgPath;
        this.path = path;
        this.offset = offset;
        this.banner = new Texture(bannerPath);
        this.backgroundColour = Chart.hashColor(name + artist);
        this.bpm = bpm;
        this.charts = charts;
    }

    public static Song readSimFile(File chart) throws IOException {

        String file = Files.readString(chart.toPath(), Charset.defaultCharset());

        String[] fileLines = file.split(";", -1);

        String name = "";
        String subtitle = "";
        String artist = "";
        String soundPath = "";
        String path = chart.getPath();
        float bpm = 0;
        float offset = 0;
        String bgPath = "";
        String banner = "";

        ArrayList<Chart> charts = new ArrayList<>();

        for (String line : fileLines) {
            line = line.replace("\r", "");
            //Gdx.app.debug("Chart -bytes", Arrays.toString(line.getBytes(Charset.defaultCharset())));
            while (!line.startsWith("#")) {
                try {
                    line = line.substring(1);
                } catch (StringIndexOutOfBoundsException e) {
                    break;
                }
            }
            if (line.startsWith("#TITLE:")) {
                name = line.substring(7);
            } else if (line.startsWith("#SUBTITLE:")) {
                subtitle = line.substring(10);
            } else if (line.startsWith("#ARTIST:")) {
                artist = line.substring(8);
            } else if (line.startsWith("#BANNER:")) {
                banner = "charts/" + line.substring(8);
            } else if (line.startsWith("#MUSIC:")) {
                soundPath = line.substring(7);
            } else if (line.startsWith("#OFFSET:")) {
                offset = Float.parseFloat(line.substring(8));
            } else if (line.startsWith("#BACKGROUND:")) {
                bgPath = line.substring(12);
            } else if (line.startsWith("#NOTES:")) {
                charts.add(Chart.parseChart(line, bpm));
            } else if (line.startsWith("#BPMS:")) {
                bpm = Float.parseFloat(line.split("=")[1].split(",")[0]);
            }
        }
        charts.sort(Comparator.comparingInt(Chart::getDifficulty));
        return new Song(name, subtitle, artist, soundPath, bgPath, path, offset, banner, bpm, charts);
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

    public String getSoundPath() {
        return soundPath;
    }

    public String getBgPath() {
        return bgPath;
    }

    public String getPath() {
        return path;
    }

    public float getOffset() {
        return offset;
    }

    public Texture getTexture() {
        return banner;
    }

    public Color getBackgroundColour() {
        return backgroundColour;
    }

    public float getBpm() {
        return bpm;
    }

    public Chart getChart(int index, boolean protectNull) {
        try {
            return charts.get(index);
        } catch (IndexOutOfBoundsException e) {
            return protectNull ? getChart(0, true) : null;
        }
    }

    public Chart getChart(int index) {
        return getChart(index, true);
    }

    public int getChartCount() {
        return charts.size();
    }
}
