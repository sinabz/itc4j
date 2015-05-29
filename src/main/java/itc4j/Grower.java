
package itc4j;

/**
 * Grower
 *
 * @author Benjamim Sonntag <benjamimsonntag@gmail.com>
 * @version 29/mai/2015
 */
final class Grower {

    static GrowResult grow(ID id, Event event) {
        if (id.equals(IDs.one()) && event.isLeaf())
            return new GrowResult(Events.with(event.getValue() + 1), 0);
        if (event.isLeaf()) {
            GrowResult er = grow(id, Events.with(event.getValue(), Events.zero(), Events.zero()));
            er.setC(er.getC() + event.maxDepth() + 1);
            return er;
        }
        if (id.getLeft() != null && id.getLeft().equals(IDs.zero())) {
            GrowResult er = grow(id.getRight(), event.getRight());
            Event e = Events.with(event.getValue(), event.getLeft(), er.getEvent());
            return new GrowResult(e, er.getC() + 1);
        }
        if (id.getRight() != null && id.getRight().equals(IDs.zero())) {
            GrowResult er = grow(id.getLeft(), event.getLeft());
            Event e = Events.with(event.getValue(), er.getEvent(), event.getRight());
            return new GrowResult(e, er.getC() + 1);
        }
        GrowResult left = grow(id.getLeft(), event.getLeft());
        GrowResult right = grow(id.getRight(), event.getRight());
        if (left.getC() < right.getC()) {
            Event e = Events.with(event.getValue(), left.getEvent(), event.getRight());
            return new GrowResult(e, left.getC() + 1);
        } else {
            Event e = Events.with(event.getValue(), event.getLeft(), right.getEvent());
            return new GrowResult(e, right.getC() + 1);
        }
    }

    private Grower() { }
    
}
