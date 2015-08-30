package player.visitor;

import org.junit.Before;
import org.junit.Test;
import player.ast.*;
import player.compiler.Lexer;
import player.compiler.Parser;

import static org.junit.Assert.*;

public class AbcPlayVisitorTest
{

    private AbstractSyntaxTree abcTune;

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testPlayWithOctaves() throws Exception
    {
        abcTune = getParser("X:1\n" +
                        "T:Paddy O'Rafferty\n" +
                        "C:Trad.\n" +
                        "M:6/8\n" +
                        "Q:240\n" +
                        "K:C\n" +
                        "dff ce'e\n"
        ).expectAbcTune();

        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();

        assertEquals("Event: NOTE_ON  Pitch: 74  Tick: 0\n" +
                "Event: NOTE_OFF Pitch: 74  Tick: 1\n" +
                "Event: NOTE_ON  Pitch: 77  Tick: 1\n" +
                "Event: NOTE_OFF Pitch: 77  Tick: 2\n" +
                "Event: NOTE_ON  Pitch: 77  Tick: 2\n" +
                "Event: NOTE_OFF Pitch: 77  Tick: 3\n" +

                "Event: NOTE_ON  Pitch: 72  Tick: 3\n" +
                "Event: NOTE_OFF Pitch: 72  Tick: 4\n" +
                "Event: NOTE_ON  Pitch: 88  Tick: 4\n" +
                "Event: NOTE_OFF Pitch: 88  Tick: 5\n" +
                "Event: NOTE_ON  Pitch: 76  Tick: 5\n" +
                "Event: NOTE_OFF Pitch: 76  Tick: 6\n" +
                "***** End of track *****   Tick: 6\n", player.toString());
    }

    @Test
    public void testPlayWithOnlyKeyAccidental() throws Exception
    {
        abcTune = getParser("X:1\n" +
                        "T:Paddy O'Rafferty\n" +
                        "C:Trad.\n" +
                        "M:6/8\n" +
                        "Q:240\n" +
                        "K:D\n" +
                        "dff ce'e\n"
        ).expectAbcTune();

        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();

        assertEquals("Event: NOTE_ON  Pitch: 74  Tick: 0\n" +
                "Event: NOTE_OFF Pitch: 74  Tick: 1\n" +
                "Event: NOTE_ON  Pitch: 78  Tick: 1\n" +
                "Event: NOTE_OFF Pitch: 78  Tick: 2\n" +
                "Event: NOTE_ON  Pitch: 78  Tick: 2\n" +
                "Event: NOTE_OFF Pitch: 78  Tick: 3\n" +

                "Event: NOTE_ON  Pitch: 73  Tick: 3\n" +
                "Event: NOTE_OFF Pitch: 73  Tick: 4\n" +
                "Event: NOTE_ON  Pitch: 88  Tick: 4\n" +
                "Event: NOTE_OFF Pitch: 88  Tick: 5\n" +
                "Event: NOTE_ON  Pitch: 76  Tick: 5\n" +
                "Event: NOTE_OFF Pitch: 76  Tick: 6\n" +
                "***** End of track *****   Tick: 6\n", player.toString());
    }

    @Test
    public void testPlayWithKeyAndAccidental() throws Exception
    {
        abcTune = getParser("X:1\n" +
                        "T:Paddy O'Rafferty\n" +
                        "C:Trad.\n" +
                        "M:6/8\n" +
                        "Q:240\n" +
                        "K:D\n" +
                        "dff _cec | dff cee\n"
        ).expectAbcTune();

        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();

        assertEquals("Event: NOTE_ON  Pitch: 74  Tick: 0\n" +
                "Event: NOTE_OFF Pitch: 74  Tick: 1\n" +
                "Event: NOTE_ON  Pitch: 78  Tick: 1\n" +
                "Event: NOTE_OFF Pitch: 78  Tick: 2\n" +
                "Event: NOTE_ON  Pitch: 78  Tick: 2\n" +
                "Event: NOTE_OFF Pitch: 78  Tick: 3\n" +
                "Event: NOTE_ON  Pitch: 71  Tick: 3\n" +
                "Event: NOTE_OFF Pitch: 71  Tick: 4\n" +
                "Event: NOTE_ON  Pitch: 76  Tick: 4\n" +
                "Event: NOTE_OFF Pitch: 76  Tick: 5\n" +
                "Event: NOTE_ON  Pitch: 71  Tick: 5\n" +
                "Event: NOTE_OFF Pitch: 71  Tick: 6\n" +

                "Event: NOTE_ON  Pitch: 74  Tick: 6\n" +
                "Event: NOTE_OFF Pitch: 74  Tick: 7\n" +
                "Event: NOTE_ON  Pitch: 78  Tick: 7\n" +
                "Event: NOTE_OFF Pitch: 78  Tick: 8\n" +
                "Event: NOTE_ON  Pitch: 78  Tick: 8\n" +
                "Event: NOTE_OFF Pitch: 78  Tick: 9\n" +
                "Event: NOTE_ON  Pitch: 73  Tick: 9\n" +
                "Event: NOTE_OFF Pitch: 73  Tick: 10\n" +
                "Event: NOTE_ON  Pitch: 76  Tick: 10\n" +
                "Event: NOTE_OFF Pitch: 76  Tick: 11\n" +
                "Event: NOTE_ON  Pitch: 76  Tick: 11\n" +
                "Event: NOTE_OFF Pitch: 76  Tick: 12\n" +
                "***** End of track *****   Tick: 12\n", player.toString());
    }

    @Test
    public void testPlay() throws Exception
    {
        abcTune = getParser("X:1\n" +
                        "T:Paddy O'Rafferty\n" +
                        "C:Trad.\n" +
                        "M:6/8\n" +
                        "Q:240\n" +
                        "K:D\n" +
                        "dff cee|def gfe|dff cee|dfe dBA|dff cee|def gfe|faf gfe |[1 dfe dBA :|[2 dfe dcB|]\n" +
                        "~A3 B3|gfe fdB|AFA B2c|dfe dcB|~A3 ~B3|efe efg|faf gfe  |[1 dfe dcB  :|[2 dfe dBA|]\n" +
                        "fAA eAA|def gfe|fAA eAA|dfe dBA|fAA eAA|def gfe|faf gfe|dfe dBA :|"
        ).expectAbcTune();

        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();
    }

    public Parser getParser(String str)
    {
        return new Parser(new Lexer(str));
    }
}
