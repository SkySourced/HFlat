package com.hflat.game.note;

import com.hflat.game.chart.Play;

public class PlayNote extends Note {

    private Judgement judgement;
    Play parent;

    public PlayNote(int id, Lane lane, float barTime, float bpm, NoteType type, NoteDenom colour, Play parent) {
        super(id, lane, barTime, bpm, type, colour);
        this.parent = parent;
    }

    public Judgement getJudgement() {
        return judgement;
    }

    public void setJudgement(Judgement judgement) {
        this.judgement = judgement;
    }
}
