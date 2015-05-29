package itc4j;

/**
 * Events
 *
 * @author Benjamim Sonntag <benjamimsonntag@gmail.com>
 * @version 29/mai/2015
 */
final class Events {
    
    static Event zero() {
        return with(0);
    }
    
    static Event with(int value) {
        return new LeafEvent(value);
    }
    
    static Event with(int value, Event left, Event right) {
        return new NonLeafEvent(value, left, right);
    }

    private Events() { }
    
}
