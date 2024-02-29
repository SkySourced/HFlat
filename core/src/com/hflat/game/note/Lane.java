package com.hflat.game.note;

public enum Lane {
    LEFT (270),
    DOWN (180),
    UP (0),
    RIGHT (90);

    final int rotation;
    Lane (int rotation) {
        this.rotation = rotation;
    }

    public static Lane fromInt(int i) {
        return switch (i) {
            case 0 -> LEFT;
            case 1 -> DOWN;
            case 2 -> UP;
            case 3 -> RIGHT;
            default -> null;
        };
    }

    public int toInt() {
        return switch (this) {
            case LEFT -> 0;
            case DOWN -> 1;
            case UP -> 2;
            case RIGHT -> 3;
        };
    }

    public int getRotation() {
        return rotation;
    }
}
