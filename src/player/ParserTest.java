package player;

import org.junit.Test;
import player.ast.*;

import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testBasenote() throws Exception
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
    public void testAccidental() throws Exception
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
    public void testOctave() throws Exception
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

    @Test
    public void testPitch() throws Exception
    {
        Parser parser = getParser("B");
        assertEquals(new Pitch(new Basenote('B'), Accidental.getEmptyObj(), Octave.getEmptyObj()), parser.expectPitch());

        parser = getParser("__c");
        assertEquals(new Pitch(new Basenote('c'), new Accidental(Accidental.Type.DOUBLE_FLAT), Octave.getEmptyObj()), parser.expectPitch());

        parser = getParser("^A,");
        assertEquals(new Pitch(new Basenote('A'), new Accidental(Accidental.Type.SHARP), new Octave(Octave.Type.DOWN, 1)), parser.expectPitch());

        parser = getParser("B'");
        assertEquals(new Pitch(new Basenote('B'), Accidental.getEmptyObj(), new Octave(Octave.Type.UP, 1)), parser.expectPitch());
    }

    @Test
    public void testExpectRest() throws Exception
    {
        Parser parser = getParser("z");
        assertEquals(Rest.getInstance(), parser.expectRest());
    }

    public Parser getParser(String str)
    {
        return new Parser(new Lexer(str));
    }
}
