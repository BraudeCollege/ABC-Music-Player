package player;

import player.ast.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Accidental ac = Accidental.getEmpty();
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
     * @return an ast with the root is a NoteElement
     * @throws UnexpectedTokenException
     */
    public NoteElement expectNoteElement() throws UnexpectedTokenException
    {
        NoteElement noteElement = null;
        try {
            noteElement = expectNote();
        } catch (UnexpectedTokenException e) {
        }

        if (noteElement == null) {
            try {
                noteElement = expectMultiNote();
            } catch (UnexpectedTokenException e) {
            }
        }

        if (noteElement != null)
            return noteElement;

        throw new UnexpectedTokenException("Unexpected token, expect a Note or a Rest");

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
     * @return a note length
     */
    public NoteLength expectNoteLength()
    {
        int multiplier = 1;
        int divider = 1;

        try {
            multiplier = expectNumber();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        try {
            expectSlash();

            divider = 2; // divider is set default to 2 if there is a '/'

            divider = expectNumber();

        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        return new NoteLength(multiplier, divider);
    }

    /**
     * @return a note
     */
    public Note expectNote() throws UnexpectedTokenException
    {
        NoteOrRest noteOrRest = expectNoteOrRest();

        NoteLength noteLength = expectNoteLength();

        return new Note(noteOrRest, noteLength);
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

    /**
     * @return a MultiNote
     */
    public MultiNote expectMultiNote() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() != TokenType.OPEN_BRACKET) {
            lex.backtrack();
            throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect an open bracket");
        }

        ArrayList<Note> notes = new ArrayList<>();

        while (true) {

            try {
                expectSpaces();
            } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
            }

            try {
                notes.add(expectNote());
            } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
                break;
            }
        }

        if (notes.isEmpty())
            throw new UnexpectedTokenException("A MuliNote consists of at least one Note, nothing found");

        MultiNote multiNote = new MultiNote(notes);

        token = lex.nextToken();
        if (token.getType() != TokenType.CLOSE_BRACKET) {
            lex.backtrack();
            throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect a close bracket");
        }

        return multiNote;
    }

    /**
     * expect a '/' character
     *
     * @throws player.Parser.UnexpectedTokenException if there is no slash
     */
    private void expectSlash() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();
        if (token.getType() != TokenType.SLASH) {
            lex.backtrack();
            throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect spaces");
        }
    }

    /**
     * @return spaces
     */
    private String expectSpaces() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();
        if (token.getType() != TokenType.SPACE) {
            lex.backtrack();
            throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect spaces");
        }

        return token.getValue();
    }

    /**
     * @return
     */
    public TupletSpec expectTupletSpec() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() != TokenType.OPEN_PAREN) {
            lex.backtrack();
            throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect a open parenthesis");
        }

        return new TupletSpec(expectDigit());
    }

    /**
     * @return int expected digit
     */
    private int expectDigit() throws UnexpectedTokenException
    {
        int digit = expectNumber();

        if (digit > 9)
            throw new UnexpectedTokenException("Unexpected token '" + digit + "', expect a digit only ");

        return digit;
    }


    public TupletElement expectTupletElement() throws UnexpectedTokenException
    {
        TupletSpec tupletSpec = expectTupletSpec();

        List<NoteElement> noteElements = new ArrayList<>();
        for (int i = 0; i < tupletSpec.getCount(); i++) {

            try {
                expectSpaces();
            } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
            }

            try {
                noteElements.add(expectNoteElement());
            } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
                lex.backtrack(i);
                lex.backtrack(2); // for TupletSpec
                throw e;
            }
        }

        return new TupletElement(tupletSpec, noteElements);
    }

    /**
     * @return a barline
     * @throws UnexpectedTokenException if no barline is found
     */
    public Barline expectBarline() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        switch (token.getType()) {
            case SINGLE_BAR:
                return Barline.getInstance(Barline.Type.SINGLE_BAR);
            case DOUBLE_BAR:
                return Barline.getInstance(Barline.Type.DOUBLE_BAR);
            case OPEN_BAR:
                return Barline.getInstance(Barline.Type.OPEN_BAR);
            case CLOSE_BAR:
                return Barline.getInstance(Barline.Type.CLOSE_BAR);
            case OPEN_REPEAT_BAR:
                return Barline.getInstance(Barline.Type.OPEN_REPEAT_BAR);
            case CLOSE_REPEAT_BAR:
                return Barline.getInstance(Barline.Type.CLOSE_REPEAT_BAR);
        }

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect a barline");
    }

    /**
     * @return an NthRepeat
     * @throws UnexpectedTokenException if no NthRepeat bar is found
     */
    public NthRepeat expectNthRepeat() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() != TokenType.NTH_REPEAT) {
            lex.backtrack();
            throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect an nth-repeat");
        }

        return new NthRepeat(Integer.valueOf(token.getValue().substring(1)));
    }

    /**
     * @return an Element
     * @throws UnexpectedTokenException if no element is found
     */
    public Element expectElement() throws UnexpectedTokenException
    {
        Element element = null;

        try {
            element = expectNoteElement();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        if (element != null) return element;

        try {
            element = expectTupletElement();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        if (element != null) return element;

        try {
            element = expectBarline();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        if (element != null) return element;

        try {
            element = expectNthRepeat();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        if (element != null) return element;

        throw new UnexpectedTokenException("Expect an Element but nothing found");
    }

    /**
     * @return FieldVoice
     * @throws UnexpectedTokenException if field voice is not found
     */
    public FieldVoice expectFieldVoice() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();
        if (token.getType() != TokenType.FIELD_V) {
            lex.backtrack();
            throw new UnexpectedTokenException("Expect a Field_V");
        }

        String text = token.getValue().substring(2);

        Pattern p = Pattern.compile("(.+)");
        Matcher m = p.matcher(text);
        m.find();

        return new FieldVoice(m.group(1));
    }

    /**
     * @return MidTuneField
     * @throws UnexpectedTokenException no MidTuneField is found
     */
    public MidTuneField expectMidTuneField() throws UnexpectedTokenException
    {
        return expectFieldVoice();
    }

    /**
     * @return comment string
     * @throws UnexpectedTokenException
     */
    public Comment expectComment() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();
        if (token.getType() != TokenType.COMMENT) {
            lex.backtrack();
            throw new UnexpectedTokenException("Expect a comment");
        }

        return new Comment(token.getValue().substring(1));
    }

    /**
     * @return collection of elements
     * @throws UnexpectedTokenException if no element is found
     */
    public ElementLine expectElementLine() throws UnexpectedTokenException
    {
        ArrayList<Element> elements = new ArrayList<>();

        while (true) {

            try {
                expectSpaces();
            } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
            }

            try {
                elements.add(expectElement());
            } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
                break;
            }
        }

        if (elements.isEmpty())
            throw new UnexpectedTokenException("At least one Element expected but nothing found");

        return new ElementLine(elements);

    }

    public AbcLine expectAbcLine() throws UnexpectedTokenException
    {
        AbcLine line = null;
        try {
            line = expectElementLine();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        if (line != null)
            return line;

        try {
            line = expectMidTuneField();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        if (line != null)
            return line;

        return expectComment();
    }

    static class UnexpectedTokenException extends Exception
    {
        public UnexpectedTokenException(String message)
        {
            super(message);
        }
    }
}
