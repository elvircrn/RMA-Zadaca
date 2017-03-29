package ba.unsa.etf.rma.elvircrn.movieinfo.models;


import android.graphics.Color;

import java.io.Serializable;
import java.util.Locale;

import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.Helpers;

public class Actor implements Serializable {
    public String getGodinaFormatted() {
        return new StringBuilder().append(String.format(Locale.getDefault(), "%d", godinaRodjenja))
                .append(" / ")
                .append(godinaSmrti == -1 ? "N/A" :
                        String.format(Locale.getDefault(), "%d", godinaSmrti))
                .toString();
    }

    public enum Gender {MALE, FEMALE, NONBINARY };

    public Actor(String ime,
                 String prezime,
                 String mjestoRodjenja,
                 int godinaRodjenja,
                 int rating,
                 int godinaSmrti,
                 Gender gender,
                 String biografija,
                 String imdbLink) {
        this.ime = ime;
        this.prezime = prezime;
        this.mjestoRodjenja = mjestoRodjenja;
        this.godinaRodjenja = godinaRodjenja;
        this.rating = rating;
        this.godinaSmrti = godinaSmrti;
        this.gender = gender;
        this.biografija = biografija;
        this.imdbLink = imdbLink;
    }

    private String ime;
    private String prezime;
    private String mjestoRodjenja;
    private int godinaRodjenja;
    private int rating;
    private int godinaSmrti = -1;
    private Gender gender;
    private String biografija;
    private String imdbLink;
    private String imgUrl;

    public Actor(String ime, String prezime, String mjestoRodjenja, int godinaRodjenja, int rating, int godinaSmrti, Gender gender, String biografija, String imdbLink, String imageUrl) {
        this.ime = ime;
        this.prezime = prezime;
        this.mjestoRodjenja = mjestoRodjenja;
        this.godinaRodjenja = godinaRodjenja;
        this.rating = rating;
        this.godinaSmrti = godinaSmrti;
        this.gender = gender;
        this.biografija = biografija;
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

    public int getGodinaSmrti() {
        return godinaSmrti;
    }

    public void setGodinaSmrti(int godinaSmrti) {
        this.godinaSmrti = godinaSmrti;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getBiografija() {
        return biografija;
    }

    public void setBiografija(String biografija) {
        this.biografija = biografija;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getMjestoRodjenja() {
        return mjestoRodjenja;
    }

    public void setMjestoRodjenja(String mjestoRodjenja) {
        this.mjestoRodjenja = mjestoRodjenja;
    }

    public int getGodinaRodjenja() {
        return godinaRodjenja;
    }

    public void setGodinaRodjenja(int godinaRodjenja) {
        this.godinaRodjenja = godinaRodjenja;
    }

    public boolean isDead() { return godinaSmrti == -1; }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getFullName() {
        return Helpers.coalesce(new StringBuffer().append(ime)
                                                  .append(" ")
                                                  .append(prezime).toString(), "");
    }

    public String getImageUrl() {
        return imgUrl;
    }

    public Gender getGenderColor() { return gender; }
}
