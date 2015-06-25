package itc4j;

/**
 * @author Sina Bagherzadeh
 */
final class GrowResult {

    private final Event event;
    private int cost;

    GrowResult(Event event, int cost) {
        this.event = event;
        this.cost = cost;
    }

    Event getEvent() {
        return event;
    }

    int getCost() {
        return cost;
    }

    void setCost(int cost) {
        this.cost = cost;
    }

}
