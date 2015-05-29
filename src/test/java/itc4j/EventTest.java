package itc4j;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Sina Bagherzadeh
 */
public class EventTest {
    
    private final Event event1 = Events.with(1);
    private final Event event2 = Events.with(2, Events.with(0), Events.with(2));
    private final Event event3 = Events.with(3);
    
    @Test
    public void testEquals() {
        assertTrue(event1.equals(Events.with(1)));
        assertFalse(event1.equals(event2));
        
        assertTrue(event2.equals(Events.with(2, Events.with(0), Events.with(2))));
    }
    
    @Test
    public void testIsLeaf() {
        assertTrue(event1.isLeaf());
        assertFalse(event2.isLeaf());
    }
    
    @Test
    public void testMin() {
        assertIntEquals(1, event1.min());
        assertIntEquals(2, event2.min());
    }
    
    @Test
    public void testMax() {
        assertIntEquals(1, event1.max());
        assertIntEquals(4, event2.max());
    }
    
    @Test
    public void testMaxDepth() {
        assertIntEquals(0, event1.maxDepth());
        assertIntEquals(1, event2.maxDepth());
    }
    
    @Test
    public void testLift() {
        assertIntEquals(2, event1.lift(1).getValue());
        assertIntEquals(3, event2.lift(1).getValue());
    }
    
    @Test
    public void testSink() {
        assertIntEquals(0, event1.sink(1).getValue());
        assertIntEquals(1, event2.sink(1).getValue());
    }

    @Test
    public void testNormalize() {
        Event expected, event;
        
        expected = Events.with(3);
        event = Events.with(2, Events.with(1), Events.with(1));
        // norm((2, 1, 1)) == 3
        assertEquals(expected, event.normalize());
        
        expected = Events.with(4, Events.with(0, Events.with(1), Events.with(0)), Events.with(1));
        event = Events.with(2, Events.with(2, Events.with(1), Events.with(0)), Events.with(3));
        // norm((2, (2, 1, 0), 3)) == (4, (0, 1, 0), 1)
        assertEquals(expected, event.normalize());
    }
    
    @Test
    public void testNormalize_NormalizedEvents() {
        assertEquals(event1, event1.normalize());
        assertEquals(event2, event2.normalize());
    }
    
    @Test
    public void testLeq() {
        assertTrue(event1.leq(event1));
        assertTrue(event1.leq(event2));
        assertTrue(event1.leq(event3));
        
        assertFalse(event2.leq(event1));
        assertTrue(event2.leq(event2));
        assertFalse(event2.leq(event3));
        
        assertFalse(event3.leq(event1));
        assertFalse(event3.leq(event2));
        assertTrue(event3.leq(event3));
    }
    
    @Test
    public void testJoin() {
        assertEquals(event1, event1.join(event1));
        assertEquals(event2, event1.join(event2));
        assertEquals(event3, event1.join(event3));
        
        Event expected = Events.with(3, Events.with(0), Events.with(1));
        assertEquals(expected, event2.join(event3));
    }
    
    private static void assertIntEquals(int expected, int actual) {
        long lExpected = expected;
        long lActual = actual;
        Assert.assertEquals(lExpected, lActual);
    }
}
