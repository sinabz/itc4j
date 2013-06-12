package itc4j;

import java.io.Serializable;

import static itc4j.ID.*;

/**
 * @author Sina Bagherzadeh
 */
public final class Stamp implements Serializable {
    private ID id;
    private Event event;

    public Stamp() {
        id = newID_1();
        event = new Event(0);
    }

    Stamp(ID id, Event event) {
        this.id = id;
        this.event = event;
    }

    static Stamp[] fork(Stamp s) {
        Stamp[] result = new Stamp[2];
        ID[] ids = ID.split(s.id);
        result[0] = new Stamp(ids[0], s.event);
        result[1] = new Stamp(ids[1], s.event);
        return result;
    }

    static Stamp[] peek(Stamp s) {
        return new Stamp[]{new Stamp(s.id, s.event), new Stamp(newID_0(), s.event)};
    }

    static Stamp join(Stamp s1, Stamp s2) {
        return new Stamp(ID.sum(s1.id, s2.id), Event.join(s1.event, s2.event));
    }

    private static Event fill(ID id, Event event) {
        if (id.equals(ID_0))
            return event;
        if (id.equals(ID_1))
            return new Event(Event.max(event));
        if (Event.isValuedOnly(event))
            return new Event(event.getValue());
        if (id.getLeft() != null && id.getLeft().equals(ID_1)) {
            Event er = fill(id.getRight(), event.getRight());
            int max = Math.max(Event.max(event.getLeft()), Event.min(er));
            return Event.norm(new Event(event.getValue(), new Event(max), er));
        }
        if (id.getRight() != null && id.getRight().equals(ID_1)) {
            Event el = fill(id.getLeft(), event.getLeft());
            int max = Math.max(Event.max(event.getRight()), Event.min(el));
            return Event.norm(new Event(event.getValue(), el, new Event(max)));
        }
        return Event.norm(new Event(event.getValue(), fill(id.getLeft(), event.getLeft()),
                fill(id.getRight(), event.getRight())));
    }

    private static GrowResult grow(ID id, Event event) {
        if (id.equals(ID_1) && Event.isValuedOnly(event))
            return new GrowResult(new Event(event.getValue() + 1), 0);
        if (Event.isValuedOnly(event)) {
            GrowResult er = grow(id, new Event(event.getValue(), new Event(0), new Event(0)));
            er.setC(er.getC() + event.maxDepth() + 1);
            return er;
        }
        if (id.getLeft() != null && id.getLeft().equals(ID_0)) {
            GrowResult er = grow(id.getRight(), event.getRight());
            Event e = new Event(event.getValue(), event.getLeft(), er.getEvent());
            return new GrowResult(e, er.getC() + 1);
        }
        if (id.getRight() != null && id.getRight().equals(ID_0)) {
            GrowResult er = grow(id.getLeft(), event.getLeft());
            Event e = new Event(event.getValue(), er.getEvent(), event.getRight());
            return new GrowResult(e, er.getC() + 1);
        }
        GrowResult left = grow(id.getLeft(), event.getLeft());
        GrowResult right = grow(id.getRight(), event.getRight());
        if (left.getC() < right.getC()) {
            Event e = new Event(event.getValue(), left.getEvent(), event.getRight());
            return new GrowResult(e, left.getC() + 1);
        } else {
            Event e = new Event(event.getValue(), event.getLeft(), right.getEvent());
            return new GrowResult(e, right.getC() + 1);
        }
    }

    static Stamp event(Stamp s) {
        Event e = fill(s.id, s.event);
        if (!s.event.equals(e))
            return new Stamp(s.id, e);
        else {
            GrowResult gr = grow(s.id, s.event);
            return new Stamp(s.id, gr.getEvent());
        }
    }

    public String toString() {
        return "(" + id + ", " + event + ")";
    }

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

    public Stamp[] send(Stamp s) {
        return peek(event(s.clone()));
    }

    public Stamp receive(Stamp s1, Stamp s2) {
        return event(join(s1.clone(), s2.clone()));
    }

    public Stamp[] sync(Stamp s1, Stamp s2) {
        return fork(join(s1.clone(), s2.clone()));
    }

    public static boolean leq(Stamp s1, Stamp s2) {
        return Event.leq(s1.event, s2.event);
    }

}
