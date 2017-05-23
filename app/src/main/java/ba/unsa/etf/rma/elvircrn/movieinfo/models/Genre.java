package ba.unsa.etf.rma.elvircrn.movieinfo.models;

import java.util.TreeMap;

public class Genre {
    int id;
    private static String locale;

    private TreeMap<String, String> translations = new TreeMap<>();

    public Genre(int id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.setImgUrl(imgUrl);
    }

    public Genre(String name, String imgUrl) {
        this.name = name;
        this.setImgUrl(imgUrl);
    }

    public static void setLocale(String locale) {
        Genre.locale = locale;
    }

    protected String name;

    public String getName() {
        if (locale != null && locale != "" && translations.containsKey(locale)) {
            return translations.get(locale);
        } else {
            return name;
        }
    }

    /**
     * Implementirana je podrska za jednostavno dodavanje jezika, za slucaj da je bilo potrebno
     * dodati i tu mogucnost.
     */
    public Genre addTranslation(String language, String translation) {
        translations.put(language, translation);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl.replace(" ", "").toLowerCase();
    }

    protected String imgUrl = "genredefault";

    public int getId() {
        return id;
    }
}
