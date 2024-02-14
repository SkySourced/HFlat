package com.hflat.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

/**
 * An enum to represent note denominations
 * */
public enum NoteDenom {
    FOURTH (new Texture("notes/note4th.png"), 1.0f),
    EIGHTH (new Texture("notes/note8th.png"), 0.5f),
    TWELFTH (new Texture("notes/note12th.png"), 1.0f/3.0f),
    SIXTEENTH (new Texture("notes/note16th.png"), 0.25f),
    TWENTYFOURTH (new Texture("notes/note24th.png"), 1.0f/6.0f),
    THIRTYSECOND (new Texture("notes/note32nd.png"), 0.125f),
    SIXTYFOURTH (new Texture("notes/note64th.png"), 0.0625f),
    ONEHUNDREDTWENTYEIGHTH (new Texture("notes/note128th.png"), 0.03125f),
    ONEHUNDREDNINETYSECOND (new Texture("notes/note192nd.png"), 4f/192f);
    
    final Texture texture;
    final float length;

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
            case "64th" -> SIXTYFOURTH;
            case "128th" -> ONEHUNDREDTWENTYEIGHTH;
            case "192nd" -> ONEHUNDREDNINETYSECOND;
            default -> null;
        };
    }

    public static NoteDenom fromLength(float length) {
        Gdx.app.debug("NoteDenom", String.valueOf(length));
        length = length % 1;
        if (length % 1/4f == 0) {
            return FOURTH;
        } else if (length % 1/8f == 0) {
            return EIGHTH;
        } else if (length % 1/12f == 0) {
            return TWELFTH;
        } else if (length % 1/16f == 0) {
            return SIXTEENTH;
        } else if (length % 1/24f == 0) {
            return TWENTYFOURTH;
        } else if (length % 1/32f == 0) {
            return THIRTYSECOND;
        } else if (length % 1/64f == 0) {
            return SIXTYFOURTH;
        } else if (length % 1/128f == 0) {
            return ONEHUNDREDTWENTYEIGHTH;
        } else if (length % 1/192f == 0) {
            return ONEHUNDREDNINETYSECOND;
        } else {
            return null;
        }
    }

    public Texture getTexture() {
        return texture;
    }
}
