package ba.unsa.etf.rma.elvircrn.movieinfo.models;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Locale;

import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.JHelpers;

public class Actor implements Parcelable {
    public String getGodinaFormatted() {
        return new StringBuilder().append(String.format(Locale.getDefault(), "%d", yearOfBirth))
                .append(" / ")
                .append(yearOfDeath == -1 ? "N/A" :
                        String.format(Locale.getDefault(), "%d", yearOfDeath))
                .toString();
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public enum Gender { MALE, FEMALE, NONBINARY };

    public Actor(String name,
                 String surname,
                 String placeOfBirth,
                 int yearOfBirth,
                 int rating,
                 int yearOfDeath,
                 Gender gender,
                 String biography,
                 String imdbLink) {
        this.name = name;
        this.surname = surname;
        this.placeOfBirth = placeOfBirth;
        this.yearOfBirth = yearOfBirth;
        this.rating = rating;
        this.yearOfDeath = yearOfDeath;
        this.gender = gender;
        this.biography = biography;
        this.imdbLink = imdbLink;
    }

    private String name;
    private String surname;
    private String placeOfBirth;
    private int yearOfBirth;
    private int rating;
    private int yearOfDeath = -1;
    private Gender gender;
    private String biography;
    private String imdbLink;
    private String imgUrl;
    private int  id;

    public Actor(String name,
                 String surname,
                 String placeOfBirth,
                 int yearOfBirth,
                 int rating,
                 int yearOfDeath,
                 Gender gender,
                 String biography,
                 String imdbLink,
                 String imageUrl) {
        this.name = name;
        this.surname = surname;
        this.placeOfBirth = placeOfBirth;
        this.yearOfBirth = yearOfBirth;
        this.rating = rating;
        this.yearOfDeath = yearOfDeath;
        this.gender = gender;
        this.biography = biography;
        this.imdbLink = imdbLink;
        this.imgUrl = imageUrl;
    }


    public void setImageUrl(String imageUrl) {
        this.imgUrl = imageUrl;
    }

    public String getImdbLink() {
        return imdbLink;
    }

    public void setImdbLink(String imdbLink) {
        this.imdbLink = imdbLink;
    }

    public int getYearOfDeath() {
        return yearOfDeath;
    }

    public void setYearOfDeath(int yearOfDeath) {
        this.yearOfDeath = yearOfDeath;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBiography() {
        return biography;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public boolean isDead() { return yearOfDeath == -1; }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFullName() {
        return JHelpers.coalesce(new StringBuffer().append(name)
                                                  .append(" ")
                                                  .append(surname).toString(), "");
    }

    public String getImageUrl() {
        return imgUrl;
    }

    public Gender getGenderColor() { return gender; }

    // Parcelable

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.surname);
        dest.writeString(this.placeOfBirth);
        dest.writeInt(this.yearOfBirth);
        dest.writeInt(this.rating);
        dest.writeInt(this.yearOfDeath);
        dest.writeInt(this.gender == null ? -1 : this.gender.ordinal());
        dest.writeString(this.biography);
        dest.writeString(this.imdbLink);
        dest.writeString(this.imgUrl);
    }

    protected Actor(Parcel in) {
        this.name = in.readString();
        this.surname = in.readString();
        this.placeOfBirth = in.readString();
        this.yearOfBirth = in.readInt();
        this.rating = in.readInt();
        this.yearOfDeath = in.readInt();
        int tmpGender = in.readInt();
        this.gender = tmpGender == -1 ? null : Gender.values()[tmpGender];
        this.biography = in.readString();
        this.imdbLink = in.readString();
        this.imgUrl = in.readString();
    }

    public static final Parcelable.Creator<Actor> CREATOR = new Parcelable.Creator<Actor>() {
        public Actor createFromParcel(Parcel source) {
            return new Actor(source);
        }

        public Actor[] newArray(int size) {
            return new Actor[size];
        }
    };
}
