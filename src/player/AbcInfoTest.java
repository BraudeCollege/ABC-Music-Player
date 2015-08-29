package player;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import player.ast.*;
import player.compiler.Lexer;
import player.compiler.Parser;

public class AbcInfoTest
{

    private AbstractSyntaxTree abcTune;

    @Before
    public void setUp() throws Exception
    {
        abcTune = getParser("X:1\n" +
                "%Comment 1\n" +
                "%Comment 2\n" +
                "T:ABC Title\n" +
                "C:Beethoven\n" +
                "L:4/8\n" +
                "Q:120\n" +
                "M:C\n" +
                "V:Voice1\n" +
                "K:C#m\n" +
                "[C z3/4] |: (3 A/ B4 E/6 :|[2 \n" +
                "[A z] |: (3 A B E :|[2\n").expectAbcTune();

    }

    private Parser getParser(String str)
    {
        return new Parser(new Lexer(str));
    }

    @Test
    public void testDefaults() throws Exception
    {
        abcTune = getParser("X:1\n" +
                "T:ABC Title\n" +
                "K:C#m\n" +
                "A\n").expectAbcTune();

        AbcInfo collector = new AbcInfo(abcTune);

        assertEquals(new RationalNumber(1,8), collector.getDefaultNoteLength());
        assertEquals(100, collector.getTempo());
        assertEquals(new MeterFraction(4,4), collector.getMeter());
        assertEquals("Unknown", collector.getComposer());
    }

    @Test
    public void testMinLength() throws Exception
    {
        AbcInfo collector = new AbcInfo(abcTune);
        assertEquals(new RationalNumber(1,12), collector.getMinNoteLength());
    }

    @Test
    public void testFields() throws Exception
    {
        abcTune = getParser("X:103\n" +
                "T:ABC Title\n" +
                "C:Mozart\n" +
                "M:C|\n" +
                "K:C#m\n" +
                "A\n").expectAbcTune();

        AbcInfo collector = new AbcInfo(abcTune);

        assertEquals("Mozart", collector.getComposer());
        assertEquals("ABC Title", collector.getTitle());
        assertEquals(103, collector.getId());
        assertEquals(MeterCPipe.getInstance(), collector.getMeter());
    }
}
