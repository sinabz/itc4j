package itc4j;

import java.io.Serializable;
import java.util.Objects;

/**
 * NonLeafEvent
 *
 * @author Sina Bagherzadeh
 * @author Benjamim Sonntag <benjamimsonntag@gmail.com>
 * @version 29/mai/2015
 */
final class NonLeafEvent extends Event implements Serializable {

    private static final long serialVersionUID = 4390279981057181340L;

    private final int value;
    private final Event left;
    private final Event right;

    NonLeafEvent(int value, Event left, Event right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    @Override
    int getValue() {
        return value;
    }

    Event getLeft() {
        return left;
    }

    Event getRight() {
        return right;
    }

    @Override
    int min() {
        int min = Math.min(left.min(), right.min());
        return value + min;
    }

    @Override
    int max() {
        int max = Math.max(left.max(), right.max());
        return value + max;
    }

    @Override
    protected int maxDepth(int depth) {
        int leftDepth = left.maxDepth(depth + 1);
        int rightDepth = right.maxDepth(depth + 1);
        return Math.max(leftDepth, rightDepth);
    }

    @Override
    boolean isLeaf() {
        return false;
    }

    @Override
    Event lift(int m) {
        return Events.with(value + m, left, right);
    }

    @Override
    Event sink(int m) {
        return Events.with(value - m, left, right);
    }

    @Override
    Event normalize() {
        if (left.isLeaf() && right.isLeaf() && left.getValue() == right.getValue()) {
            return Events.with(value + left.getValue());
        }
        else {
            int min = Math.min(left.min(), right.min());
            return Events.with(value + min, left.sink(min), right.sink(min));
        }
    }

    @Override
    boolean leq(Event other) {
        if (other.isLeaf()) {
            return leqLeaf(other);
        }
        else {
            return leqNonLeafs(other);
        }
    }

    private boolean leqLeaf(Event other) {
        return value <= other.getValue() &&
               liftedLeft(this).leq(other) &&
               liftedRight(this).leq(other);
    }

    private static Event liftedLeft(Event event) {
        return event.getLeft().lift(event.getValue());
    }

    private static Event liftedRight(Event event) {
        return event.getRight().lift(event.getValue());
    }

    private boolean leqNonLeafs(Event other) {
        return value <= other.getValue() &&
               liftedLeft(this).leq(liftedLeft(other)) &&
               liftedRight(this).leq(liftedRight(other));
    }

    @Override
    Event join(Event other) {
        if (other.isLeaf()) {
            return join(Events.with(other.getValue(), Events.zero(), Events.zero()));
        }
        else {
            return joinNonLeaf(other);
        }
    }

    private Event joinNonLeaf(Event other) {
        if (value > other.getValue()) {
            return other.join(this);
        }
        else {
            Event join = Events.with(value, leftJoin(other), rightJoin(other));
            return join.normalize();
        }
    }

    private Event leftJoin(Event other) {
        Event otherLiftedLeft = other.getLeft().lift(other.getValue() - value);
        return left.join(otherLiftedLeft);
    }

    private Event rightJoin(Event other) {
        Event otherLiftedRight = other.getRight().lift(other.getValue() - value);
        return right.join(otherLiftedRight);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof NonLeafEvent)) {
            return false;
        }
        else {
            NonLeafEvent other = (NonLeafEvent)object;
            return value == other.value &&
                   Objects.equals(left, other.left) &&
                   Objects.equals(right, other.right);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + value;
        hash = 37 * hash + left.hashCode();
        hash = 37 * hash + right.hashCode();
        return hash;
    }

    @Override
    public String toString() {
        return "(" + value + ", " + left + ", " + right + ")";
    }

}
