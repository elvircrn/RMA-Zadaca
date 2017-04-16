package ba.unsa.etf.rma.elvircrn.movieinfo;


import java.util.ArrayList;
import java.util.Map;

import ba.unsa.etf.rma.elvircrn.movieinfo.models.Director;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Genre;
import ba.unsa.etf.rma.elvircrn.movieinfo.models.Actor;

public class DataProvider {
    private static DataProvider instance = null;

    public ArrayList<Actor> getActors() {
        return actors;
    }

    public ArrayList<Director> getDirectors() {
        return directors;
    }

    private ArrayList<Actor> actors;
    private ArrayList<Director> directors;
    private ArrayList<Genre> genres;

    public ArrayList<Genre> getGenres() {
        return genres;
    }

    private DataProvider() {
        actors = new ArrayList<>();
        directors = new ArrayList<>();
        genres = new ArrayList<>();
    }

    public static DataProvider getInstance() {
        if (instance == null)
            instance = new DataProvider();
        return instance;
    }

    private void clear() {
        actors.clear();
        directors.clear();
        genres.clear();
    }

    public void seed() {
        clear();

        seedActors();
        seedDirectors();
        seedGenres();

        int id = 0;
        for (Actor actor : actors) {
            actor.setId(id++);
        }
    }

    public void setLocale(String locale) {
        Genre.setLocale(locale);
    }

    void seedGenres() {
        genres.add(new Genre("Horror", "horror").addTranslation("hrvatski", "Horor").addTranslation("bosanski", "Horor"));
        genres.add(new Genre("Comedy", "comedy").addTranslation("hrvatski", "Komedija").addTranslation("bosanski", "Komedija"));
        genres.add(new Genre("Drama", "drama").addTranslation("hrvatski", "Drama").addTranslation("bosanski", "Drama"));
        genres.add(new Genre("SciFi", "scifi").addTranslation("hrvatski", "SciFi").addTranslation("bosanski", "Scifi"));
    }

    void seedDirectors() {
        directors.add(new Director("Quentin", "Tarantino"));
        directors.add(new Director("Stanley", "Kubrick"));
        directors.add(new Director("Martin", "Scorsese"));
        directors.add(new Director("Ridley", "Scott"));
    }

