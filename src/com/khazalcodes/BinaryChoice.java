package com.khazalcodes;

public enum BinaryChoice {
    YES(1),
    NO(2);

    public final int value;

    private BinaryChoice(int value) {
        this.value = value;
    }
}
