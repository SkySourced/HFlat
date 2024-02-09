package com.hflat.game;

import java.util.ArrayList;

public class Chart {
    public String name;
    public String subtitle;
    public String artist;
    public String path;
    public int difficulty;
    private int bpm;

    private ArrayList<Note> notes;

    public Chart(String name, String subtitle, String artist, int difficulty, String path) {
        this.name = name;
        this.subtitle = subtitle;
        this.artist = artist;
        this.difficulty = difficulty;
        this.path = path;
        this.notes = new ArrayList<>();
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
}
