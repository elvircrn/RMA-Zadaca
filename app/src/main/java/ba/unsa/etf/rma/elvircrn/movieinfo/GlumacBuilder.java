package ba.unsa.etf.rma.elvircrn.movieinfo;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Glumac;

public class GlumacBuilder {
    private String ime = "Stan";
    private String prezime = "Lee";
    private String mjestoRodjenja = "New York";
    private int godinaRodjenja = 1921;
    private int rating = 5;
    private int godinaSmrti = -1;
    private Glumac.Spol spol = Glumac.Spol.MUSKI; // random.org
    private String biografija = "BIOGRAFIJA";
    private String imdbLink = "https://www.google.com";
    private String imgUrl = "tsm1";

    public GlumacBuilder setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public GlumacBuilder setIme(String ime) {
        this.ime = ime;
        return this;
    }

    public GlumacBuilder setPrezime(String prezime) {
        this.prezime = prezime;
        return this;
    }

    public GlumacBuilder setMjestoRodjenja(String mjestoRodjenja) {
        this.mjestoRodjenja = mjestoRodjenja;
        return this;
    }

    public GlumacBuilder setGodinaRodjenja(int godinaRodjenja) {
        this.godinaRodjenja = godinaRodjenja;
        return this;
    }

    public GlumacBuilder setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public GlumacBuilder setGodinaSmrti(int godinaSmrti) {
        this.godinaSmrti = godinaSmrti;
        return this;
    }

    public GlumacBuilder setSpol(Glumac.Spol spol) {
        this.spol = spol;
        return this;
    }

    public GlumacBuilder setBiografija(String biografija) {
        this.biografija = biografija;
        return this;
    }

    public GlumacBuilder setImdbLink(String imdbLink) {
        this.imdbLink = imdbLink;
        return this;
    }

    public Glumac createGlumac() {
        return new Glumac(ime, prezime, mjestoRodjenja, godinaRodjenja, rating, godinaSmrti, spol, biografija, imdbLink, imgUrl);
    }
}