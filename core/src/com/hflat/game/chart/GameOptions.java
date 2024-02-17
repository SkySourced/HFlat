package com.hflat.game.chart;

public class GameOptions {
    public static final int NUM_OPTIONS = 7;
    private float noteSpeed;
    private BackgroundFilter backgroundFilter;
    private boolean showJudgements;
    private boolean showCombo;
    private int visualOffset; // ms
    private float musicRate;
    private float mini;

    public GameOptions() {
        this.noteSpeed = 1.0f;
        this.backgroundFilter = BackgroundFilter.NONE;
        this.showJudgements = true;
        this.showCombo = true;
        this.visualOffset = 0;
        this.musicRate = 1f;
        this.mini = 1f;
    }

    public float getNoteSpeed() {
        return noteSpeed;
    }
    public GameOptions setNoteSpeed(float noteSpeed) {
        this.noteSpeed = noteSpeed;
        return this;
    }
    public BackgroundFilter getBackgroundFilter() {
        return backgroundFilter;
    }
    public GameOptions setBackgroundFilter(BackgroundFilter backgroundFilter) {
        this.backgroundFilter = backgroundFilter;
        return this;
    }
    public boolean isShowJudgements() {
        return showJudgements;
    }
    public GameOptions setShowJudgements(boolean showJudgements) {
        this.showJudgements = showJudgements;
        return this;
    }
    public boolean isShowCombo() {
        return showCombo;
    }
    public GameOptions setShowCombo(boolean showCombo) {
        this.showCombo = showCombo;
        return this;
    }
    public int getVisualOffset() {
        return visualOffset;
    }
    public GameOptions setVisualOffset(int visualOffset) {
        this.visualOffset = visualOffset;
        return this;
    }
    public float getMusicRate() {
        return musicRate;
    }
    public GameOptions setMusicRate(float musicRate) {
        this.musicRate = musicRate;
        return this;
    }
    public float getMini() {
        return mini;
    }
    public GameOptions setMini(float mini) {
        this.mini = mini;
        return this;
    }
}

