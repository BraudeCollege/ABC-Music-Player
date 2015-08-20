package player;


import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest
{

    @Test
    public void testInitState()
    {
        assertTrue(new Lexer("ABC content").hasNext());
    }

    @Test
    public void testEmptyString()
    {
        assertFalse(new Lexer("").hasNext());
    }
}
