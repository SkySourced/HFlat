package com.hflat.game.note;

import com.hflat.game.chart.Play;

import static com.hflat.game.HFlatGame.options;

public class PlayNote extends Note {

    private Judgement judgement;
    Play parent;
    private final long time;

    public PlayNote(int id, Lane lane, float barTime, float bpm, NoteType type, NoteDenom colour, Play parent) {
        super(id, lane, barTime, bpm, type, colour);
        this.parent = parent;
        this.time = (long) (barTime /(4/bpm) * 60 * options.getMusicRate()); // in ms
    }

    public Judgement getJudgement() {
        return judgement;
    }

    public void setJudgement(Judgement judgement) {
        this.judgement = judgement;
    }

    public float getTime() {
        return time;
    }
}
