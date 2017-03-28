package ba.unsa.etf.rma.elvircrn.movieinfo.models;

public class Genre {
    public Genre(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
    }

    protected String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    protected String imgUrl;
}
