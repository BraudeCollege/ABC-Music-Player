package player;

import player.ast.AbstractSyntaxTree;
import player.ast.Accidental;
import player.ast.Basenote;
import player.ast.Octave;

/**
 * Perform semantic analysis and produce an abstract syntax tree
 */
public class Parser
{

    private final Lexer lex;

    public Parser(Lexer lex)
    {
        this.lex = lex;
    }

    /**
     * @return an ast with the root is a basenote
     * @throws player.Parser.UnexpectedTokenException if no basenote found
     * @throws player.Lexer.RunOutOfTokenException if there is no token left
     */
    public AbstractSyntaxTree expectBasenote()
    {
        Token token =  lex.nextToken();

        switch (token.getType()) {
            case A: case B: case C: case D: case E: case F: case G:
            case a: case b: case c: case d: case e: case f: case g:
                return new Basenote(token.getValue().charAt(0));
        }

        throw new UnexpectedTokenException("Unexpected token '"+ token.getValue() +"', expect a Basenote");
    }

    /**
     * @return an ast with the root is an accidental
     * @throws player.Parser.UnexpectedTokenException if no basenote found
     * @throws player.Lexer.RunOutOfTokenException if there is no token left
     */
    public AbstractSyntaxTree expectAccidental()
    {
        Token token =  lex.nextToken();

        switch (token.getType()) {
            case ACC_SHARP:
                return new Accidental(Accidental.Type.SHARP);
            case ACC_SHARP_DOUBLE:
                return new Accidental(Accidental.Type.DOUBLE_SHARP);
            case ACC_FLAT:
                return new Accidental(Accidental.Type.FLAT);
            case ACC_FLAT_DOUBLE:
                return new Accidental(Accidental.Type.DOUBLE_FLAT);
            case ACC_NEUTRAL:
                return new Accidental(Accidental.Type.NEUTRAL);
        }

        throw new UnexpectedTokenException("Unexpected token '"+ token.getValue() +"', expect an accidental");
    }

    /**
     * @return an ast with the root is an octave
     * @throws player.Parser.UnexpectedTokenException if no basenote found
     * @throws player.Lexer.RunOutOfTokenException if there is no token left
     */
    public AbstractSyntaxTree expectOctave()
    {

        Token token =  lex.nextToken();
        int levels = token.getValue().length();

        switch (token.getType()){
            case OCTAVE_UP: return new Octave(Octave.Type.UP, levels);
            case OCTAVE_DOWN: return new Octave(Octave.Type.DOWN, levels);
        }


        throw new UnexpectedTokenException("Unexpected token '"+ token.getValue() +"', expect an octave");
    }

    static class UnexpectedTokenException extends RuntimeException {

        public UnexpectedTokenException(String message)
        {
            super(message);
        }
    }
}
