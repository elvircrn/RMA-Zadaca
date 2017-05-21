package ba.unsa.etf.rma.elvircrn.movieinfo.common.mappers;

import java.util.ArrayList;
import java.util.List;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;

public class ActorMapper {
    private ActorMapper() { }

    public static Actor toActor(PersonDTO personDTO) {
        Actor actor = new Actor();
        actor.setName(personDTO.getName());
        actor.setId(personDTO.getId());
        actor.setImageUrl(personDTO.getProfilePath());
        return actor;
    }

    public static ArrayList<Actor> getActorModels(List<PersonDTO> personDTOs) {
        if (personDTOs == null)
            return new ArrayList<>();
        ArrayList<Actor> actors = new ArrayList<>();
        for (PersonDTO personDTO : personDTOs) {
            actors.add(toActor(personDTO));
        }
        return actors;
    }
}
