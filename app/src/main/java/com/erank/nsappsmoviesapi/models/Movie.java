package com.erank.nsappsmoviesapi.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.erank.nsappsmoviesapi.utils.ArrayConverter;
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "Movies")
public class Movie {

    @PrimaryKey @NonNull
    private String title;
    private int releaseYear;

    private String image;
    private double rating;

    @TypeConverters(ArrayConverter.class)
    @SerializedName("genre")
    private String[] genres;

    public Movie(String title, String image, double rating, int releaseYear, String[] genres) {
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.genres = genres;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public String[] getGenres() {
        return genres;
    }

    public void setGenres(String[] genres) {
        this.genres = genres;
    }
}