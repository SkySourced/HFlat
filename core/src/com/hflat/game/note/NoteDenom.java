package com.hflat.game.note;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static com.hflat.game.Game.Ref.DENOM_ROUND_PLACES;

/**
 * An enum to represent note denominations
 * */

/* [FOURTH] 0.25
 * [EIGHTH] 0.125
 * [TWELFTH] 0.083333
 * [SIXTEENTH] 0.0625
 * [TWENTYFOURTH] 0.041667
 * [THIRTYSECOND] 0.03125
 * [FOURTYEIGHTH] 0.020833
 * [SIXTYFOURTH] 0.015625
 * [ONEHUNDREDTWENTYEIGHTH] 0.0078125
 * [ONEHUNDREDNINETYSECOND] 0.005208
 */
public enum NoteDenom {
    FOURTH (new Texture("notes/note4th.png"), 1f/4f),
    EIGHTH (new Texture("notes/note8th.png"), 1f/8f),
    TWELFTH (new Texture("notes/note12th.png"), (float) (Math.round(Math.pow(10, DENOM_ROUND_PLACES - 1) * (1.0f/12.0f)) / Math.pow(10, DENOM_ROUND_PLACES - 1))),
    SIXTEENTH (new Texture("notes/note16th.png"), 1f/16f),
    TWENTYFOURTH (new Texture("notes/note24th.png"), (float) (Math.round(Math.pow(10, DENOM_ROUND_PLACES - 1) * (1.0f/24.0f)) / Math.pow(10, DENOM_ROUND_PLACES - 1))),
    THIRTYSECOND (new Texture("notes/note32nd.png"), 1f/32f),
    FOURTYEIGHTH (new Texture("notes/note48th.png"), (float) (Math.round(Math.pow(10, DENOM_ROUND_PLACES - 1) * (1.0f/48.0f)) / Math.pow(10, DENOM_ROUND_PLACES - 1))),
    SIXTYFOURTH (new Texture("notes/note64th.png"), 1f/64f),
    ONEHUNDREDTWENTYEIGHTH (new Texture("notes/note128th.png"), 1f/128f),
    ONEHUNDREDNINETYSECOND (new Texture("notes/note192nd.png"), (float) (Math.round(Math.pow(10, DENOM_ROUND_PLACES - 1) * (1f/192f)) / Math.pow(10, DENOM_ROUND_PLACES - 1)));
    
    final Texture texture;
    final float length;
    static final float marginOfError = 0.0001f;
    NoteDenom(Texture texture, float length) {
        this.texture = texture;
        this.length = length;
    }

    /**
     * Returns the length of the note in string form, generally for logging
     * e.g. "4th", "32nd", "128th"
     * @return the length of the note in string form
     */
    public String toString() {
        return switch (this) {
            case FOURTH -> "4th";
            case EIGHTH -> "8th";
            case TWELFTH -> "12th";
            case SIXTEENTH -> "16th";
            case TWENTYFOURTH -> "24th";
            case THIRTYSECOND -> "32nd";
            case FOURTYEIGHTH -> "48th";
            case SIXTYFOURTH -> "64th";
            case ONEHUNDREDTWENTYEIGHTH -> "128th";
            case ONEHUNDREDNINETYSECOND -> "192nd";
            default -> "null";
        };
    }

    // might not be necessary but copilot made it, and it's here just in case

    /**
     * Converts a string to a NoteDenom
     * @param s the string to convert
     * @return the NoteDenom represented by the string
     */
    public static NoteDenom fromString(String s) {
        return switch (s) {
            case "4th" -> FOURTH;
            case "8th" -> EIGHTH;
            case "12th" -> TWELFTH;
            case "16th" -> SIXTEENTH;
            case "24th" -> TWENTYFOURTH;
            case "32nd" -> THIRTYSECOND;
            case "48th" -> FOURTYEIGHTH;
            case "64th" -> SIXTYFOURTH;
            case "128th" -> ONEHUNDREDTWENTYEIGHTH;
            case "192nd" -> ONEHUNDREDNINETYSECOND;
            default -> null;
        };
    }

    public static NoteDenom fromLength(float length) {
        length = length % 1; //
        if (length % FOURTH.length == 0) {
            return FOURTH;
        } else if (length % EIGHTH.length == 0) {
            return EIGHTH;
        } else if (length % SIXTEENTH.length == 0) {
            return SIXTEENTH;
        } else if (length % THIRTYSECOND.length == 0) {
            return THIRTYSECOND;
        } else if (length % SIXTYFOURTH.length == 0) {
            return SIXTYFOURTH;
        } else if (length % ONEHUNDREDTWENTYEIGHTH.length == 0) {
            return ONEHUNDREDTWENTYEIGHTH;
        }
        length = Float.parseFloat(String.valueOf(length).substring(0, String.valueOf(length).length() - 1));
        Gdx.app.debug("NoteDenom", String.valueOf(length));
        if (length % TWELFTH.length - marginOfError < 0) {
            return TWELFTH;
        } else if (length % TWENTYFOURTH.length - marginOfError < 0) {
            return TWENTYFOURTH;
        } else if (length % FOURTYEIGHTH.length - marginOfError < 0) {
            return FOURTYEIGHTH;
        } else if (length % ONEHUNDREDNINETYSECOND.length - marginOfError < 0) {
            return ONEHUNDREDNINETYSECOND;
        } else {
            return null;
        }
    }

    public float getLength() {
        return length;
    }

    public Texture getTexture() {
        return texture;
    }
}
