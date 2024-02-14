package com.hflat.game;

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
        if (length == 1.0f) {
            return FOURTH;
        } else if (length == 0.5f) {
            return EIGHTH;
        } else if (length == 1.0f/3.0f) {
            return TWELFTH;
        } else if (length == 0.25f) {
            return SIXTEENTH;
        } else if (length == 1.0f/6.0f) {
            return TWENTYFOURTH;
        } else if (length == 0.125f) {
            return THIRTYSECOND;
        } else if (length == 0.0625f) {
            return SIXTYFOURTH;
        } else if (length == 0.03125f) {
            return ONEHUNDREDTWENTYEIGHTH;
        } else if (length == 4f/192f) {
            return ONEHUNDREDNINETYSECOND;
        } else {
            return null;
        }
    }

    public Texture getTexture() {
        return texture;
    }
}
