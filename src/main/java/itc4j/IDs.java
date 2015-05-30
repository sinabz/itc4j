
package itc4j;

/**
 * IDs
 *
 * @author Benjamim Sonntag <benjamimsonntag@gmail.com>
 * @version 28/mai/2015
 */
final class IDs {
    
    static ID zero() {
        return new LeafID(0);
    }
    
    static ID one() {
        return new LeafID(1);
    }
    
    static ID with(ID id1, ID id2) {
        return new NonLeafID(id1, id2);
    }

    private IDs() { }
    
}
