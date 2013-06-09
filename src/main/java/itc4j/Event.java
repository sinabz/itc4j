package itc4j;

import java.io.Serializable;

/**
 * @author Sina Bagherzadeh
 */
public final class Event implements Serializable {

    protected Event left = null;
    protected Event right = null;
    protected int value;

    protected Event() {
        value = 0;
    }

    protected Event(int value) {
        this.value = value;
    }

    protected Event(Event left, Event right, int value) {
        this.value = value;
        this.left = left;
        this.right = right;
    }

    protected static Event lift(Event e, int m) {
        if (e.right == null && e.left == null)
            return new Event(e.value + m);
        else
            return new Event(e.left == null ? null : e.left.clone(),
                    e.right == null ? null : e.right.clone(), e.value + m);
    }

    protected static Event sink(Event e, int m) {
        if (e.right == null && e.left == null)
            return new Event(e.value - m);
        else
            return new Event(e.left == null ? null : e.left.clone(),
                    e.right == null ? null : e.right.clone(), e.value - m);
    }

    protected static int min(Event e) {
        if (e == null)
            return 0;
        return e.value;
    }

    protected static int max(Event e) {
        if (e == null)
            return 0;
        if (e.left == null && e.right == null)
            return e.value;
        if (e.left != null && e.right != null) {
            int m = Math.max(max(e.left), max(e.right));
            return e.value + m;
        }
        throw new RuntimeException("Every node must have either two children or no child.");
    }

    public static Event norm(Event e) {
        if (e.left == null && e.right == null)
            return new Event(e.value);
        else if (e.left != null && e.right != null && e.left.value == e.right.value)
            return new Event(e.value + e.left.value);
        else if (e.left != null && e.right != null) {
            int m = Math.min(min(e.left), min(e.right));
            return new Event(sink(e.left, m), sink(e.right, m), e.value + m);
        }
        throw new RuntimeException("Every node must have either two children or no child.");
    }

    @SuppressWarnings({"ConstantConditions"})
    private static boolean InnerLeq(Event e1, Event e2) {
        if (e1.left == null && e1.right == null)
            return e1.value <= e2.value;
        else if (e2.left == null && e2.right == null)
            return e1.value <= e2.value &&
                    InnerLeq(lift(e1.left, e1.value), e2) &&
                    InnerLeq(lift(e1.right, e1.value), e2);
        else if (e1.left != null && e1.right != null && e2.left != null && e2.right != null)
            return e1.value <= e2.value &&
                    InnerLeq(lift(e1.left, e1.value), lift(e2.left, e2.value)) &&
                    InnerLeq(lift(e1.right, e1.value), lift(e2.right, e2.value));
        throw new RuntimeException("Every node must have either two children or no child.");
    }

    protected static boolean leq(Event e1, Event e2) {
        return InnerLeq(e1.clone(), e2.clone());
    }

    private static Event innerJoin(Event e1, Event e2) {
        if (e1.right == null && e1.left == null && e2.left == null && e2.right == null)
            return new Event(Math.max(e1.value, e2.value));
        else if (e1.right == null && e1.left == null)
            return innerJoin(new Event(new Event(0), new Event(0), e1.value) ,e2);
        else if (e2.left == null && e2.right == null)
            return innerJoin(e1.clone(), new Event(new Event(0), new Event(0), e2.value));
        else if (e1.right != null && e1.left != null && e2.left != null && e2.right != null) {
            if (e1.value > e2.value)
                return innerJoin(e2.clone(), e1.clone());
            else {
                Event left = innerJoin(e1.left ,lift(e2.left ,e2.value - e1.value));
                Event right = innerJoin(e1.right ,lift(e2.right ,e2.value - e1.value));
                return norm(new Event(left, right, e1.value));
            }
        }
        throw new RuntimeException("Every node must have either two children or no child.");
    }

    protected static Event join(Event e1, Event e2) {
        return innerJoin(e1.clone(), e2.clone());
    }

    @SuppressWarnings({"ConstantConditions", "SimplifiableIfStatement"})
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (!(o instanceof Event))
            return false;
        Event e = (Event) o;
        if (left == null && right == null && e.left != null && e.right != null )
            return false;
        if (left != null && right != null && e.left == null && e.right == null )
            return false;
        if (left == null && right == null && e.left == null && e.right == null )
            return value == e.value;
        if (left != null && right != null && e.left != null && e.right != null )
            return value == e.value && left.equals(e.left) && right.equals(e.right);
        return false;
    }

    public String toString() {
        if (right == null && left == null)
            return String.valueOf(value);
        return new StringBuilder()
                .append("(").append(value).append(", ").append(left).append(", ")
                .append(right).append(")").toString();
    }

    @SuppressWarnings({"ConstantConditions"})
    private int maxDepth(int depth) {
        if (left == null && right == null)
            return depth;
        return Math.max(left.maxDepth(depth + 1), right.maxDepth(depth + 1));
    }

    public int maxDepth() {
        return maxDepth(0);
    }

    @SuppressWarnings({"CloneDoesntCallSuperClone"})
    public Event clone() {
        Event clone = new Event();
        clone.value = value;
        if (right != null)
            clone.right = right.clone();
        if (left != null)
            clone.left = left.clone();
        return clone;
    }


}
