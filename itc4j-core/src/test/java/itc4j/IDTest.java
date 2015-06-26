package itc4j;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Sina Bagherzadeh
 * @author Benjamim Sonntag <benjamimsonntag@gmail.com>
 */
public class IDTest {

    private final ID zero = IDs.zero();
    private final ID one = IDs.one();
    private final ID zeroZero = IDs.with(IDs.zero(), IDs.zero());
    private final ID zeroOne = IDs.with(IDs.zero(), IDs.one());
    private final ID oneZero = IDs.with(IDs.one(), IDs.zero());
    private final ID oneOne = IDs.with(IDs.one(), IDs.one());

    @Test
    public void testIsLeaf() {
        assertTrue(zero.isLeaf());
        assertTrue(one.isLeaf());
        assertFalse(zeroZero.isLeaf());
        assertFalse(zeroOne.isLeaf());
        assertFalse(oneZero.isLeaf());
        assertFalse(oneOne.isLeaf());
    }

    @Test
    public void testIsZero() {
        assertTrue(zero.isZero());
        assertFalse(one.isZero());
        assertFalse(zeroZero.isZero());
        assertFalse(zeroOne.isZero());
        assertFalse(oneZero.isZero());
        assertFalse(oneOne.isZero());
    }

    @Test
    public void testIsOne() {
        assertFalse(zero.isOne());
        assertTrue(one.isOne());
        assertFalse(zeroZero.isOne());
        assertFalse(zeroOne.isOne());
        assertFalse(oneZero.isOne());
        assertFalse(oneOne.isOne());
    }

    @Test
    public void testEquals_Leafs() {
        assertTrue(zero.equals(zero));
        assertFalse(zero.equals(one));

        assertTrue(one.equals(one));
        assertFalse(one.equals(zero));
    }

    @Test
    public void testEquals_NonLeafs() {
        assertTrue(zeroZero.equals(zeroZero));
        assertTrue(zeroOne.equals(zeroOne));
        assertTrue(oneZero.equals(oneZero));
        assertTrue(oneOne.equals(oneOne));
    }

    @Test
    public void testNormalize() {
        assertEquals(zero, zero.normalize());
        assertEquals(one, one.normalize());

        assertEquals(zero, zeroZero.normalize());
        assertEquals(one, oneOne.normalize());

        assertEquals(zeroOne, zeroOne.normalize());
        assertEquals(oneZero, oneZero.normalize());

        assertEquals(zero, IDs.with(zero, zeroZero).normalize());
        assertEquals(zero, IDs.with(zeroZero, zero).normalize());
        assertEquals(oneZero, IDs.with(one, zeroZero).normalize());
        assertEquals(zeroOne, IDs.with(zeroZero, one).normalize());

        assertEquals(one, IDs.with(one, oneOne).normalize());
        assertEquals(one, IDs.with(oneOne, one).normalize());
        assertEquals(zeroOne, IDs.with(zero, oneOne).normalize());
        assertEquals(oneZero, IDs.with(oneOne, zero).normalize());
    }

    @Test
    public void testSplit_Leaf() {
        // split(0) = (0, 0)
        ID[] zeroSplit = zero.split();
        assertEquals(zero, zeroSplit[0]);
        assertEquals(zero, zeroSplit[1]);
        // split(1) = ((1,0), (0,1)
        ID[] oneSplit = one.split();
        assertEquals(oneZero, oneSplit[0]);
        assertEquals(zeroOne, oneSplit[1]);
    }

    @Test
    public void testSplit_ZeroOne() {
        // split((0, i)) = ((0,i1), (0,i2)), where (i1, i2) = split(i)
        ID[] splitOne = one.split();
        ID[] expected = new ID[] {
            IDs.with(zero, splitOne[0]),
            IDs.with(zero, splitOne[1])
        };
        assertArrayEquals(expected, zeroOne.split());
    }

    @Test
    public void testSplit_OneZero() {
        // split((i, 0)) = ((i1,0), (i2,0)), where (i1, i2) = split(i)
        ID[] splitOne = one.split();
        ID[] expected = new ID[] {
            IDs.with(splitOne[0], zero),
            IDs.with(splitOne[1], zero)
        };
        assertArrayEquals(expected, oneZero.split());
    }

    @Test
    public void testSplit_OneOne() {
        // split((i1, i2)) = ((i1,0), (0,i2))
        ID[] expected = new ID[] { oneZero, zeroOne };
        assertArrayEquals(expected, oneOne.split());
    }

    @Test
    public void testSum_Leafs() {
        assertEquals(zero, zero.sum(zero));
        assertEquals(one, zero.sum(one));
        assertEquals(one, one.sum(zero));
    }

    @Test
    public void testSum_NonLeafs() {
        assertEquals(one, oneZero.sum(zeroOne));
        assertEquals(one, zeroOne.sum(oneZero));

        ID expected = IDs.with(one, oneZero);
        assertEquals(expected, oneZero.sum(IDs.with(zero, oneZero)));
    }

    @Test
    public void testSumOfSplit() {
        ID[] splitOne = one.split();
        assertEquals(one, splitOne[0].sum(splitOne[1]));
    }

}
