package com.hflat.game;

/**
 * An enum to represent the different types of notes
 * */
public enum NoteType {
    TAP('1'),
    HOLD('2'),
    MINE('M'),
    JACK('4'),
    END('3');

    final char smChar; // the character used in the .sm file to represent this note type

    NoteType(char smChar) {
        this.smChar = smChar;
    }
}
