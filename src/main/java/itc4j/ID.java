package itc4j;

/**
 * @author Sina Bagherzadeh
 */
abstract class ID {
    
    abstract ID getLeft();
    
    abstract ID getRight();
    
    abstract boolean isLeaf();
    
    abstract boolean isZero();
    
    abstract boolean isOne();

    abstract ID normalize();

    abstract ID[] split();

    abstract ID sum(ID other);
    
}
