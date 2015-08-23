package player;

import org.junit.Before;
import org.junit.Test;
import player.ast.Accidental;
import player.ast.Basenote;

import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testBasenote()
    {
        Parser pars = getParser("A");
        assertEquals(new Basenote('A'), pars.expectBasenote());
    }


    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testBaseNoteFail() throws Exception
    {
        Parser pars = getParser("~");
        pars.expectBasenote();
    }

    @Test(expected = Lexer.RunOutOfTokenException.class)
    public void testBaseNoteEmptyLexer() throws Exception
    {
        Parser pars = getParser("");

        pars.expectBasenote();
    }


    @Test
    public void testAccidental()
    {
        Parser pars = getParser("^^");
        assertEquals(new Accidental(Accidental.Type.DOUBLE_SHARP), pars.expectAccidental());

        pars = getParser("__");
        assertEquals(new Accidental(Accidental.Type.DOUBLE_FLAT), pars.expectAccidental());

        pars = getParser("=");
        assertEquals(new Accidental(Accidental.Type.NEUTRAL), pars.expectAccidental());
    }


    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testAccidentalFail() throws Exception
    {
        Parser pars = getParser("!");

        pars.expectAccidental();
    }

    @Test
    public void testOctave()
    {
        Parser parser  = getParser("'''");
        assertEquals(new Octave(Octave.Type.UP, 3), parser.expectOctave());

        parser  = getParser(",,");
        assertEquals(new Octave(Octave.Type.DOWN, 2), parser.expectOctave());
    }


    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testOctaveFail() throws Exception
    {
        Parser pars  = getParser("xx");
        pars.expectOctave();
    }

    public Parser getParser(String str)
    {
        return new Parser(new Lexer(str));
    }
}
