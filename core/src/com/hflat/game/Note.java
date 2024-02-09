package com.hflat.game;

public class Note {

    public Lane lane;
    public float time;
    public NoteType type;
    public NoteDenom colour;

    public Note(Lane lane, float time, NoteType type) {
        this.lane = lane;
        this.time = time;
        this.type = type;
        this.colour = NoteDenom.fromLength(this.time % 1);
    }

    public Lane getLane() {
        return lane;
    }

    public float getTime() {
        return time;
    }

    public NoteType getType() {
        return type;
    }

    public Judgement judge(float time) {
        float diff = Math.abs(this.time - time);
        Judgement judgement;
        if (diff < Judgement.MARVELLOUS.timingWindow) {
            judgement = Judgement.MARVELLOUS;
        } else if (diff < Judgement.FANTASTIC.timingWindow) {
            judgement = Judgement.FANTASTIC;
        } else if (diff < Judgement.EXCELLENT.timingWindow) {
            judgement = Judgement.EXCELLENT;
        } else if (diff < Judgement.GREAT.timingWindow) {
            judgement = Judgement.GREAT;
        } else if (diff < Judgement.DECENT.timingWindow) {
            judgement = Judgement.DECENT;
        } else if (diff < Judgement.WAY_OFF.timingWindow) {
            judgement = Judgement.WAY_OFF;
        } else {
            judgement = Judgement.MISS;
        }
        return judgement;
    }
}
