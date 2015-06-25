package itc4j;

import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Sina Bagherzadeh
 * @author Benjamim Sonntag <benjamimsonntag@gmail.com>
 */
public class StampTest {

    private Stamp seedStamp;
    private Stamp forkedStamp1;
    private Stamp forkedStamp2;
    private Stamp joinedStamp;
    private List<Stamp> stamps;

    @Before
    public void setup() {
        seedStamp = new Stamp();
        Stamp[] fork = seedStamp.event().fork();
        forkedStamp1 = fork[0];
        forkedStamp2 = fork[1].event();
        joinedStamp = forkedStamp1.join(forkedStamp2);
        stamps = Arrays.asList(seedStamp, forkedStamp1, forkedStamp2, joinedStamp);
    }

    @Test
    public void testSeedStampIsOneZero() {
        assertTrue(seedStamp.getId().isOne());
        assertTrue(seedStamp.getEvent().isLeaf());
        assertIntEquals(0, seedStamp.getEvent().getValue());
    }

    @Test
    public void testEquals() {
        assertTrue(seedStamp.equals(new Stamp()));
        assertFalse(seedStamp.equals(forkedStamp1));
        assertTrue(forkedStamp1.equals(forkedStamp1));
        assertFalse(forkedStamp1.equals(forkedStamp2));
    }

    @Test
    public void testPeek() {
        for (Stamp stamp : stamps) {
            Stamp[] peek = stamp.peek();

            assertIntEquals(2, peek.length);
            assertTrue(peek[0].equals(stamp));
            assertTrue(peek[1].getId().isZero());
            assertTrue(peek[1].getEvent().equals(stamp.getEvent()));
            assertNormalizedStamp(peek[0]);
            assertNormalizedStamp(peek[1]);
        }
    }

    @Test
    public void testFork() {
        for (Stamp stamp : stamps) {
            Stamp[] fork = stamp.fork();
            ID[] splitIDs = stamp.getId().split();

            assertIntEquals(2, fork.length);
            assertEquals(stamp.getEvent(), fork[0].getEvent());
            assertEquals(stamp.getEvent(), fork[1].getEvent());
            assertEquals(splitIDs[0], fork[0].getId());
            assertEquals(splitIDs[1], fork[1].getId());
            assertNormalizedStamp(fork[0]);
            assertNormalizedStamp(fork[1]);
        }
    }

    @Test
    public void testJoin() {
        Stamp expected = new Stamp(IDs.one(),
                Events.with(1, Events.zero(), Events.with(1)));

        assertEquals(expected, forkedStamp1.join(forkedStamp2));
        assertEquals(expected, forkedStamp2.join(forkedStamp1));
        assertNormalizedStamp(forkedStamp1.join(forkedStamp2));
    }

    @Test
    public void testEvent() {
        for (Stamp stamp : stamps) {
            Stamp evented = stamp.event();

            assertTrue(stamp.getEvent().leq(evented.getEvent()));
            assertNormalizedStamp(evented);
        }
    }

    @Test
    public void testForkEventJoin() {
        Stamp[] fork1 = seedStamp.fork();
        Stamp event1 = fork1[0].event();
        Stamp event2 = fork1[1].event().event();
        Stamp[] fork2 = event1.fork();
        Stamp event11 = fork2[0].event();
        Stamp join1 = fork2[1].join(event2);
        Stamp[] fork22 = join1.fork();
        Stamp join2 = fork22[0].join(event11);
        Stamp event3 = join2.event();

        Stamp expected = new Stamp(IDs.with(IDs.one(), IDs.zero()), Events.with(2));
        assertEquals(expected, event3);
    }

    @Test
    public void testLeq() {
        Stamp s1 = new Stamp();
        Stamp s2 = new Stamp();
        Assert.assertTrue(s1.leq(s2.event()));
        Assert.assertFalse(s2.event().leq(s1));
    }

    private static void assertIntEquals(int expected, int actual) {
        assertEquals((long)expected, (long)actual);
    }

    private static void assertNormalizedStamp(Stamp stamp) {
        assertEquals(stamp.getId(), stamp.getId().normalize());
        assertEquals(stamp.getEvent(), stamp.getEvent().normalize());
    }

}
