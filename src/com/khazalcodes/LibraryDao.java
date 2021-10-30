package com.khazalcodes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LibraryDao {
    private static final Path DVD_FILE_PATH = Path.of("./src/com/khazalcodes/dvds.csv");
    private static StringBuilder newLibrary = new StringBuilder();


    public static Map<Integer, DVDModel> readLibrary() {
        Map<Integer, DVDModel> DVDHashMap = new HashMap<>();
        List<String[]> dvdStrings = new ArrayList<>();

        try {
            List<String> lines = Files.readAllLines(DVD_FILE_PATH);
            dvdStrings = lines.stream()
                    .map(line -> line.split(","))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            System.out.println("File could not be read. Check if the path is correct");
        }

        dvdStrings.stream()
                .map(LibraryDao::toDVDModel)
                .forEach(dvdModel -> DVDHashMap.put(dvdModel.getKey(), dvdModel));

        return DVDHashMap;
    }

    public static void saveLibrary(Map<Integer, DVDModel> DVDHashMap) {
        DVDHashMap.values().forEach(LibraryDao::appendToNewLibrary);

        if (newLibrary.length() > 0) {
            newLibrary.deleteCharAt(newLibrary.length() -1); // Remove final line separator
        }

        try {
            Files.writeString(DVD_FILE_PATH, newLibrary.toString());
        } catch (IOException e) {
            System.out.println("File could not be written to. Check if the path is correct");
        }
    }

    private static void appendToNewLibrary(DVDModel model) {
        String sep = ",";

        newLibrary.append(model.getTitle()).append(sep)
                .append(model.getReleaseDateInOriginalFormat()).append(sep)
                .append(model.getRating()).append(sep)
                .append(model.getDirector()).append(sep)
                .append(model.getStudio()).append(sep)
                .append(model.getReview()).append(System.lineSeparator());
    }

    public static DVDModel toDVDModel(String[] dvd) {
        String title = dvd[0];
        String releaseDate = dvd[1];
        String rating = dvd[2];
        String director = dvd[3];
        String studio = dvd[4];
        String review = dvd[5];

        return new DVDModel(title, releaseDate, rating, director, studio, review);
    }
}
