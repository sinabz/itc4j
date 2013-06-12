package itc4j;

import org.junit.Assert;
import org.junit.Test;

import static itc4j.Event.norm;

/**
 * @author Sina Bagherzadeh
 */
public class EventTest {

    @Test
    public void testNorm() {
        Assert.assertEquals(new Event(3),
                norm(new Event(2, new Event(1), new Event(1))));//norm(2, 1, 1)
        Assert.assertEquals(new Event(4, new Event(0, new Event(1), new Event(0)), new Event(1)), //(4, (0, 1, 0), 1)==
                norm(new Event(2, new Event(2, new Event(1), new Event(0)), new Event(3))));//norm(2, (2, 1, 0), 3)
    }
}
