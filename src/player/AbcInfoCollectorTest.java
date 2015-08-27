package player;

import org.junit.Before;
import org.junit.Test;
import player.ast.AbstractSyntaxTree;
import player.compiler.Lexer;
import player.compiler.Parser;

public class AbcInfoCollectorTest
{

    private AbstractSyntaxTree ast;

    @Before
    public void setUp() throws Exception
    {

        ast = (getParser("X:1\n" +
                "%Comment 1\n" +
                "%Comment 2\n" +
                "T:ABC Title\n" +
                "C:Beethoven\n" +
                "L:2/4\n" +
                "Q:120\n" +
                "M:C\n" +
                "V:Voice1\n" +
                "K:C#m\n" +
                "[C z3/4] |: (3 A/ B4 E/6 :|[2 \n"
                + "V: The field voice\n" +
                "% this is a comment\n" +
                "[A z] |: (3 A B E :|[2\n")).expectAbcTune();

    }

    @Test
    public void testGetDefaultLength()
    {
        AbcInfoCollector collector = new AbcInfoCollector();

//        assertEquals();


    }

    public Parser getParser(String str)
    {
        return new Parser(new Lexer(str));
    }

}
