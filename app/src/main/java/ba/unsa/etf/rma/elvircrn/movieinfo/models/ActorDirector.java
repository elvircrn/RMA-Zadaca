package ba.unsa.etf.rma.elvircrn.movieinfo.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class ActorDirector {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;
    @ColumnInfo(name = "actor_id")
    private int actorId;

    @Ignore
    public ActorDirector(int actorId, int directorId) {
        this.actorId = actorId;
        this.directorId = directorId;
    }

    public ActorDirector(int id, int actorId, int directorId) {
        this.id = id;
        this.actorId = actorId;
        this.directorId = directorId;
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

    public int getDirectorId() {
        return directorId;
    }

    public void setDirectorId(int directorId) {
        this.directorId = directorId;
    }

    @ColumnInfo(name = "director_id")

    private int directorId;

}
