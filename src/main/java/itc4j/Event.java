package itc4j;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sina Bagherzadeh
 */
abstract class Event implements Cloneable {

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

    @Override
    protected Event clone() {
        try {
            return (Event)super.clone();
        }
        catch(CloneNotSupportedException ex) {
            Logger.getLogger(Event.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

}
