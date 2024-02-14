package com.hflat.game;

/**
 * An enum to represent the different types of notes
 * */
public class Note {

    public Lane lane;
    public float barTime;
    public long time;
    public NoteType type;
    public NoteDenom colour;

    public Note(Lane lane, float barTime, int bpm, NoteType type) {
        this.lane = lane;
        this.barTime = barTime;
        this.time = (long) (barTime * 60 / bpm * 1000); // this I think is in ms, but we might want to make it a bit more precise
        this.type = type;
        this.colour = NoteDenom.fromLength(this.barTime % 1); // this also needs to be changed
    }

    /**
     * Get the lane of the note
     * @return the Lane of the note
     */
    public Lane getLane() {
        return lane;
    }

    /**
     * Get the time of the note in number of bars since the start of the song
     * @return the time of the note
     */
    public float getTime() {
        return barTime;
    }

    /**
     * Get the type of the note
     * @return the NoteType for the note
     */
    public NoteType getType() {
        return type;
    }

    /**
     * Determine the judgement for a note based on the time the player hit it
     * @return the Judgement for the note
     */
    public Judgement judge(float time) {
        float diff = Math.abs(this.barTime - time);
        // this.time is in fractions of bars and float time is probably easiest in some division of seconds
        // I think i thought i would convert it when the note was hit bc its essentially just bars * bpm but i will probably forget that
        //Yeah maybe just have it all converted while it's loading that'll make the performance a little better
        //Not like it's that hard to run
        // like multiply all the this.time s by 60/bpm when loading in to a chart
        //Well I mean it might make for slightly better performance?
        // i think a multiplication per note hit is fine
        //Ik
        // actually we could have two different times one in bars and one in seconds
        // the Judgement.timingWindows are in ms
        //That is a good idea
        //Is there any way for me to see the window when we run it?
        // oh not right now i think
        // it kind of sucks rn its scaled for my laptop and its tiny on my monitor
        // if we can find a way to get the resolution of the monitor we could scale it to that
        //
        //I'll look into resolution now
        // I will probably commit then go eat
        //I need to as well
        // okay i will try get the last of the note parsing to work tonight if my brain feels like working
        //Yep
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
