package itc4j;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Sina Bagherzadeh
 */
abstract class ID implements Cloneable {
    
    abstract ID getLeft();
    
    abstract ID getRight();
    
    abstract boolean isLeaf();
    
    abstract boolean isZero();
    
    abstract boolean isOne();

    abstract ID normalize();

    abstract ID[] split();

    abstract ID sum(ID other);

    @Override
    protected ID clone() {
        try {
            return (ID)super.clone();
        }
        catch(CloneNotSupportedException ex) {
            Logger.getLogger(ID.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
    
}
