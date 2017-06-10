package ba.unsa.etf.rma.elvircrn.movieinfo.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.TreeMap;

@Entity
public class Genre {
    @ColumnInfo(name = "id")
    @PrimaryKey
    protected int id;
    @ColumnInfo(name = "name")
    protected String name;

    @Ignore
    protected String imgUrl = "genredefault";
    @Ignore
    private static String locale;
    @Ignore
    private TreeMap<String, String> translations = new TreeMap<>();

    public Genre() { }

    @Ignore
    public Genre(int id, String name, String imgUrl) {
        this.id = id;
        this.name = name;
        this.setImgUrl(imgUrl);
    }

    @Ignore
    public Genre(String name, String imgUrl) {
        this.name = name;
        this.setImgUrl(imgUrl);
    }

    public static void setLocale(String locale) {
        Genre.locale = locale;
    }

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

    public int getId() {
        return id;
    }
}
