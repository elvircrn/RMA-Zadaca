package ba.unsa.etf.rma.elvircrn.movieinfo.mappers;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;
import ba.unsa.etf.rma.elvircrn.movieinfo.services.dto.PersonDTO;

public class PersonMapper {
    private PersonMapper() { }

    public static Actor toActorFromActor(Actor actor, PersonDTO personDTO) {
        actor.setName(personDTO.getName());
        actor.setId(personDTO.getId());
        actor.setBiography(personDTO.getBiography());

        if (personDTO.getProfilePath() != null)
            actor.setImageUrl(Actor.IMAGE_BASE.concat(personDTO.getProfilePath()));

        if (personDTO.getBirthday() != null && !personDTO.getBirthday().isEmpty())
            actor.setYearOfBirth(new Scanner(personDTO.getBirthday()).useDelimiter("\\D+").nextInt());

        if (personDTO.getDeathday() != null && !personDTO.getDeathday().isEmpty())
            actor.setYearOfDeath(new Scanner(personDTO.getDeathday()).useDelimiter("\\D+").nextInt());

        if (personDTO.getGender() != null)
            actor.setGender(Actor.Gender.values()[personDTO.getGender()]);

        if (personDTO.getImdbId() != null && !personDTO.getImdbId().isEmpty())
            actor.setImdbLink(Actor.IMDB_BASE.concat(personDTO.getImdbId()));

        if (personDTO.getPopularity() != null)
            actor.setRating(personDTO.getPopularity().intValue());

        if (personDTO.getMovies() != null && !personDTO.getMovies().isEmpty())
            actor.setMovies(MovieMapper.toMovies(personDTO.getMovies()));

        return actor;
    }

    public static Actor toActor(PersonDTO personDTO) {
        Actor actor = new Actor();
        return toActorFromActor(actor, personDTO);
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

    /**
     * Posto Gson ne podrzava updejtovanje postojecih java objekata, a kako se podaci koji dobiju
     * putem servisa za detalje ne nalaze u relaciji kao podskup sa podacima koji se dobiju iz
     * servisa za pretragu, to je potrebno 'rucno' izvrsiti azuriranje postojceg DTO-a a.
     * @param a
     * @param b
     * @return potpun objekat koji sadrzi sve potrebne informacije o nekoj osobi
     */
    public static PersonDTO expand(PersonDTO a, PersonDTO b) {
        a.setBiography(b.getBiography());
        a.setBirthday(b.getBirthday());
        a.setDeathday(b.getDeathday());
        a.setGender(b.getGender());
        a.setHomepage(b.getHomepage());
        a.setPlaceOfBirth(b.getPlaceOfBirth());
        return a;
    }
}
