package com.hflat.game.chart;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import com.badlogic.gdx.Gdx;
import com.hflat.game.Game;
import com.hflat.game.Game.GameState;

/**
 * You're not going to believe this, it manages songs/charts
 * When instantiated it searches its directory for songs
 * It will then parse songs & charts and start the game from there
 */
public class SongManager {
    private ArrayList<Song> songs;
    private String currentTask;

    /**
     * Constructor for song manager
     */
    public SongManager(File songDirectory, Game game) {
        Gdx.app.debug("SongManager", Arrays.toString(songDirectory.listFiles()));
        ArrayList<File> simFiles = new ArrayList<>();
        try {
            for (File file : Objects.requireNonNull(songDirectory.listFiles())) {
                if (file.getName().endsWith(".sm")) {
                    simFiles.add(file);
                    Gdx.app.log("SongManager", String.format("Found song: %s", file.getName()));
                }
            }
        } catch (NullPointerException e) { // I think it will say it's an error anyway
            Gdx.app.error("SongManager", "No files found", e);
        }
        this.songs = new ArrayList<>();
        for (File simFile : simFiles) {
            currentTask = "Parsing " + simFile.getName();
            try {
                songs.add(Song.readSimFile(simFile));
                for (Song song : songs) for (Chart chart : song.charts) if (chart != null) chart.setSong(song);
                Gdx.app.log("SongManager", "Parsed song: " + simFile.getName());
            } catch (Exception e) {
                Gdx.app.error("SongManager", "Error parsing song: " + simFile.getName(), e);
            }
        }
        game.setState(GameState.SONG_SELECT);
        currentTask = "";
    }

    public String getCurrentTask() {
        return currentTask;
    }

    public Song getSong(int index) {
        return songs.get(index);
    }

    public int getSongCount() {
        return songs.size();
    }
}
