package player.visitor;

import org.junit.Before;
import org.junit.Test;
import player.ast.*;
import player.compiler.Lexer;
import player.compiler.Parser;

public class AbcPlayVisitorTest
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
                "Q:60\n" +
                "M:C\n" +
                "V:Voice1\n" +
                "K:C#m\n" +
                "[C A3/4] \n"
                + "V: The field voice\n" +
                "% this is a comment\n").expectAbcTune();

    }

    @Test
    public void testPlayPitch() throws Exception
    {
        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();

        //assertEquals( "Event: NOTE_ON  Pitch: 60  Tick: 0\n" +
        //              "Event: NOTE_OFF Pitch: 60  Tick: 6\n" +
        //              "Event: NOTE_ON  Pitch: 69  Tick: 6\n" +
        //              "Event: NOTE_OFF Pitch: 69  Tick: 12\n" +
        //              "***** End of track *****   Tick: 18\n", player.toString());
    }

    public Parser getParser(String str)
    {
        return new Parser(new Lexer(str));
    }
}
