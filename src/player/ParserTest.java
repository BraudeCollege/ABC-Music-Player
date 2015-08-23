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

}
