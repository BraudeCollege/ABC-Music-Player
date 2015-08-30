package player.visitor;


import org.junit.Test;
import static org.junit.Assert.*;


public class MathHelpersTest
{
    @Test
    public void testlcm()
    {
        assertEquals(24, MathHelpers.lcm(new int[] { 4, 8, 6 }));
        assertEquals(84, MathHelpers.lcm(new int[] { 4, 7, 3 }));
        assertEquals(0, MathHelpers.lcm(new int[]{2, 8, 0}));
    }
}
