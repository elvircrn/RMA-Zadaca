package ba.unsa.etf.rma.elvircrn.movieinfo.models;


import java.io.Serializable;
import java.util.Locale;

import ba.unsa.etf.rma.elvircrn.movieinfo.helpers.Helpers;

public class Glumac implements Serializable{
    public String getGodinaFormatted() {
        return new StringBuilder().append(String.format(Locale.getDefault(), "%d", godinaRodjenja))
                .append(" / ")
                .append(godinaSmrti == -1 ? "N/A" :
                        String.format(Locale.getDefault(), "%d", godinaSmrti))
                .toString();
    }

    public enum Spol { MUSKI, ZENSKI, NONBINARY };

    public Glumac(String ime,
                  String prezime,
                  String mjestoRodjenja,
                  int godinaRodjenja,
                  int rating,
                  int godinaSmrti,
                  Spol spol,
                  String biografija,
                  String imdbLink) {
        this.ime = ime;
        this.prezime = prezime;
        this.mjestoRodjenja = mjestoRodjenja;
        this.godinaRodjenja = godinaRodjenja;
        this.rating = rating;
        this.godinaSmrti = godinaSmrti;
        this.spol = spol;
        this.biografija = biografija;
        this.imdbLink = imdbLink;
    }

    private String ime;
    private String prezime;
    private String mjestoRodjenja;
    private int godinaRodjenja;
    private int rating;
    private int godinaSmrti = -1;
    private Spol spol;
    private String biografija;
    private String imdbLink;
    private String imgUrl;

    public Glumac(String ime, String prezime, String mjestoRodjenja, int godinaRodjenja, int rating, int godinaSmrti, Spol spol, String biografija, String imdbLink, String imageUrl) {
        this.ime = ime;
        this.prezime = prezime;
        this.mjestoRodjenja = mjestoRodjenja;
        this.godinaRodjenja = godinaRodjenja;
        this.rating = rating;
        this.godinaSmrti = godinaSmrti;
        this.spol = spol;
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

    public Spol getSpol() {
        return spol;
    }

    public void setSpol(Spol spol) {
        this.spol = spol;
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
}
