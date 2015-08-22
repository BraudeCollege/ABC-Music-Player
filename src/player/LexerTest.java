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
        Lexer lex = new Lexer("X:1\nT:Title");
        System.out.println(lex);
        assertTrue(lex.hasNext());
    }

    @Test
    public void testEmptyString() throws Exception
    {
        Lexer lex = new Lexer("");
        System.out.println(lex);
        assertFalse(lex.hasNext());
    }
}
