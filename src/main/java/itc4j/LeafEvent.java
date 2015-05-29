package itc4j;

import java.io.Serializable;

/**
 * LeafEvent
 *
 * @author Sina Bagherzadeh
 * @author Benjamim Sonntag <benjamimsonntag@gmail.com>
 * @version 29/mai/2015
 */
final class LeafEvent extends Event implements Serializable, Cloneable {

    private static final long serialVersionUID = -7441138365249091187L;
    
    private final int value;

    LeafEvent() {
        value = 0;
    }

    LeafEvent(int value) {
        this.value = value;
    }

    @Override
    int getValue() {
        return value;
    }

    @Override
    Event getLeft() {
        return null;
    }

    @Override
    Event getRight() {
        return null;
    }

    @Override
    int max() {
        return value;
    }

    @Override
    int min() {
        return value;
    }

    @Override
    protected int maxDepth(int depth) {
        return depth;
    }

    @Override
    boolean isLeaf() {
        return true;
    }

    @Override
    Event lift(int m) {
        return new LeafEvent(value + m);
    }

    @Override
    Event sink(int m) {
        return new LeafEvent(value - m);
    }

    @Override
    Event normalize() {
        return this;
    }

    @Override
    boolean leq(Event other) {
        return value <= other.getValue();
    }

    @Override
    Event join(Event other) {
        if (other.isLeaf()) {
            return new LeafEvent(Math.max(value, other.getValue()));
        }
        else {
            return Events.with(value, Events.zero(), Events.zero()).join(other);
        }
    }

    @Override
    public boolean equals(Object object) {
        if(!(object instanceof LeafEvent)) {
            return false;
        }
        else {
            final LeafEvent other = (LeafEvent)object;
            return this.value == other.value;
        }
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 41 * hash + this.value;
        return hash;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @Override
    protected Event clone() {
        return super.clone();
    }
    
}
