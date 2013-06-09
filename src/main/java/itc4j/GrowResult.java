package itc4j;

import java.io.Serializable;

/**
 * @author Sina Bagherzadeh
 */
public final class GrowResult implements Serializable {
    protected Event event;
    protected int c;

    public GrowResult(Event event, int c) {
        this.event = event;
        this.c = c;
    }
}
