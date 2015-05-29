package itc4j;

import java.io.Serializable;

/**
 * @author Sina Bagherzadeh
 */
public final class Stamp implements Serializable {
    private ID id;
    private Event event;

    public Stamp() {
        id = IDs.one();
        event = new Event(0);
    }

    Stamp(ID id, Event event) {
        this.id = id;
        this.event = event;
    }

    static Stamp[] fork(Stamp s) {
        Stamp[] result = new Stamp[2];
        ID[] ids = s.id.split();
        result[0] = new Stamp(ids[0], s.event);
        result[1] = new Stamp(ids[1], s.event);
        return result;
    }

    static Stamp[] peek(Stamp s) {
        return new Stamp[]{new Stamp(s.id, s.event), new Stamp(IDs.zero(), s.event)};
    }

    static Stamp join(Stamp s1, Stamp s2) {
        return new Stamp(s1.id.sum(s2.id), s1.event.join(s2.event));
    }

    private static Event fill(ID id, Event event) {
        if (id.equals(IDs.zero()))
            return event;
        if (id.equals(IDs.one()))
            return new Event(event.max());
        if (event.isLeaf())
            return new Event(event.getValue());
        if (id.getLeft() != null && id.getLeft().equals(IDs.one())) {
            Event er = fill(id.getRight(), event.getRight());
            int max = Math.max(event.getLeft().max(), er.min());
            return new Event(event.getValue(), new Event(max), er).normalize();
        }
        if (id.getRight() != null && id.getRight().equals(IDs.one())) {
            Event el = fill(id.getLeft(), event.getLeft());
            int max = Math.max(event.getRight().max(), el.min());
            return new Event(event.getValue(), el, new Event(max)).normalize();
        }
        return new Event(event.getValue(), fill(id.getLeft(), event.getLeft()),
                fill(id.getRight(), event.getRight())).normalize();
    }

    private static GrowResult grow(ID id, Event event) {
        if (id.equals(IDs.one()) && event.isLeaf())
            return new GrowResult(new Event(event.getValue() + 1), 0);
        if (event.isLeaf()) {
            GrowResult er = grow(id, new Event(event.getValue(), new Event(0), new Event(0)));
            er.setC(er.getC() + event.maxDepth() + 1);
            return er;
        }
        if (id.getLeft() != null && id.getLeft().equals(IDs.zero())) {
            GrowResult er = grow(id.getRight(), event.getRight());
            Event e = new Event(event.getValue(), event.getLeft(), er.getEvent());
            return new GrowResult(e, er.getC() + 1);
        }
        if (id.getRight() != null && id.getRight().equals(IDs.zero())) {
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
        return s1.event.leq(s2.event);
    }

}
