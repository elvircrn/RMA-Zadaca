package ba.unsa.etf.rma.elvircrn.movieinfo.models;

import android.support.v4.util.Pair;

import java.util.ArrayList;
import java.util.TreeMap;

public class Genre {
    private static String locale;

    private TreeMap<String, String> translations;

    public Genre(String name, String imgUrl) {
        this.name = name;
        this.imgUrl = imgUrl;
        translations = new TreeMap<>();
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
        this.imgUrl = imgUrl;
    }

    protected String imgUrl;
}
