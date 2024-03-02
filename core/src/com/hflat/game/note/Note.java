package com.hflat.game.note;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.hflat.game.HFlatGame;

import static com.hflat.game.HFlatGame.options;

/**
 * An enum to represent the different types of notes
 */
public class Note extends Actor {

    public Lane lane;
    public float barTime;
    public long time;
    public int bpm;
    public NoteType type;
    public NoteDenom colour;

    public Note(Lane lane, float barTime, int bpm, NoteType type, NoteDenom colour) {
        this.lane = lane;
        this.barTime = barTime;
        this.bpm = bpm;
        this.time = (long) (barTime * 60 / bpm * 1000); // this I think is in ms, but we might want to make it a bit more precise
        this.type = type;
        this.colour = colour;
    }

    /**
     * Get the lane of the note
     *
     * @return the Lane of the note
     */
    public Lane getLane() {
        return lane;
    }

    /**
     * Get the time of the note in number of bars since the start of the song
     *
     * @return the time of the note
     */
    public float getTime() {
        return barTime;
    }

    /**
     * Get the type of the note
     *
     * @return the NoteType for the note
     */
    public NoteType getType() {
        return type;
    }

    /**
     * Determine the judgement for a note based on the time the player hit it
     *
     * @return the Judgement for the note
     */
    public Judgement judge(float time) {
        float diff = Math.abs(this.barTime - time);
        Judgement judgement;
        if (diff < Judgement.MARVELLOUS.timingWindow) {
            judgement = Judgement.MARVELLOUS;
        } else if (diff < Judgement.FANTASTIC.timingWindow) {
            judgement = Judgement.FANTASTIC;
        } else if (diff < Judgement.EXCELLENT.timingWindow) {
            judgement = Judgement.EXCELLENT;
        } else if (diff < Judgement.GREAT.timingWindow) {
            judgement = Judgement.GREAT;
        } else if (diff < Judgement.GOOD.timingWindow) {
            judgement = Judgement.GOOD;
        } else if (diff < Judgement.DECENT.timingWindow) {
            judgement = Judgement.DECENT;
        } else if (diff < Judgement.WAY_OFF.timingWindow) {
            judgement = Judgement.WAY_OFF;
        } else {
            judgement = Judgement.MISS;
        }
        return judgement;
    }

    /**
     * Set the initial position of the note
     * @return the initial y position of the note
     */
    private float setInitialY() {
        return barTime * bpm * options.getNoteSpeed() * options.getMusicRate() * HFlatGame.NOTE_SPACING;
    }

    public static void drawNote(Texture texture, Lane lane, SpriteBatch batch) {
        Sprite sprite = new Sprite(texture);
        int firstXCoord = (int) (Gdx.graphics.getWidth() - 4 * HFlatGame.Ref.DEFAULT_ARROW_SIZE * options.getMini() - 3 * HFlatGame.Ref.DEFAULT_ARROW_SPACING * options.getMini()) / 2;
        sprite.setBounds(firstXCoord + lane.toInt() * 95 * options.getMini(), 560, HFlatGame.Ref.DEFAULT_ARROW_SIZE * options.getMini(), HFlatGame.Ref.DEFAULT_ARROW_SIZE * options.getMini());
        sprite.setOriginCenter();
        sprite.setRotation(lane.rotation);
        sprite.draw(batch);
    }

    public static void drawNote(Texture texture, Lane lane, SpriteBatch batch, int y) {
        Sprite sprite = new Sprite(texture);
        sprite.setBounds(20 + lane.toInt() * 95, y, 80 * options.getMini(), 80 * options.getMini());
        sprite.setOriginCenter();
        sprite.setRotation(lane.rotation);
        sprite.draw(batch);
    }

    public String toString() {
        return "Note: " + lane + " " + barTime + " " + type + " " + colour;
    }
}
