package ba.unsa.etf.rma.elvircrn.movieinfo.common.mappers;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.ActorDTO;

public class ActorMapper {
    private ActorMapper() { }

    public static Actor toActor(ActorDTO actorDTO) {
        Actor actor = new Actor();
        actor.setName(actorDTO.getName());
        actor.setId(actorDTO.getId());
        actor.setImageUrl(actorDTO.getProfilePath());
        return actor;
    }

    public static ArrayList<Actor> getActorModels(List<ActorDTO> actorDTOs)
    {
        ArrayList<Actor> actors = new ArrayList<>();
        for (ActorDTO actorDTO : actorDTOs) {
            actors.add(toActor(actorDTO));
        }
        return actors;
    }
}
