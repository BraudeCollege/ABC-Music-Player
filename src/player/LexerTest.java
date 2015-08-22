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
        assertTrue(lex.hasNext());
    }

    @Test
    public void testEmptyString() throws Exception
    {
        Lexer lex = new Lexer("");
        assertFalse(lex.hasNext());
    }

    @Test
    public void testHasTokenTillEmpty() throws Exception
    {

        Lexer lex = new Lexer("A/2");

        assertTrue(lex.hasNext());
        assertEquals(new Token(TokenType.A,"A"),lex.nextToken());
        assertEquals(new Token(TokenType.SLASH,"/"),lex.nextToken());
        assertEquals(new Token(TokenType.DIGIT,"2"),lex.nextToken());
        assertFalse(lex.hasNext());
    }
}
