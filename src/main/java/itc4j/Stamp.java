package itc4j;

import java.io.Serializable;

/**
 * @author Sina Bagherzadeh
 */
public final class Stamp implements Serializable {

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

    public Stamp[] fork() {
        ID[] ids = id.split();
        return new Stamp[] {
            new Stamp(ids[0], event),
            new Stamp(ids[1], event)
        };
    }

    public Stamp[] peek() {
        return new Stamp[] {
            new Stamp(id, event),
            new Stamp(IDs.zero(), event)
        };
    }

    public Stamp join(Stamp other) {
        ID idSum = id.sum(other.id);
        Event eventJoin = event.join(other.event);
        return new Stamp(idSum, eventJoin);
    }

    public Stamp event() {
        Event filled = Filler.fill(id, event);
        if (!filled.equals(event)) {
            return new Stamp(id, filled);
        }
        else {
            return new Stamp(id, Grower.grow(id, event));
        }
    }

    @Override
    public String toString() {
        return "(" + id + ", " + event + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Stamp)) {
            return false;
        }
        else {
            Stamp other = (Stamp) o;
            return id.equals(other.getId()) && event.equals(other.getEvent());
        }
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + event.hashCode();
        return result;
    }

    //----------------------------------------- API methods --------------------------------------

    public Stamp[] send() {
        return event().peek();
    }

    public Stamp receive(Stamp other) {
        return join(other).event();
    }

    public Stamp[] sync(Stamp other) {
        return join(other).fork();
    }

    public boolean leq(Stamp other) {
        return event.leq(other.event);
    }

}
