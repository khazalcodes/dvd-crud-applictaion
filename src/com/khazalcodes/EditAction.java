package com.khazalcodes;

public enum EditAction {
    TITLE(1),
    RELEASE_DATE(2),
    RATING(3),
    DIRECTOR(4),
    STUDIO(5),
    REVIEW(6),
    CANCEL(7),
    SAVE(8),;

    public final int value;

    private EditAction(int value) {
        this.value = value;
    }
}
