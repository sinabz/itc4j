package itc4j;

/**
 * @author Sina Bagherzadeh
 */
abstract class Event {

    Event() {
        
    }

    abstract int getValue();

    abstract Event getLeft();

    abstract Event getRight();

    abstract int min();

    abstract int max();

    final int maxDepth() {
        return maxDepth(0);
    }

    protected abstract int maxDepth(int depth);
    
    abstract boolean isLeaf();

    abstract Event lift(int m);

    abstract Event sink(int m);

    abstract Event normalize();

    abstract boolean leq(Event other);

    abstract Event join(Event other);

}
