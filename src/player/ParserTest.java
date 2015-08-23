package player;

import org.junit.Test;
import player.ast.Basenote;

import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testBasenote()
    {
        Lexer lex = new Lexer("A");
        Parser pars = new Parser(lex);

        assertEquals(new Basenote('A'),pars.expectBasenote());
    }


    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testBaseNoteFail() throws Exception
    {
        Lexer lex = new Lexer("~");
        Parser pars = new Parser(lex);

        pars.expectBasenote();
    }

    @Test(expected = Lexer.RunOutOfTokenException.class)
    public void testBaseNoteEmptyLexer() throws Exception
    {
        Lexer lex = new Lexer("");
        Parser pars = new Parser(lex);

        pars.expectBasenote();
    }
}
