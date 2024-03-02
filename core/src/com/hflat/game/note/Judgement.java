package com.hflat.game.note;


import com.badlogic.gdx.graphics.Color;
import com.hflat.game.HFlatGame;

public enum Judgement {
    MARVELLOUS(0.015f, 4, HFlatGame.Ref.MARVELLOUS_COLOUR),
    FANTASTIC(0.023f, 3, HFlatGame.Ref.FANTASTIC_COLOUR),
    EXCELLENT(0.0445f, 2, HFlatGame.Ref.EXCELLENT_COLOUR),
    GREAT(0.1035f, 1, HFlatGame.Ref.GREAT_COLOUR),
    GOOD(0.1425f, 0, HFlatGame.Ref.GOOD_COLOUR),
    DECENT(0.1615f, 1, HFlatGame.Ref.DECENT_COLOUR),
    WAY_OFF(0.2145f, -2, HFlatGame.Ref.WAY_OFF_COLOUR),
    MISS(0, -3, HFlatGame.Ref.MISS_COLOUR);

    final float timingWindow;
    final int score;
    final Color color;

    Judgement(float timingWindow, int score, Color color) {
        this.timingWindow = timingWindow;
        this.score = score;
        this.color = color;
    }

    public float getTimingWindow() {
        return timingWindow;
    }

    public int getScore() {
        return score;
    }

    public Color getColor() {
        return color;
    }
}
