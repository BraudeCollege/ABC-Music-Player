package player;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import player.ast.*;
import player.compiler.Lexer;
import player.compiler.Parser;

public class AbcInfoCollectorTest
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
                "[C z3/4] |: (3 A/ B4 E/6 :|[2 \n"
                + "V: The field voice\n" +
                "% this is a comment\n" +
                "[A z] |: (3 A B E :|[2\n").expectAbcTune();

    }

    @Test
    public void testGetDefaultLength()
    {
//        AbcInfoCollector collector = new AbcInfoCollector(abcTune);

    }

    @Test
    public void testGetBpm() throws Exception
    {
        AbcInfoCollector collector = new AbcInfoCollector(abcTune);

        collector.on(new FieldTempo(120));

        collector.on(new FieldDefaultLength(new NoteLengthStrict(4,8)));

        assertEquals(240, collector.getBpm());
    }

    @Test
    public void testBeatsPerBar() throws Exception
    {
        AbcInfoCollector collector = new AbcInfoCollector(abcTune);

        collector.on(new FieldMeter(new MeterFraction(4,4)));

        assertEquals(4, collector.getBeatsPerBar());
    }

    @Test
    public void testMinLength() throws Exception
    {
        AbcInfoCollector collector = new AbcInfoCollector(abcTune);
        assertEquals(new RationalNumber(1,12), collector.getMinNoteLength());
    }

    public Parser getParser(String str)
    {
        return new Parser(new Lexer(str));
    }

}
