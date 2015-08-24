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

        String line = token.getValue().substring(2);

        return new FieldVoice(removeLineFeed(line));
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

        String line = token.getValue().substring(1);

        return new Comment(removeLineFeed(line));
    }

    /**
     * Remove trailing linefeeds from string
     * @param str
     * @return string with linefeeds at the end remove
     */
    private String removeLineFeed(String str)
    {
        Pattern p = Pattern.compile("(.+)");
        Matcher m = p.matcher(str);
        m.find();

        return m.group(1);
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

        expectLinefeed();

        return new ElementLine(elements);

    }

    private void expectLinefeed() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() != TokenType.LINEFEED) {
            lex.backtrack();
            throw new UnexpectedTokenException("Linefeed expected but nothing found");
        }
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

    /**
     * @return AbcMusic containing one or more AbcLine
     * @throws UnexpectedTokenException if no AbcLine found
     */
    public AbcMusic expectAbcMusic() throws UnexpectedTokenException
    {
        ArrayList<AbcLine> lines = new ArrayList<>();

        while (true) {
            try {
                lines.add(expectAbcLine());
            } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
                break;
            }
        }

        if (lines.isEmpty())
            throw new UnexpectedTokenException("Expect an AbcLine but nothing found");

        return new AbcMusic(lines);
    }

    /**
     * @return A FieldNumber specified a Number for the ABCfile
     */
    public FieldNumber expectFieldNumber() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() != TokenType.FIELD_X) {
            lex.backtrack();
            throw new UnexpectedTokenException("Expect a Field_X");
        }

        ignoreSpaces();

        int num = expectNumber();

        ignoreSpaces();

        expectLinefeed();

        return new FieldNumber(num);
    }

    /**
     * discard spaces
     */
    private void ignoreSpaces()
    {
        try {
            expectSpaces();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }
    }

    /**
     * @return FieldTitle
     * @throws UnexpectedTokenException if no title composer is found
     */
    public FieldTitle expectFieldTitle() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() != TokenType.FIELD_T) {
            lex.backtrack();
            throw new UnexpectedTokenException("Expect a Field_T");
        }

        String line = token.getValue().substring(2);

        return new FieldTitle(removeLineFeed(line));
    }

    /**
     * @return FieldComposer
     * @throws UnexpectedTokenException if no field composer is found
     */
    public FieldComposer expectFieldComposer() throws UnexpectedTokenException
    {

        Token token = lex.nextToken();

        if (token.getType() != TokenType.FIELD_C) {
            lex.backtrack();
            throw new UnexpectedTokenException("Expect a Field_C");
        }

        String line = token.getValue().substring(2);

        return new FieldComposer(removeLineFeed(line));
    }

    public NoteLengthStrict expectNoteLengthStrict() throws UnexpectedTokenException
    {

        int multiplier = expectNumber();
        expectSlash();
        int divider = expectNumber();

        return new NoteLengthStrict(multiplier, divider);
    }

    public KeyAccidental expectKeyAccidental() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        switch (token.getType()) {
            case SHARP:
                return KeyAccidental.getSharp();
            case b:
                return KeyAccidental.getFlat();
        }

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect a Sharp(#) or a Flat(b)");
    }

    /**
     * @return Keynote
     * @throws UnexpectedTokenException if keynote not found
     */
    public Keynote expectKeynote() throws UnexpectedTokenException
    {
        Basenote basenote = expectBasenote();

        KeyAccidental ka = KeyAccidental.getNone();

        try {
            ka = expectKeyAccidental();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        return new Keynote(basenote, ka);
    }

    /**
     * @return a ModeMinor
     * @throws UnexpectedTokenException if no mode minor found
     */
    public ModeMinor expectModeMinor() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() == TokenType.MODE_MINOR)
            return ModeMinor.getInstance();

        lex.backtrack();

        throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect a mode-minor");
    }

    public Key expectKey() throws UnexpectedTokenException
    {
        Keynote keynote = expectKeynote();

        ModeMinor m = ModeMinor.getNone();

        try {
            m = expectModeMinor();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        return new Key(keynote, m);
    }

    public FieldKey expectFieldKey() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() != TokenType.FIELD_K) {
            lex.backtrack();
            throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect 'K:'");
        }

        ignoreSpaces();

        Key k = expectKey();

        ignoreSpaces();

        expectLinefeed();

        return new FieldKey(k);
    }

    /**
     * @return FieldTempo
     * @throws UnexpectedTokenException
     */
    public FieldTempo expectFieldTempo() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() != TokenType.FIELD_Q) {
            lex.backtrack();
            throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect 'Q:'");
        }

        ignoreSpaces();

        int t = expectNumber();

        ignoreSpaces();

        expectLinefeed();

        return new FieldTempo(t);
    }

    /**
     * @return
     * @throws UnexpectedTokenException
     */
    public MeterFraction expectMeterFraction() throws UnexpectedTokenException
    {
        int multiplier = expectNumber();

        expectSlash();

        int divider = expectNumber();

        return new MeterFraction(multiplier, divider);
    }

    public MeterCPipe expectCPipe() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() == TokenType.C_PIPE)
            return MeterCPipe.getInstance();

        lex.backtrack();
        throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect 'C_PIPE'");
    }

    public MeterC expectC() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() == TokenType.C)
            return MeterC.getInstance();

        lex.backtrack();
        throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect 'C'");
    }

    /**
     * @return
     * @throws UnexpectedTokenException
     */
    public Meter expectMeter() throws UnexpectedTokenException
    {
        try {
            return expectC();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        try {
            return expectCPipe();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        try {
            return expectMeterFraction();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        throw new UnexpectedTokenException("Unexpected token, expect C or C| or a Meter Fraction");
    }

    /**
     * @return FieldMeter
     * @throws UnexpectedTokenException
     */
    public FieldMeter expectFieldMeter() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() != TokenType.FIELD_M) {
            lex.backtrack();
            throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect 'M:'");
        }

        ignoreSpaces();

        Meter m = expectMeter();

        ignoreSpaces();

        expectLinefeed();

        return new FieldMeter(m);
    }

    /**
     * @return FieldDefaultLength
     * @throws UnexpectedTokenException
     */
    public FieldDefaultLength expectFieldDefaultLength() throws UnexpectedTokenException
    {
        Token token = lex.nextToken();

        if (token.getType() != TokenType.FIELD_L) {
            lex.backtrack();
            throw new UnexpectedTokenException("Unexpected token '" + token.getValue() + "', expect 'L:'");
        }

        ignoreSpaces();

        NoteLengthStrict m = expectNoteLengthStrict();

        ignoreSpaces();

        expectLinefeed();

        return new FieldDefaultLength(m);
    }

    /**
     * @return Objects whose class implements OtherField
     * @throws UnexpectedTokenException
     */
    public OtherField expectOtherField() throws UnexpectedTokenException
    {
        try {
            return expectFieldComposer();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        try {
            return expectFieldDefaultLength();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        try {
            return expectFieldMeter();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        try {
            return expectFieldTempo();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        try {
            return expectFieldVoice();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        try {
            return expectComment();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        throw new UnexpectedTokenException("Expect either field-composer, field-default-length, field-meter, field-tempo, field-voice or comment, nothing found.");
    }

    public AbcHeader expectAbcHeader() throws UnexpectedTokenException
    {
        FieldNumber fn = expectFieldNumber();

        try {
            while (true)
                expectComment();
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        FieldTitle ft = expectFieldTitle();

        List<OtherField> fields = new ArrayList<>();
        try {
            while (true)
                fields.add(expectOtherField());
        } catch (UnexpectedTokenException | Lexer.RunOutOfTokenException e) {
        }

        FieldKey fk = expectFieldKey();

        return new AbcHeader(fn, ft, fields, fk);
    }


    static class UnexpectedTokenException extends Exception
    {
        public UnexpectedTokenException(String message)
        {
            super(message);
        }
    }
}
