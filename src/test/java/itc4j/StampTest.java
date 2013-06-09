package itc4j;

import org.junit.Assert;
import org.junit.Test;

import static itc4j.Stamp.*;

/**
 * @author Sina Bagherzadeh
 */
public class StampTest {

    @Test
    public void testAll() {
        Stamp stamp = new Stamp();
        Stamp[] fork1 = fork(stamp);
        System.out.println("fork1[0] = " + fork1[0]);
        System.out.println("fork1[1] = " + fork1[1]);
        Stamp event1 = event(fork1[0]);
        System.out.println("event1 = " + event1);
        Stamp event2 = event(event(fork1[1]));
        System.out.println("event2 = " + event2);
        Stamp[] fork2 = fork(event1);
        System.out.println("fork2[0] = " + fork2[0]);
        System.out.println("fork2[1] = " + fork2[1]);
        Stamp event11 = event(fork2[0]);
        System.out.println("event11 = " + event11);
        Stamp join1 = join(fork2[1], event2);
        System.out.println("join1 = " + join1);
        Stamp[] fork22 = fork(join1);
        System.out.println("fork22[0] = " + fork22[0]);
        System.out.println("fork22[1] = " + fork22[1]);
        Stamp join2 = join(fork22[0], event11);
        System.out.println("join2 = " + join2);
        Stamp event3 = event(join2);
        System.out.println("event3 = " + event3);
        Assert.assertEquals("((1, 0), 2)", event3.toString());
    }

    @Test
    public void testLeq() {
        Stamp s1 = new Stamp();
        Stamp s2 = new Stamp();
        Assert.assertTrue(leq(s1, event(s2)));
        Assert.assertFalse(leq(event(s2), s1));
    }
}
