package net.anonhub.commonlib;

public enum Direction {
    UP_R(Type.RELATIVE),
    DOWN_R(Type.RELATIVE),
    RIGHT(Type.RELATIVE),
    LEFT(Type.RELATIVE),
    FORWARD(Type.RELATIVE),
    BACKWARD(Type.RELATIVE),

    UP_C(Type.CARDINAL),
    DOWN_C(Type.CARDINAL),
    NORTH(Type.CARDINAL),
    SOUTH(Type.CARDINAL),
    EAST(Type.CARDINAL),
    WEST(Type.CARDINAL);

    public final Type type;

    Direction(Type type) {
        this.type = type;
    }

    public enum Type{
        CARDINAL, RELATIVE
    }
}
