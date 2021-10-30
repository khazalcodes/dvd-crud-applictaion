package com.khazalcodes;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * The model object of a DVD title wihtin the library. Most of the code is self explanatory. There is the Key static
 * variable which will increment anytime a new Model is instantiated. Then there are the DateTimeFormatters which
 * allow for convienient parsing of dates, converting to a more readable format as well as storage in the csv in the
 * original and more parseable format.
 * */
public class DVDModel {

    public static final DateTimeFormatter originalFormat = DateTimeFormatter.ofPattern("ddMMyyyy");
    public static final DateTimeFormatter targetFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static int CURRENT_AVAILABLE_KEY = 0;

    private String title;
    private LocalDate releaseDate;
    private String rating;
    private String director;
    private String studio;
    private String review;
    private final int key;


    public DVDModel(String title, String releaseDate, String rating, String director, String studio, String review) {
        this.title = title;
        this.releaseDate = LocalDate.parse(releaseDate, originalFormat);
        this.rating = rating;
        this.director = director;
        this.studio = studio;
        this.review = review;
        this.key = CURRENT_AVAILABLE_KEY;
        CURRENT_AVAILABLE_KEY++;

    }

    public int getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate.format(targetFormat);
    }

    public String getReleaseDateInOriginalFormat() { return releaseDate.format(originalFormat); }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = LocalDate.parse(releaseDate, originalFormat);
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String asFileLine() {
        String sep = ",";

        return getTitle() + sep +
                getReleaseDateInOriginalFormat() + sep +
                getRating() + sep +
                getDirector() + sep +
                getStudio() + sep +
                getReview() + sep;
    }
}
