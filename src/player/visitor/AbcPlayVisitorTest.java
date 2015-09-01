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
                        "dfz ce'e\n"
        ).expectAbcTune();

        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();

        assertEquals("Event: NOTE_ON  Pitch: 74  Tick: 0\n" +
                "Event: NOTE_OFF Pitch: 74  Tick: 1\n" +
                "Event: NOTE_ON  Pitch: 78  Tick: 1\n" +
                "Event: NOTE_OFF Pitch: 78  Tick: 2\n" +

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
        abcTune = getParser("X:2\n" +
                        "T:Kitchen Girl\n" +
                        "C:Trad.\n" +
                        "Q:720\n" +
                        "K:D\n" +
                        "[c4a4] [B4g4]|efed c2cd|e2f2 gaba|g2e2 e2fg|a4 g4|efed cdef|g2d2 efed|c2A2 A4:|\n" +
                        "ABcA BAGB|ABAG EDEG|A2AB c2d2|e3f edcB|ABcA BAGB|ABAG EGAB|cBAc BAG2|A4 A4:|\n"
        ).expectAbcTune();


        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();
    }

    @Test
    public void testPlayWithChords() throws Exception
    {
        abcTune = getParser("X:1\n" +
                        "T:Paddy O'Rafferty\n" +
                        "C:Trad.\n" +
                        "M:6/8\n" +
                        "Q:240\n" +
                        "K:D\n" +
                        "df[abc] ce'e\n"
        ).expectAbcTune();

        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();

        assertEquals("Event: NOTE_ON  Pitch: 74  Tick: 0\n" +
                "Event: NOTE_OFF Pitch: 74  Tick: 1\n" +
                "Event: NOTE_ON  Pitch: 78  Tick: 1\n" +
                "Event: NOTE_OFF Pitch: 78  Tick: 2\n" +

                "Event: NOTE_ON  Pitch: 81  Tick: 2\n" +
                "Event: NOTE_ON  Pitch: 83  Tick: 2\n" +
                "Event: NOTE_ON  Pitch: 73  Tick: 2\n" +
                "Event: NOTE_OFF Pitch: 81  Tick: 3\n" +
                "Event: NOTE_OFF Pitch: 83  Tick: 3\n" +
                "Event: NOTE_OFF Pitch: 73  Tick: 3\n" +

                "Event: NOTE_ON  Pitch: 73  Tick: 3\n" +
                "Event: NOTE_OFF Pitch: 73  Tick: 4\n" +
                "Event: NOTE_ON  Pitch: 88  Tick: 4\n" +
                "Event: NOTE_OFF Pitch: 88  Tick: 5\n" +
                "Event: NOTE_ON  Pitch: 76  Tick: 5\n" +
                "Event: NOTE_OFF Pitch: 76  Tick: 6\n" +
                "***** End of track *****   Tick: 6\n", player.toString());
    }

    @Test
    public void testPlayWithTuplet() throws Exception
    {
        abcTune = getParser("X:1\n" +
                        "T:Paddy O'Rafferty\n" +
                        "C:Trad.\n" +
                        "M:C\n" +
                        "Q:120\n" +
                        "K:C\n" +
                        "CC(3ABC\n"
        ).expectAbcTune();

        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();

        assertEquals("Event: NOTE_ON  Pitch: 60  Tick: 0\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 3\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 3\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 6\n" +

                "Event: NOTE_ON  Pitch: 69  Tick: 6\n" +
                "Event: NOTE_OFF Pitch: 69  Tick: 8\n" +
                "Event: NOTE_ON  Pitch: 71  Tick: 8\n" +
                "Event: NOTE_OFF Pitch: 71  Tick: 10\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 10\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 12\n" +

                "***** End of track *****   Tick: 12\n", player.toString());
    }

    @Test
    public void testRepeatSimple() throws Exception
    {
        abcTune = getParser("X:1\n" +
                        "T:Paddy O'Rafferty\n" +
                        "C:Trad.\n" +
                        "M:6/8\n" +
                        "Q:240\n" +
                        "K:C\n" +
                        "DFF CEE:|\n"
        ).expectAbcTune();

        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();

        assertEquals("Event: NOTE_ON  Pitch: 62  Tick: 0\n" +
                "Event: NOTE_OFF Pitch: 62  Tick: 1\n" +
                "Event: NOTE_ON  Pitch: 65  Tick: 1\n" +
                "Event: NOTE_OFF Pitch: 65  Tick: 2\n" +
                "Event: NOTE_ON  Pitch: 65  Tick: 2\n" +
                "Event: NOTE_OFF Pitch: 65  Tick: 3\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 3\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 4\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 4\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 5\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 5\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 6\n" +

                "Event: NOTE_ON  Pitch: 62  Tick: 6\n" +
                "Event: NOTE_OFF Pitch: 62  Tick: 7\n" +
                "Event: NOTE_ON  Pitch: 65  Tick: 7\n" +
                "Event: NOTE_OFF Pitch: 65  Tick: 8\n" +
                "Event: NOTE_ON  Pitch: 65  Tick: 8\n" +
                "Event: NOTE_OFF Pitch: 65  Tick: 9\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 9\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 10\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 10\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 11\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 11\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 12\n" +
                "***** End of track *****   Tick: 12\n", player.toString());

    }

    @Test
    public void testRepeatWithStartAndEnd() throws Exception
    {
        abcTune = getParser("X:1\n" +
                        "T:Paddy O'Rafferty\n" +
                        "C:Trad.\n" +
                        "M:6/8\n" +
                        "Q:240\n" +
                        "K:C\n" +
                        "DFF |:CEE:|\n"
        ).expectAbcTune();

        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();

        assertEquals("Event: NOTE_ON  Pitch: 62  Tick: 0\n" +
                "Event: NOTE_OFF Pitch: 62  Tick: 1\n" +
                "Event: NOTE_ON  Pitch: 65  Tick: 1\n" +
                "Event: NOTE_OFF Pitch: 65  Tick: 2\n" +
                "Event: NOTE_ON  Pitch: 65  Tick: 2\n" +
                "Event: NOTE_OFF Pitch: 65  Tick: 3\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 3\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 4\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 4\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 5\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 5\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 6\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 6\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 7\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 7\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 8\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 8\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 9\n" +
                "***** End of track *****   Tick: 9\n", player.toString());
    }

    @Test
    public void testRepeatWithAlternateEnding() throws Exception
    {
        abcTune = getParser("X:1\n" +
                        "T:Paddy O'Rafferty\n" +
                        "C:Trad.\n" +
                        "M:6/8\n" +
                        "Q:240\n" +
                        "K:C\n" +
                        "DFF |:CEE |[1 DFF :|[2 ABC |\n"
        ).expectAbcTune();

        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();

        assertEquals(
                // DFF
                "Event: NOTE_ON  Pitch: 62  Tick: 0\n" +
                "Event: NOTE_OFF Pitch: 62  Tick: 1\n" +
                "Event: NOTE_ON  Pitch: 65  Tick: 1\n" +
                "Event: NOTE_OFF Pitch: 65  Tick: 2\n" +
                "Event: NOTE_ON  Pitch: 65  Tick: 2\n" +
                "Event: NOTE_OFF Pitch: 65  Tick: 3\n" +

                // CEE
                "Event: NOTE_ON  Pitch: 60  Tick: 3\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 4\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 4\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 5\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 5\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 6\n" +

                // DFF
                "Event: NOTE_ON  Pitch: 62  Tick: 6\n" +
                "Event: NOTE_OFF Pitch: 62  Tick: 7\n" +
                "Event: NOTE_ON  Pitch: 65  Tick: 7\n" +
                "Event: NOTE_OFF Pitch: 65  Tick: 8\n" +
                "Event: NOTE_ON  Pitch: 65  Tick: 8\n" +
                "Event: NOTE_OFF Pitch: 65  Tick: 9\n" +

                // CEE
                "Event: NOTE_ON  Pitch: 60  Tick: 9\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 10\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 10\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 11\n" +
                "Event: NOTE_ON  Pitch: 64  Tick: 11\n" +
                "Event: NOTE_OFF Pitch: 64  Tick: 12\n" +

                // ABC
                "Event: NOTE_ON  Pitch: 69  Tick: 12\n" +
                "Event: NOTE_OFF Pitch: 69  Tick: 13\n" +
                "Event: NOTE_ON  Pitch: 71  Tick: 13\n" +
                "Event: NOTE_OFF Pitch: 71  Tick: 14\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 14\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 15\n" +

                "***** End of track *****   Tick: 15\n", player.toString());
    }

    @Test
    public void testMultipleRepeat() throws Exception
    {
        abcTune = getParser("X:1\n" +
                        "T:Paddy O'Rafferty\n" +
                        "C:Trad.\n" +
                        "M:6/8\n" +
                        "Q:240\n" +
                        "K:C\n" +
                        "DFF |:CEE |[1 DFF :|[2 ABC |]\n" +
                        "ABC | CEE :| DFF :|\n"
        ).expectAbcTune();

        AbcPlayVisitor player = new AbcPlayVisitor(abcTune, new AbcInfoVisitor(abcTune));

        player.play();

        assertEquals(
                        // DFF
                        "Event: NOTE_ON  Pitch: 62  Tick: 0\n" +
                        "Event: NOTE_OFF Pitch: 62  Tick: 1\n" +
                        "Event: NOTE_ON  Pitch: 65  Tick: 1\n" +
                        "Event: NOTE_OFF Pitch: 65  Tick: 2\n" +
                        "Event: NOTE_ON  Pitch: 65  Tick: 2\n" +
                        "Event: NOTE_OFF Pitch: 65  Tick: 3\n" +

                        // CEE
                        "Event: NOTE_ON  Pitch: 60  Tick: 3\n" +
                        "Event: NOTE_OFF Pitch: 60  Tick: 4\n" +
                        "Event: NOTE_ON  Pitch: 64  Tick: 4\n" +
                        "Event: NOTE_OFF Pitch: 64  Tick: 5\n" +
                        "Event: NOTE_ON  Pitch: 64  Tick: 5\n" +
                        "Event: NOTE_OFF Pitch: 64  Tick: 6\n" +

                        // DFF
                        "Event: NOTE_ON  Pitch: 62  Tick: 6\n" +
                        "Event: NOTE_OFF Pitch: 62  Tick: 7\n" +
                        "Event: NOTE_ON  Pitch: 65  Tick: 7\n" +
                        "Event: NOTE_OFF Pitch: 65  Tick: 8\n" +
                        "Event: NOTE_ON  Pitch: 65  Tick: 8\n" +
                        "Event: NOTE_OFF Pitch: 65  Tick: 9\n" +

                        // CEE
                        "Event: NOTE_ON  Pitch: 60  Tick: 9\n" +
                        "Event: NOTE_OFF Pitch: 60  Tick: 10\n" +
                        "Event: NOTE_ON  Pitch: 64  Tick: 10\n" +
                        "Event: NOTE_OFF Pitch: 64  Tick: 11\n" +
                        "Event: NOTE_ON  Pitch: 64  Tick: 11\n" +
                        "Event: NOTE_OFF Pitch: 64  Tick: 12\n" +

                        // ABC
                        "Event: NOTE_ON  Pitch: 69  Tick: 12\n" +
                        "Event: NOTE_OFF Pitch: 69  Tick: 13\n" +
                        "Event: NOTE_ON  Pitch: 71  Tick: 13\n" +
                        "Event: NOTE_OFF Pitch: 71  Tick: 14\n" +
                        "Event: NOTE_ON  Pitch: 60  Tick: 14\n" +
                        "Event: NOTE_OFF Pitch: 60  Tick: 15\n" +

                        // ABC
                        "Event: NOTE_ON  Pitch: 69  Tick: 15\n" +
                        "Event: NOTE_OFF Pitch: 69  Tick: 16\n" +
                        "Event: NOTE_ON  Pitch: 71  Tick: 16\n" +
                        "Event: NOTE_OFF Pitch: 71  Tick: 17\n" +
                        "Event: NOTE_ON  Pitch: 60  Tick: 17\n" +
                        "Event: NOTE_OFF Pitch: 60  Tick: 18\n" +

                        // CEE
                        "Event: NOTE_ON  Pitch: 60  Tick: 18\n" +
                        "Event: NOTE_OFF Pitch: 60  Tick: 19\n" +
                        "Event: NOTE_ON  Pitch: 64  Tick: 19\n" +
                        "Event: NOTE_OFF Pitch: 64  Tick: 20\n" +
                        "Event: NOTE_ON  Pitch: 64  Tick: 20\n" +
                        "Event: NOTE_OFF Pitch: 64  Tick: 21\n" +

                        // ABC
                        "Event: NOTE_ON  Pitch: 69  Tick: 21\n" +
                        "Event: NOTE_OFF Pitch: 69  Tick: 22\n" +
                        "Event: NOTE_ON  Pitch: 71  Tick: 22\n" +
                        "Event: NOTE_OFF Pitch: 71  Tick: 23\n" +
                        "Event: NOTE_ON  Pitch: 60  Tick: 23\n" +
                        "Event: NOTE_OFF Pitch: 60  Tick: 24\n" +

                        // CEE
                        "Event: NOTE_ON  Pitch: 60  Tick: 24\n" +
                        "Event: NOTE_OFF Pitch: 60  Tick: 25\n" +
                        "Event: NOTE_ON  Pitch: 64  Tick: 25\n" +
                        "Event: NOTE_OFF Pitch: 64  Tick: 26\n" +
                        "Event: NOTE_ON  Pitch: 64  Tick: 26\n" +
                        "Event: NOTE_OFF Pitch: 64  Tick: 27\n" +

                        // DFF
                        "Event: NOTE_ON  Pitch: 62  Tick: 27\n" +
                        "Event: NOTE_OFF Pitch: 62  Tick: 28\n" +
                        "Event: NOTE_ON  Pitch: 65  Tick: 28\n" +
                        "Event: NOTE_OFF Pitch: 65  Tick: 29\n" +
                        "Event: NOTE_ON  Pitch: 65  Tick: 29\n" +
                        "Event: NOTE_OFF Pitch: 65  Tick: 30\n" +

                        // DFF
                        "Event: NOTE_ON  Pitch: 62  Tick: 30\n" +
                        "Event: NOTE_OFF Pitch: 62  Tick: 31\n" +
                        "Event: NOTE_ON  Pitch: 65  Tick: 31\n" +
                        "Event: NOTE_OFF Pitch: 65  Tick: 32\n" +
                        "Event: NOTE_ON  Pitch: 65  Tick: 32\n" +
                        "Event: NOTE_OFF Pitch: 65  Tick: 33\n" +
                        "***** End of track *****   Tick: 33\n", player.toString());
    }

    public Parser getParser(String str)
    {
        return new Parser(new Lexer(str));
    }
}
