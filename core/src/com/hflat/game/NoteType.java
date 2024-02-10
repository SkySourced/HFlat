package com.hflat.game;

public enum NoteType {
    TAP('1'),
    HOLD('2'),
    MINE('M'),
    JACK('4'),
    END('3');

    final char smChar;

    NoteType(char smChar) {
        this.smChar = smChar;
    }
}
