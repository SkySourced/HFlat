package com.hflat.game.note;

public enum Lane {
    LEFT,
    DOWN,
    UP,
    RIGHT;

    public static Lane fromInt(int i) {
        return switch (i) {
            case 0 -> LEFT;
            case 1 -> DOWN;
            case 2 -> UP;
            case 3 -> RIGHT;
            default -> null;
        };
    }
}
