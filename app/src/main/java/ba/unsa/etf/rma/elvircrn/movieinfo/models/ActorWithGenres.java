package ba.unsa.etf.rma.elvircrn.movieinfo.models;


import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Relation;

import java.util.List;

public class ActorWithGenres {
    @Embedded
    public Actor actor;
    @Relation(parentColumn = "id",
            entityColumn = "genre_id",
            entity = ActorGenre.class, projection = "genre_id")
    public List<Integer> genreIds;


    public ActorWithGenres() { }
    @Ignore
    public ActorWithGenres(Actor actor, List<Integer> genreIds) {
        this.actor = actor;
        this.genreIds = genreIds;
    }
}
