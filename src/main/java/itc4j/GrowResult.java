package itc4j;

/**
 * @author Sina Bagherzadeh
 */
final class GrowResult {
    
    private final Event event;
    private int c;

    GrowResult(Event event, int c) {
        this.event = event;
        this.c = c;
    }

    Event getEvent() {
        return event;
    }

    int getC() {
        return c;
    }

    void setC(int c) {
        this.c = c;
    }
    
}
