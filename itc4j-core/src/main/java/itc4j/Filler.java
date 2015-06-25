
package itc4j;

/**
 * Filler
 *
 * @author Sina Bagherzadeh
 * @author Benjamim Sonntag <benjamimsonntag@gmail.com>
 * @version 29/mai/2015
 */
final class Filler {

    static Event fill(ID id, Event event) {
        if (id.isLeaf()) {
            return fillWithLeafID(id, event);
        }
        else if (event.isLeaf()) {
            return event;
        }
        else {
            return fillNonLeafs(id, event);
        }
    }

    private static Event fillWithLeafID(ID leafID, Event event) {
        if (leafID.isZero()) {
            return event;
        }
        else {
            return Events.with(event.max());
        }
    }

    private static Event fillNonLeafs(ID id, Event event) {
        if (id.getLeft().isOne()) {
            return fillLeftOneID(id, event);
        }
        else if (id.getRight().isOne()) {
            return fillRightOneID(id, event);
        }
        else {
            return fillLeftRight(id, event);
        }
    }

    private static Event fillLeftOneID(ID id, Event event) {
        Event filledRight = fillRight(id, event);
        int max = Math.max(event.getLeft().max(), filledRight.min());
        return Events.with(event.getValue(), Events.with(max), filledRight).normalize();
    }

    private static Event fillRight(ID id, Event event) {
        return fill(id.getRight(), event.getRight());
    }

    private static Event fillRightOneID(ID id, Event event) {
        Event filledLeft = fillLeft(id, event);
        int max = Math.max(event.getRight().max(), filledLeft.min());
        return Events.with(event.getValue(), filledLeft, Events.with(max)).normalize();
    }

    private static Event fillLeft(ID id, Event event) {
        return fill(id.getLeft(), event.getLeft());
    }

    private static Event fillLeftRight(ID id, Event event) {
        return Events.with(event.getValue(), fillLeft(id, event), fillRight(id, event))
                .normalize();
    }

    private  Filler() { }

}
