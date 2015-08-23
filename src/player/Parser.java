package player;

import player.ast.*;

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
    public Basenote expectBasenote() throws UnexpectedTokenException
    {
        Token token =  lex.nextToken();

        switch (token.getType()) {
            case A: case B: case C: case D: case E: case F: case G:
            case a: case b: case c: case d: case e: case f: case g:
                return new Basenote(token.getValue().charAt(0));
        }

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '"+ token.getValue() +"', expect a Basenote");
    }

    /**
     * @return an ast with the root is an Accidental
     * @throws player.Parser.UnexpectedTokenException if no basenote found
     * @throws player.Lexer.RunOutOfTokenException if there is no token left
     */
    public Accidental expectAccidental() throws UnexpectedTokenException
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

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '"+ token.getValue() +"', expect an accidental");
    }

    /**
     * @return an ast with the root is an Octave
     * @throws player.Parser.UnexpectedTokenException if no basenote found
     * @throws player.Lexer.RunOutOfTokenException if there is no token left
     */
    public Octave expectOctave() throws UnexpectedTokenException
    {

        Token token =  lex.nextToken();
        int levels = token.getValue().length();

        switch (token.getType()){
            case OCTAVE_UP: return new Octave(Octave.Type.UP, levels);
            case OCTAVE_DOWN: return new Octave(Octave.Type.DOWN, levels);
        }

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '"+ token.getValue() +"', expect an octave");
    }

    /**
     * @return an ast with the root is a Pitch
     * @throws player.Parser.UnexpectedTokenException if no basenote found
     * @throws player.Lexer.RunOutOfTokenException if there is no token left
     */
    public Pitch expectPitch() throws UnexpectedTokenException
    {
        Accidental ac = Accidental.getEmptyObj();
        try {
            ac = expectAccidental();
        } catch (UnexpectedTokenException e) {
            lex.backtrack();
        }

        Basenote basenote = expectBasenote();

        Octave octave = Octave.getEmptyObj();
        try {
            octave = expectOctave();
        } catch (UnexpectedTokenException e) {
            lex.backtrack();
        } catch (Lexer.RunOutOfTokenException e) {
        }

        return new Pitch(basenote, ac, octave);
    }


    /**
     * @return an ast with the root is a Rest
     * @throws player.Parser.UnexpectedTokenException if no rest is found
     * @throws player.Lexer.RunOutOfTokenException if there is no token left
     */
    public Rest expectRest() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() == TokenType.REST)
            return Rest.getInstance();

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '"+ token.getValue() +"', expect a Rest");
    }

    static class UnexpectedTokenException extends Exception {

        public UnexpectedTokenException(String message)
        {
            super(message);
        }
    }
}