    void seedActors() {
        actors.add(new ActorBuilder().setIme("Stan")
                .setPrezime("Lee")
                .setGodinaRodjenja(1922)
                .setMjestoRodjenja("New York")
                .setRating(5)
                .setImdbLink("http://www.imdb.com/name/nm0498278")
                .setImgUrl("stanlee")
                .setBiografija("Stan Lee[1] (born Stanley Martin Lieber, December 28, 1922) is an American comic-book writer, editor, publisher, media producer, television host, actor, and former president and chairman of Marvel Comics. In collaboration with several artists, including Jack Kirby and Steve Ditko, he created Spider-Man, the Hulk, Doctor Strange, the Fantastic Four, Iron Man, Daredevil, Thor, the X-Men, and many other fictional characters, introducing a thoroughly shared universe into superhero comic books. In addition, he challenged the comics' industry's censorship organization, the Comics Code Authority, forcing it to reform its policies.[not verified in body] Lee subsequently led the expansion of Marvel Comics from a small division of a publishing house to a large multimedia corporation.\n" +
                                "He was inducted into the comic book industry's Will Eisner Award Hall of Fame in 1994 and the Jack Kirby Hall of Fame in 1995. Lee received a National Medal of Arts in 2008.\n")
                .createActor());

        actors.add(new ActorBuilder().setIme("Anthony")
                .setPrezime("Hopkins")
                .setGodinaRodjenja(1937)
                .setMjestoRodjenja("Margam")
                .setRating(5)
                .setImgUrl("anthonyhopkins")
                .setImdbLink("http://www.imdb.com/name/nm0000164/")
                .setBiografija("Sir Philip Anthony Hopkins, CBE (born 31 December 1937), is a Welsh actor of film, stage, and television.[1] After graduating from the Royal Welsh College of Music & Drama in 1957, he trained at the Royal Academy of Dramatic Art in London, and was then spotted by Laurence Olivier who invited him to join the Royal National Theatre. In 1968, he got his break in film in The Lion in Winter, playing Richard the Lionheart.\n" +
                        "Considered to be one of the greatest living actors,[2][3][4] Hopkins is well known for his portrayal of Hannibal Lecter in The Silence of the Lambs, for which he won the Academy Award for Best Actor, its sequel Hannibal, and the prequel Red Dragon. Other notable films include The Mask of Zorro, The Bounty, Meet Joe Black, The Elephant Man, Magic, 84 Charing Cross Road, Bram Stoker's Dracula, Legends of the Fall, Thor, The Remains of the Day, Amistad, Nixon, The World's Fastest Indian, Instinct, Fracture, and The Dresser. Since 2016, he has starred in the critically acclaimed HBO television series Westworld.\n" +
                        "Along with his Academy Award, Hopkins has won three BAFTA Awards, two Emmys, and the Cecil B. DeMille Award. In 1993, he was knighted by Queen Elizabeth II for services to the arts.[5] He received a star on the Hollywood Walk of Fame in 2003, and was made a Fellow of the British Academy of Film and Television Arts in 2008.[6][7]\n")
                .createActor());

        actors.add(new ActorBuilder().setIme("Quentin")
                .setPrezime("Tarantino")
                .setGodinaRodjenja(1926)
                .setImdbLink("http://www.imdb.com/name/nm0000233")
                .setMjestoRodjenja("Knoxville")
                .setRating(5)
                .setImgUrl("tarantino")
                .setBiografija("Quentin Jerome Tarantino[1] (/ˌtærənˈtiːnoʊ/; born March 27, 1963) is an American director, writer, and actor. His films are characterized by nonlinear storylines, satirical subject matter, an aestheticization of violence, extended scenes of dialogue, ensemble casts consisting of established and lesser-known performers, references to popular culture, soundtracks primarily containing songs and score pieces from the 1960s to the 1980s, and features of neo-noir film.\n" +
                                "His career began in the late 1980s, when he wrote and directed My Best Friend's Birthday, the screenplay of which formed the basis for True Romance. In the early 1990s, he began his career as an independent filmmaker with the release of Reservoir Dogs in 1992; regarded as a classic and cult hit, it was called the \"Greatest Independent Film of All Time\" by Empire. Its popularity was boosted by his second film, Pulp Fiction (1994), a black comedy crime film that was a major success both among critics and audiences. Judged the greatest film from 1983–2008 by Entertainment Weekly,[2] many critics and scholars have named it one of the most significant works of modern cinema.[3] For his next effort, Tarantino paid homage to the blaxploitation films of the 1970s with Jackie Brown (1997), an adaptation of the novel Rum Punch.\n" +
                                "Kill Bill, a highly stylized \"revenge flick\" in the cinematic traditions of Kung fu films, Japanese martial arts, Spaghetti Westerns and Italian horror, followed six years later, and was released as two films: Volume 1 in 2003 and Volume 2 in 2004. Tarantino directed Death Proof (2007) as part of a double feature with friend Robert Rodriguez, under the collective title Grindhouse. His long-postponed Inglourious Basterds, which tells the fictional alternate history story of two plots to assassinate Nazi Germany's political leadership, was released in 2009 to positive reviews. After that came 2012's critically acclaimed Django Unchained, a Western film set in the antebellum era of the Deep South. It became the highest-grossing film of his career so far, making over $425 million at the box office. His eighth film, the mystery Western The Hateful Eight, was released in its roadshow version December 25, 2015, in 70 mm film format, complete with opening \"overture\" and halfway-point intermission, after the fashion of big-budget films of the 1960s and early 1970s.\n" +
                                "Tarantino's films have garnered both critical and commercial success. He has received many industry awards, including two Academy Awards, two Golden Globe Awards, two BAFTA Awards and the Palme d'Or, and has been nominated for an Emmy and a Grammy. He was named one of the 100 Most Influential People in the World by Time in 2005.[4] Filmmaker and historian Peter Bogdanovich has called him \"the single most influential director of his generation\".[5] In December 2015, Tarantino received a star on the Hollywood Walk of Fame for his contributions to the film industry.[6]\n")
                .createActor());

        actors.add(new ActorBuilder().setIme("Leslie")
                .setPrezime("Nielsen")
                .setGodinaRodjenja(1926)
                .setGodinaSmrti(2010)
                .setImdbLink("http://www.imdb.com/name/nm0000558")
                .setMjestoRodjenja("Regina")
                .setRating(5)
                .setImgUrl("leslienielsen")
                .setBiografija("Leslie William Nielsen, OC (11 February 1926 – 28 November 2010) was a Canadian actor, comedian, and producer.[1][2] He appeared in more than 100 films and 150 television programs, portraying more than 220 characters.[3]\n" +
                        "Nielsen was born in Regina, Saskatchewan. He enlisted in the Royal Canadian Air Force and later worked as a disc jockey before receiving a scholarship to study theatre at the Neighborhood Playhouse. Making his acting debut in 1948, by 1950 he was appearing in 46 live television programs a year. Nielsen made his film debut in 1956, with supporting roles in several drama, western, and romance films produced between the 1950s and the 1970s.\n")
                .createActor());

        actors.add(new ActorBuilder().setIme("Uma")
                .setPrezime("Thurman")
                .setGender(Actor.Gender.FEMALE)
                .setGodinaRodjenja(1985)
                .setMjestoRodjenja("Boston")
                .setRating(5)
                .setImdbLink("http://www.imdb.com/name/nm0000235")
                .setImgUrl("umathurman")
                .setBiografija("Uma Karuna Thurman (born April 29, 1970)[1] is an American actress and model. She has performed in a variety of films, ranging from romantic comedies and dramas to science fiction and action movies. Following her appearances on the December 1985 and May 1986 covers of British Vogue, she starred in Dangerous Liaisons (1988). Thurman rose to international prominence with her performance in Quentin Tarantino's Pulp Fiction (1994), for which she was nominated for the Academy Award, the BAFTA Award and the Golden Globe Award for Best Supporting Actress. She starred in several more films throughout the 1990s such as Batman & Robin (1997), Gattaca (1997) and Les Misérables (1998).\n" +
                        "Thurman was awarded the Golden Globe for Best Actress in a Television Film for Hysterical Blindness (2002). Hailed as Quentin Tarantino's muse,[2] she reunited with the director to play the main role in both Kill Bill films (2003–2004), which brought her two additional Golden Globe Award nominations. Other acting credits in the decade include Be Cool (2005), The Producers (2005) and My Super Ex-Girlfriend (2006). She received a Primetime Emmy Award nomination for Outstanding Guest Actress in a Drama Series for her five-episode role in Smash (2012), and garnered critical praise for her supporting performance in the Volume I of Lars von Trier's two-part art drama Nymphomaniac (2013).\n")
                .createActor());
    }
}
