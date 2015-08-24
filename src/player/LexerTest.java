package player;


import org.junit.Test;

import static org.junit.Assert.*;

public class LexerTest
{

    @Test
    public void testSimpleNextToken() throws Exception
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
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertTrue(lex.hasNext());
        assertEquals(new Token(TokenType.SLASH, "/"), lex.nextToken());
        assertTrue(lex.hasNext());
        assertEquals(new Token(TokenType.NUMBER, "2"), lex.nextToken());
        assertFalse(lex.hasNext());
        assertFalse(lex.hasNext());
    }

    @Test(expected = Lexer.RunOutOfTokenException.class)
    public void testFromEmptyToNextToken() throws Exception
    {
        Lexer lex = new Lexer("");

        lex.nextToken();
    }

    @Test
    public void testSampleFile() throws Exception
    {
        Lexer lex = new Lexer("X:1\n" +
                "T: Note lengths and default note length\n" +
                "M:C\n" +
                "% A shit on the head is some comments\n" +
                "K:C\n" +
                "L:1/16\n" +
                "A/2 A/ A A2 A3 A4 A6 A7 A8 A12 A15 A16|]\n");


        assertEquals(new Token(TokenType.FIELD_X, "X:"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "1"), lex.nextToken());
        assertEquals(new Token(TokenType.LINEFEED, "\n"), lex.nextToken());
        assertEquals(new Token(TokenType.FIELD_T, "T:"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken()); // _TODO: should not inteprete as SPACE ???
        assertEquals(new Token(TokenType.DOT_PLUS, "Note lengths and default note length"), lex.nextToken());
        assertEquals(new Token(TokenType.LINEFEED, "\n"), lex.nextToken());
        assertEquals(new Token(TokenType.FIELD_M, "M:"), lex.nextToken());
        assertEquals(new Token(TokenType.C, "C"), lex.nextToken());
        assertEquals(new Token(TokenType.LINEFEED, "\n"), lex.nextToken());
        assertEquals(new Token(TokenType.COMMENT, "% A shit on the head is some comments"), lex.nextToken());
        assertEquals(new Token(TokenType.LINEFEED, "\n"), lex.nextToken());
        assertEquals(new Token(TokenType.FIELD_K, "K:"), lex.nextToken());
        assertEquals(new Token(TokenType.C, "C"), lex.nextToken());
        assertEquals(new Token(TokenType.LINEFEED, "\n"), lex.nextToken());
        assertEquals(new Token(TokenType.FIELD_L, "L:"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "1"), lex.nextToken());
        assertEquals(new Token(TokenType.SLASH, "/"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "16"), lex.nextToken());
        assertEquals(new Token(TokenType.LINEFEED, "\n"), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.SLASH, "/"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "2"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.SLASH, "/"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "2"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "3"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "4"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "6"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "7"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "8"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "12"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "15"), lex.nextToken());
        assertEquals(new Token(TokenType.SPACE, " "), lex.nextToken());
        assertEquals(new Token(TokenType.A, "A"), lex.nextToken());
        assertEquals(new Token(TokenType.NUMBER, "16"), lex.nextToken());
        assertEquals(new Token(TokenType.CLOSE_BAR, "|]"), lex.nextToken());
        assertEquals(new Token(TokenType.LINEFEED, "\n"), lex.nextToken());

    }

    @Test
    public void testBacktrack() throws Exception
    {
        Lexer lex = new Lexer("AB");

        lex.nextToken();
        lex.nextToken();
        assertFalse(lex.hasNext());

        lex.backtrack();
        assertTrue(lex.hasNext());
        assertEquals(new Token(TokenType.B, "B"), lex.nextToken());
    }
}
