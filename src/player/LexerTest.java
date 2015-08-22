package player;


import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest
{

    @Test public void testSimpleNextToken() throws Exception
    {
        Lexer lex = new Lexer("A/2 A/ A A2 A3");

        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
    }

    @Test
    public void testInitState() throws Exception
    {
        assertTrue(new Lexer("X:1\nT:Title").hasNext());
    }

    @Test
    public void testEmptyString() throws Exception
    {
        assertFalse(new Lexer("").hasNext());
    }
}
