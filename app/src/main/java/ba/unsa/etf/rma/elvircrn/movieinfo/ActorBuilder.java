package ba.unsa.etf.rma.elvircrn.movieinfo;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;

public class ActorBuilder {
    private String ime = "Stan";
    private String prezime = "Lee";
    private String mjestoRodjenja = "New York";
    private int godinaRodjenja = 1921;
    private int rating = 5;
    private int godinaSmrti = -1;
    private Actor.Gender gender = Actor.Gender.MALE; // random.org
    private String biografija = "BIOGRAFIJA";
    private String imdbLink = "https://www.google.com";
    private String imgUrl = "tsm1";

    public ActorBuilder setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public ActorBuilder setIme(String ime) {
        this.ime = ime;
        return this;
    }

    public ActorBuilder setPrezime(String prezime) {
        this.prezime = prezime;
        return this;
    }

    public ActorBuilder setMjestoRodjenja(String mjestoRodjenja) {
        this.mjestoRodjenja = mjestoRodjenja;
        return this;
    }

    public ActorBuilder setGodinaRodjenja(int godinaRodjenja) {
        this.godinaRodjenja = godinaRodjenja;
        return this;
    }

    public ActorBuilder setRating(int rating) {
        this.rating = rating;
        return this;
    }

    public ActorBuilder setGodinaSmrti(int godinaSmrti) {
        this.godinaSmrti = godinaSmrti;
        return this;
    }

    public ActorBuilder setGender(Actor.Gender gender) {
        this.gender = gender;
        return this;
    }

    public ActorBuilder setBiografija(String biografija) {
        this.biografija = biografija;
        return this;
    }

    public ActorBuilder setImdbLink(String imdbLink) {
        this.imdbLink = imdbLink;
        return this;
    }

    public Actor createActor() {
        return new Actor(ime, prezime, mjestoRodjenja, godinaRodjenja, rating, godinaSmrti, gender, biografija, imdbLink, imgUrl);
    }
}