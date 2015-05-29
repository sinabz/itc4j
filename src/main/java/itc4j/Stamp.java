package itc4j;

import java.io.Serializable;

/**
 * @author Sina Bagherzadeh
 */
public final class Stamp implements Cloneable, Serializable {

    private static final long serialVersionUID = 1750149585711104601L;
    
    private final ID id;
    private final Event event;

    public Stamp() {
        id = IDs.one();
        event = Events.zero();
    }

    Stamp(ID id, Event event) {
        this.id = id;
        this.event = event;
    }

    ID getId() {
        return id;
    }

    Event getEvent() {
        return event;
    }

    Stamp[] fork() {
        ID[] ids = id.split();
        return new Stamp[] {
            new Stamp(ids[0], event),
            new Stamp(ids[1], event)
        };
    }

    Stamp[] peek() {
        return new Stamp[] {
            new Stamp(id, event),
            new Stamp(IDs.zero(), event)
        };
    }

    Stamp join(Stamp other) {
        ID idSum = id.sum(other.id);
        Event eventJoin = event.join(other.event);
        return new Stamp(idSum, eventJoin);
    }

    Stamp event() {
        Event filled = Filler.fill(id, event);
        if (!filled.equals(event)) {
            return new Stamp(id, filled);
        }
        else {
            GrowResult growth = Grower.grow(id, event);
            return new Stamp(id, growth.getEvent());
        }
    }

    @Override
    public String toString() {
        return "(" + id + ", " + event + ")";
    }

    @Override
    public Stamp clone() {
        return new Stamp(id.clone(), event.clone());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stamp stamp = (Stamp) o;
        return !(event != null ? !event.equals(stamp.event) : stamp.event != null) &&
                !(id != null ? !id.equals(stamp.id) : stamp.id != null);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (event != null ? event.hashCode() : 0);
        return result;
    }

    //----------------------------------------- API methods --------------------------------------

    public static Stamp[] send(Stamp stamp) {
        return stamp.event().peek();
    }

    public static Stamp receive(Stamp stamp1, Stamp stamp2) {
        Stamp join = stamp1.join(stamp2);
        return join.event();
    }

    public static Stamp[] sync(Stamp stamp1, Stamp stamp2) {
        Stamp join = stamp1.join(stamp2);
        return join.fork();
    }

    public static boolean leq(Stamp stamp1, Stamp stamp2) {
        return stamp1.event.leq(stamp2.event);
    }

}
