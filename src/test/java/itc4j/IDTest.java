package itc4j;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sina Bagherzadeh
 */
public class IDTest {

    @Test
    public void testNorm() {
        Assert.assertEquals(ID.ID_1,
                ID.norm(new ID(ID.newID_1(), new ID(ID.newID_1(), ID.newID_1()))));//norm(1, (1, 1))
    }
}
