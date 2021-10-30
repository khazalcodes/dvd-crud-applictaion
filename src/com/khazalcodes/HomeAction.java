package com.khazalcodes;

public enum HomeAction {
    VIEW(1),
    SEARCH(2),
    ADD(3),
    DELETE(4),
    EDIT(5),
    QUIT(6);

    public final int value;

    private HomeAction(int value) {
        this.value = value;
    }
}
