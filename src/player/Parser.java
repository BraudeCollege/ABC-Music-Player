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
     * @throws player.Lexer.RunOutOfTokenException    if there is no token left
     */
    public Basenote expectBasenote() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        switch (token.getType()) {
            case A:
            case B:
            case C:
            case D:
            case E:
            case F:
            case G:
            case a:
            case b:
            case c:
            case d:
            case e:
            case f:
            case g:
                return new Basenote(token.getValue().charAt(0));
        }

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect a Basenote");
    }

    /**
     * @return an ast with the root is an Accidental
     * @throws player.Parser.UnexpectedTokenException if no basenote found
     * @throws player.Lexer.RunOutOfTokenException    if there is no token left
     */
    public Accidental expectAccidental() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        switch (token.getType()) {
            case ACC_SHARP:
                return Accidental.getInstance(Accidental.Type.SHARP);
            case ACC_SHARP_DOUBLE:
                return Accidental.getInstance(Accidental.Type.DOUBLE_SHARP);
            case ACC_FLAT:
                return Accidental.getInstance(Accidental.Type.FLAT);
            case ACC_FLAT_DOUBLE:
                return Accidental.getInstance(Accidental.Type.DOUBLE_FLAT);
            case ACC_NEUTRAL:
                return Accidental.getInstance(Accidental.Type.NEUTRAL);
        }

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect an accidental");
    }

    /**
     * @return an ast with the root is an Octave
     * @throws player.Parser.UnexpectedTokenException if no basenote found
     * @throws player.Lexer.RunOutOfTokenException    if there is no token left
     */
    public Octave expectOctave() throws UnexpectedTokenException
    {

        Token token = lex.nextToken();
        int levels = token.getValue().length();

        switch (token.getType()) {
            case OCTAVE_UP:
                return Octave.getUp(levels);
            case OCTAVE_DOWN:
                return Octave.getDown(levels);
        }

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect an octave");
    }

    /**
     * @return an ast with the root is a Pitch
     * @throws player.Parser.UnexpectedTokenException if no basenote found
     * @throws player.Lexer.RunOutOfTokenException    if there is no token left
     */
    public Pitch expectPitch() throws UnexpectedTokenException
    {
        Accidental ac = Accidental.getEmptyObj();
        try {
            ac = expectAccidental();
        } catch (UnexpectedTokenException e) {
        }

        Basenote basenote = expectBasenote();

        Octave octave = Octave.getEmpty();
        try {
            octave = expectOctave();
        } catch (UnexpectedTokenException e) {
        } catch (Lexer.RunOutOfTokenException e) {
        }

        return new Pitch(basenote, ac, octave);
    }


    /**
     * @return an ast with the root is a Rest
     * @throws player.Parser.UnexpectedTokenException if no rest is found
     * @throws player.Lexer.RunOutOfTokenException    if there is no token left
     */
    public Rest expectRest() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() == TokenType.REST)
            return Rest.getInstance();

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect a Rest");
    }

    /**
     * @return an ast with the root is a NoteOrRest
     * @throws UnexpectedTokenException
     */
    public NoteOrRest expectNoteOrRest() throws UnexpectedTokenException
    {
        NoteOrRest note = null;
        try {
            note = expectPitch();
        } catch (UnexpectedTokenException e) {
        }

        if (note == null) {
            try {
                note = expectRest();
            } catch (UnexpectedTokenException e) {
            }
        }

        if (note != null)
            return note;

        throw new UnexpectedTokenException("Unexpected token, expect a Note or a Rest");

    }

    /**
     * @return
     */
    public NoteLength expectNoteLength() throws UnexpectedTokenException
    {
        int multiplier = 1;
        int divider = 1;

        try {
            multiplier = expectNumber();
        } catch (UnexpectedTokenException e) {
        } catch (Lexer.RunOutOfTokenException e) {}

        try {

            Token token = lex.nextToken();

            if (token.getType() == TokenType.SLASH) {

                divider = 2; // divider is set default to 2 if there is a '/'

                divider = expectNumber();
            }

        } catch (Lexer.RunOutOfTokenException e) {}

        return new NoteLength(multiplier, divider);
    }

    /**
     * @return int read from lexer
     */
    private int expectNumber() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() == TokenType.NUMBER)
            return Integer.valueOf(token.getValue());

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect a Number");
    }

    static class UnexpectedTokenException extends Exception
    {

        public UnexpectedTokenException(String message)
        {
            super(message);
        }
    }
}
