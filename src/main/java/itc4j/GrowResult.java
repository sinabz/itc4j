package itc4j;

import java.io.Serializable;

/**
 * @author Sina Bagherzadeh
 */
public final class GrowResult implements Serializable {
    private Event event;
    private int c;

    public GrowResult(Event event, int c) {
        this.event = event;
        this.c = c;
    }

    public Event getEvent() {
        return event;
    }

    public int getC() {
        return c;
    }

    public void setC(int c) {
        this.c = c;
    }
}
