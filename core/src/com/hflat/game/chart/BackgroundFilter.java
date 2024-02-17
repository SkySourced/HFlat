package com.hflat.game.chart;

public enum BackgroundFilter {
    NONE (0f),
    DARK (0.5f),
    DARKER (0.75f),
    DARKEST (0.9f),
    NO_BACKGROUND (1f);

    final float opacity;
    BackgroundFilter(float opacity) {
        this.opacity = opacity;
    }

    public float getOpacity() {
        return opacity;
    }

    @Override
    public String toString() {
        return switch (this) {
            case NONE -> "None";
            case DARK -> "Dark";
            case DARKER -> "Darker";
            case DARKEST -> "Darkest";
            case NO_BACKGROUND -> "No Background";
        };
    }

    public BackgroundFilter next() {
        return switch (this) {
            case NONE -> DARK;
            case DARK -> DARKER;
            case DARKER -> DARKEST;
            case DARKEST -> NO_BACKGROUND;
            case NO_BACKGROUND -> NONE;
        };
    }

    public BackgroundFilter previous() {
        return switch (this) {
            case NONE -> NO_BACKGROUND;
            case DARK -> NONE;
            case DARKER -> DARK;
            case DARKEST -> DARKER;
            case NO_BACKGROUND -> DARKEST;
        };
    }
}
