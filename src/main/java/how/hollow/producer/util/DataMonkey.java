package how.hollow.producer.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import how.hollow.producer.datamodel.Actor;
import how.hollow.producer.datamodel.Movie;

public class DataMonkey {
    private final Monkey<Actor> actorMonkey;
    private final Monkey<Movie> movieMonkey;

    public DataMonkey() {
        this(new ActorMonkey(){
            public Actor ook(Actor a) {
                if(introduceChaos(19)) {
                    return new Actor(a.actorId, perturbWords(a.actorName));
                } else if(introduceChaos(57)) {
                    return new Actor(a.actorId - 1, a.actorName);
                } else {
                    return a;
                }
            };
        }, new MovieMonkey(){
            public Movie ook(Movie m) {
                if(introduceChaos(13)) {
                    return new Movie(m.id, m.title, perturb(m.actors));
                } else if(introduceChaos(43)) {
                    return new Movie(m.id + 1, m.title, m.actors);
                } else {
                    return m;
                }
            };
        });
    }

    public DataMonkey(Monkey<Actor> actorMonkey, Monkey<Movie> movieMonkey) {
        this.actorMonkey = actorMonkey;
        this.movieMonkey = movieMonkey;
    }

    public Actor ook(Actor actor) {
        return actorMonkey.ook(actor);
    }

    public Movie ook(Movie movie) {
        return movieMonkey.ook(movie);
    }

    public static interface Monkey<T> {
        T ook(T t);
    }

    private static abstract class Chaos {
        private final Random rand;

        public Chaos() {
            this.rand = new Random();
        }

        boolean introduceChaos(int probability) {
            return rand.nextInt(probability) == 1;
        }

        <E> Set<E> perturb(Set<E> s) {
            List<E> l = new ArrayList<>(s);
            Collections.shuffle(l, rand);
            return new HashSet<>(l.subList(0, l.size() - 1));
        }

        String perturbWords(String s) {
            List<String> wordList = Arrays.asList(s.split("\\s+"));
            if(wordList.isEmpty()) {
                return "Mysterio";
            } else {
                Collections.shuffle(wordList, rand);
                Iterator<String> it = wordList.iterator();
                StringBuilder sb = new StringBuilder(it.next());
                while(it.hasNext()) sb.append(' ').append(it.next());
                return sb.toString();
            }
        }
    }

    private static abstract class MovieMonkey extends Chaos implements Monkey<Movie> {}
    private static abstract class ActorMonkey extends Chaos implements Monkey<Actor> {}
}