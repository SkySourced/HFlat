package com.hflat.game.note;


import com.badlogic.gdx.graphics.Color;
import com.hflat.game.HFlatGame;

public enum Judgement {
    /**
     * The different judgements for notes, don't reorder without changing {@link com.hflat.game.chart.Play#judge(Lane)}
     * */
    MARVELLOUS(0.015f, 4, HFlatGame.Ref.MARVELLOUS_COLOUR, 1f),
    FANTASTIC(0.023f, 3, HFlatGame.Ref.FANTASTIC_COLOUR, 0.7f),
    EXCELLENT(0.0445f, 2, HFlatGame.Ref.EXCELLENT_COLOUR, 0.5f),
    GREAT(0.1035f, 1, HFlatGame.Ref.GREAT_COLOUR , 0.3f),
    OK(0.1425f, 0, HFlatGame.Ref.OK_COLOUR , 0.2f),
    DECENT(0.1615f, 1, HFlatGame.Ref.DECENT_COLOUR, 0f),
    WAY_OFF(0.2145f, -2, HFlatGame.Ref.WAY_OFF_COLOUR, -1f),
    MISS(0, -3, HFlatGame.Ref.MISS_COLOUR, -10f);

    final float timingWindow;
    final int score;
    final Color color;
    final float lifeImpact;

    Judgement(float timingWindow, int score, Color color, float lifeImpact) {
        this.timingWindow = timingWindow;
        this.score = score;
        this.color = color;
        this.lifeImpact = lifeImpact;
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

    public float getLifeImpact() {
        return lifeImpact;
    }
}
