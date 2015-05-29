package itc4j;

import java.io.Serializable;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sina Bagherzadeh
 */
public final class Event implements Serializable, Cloneable {

    private static final long serialVersionUID = -9008822740100066140L;

    private Event left;
    private Event right;
    private final int value;

    Event() {
        value = 0;
        this.left = null;
        this.right = null;
    }

    Event(int value) {
        this.value = value;
        this.left = null;
        this.right = null;
    }

    Event(int value, Event left, Event right) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    Event getLeft() {
        return left;
    }

    Event getRight() {
        return right;
    }

    int getValue() {
        return value;
    }

    int min() {
        if (isLeaf()) {
            return value;
        }
        else {
            int min = Math.min(left.min(), right.min());
            return value + min;
        }
    }

    int max() {
        if (isLeaf()) {
            return value;
        }
        else {
            int max = Math.max(left.max(), right.max());
            return value + max;
        }
    }

    public int maxDepth() {
        return maxDepth(0);
    }

    private int maxDepth(int depth) {
        if (isLeaf()) {
            return depth;
        }
        else {
            int leftDepth = left.maxDepth(depth + 1);
            int rightDepth = right.maxDepth(depth + 1);
            return Math.max(leftDepth, rightDepth);
        }
    }
    
    boolean isLeaf() {
        return left == null && right == null;
    }

    Event lift(int m) {
        return new Event(value + m, left, right);
    }

    Event sink(int m) {
        return new Event(value - m, left, right);
    }

    public Event normalize() {
        if (isLeaf()) {
            return this;
        }
        else {
            return normalizeNonLeaf();
        }
    }

    private Event normalizeNonLeaf() {
        if (left.isLeaf() && right.isLeaf() && left.value == right.value) {
            return new Event(value + left.value);
        }
        else {
            int min = Math.min(left.min(), right.min());
            return new Event(value + min, left.sink(min), right.sink(min));
        }
    }

    boolean leq(Event other) {
        if (isLeaf()) {
            return value <= other.value;
        }
        else if (other.isLeaf()) {
            return value <= other.value &&
                   liftedLeft().leq(other) &&
                   liftedRight().leq(other);
        }
        else {
            return leqNonLeafs(other);
        }
    }

    private Event liftedRight() {
        return right.lift(value);
    }

    private Event liftedLeft() {
        return left.lift(value);
    }

    private boolean leqNonLeafs(Event other) {
        return value <= other.value &&
               liftedLeft().leq(other.liftedLeft()) &&
               liftedRight().leq(other.liftedRight());
    }

    Event join(Event other) {
        if (isLeaf()) {
            return joinLeaf(other);
        }
        else {
            return joinNonLeaf(other);
        }
    }

    private Event joinLeaf(Event other) {
        if (other.isLeaf()) {
            return new Event(Math.max(value, other.value));
        }
        else {
            return new Event(value, new Event(0), new Event(0)).join(other);
        }
    }
    
    private Event joinNonLeaf(Event other) {
        if (other.isLeaf()) {
            return join(new Event(other.value, new Event(0), new Event(0)));
        }
        else {
            if (value > other.value)
                return other.join(this);
            else {
                return new Event(value, leftJoin(other), rightJoin(other)).normalize();
            }
        }
    }

    private Event leftJoin(Event other) {
        Event otherLiftedLeft = other.left.lift(other.value - value);
        return left.join(otherLiftedLeft);
    }

    private Event rightJoin(Event other) {
        Event otherLiftedRight = other.right.lift(other.value - value);
        return right.join(otherLiftedRight);
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Event)) {
            return false;
        }
        else {
            Event other = (Event)object;
            return value == other.value &&
                   Objects.equals(left, other.left) &&
                   Objects.equals(right, other.right);
        }
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(left);
        hash = 23 * hash + Objects.hashCode(right);
        hash = 23 * hash + this.value;
        return hash;
    }

    @Override
    public String toString() {
        if (isLeaf()) {
            return String.valueOf(value);
        }
        else {
            return "(" + value + ", " + left + ", " + right + ")";
        }
    }

    @Override
    public Event clone() {
        try {
            Event clone = (Event)super.clone();
            if (right != null)
                clone.right = right.clone();
            if (left != null)
                clone.left = left.clone();
            return clone;
        }
        catch(CloneNotSupportedException ex) {
            Logger.getLogger(Event.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
