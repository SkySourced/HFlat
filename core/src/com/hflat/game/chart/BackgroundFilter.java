package com.hflat.game.chart;

public enum BackgroundFilter {
    NONE (0f),
    DARK (0.5f),
    DARKER (0.75f),
    DARKEST (0.9f),
    OFF (1f);

    final float opacity;
    BackgroundFilter(float opacity) {
        this.opacity = opacity;
    }

    public float getOpacity() {
        return opacity;
    }

    @Override
    public String toString() {
        switch (this) {
            case NONE:
                return "None";
            case DARK:
                return "Dark";
            case DARKER:
                return "Darker";
            case DARKEST:
                return "Darkest";
            case OFF:
                return "Off";
            default:
                return "None";
        }
    }

    public BackgroundFilter next() {
        switch (this) {
            case NONE:
                return DARK;
            case DARK:
                return DARKER;
            case DARKER:
                return DARKEST;
            case DARKEST:
                return OFF;
            case OFF:
                return NONE;
            default:
                return NONE;
        }
    }

    public BackgroundFilter previous() {
        switch (this) {
            case NONE:
                return OFF;
            case DARK:
                return NONE;
            case DARKER:
                return DARK;
            case DARKEST:
                return DARKER;
            case OFF:
                return DARKEST;
            default:
                return NONE;
        }
    }
}
