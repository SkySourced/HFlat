package com.hflat.game;


public enum Judgement {
    MARVELLOUS(0.015f, 3),
    FANTASTIC(0.023f, 3),
    EXCELLENT(0.0445f, 2),
    GREAT(0.1035f, 1),
    DECENT(0.1365f, 0),
    WAY_OFF(0.1815f, 0),
    MISS(0, 0);

    final float timingWindow; //
    final int score;

    Judgement(float timingWindow, int score) {
        this.timingWindow = timingWindow;
        this.score = score;
    }
}
