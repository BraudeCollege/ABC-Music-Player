package player;


import org.junit.Test;

import static org.junit.Assert.*;


public class RationalNumberTest
{
    @Test
    public void testCompare()
    {
        RationalNumber op1 = new RationalNumber(2,5);
        RationalNumber op2 = new RationalNumber(3,5);
        RationalNumber op3 = new RationalNumber(2,5);

        assertTrue(op1.compareTo(op2) < 0);
        assertTrue(op2.compareTo(op3) > 0);
        assertTrue(op1.compareTo(op3) == 0);
    }
}
