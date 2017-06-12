package ba.unsa.etf.rma.elvircrn.movieinfo.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ActorGenre {
    public ActorGenre() { }
    @Ignore
    public ActorGenre(int id, int actorId, int genreId) {
        this.id = id;
        this.actorId = actorId;
        this.genreId = genreId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getActorId() {
        return actorId;
    }

    public void setActorId(int actorId) {
        this.actorId = actorId;
    }

    public int getGenreId() {
        return genreId;
    }

    public void setGenreId(int genreId) {
        this.genreId = genreId;
    }

    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "actor_id")
    private int actorId;
    @ColumnInfo(name = "genre_id")
    private int genreId;

}
