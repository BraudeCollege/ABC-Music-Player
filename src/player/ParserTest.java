package player;

import org.junit.Test;
import player.ast.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ParserTest
{

    @Test
    public void testBasenote() throws Exception
    {
        Parser pars = getParser("A");
        assertEquals(new Basenote('A'), pars.expectBasenote());
    }


    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testBaseNoteFail() throws Exception
    {
        Parser pars = getParser("~");
        pars.expectBasenote();
    }

    @Test(expected = Lexer.RunOutOfTokenException.class)
    public void testBaseNoteEmptyLexer() throws Exception
    {
        Parser pars = getParser("");

        pars.expectBasenote();
    }


    @Test
    public void testAccidental() throws Exception
    {
        Parser pars = getParser("^^");
        assertEquals(Accidental.getInstance(Accidental.Type.DOUBLE_SHARP), pars.expectAccidental());

        pars = getParser("__");
        assertEquals(Accidental.getInstance(Accidental.Type.DOUBLE_FLAT), pars.expectAccidental());

        pars = getParser("=");
        assertEquals(Accidental.getInstance(Accidental.Type.NEUTRAL), pars.expectAccidental());
    }


    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testAccidentalFail() throws Exception
    {
        Parser pars = getParser("!");

        pars.expectAccidental();
    }

    @Test
    public void testOctave() throws Exception
    {
        Parser parser = getParser("'''");
        assertEquals(Octave.getUp(3), parser.expectOctave());

        parser = getParser(",,");
        assertEquals(Octave.getDown(2), parser.expectOctave());
    }


    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testOctaveFail() throws Exception
    {
        Parser pars = getParser("xx");
        pars.expectOctave();
    }

    @Test
    public void testPitch() throws Exception
    {
        Parser parser = getParser("B");
        assertEquals(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty()), parser.expectPitch());

        parser = getParser("__c");
        assertEquals(new Pitch(new Basenote('c'), Accidental.getInstance(Accidental.Type.DOUBLE_FLAT), Octave.getEmpty()), parser.expectPitch());

        parser = getParser("^A,");
        assertEquals(new Pitch(new Basenote('A'), Accidental.getInstance(Accidental.Type.SHARP), Octave.getDown(1)), parser.expectPitch());

        parser = getParser("B'");
        assertEquals(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getUp(1)), parser.expectPitch());
    }

    @Test
    public void testExpectRest() throws Exception
    {
        Parser parser = getParser("z");
        assertEquals(Rest.getInstance(), parser.expectRest());
    }

    @Test
    public void testExpectNoteLength() throws Exception
    {
        Parser parser = getParser("4");
        assertEquals(new NoteLength(4, 1), parser.expectNoteLength());

        parser = getParser("4/4");
        assertEquals(new NoteLength(4, 4), parser.expectNoteLength());

        parser = getParser("/32");
        assertEquals(new NoteLength(1, 32), parser.expectNoteLength());

        parser = getParser("/");
        assertEquals(new NoteLength(1, 2), parser.expectNoteLength());

        parser = getParser("10/");
        assertEquals(new NoteLength(10, 2), parser.expectNoteLength());

        parser = getParser("");
        assertEquals(new NoteLength(1, 1), parser.expectNoteLength());
    }

    @Test
    public void testNoteOrRest() throws Exception
    {

        Parser parser = getParser("z");

        assertEquals(Rest.getInstance(), parser.expectNoteOrRest());

        parser = getParser("B");

        assertEquals(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty()), parser.expectNoteOrRest());

        parser = getParser("__c");
        assertEquals(new Pitch(new Basenote('c'), Accidental.getInstance(Accidental.Type.DOUBLE_FLAT), Octave.getEmpty()), parser.expectNoteOrRest());


        parser = getParser("^D,,");
        assertEquals(new Pitch(new Basenote('D'), Accidental.getInstance(Accidental.Type.SHARP), Octave.getDown(2)), parser.expectNoteOrRest());
    }

    @Test
    public void testNote() throws Exception
    {
        Parser parser = getParser("A");
        assertEquals(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)), parser.expectNote());

        parser = getParser("^^A,2/3");
        assertEquals(new Note(new Pitch(new Basenote('A'), Accidental.getInstance(Accidental.Type.DOUBLE_SHARP), Octave.getDown(1)), new NoteLength(2, 3)), parser.expectNote());

        parser = getParser("^B2/4");
        assertEquals(new Note(new Pitch(new Basenote('B'), Accidental.getInstance(Accidental.Type.SHARP), Octave.getEmpty()), new NoteLength(2, 4)), parser.expectNote());

        parser = getParser("C2/");
        assertEquals(new Note(new Pitch(new Basenote('C'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(2, 2)), parser.expectNote());

        parser = getParser("z/3");
        assertEquals(new Note(Rest.getInstance(), new NoteLength(1, 3)), parser.expectNote());

    }

    @Test
    public void testMultinote() throws Exception

    {
        Parser parser = getParser("[A1/2 z/3 B _C']");

        ArrayList<Note> notes = new ArrayList<>();
        notes.add(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 2)));
        notes.add(new Note(Rest.getInstance(), new NoteLength(1, 3)));
        notes.add(new Note(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        notes.add(new Note(new Pitch(new Basenote('C'), Accidental.getInstance(Accidental.Type.FLAT), Octave.getUp(1)), new NoteLength(1, 1)));

        assertEquals(new MultiNote(notes), parser.expectMultiNote());
    }

    @Test
    public void testNoteElement() throws Exception
    {
        Parser parser = getParser("[A1/2 z/3]");

        ArrayList<Note> notes = new ArrayList<>();
        notes.add(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 2)));
        notes.add(new Note(Rest.getInstance(), new NoteLength(1, 3)));

        assertEquals(new MultiNote(notes), parser.expectNoteElement());

        parser = getParser("A1/2");
        assertEquals(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 2)), parser.expectNoteElement());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testMultiNoteFailsBracket() throws Exception
    {
        getParser("A1/2").expectMultiNote();
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testMultiNoteFailsEmpty() throws Exception
    {
        getParser("[  ]").expectMultiNote();
    }

    @Test
    public void testTupletSpec() throws Exception
    {
        Parser parser = getParser("(3");
        assertEquals(new TupletSpec(3), parser.expectTupletSpec());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testTupletSpecFails() throws Exception
    {
        Parser parser = getParser("(A");
        parser.expectTupletSpec();
    }

    @Test
    public void testTupletElement() throws Exception
    {
        Parser parser = getParser("(3 A1/2 [CDF] E");

        ArrayList<NoteElement> notes = new ArrayList<>();
        notes.add(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 2)));
        ArrayList<Note> mulNotes = new ArrayList<>();
        mulNotes.add(new Note(new Pitch(new Basenote('C'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        mulNotes.add(new Note(new Pitch(new Basenote('D'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        mulNotes.add(new Note(new Pitch(new Basenote('F'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        notes.add(new MultiNote(mulNotes));
        notes.add(new Note(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));

        TupletElement tupletElement = new TupletElement(new TupletSpec(3), notes);
        assertEquals(tupletElement, parser.expectTupletElement());
    }

    @Test
    public void testBarline() throws Exception
    {
        Parser pars = getParser("||");
        assertEquals(Barline.getInstance(Barline.Type.DOUBLE_BAR), pars.expectBarline());

        pars = getParser("|]");
        assertEquals(Barline.getInstance(Barline.Type.CLOSE_BAR), pars.expectBarline());

        pars = getParser("|");
        assertEquals(Barline.getInstance(Barline.Type.SINGLE_BAR), pars.expectBarline());

        pars = getParser("[|");
        assertEquals(Barline.getInstance(Barline.Type.OPEN_BAR), pars.expectBarline());

        pars = getParser("|:");
        assertEquals(Barline.getInstance(Barline.Type.OPEN_REPEAT_BAR), pars.expectBarline());

        pars = getParser(":|");
        assertEquals(Barline.getInstance(Barline.Type.CLOSE_REPEAT_BAR), pars.expectBarline());


    }


    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testBarlineFail() throws Exception
    {
        getParser("[x").expectBarline();
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testBarlineFailTwo() throws Exception
    {
        getParser(":]").expectBarline();
    }


    @Test
    public void testNthRepeat() throws Exception
    {
        Parser parser = getParser("[1");

        assertEquals(new NthRepeat(1), parser.expectNthRepeat());

        parser = getParser("[2");
        assertEquals(new NthRepeat(2), parser.expectNthRepeat());

        parser = getParser("[10");
        assertEquals(new NthRepeat(10), parser.expectNthRepeat());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testNthRepeatFails() throws Exception
    {
        getParser("[").expectNthRepeat();
    }


    @Test
    public void testElement() throws Exception
    {
        Parser parser = getParser("[A1/2 z/3]");
        ArrayList<Note> notes = new ArrayList<>();
        notes.add(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 2)));
        notes.add(new Note(Rest.getInstance(), new NoteLength(1, 3)));
        assertEquals(new MultiNote(notes), parser.expectElement());

        parser = getParser("(3 A1/2 [CDF] E");

        ArrayList<NoteElement> notes2 = new ArrayList<>();
        notes2.add(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 2)));

        ArrayList<Note> mulNotes = new ArrayList<>();
        mulNotes.add(new Note(new Pitch(new Basenote('C'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        mulNotes.add(new Note(new Pitch(new Basenote('D'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        mulNotes.add(new Note(new Pitch(new Basenote('F'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        notes2.add(new MultiNote(mulNotes));
        notes2.add(new Note(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));

        TupletElement tupletElement = new TupletElement(new TupletSpec(3), notes2);
        assertEquals(tupletElement, parser.expectElement());

        Parser pars = getParser("||");
        assertEquals(Barline.getInstance(Barline.Type.DOUBLE_BAR), pars.expectElement());

        parser = getParser("[2");
        assertEquals(new NthRepeat(2), parser.expectElement());
    }

    @Test
    public void testExpectFieldVoice() throws Exception
    {
        Parser parser = getParser("V: The field voice\n");
        assertEquals(new FieldVoice(" The field voice"), parser.expectFieldVoice());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testFieldVoiceFails() throws Exception
    {
        getParser("X: 1\n").expectFieldVoice();
    }

    @Test
    public void testExpectMidTuneField() throws Exception
    {
        Parser parser = getParser("V: The field voice\n");
        assertEquals(new FieldVoice(" The field voice"), parser.expectMidTuneField());
    }

    @Test
    public void testExpectComment() throws Exception
    {
        Parser parser = getParser("% this is a comment\n");
        assertEquals(new Comment(" this is a comment"), parser.expectComment());
    }

    @Test
    public void testElementLine() throws Exception
    {
        Parser parser = getParser("[A z] |: (3 A B E :|[2 \n");

        List<Element> elementLines = new ArrayList<>();

        List<Note> notes = new ArrayList<>();
        notes.add(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        notes.add(new Note(Rest.getInstance(), new NoteLength(1, 1)));
        NoteElement multiNote = new MultiNote(notes);

        Barline openRepeatBar = new Barline(Barline.Type.OPEN_REPEAT_BAR);
        Barline closeRepeatBar = new Barline(Barline.Type.CLOSE_REPEAT_BAR);

        TupletSpec tupletSpec = new TupletSpec(3);
        List<NoteElement> noteElements = new ArrayList<>();
        noteElements.add(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        noteElements.add(new Note(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        noteElements.add(new Note(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        TupletElement tupletElement = new TupletElement(tupletSpec, noteElements);

        NthRepeat twiceRepeat = new NthRepeat(2);

        elementLines.add(multiNote);
        elementLines.add(openRepeatBar);
        elementLines.add(tupletElement);
        elementLines.add(closeRepeatBar);
        elementLines.add(twiceRepeat);

        assertEquals(new ElementLine(elementLines), parser.expectElementLine());
    }

    @Test
    public void testAbcLine() throws Exception
    {
        Parser parser = getParser("[C z3/4] |: (3 A/ B4 E/6 :|[2  \n");

        List<Element> elementLines = new ArrayList<>();

        List<Note> notes = new ArrayList<>();
        notes.add(new Note(new Pitch(new Basenote('C'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        notes.add(new Note(Rest.getInstance(), new NoteLength(3, 4)));
        NoteElement multiNote = new MultiNote(notes);

        Barline openRepeatBar = new Barline(Barline.Type.OPEN_REPEAT_BAR);
        Barline closeRepeatBar = new Barline(Barline.Type.CLOSE_REPEAT_BAR);

        TupletSpec tupletSpec = new TupletSpec(3);
        List<NoteElement> noteElements = new ArrayList<>();
        noteElements.add(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 2)));
        noteElements.add(new Note(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(4, 1)));
        noteElements.add(new Note(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 6)));
        TupletElement tupletElement = new TupletElement(tupletSpec, noteElements);

        NthRepeat twiceRepeat = new NthRepeat(2);

        elementLines.add(multiNote);
        elementLines.add(openRepeatBar);
        elementLines.add(tupletElement);
        elementLines.add(closeRepeatBar);
        elementLines.add(twiceRepeat);

        assertEquals(new ElementLine(elementLines), parser.expectAbcLine());

        parser = getParser("V: The field voice\n");
        assertEquals(new FieldVoice(" The field voice"), parser.expectAbcLine());

        parser = getParser("% this is a comment\n");
        assertEquals(new Comment(" this is a comment"), parser.expectAbcLine());

    }

    @Test
    public void testAbcMusic() throws Exception
    {
        Parser parser = getParser("[C z3/4] |: (3 A/ B4 E/6 :|[2 \n"
                        + "V: The field voice\n" +
                        "% this is a comment\n" +
                        "[A z] |: (3 A B E :|[2\n"
        );

        List<Element> elements1 = new ArrayList<>();

        List<Note> notes1 = new ArrayList<>();
        notes1.add(new Note(new Pitch(new Basenote('C'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        notes1.add(new Note(Rest.getInstance(), new NoteLength(3, 4)));
        NoteElement multiNote1 = new MultiNote(notes1);

        Barline openRepeatBar1 = new Barline(Barline.Type.OPEN_REPEAT_BAR);
        Barline closeRepeatBar1 = new Barline(Barline.Type.CLOSE_REPEAT_BAR);

        TupletSpec tupletSpec1 = new TupletSpec(3);
        List<NoteElement> noteElements1 = new ArrayList<>();
        noteElements1.add(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 2)));
        noteElements1.add(new Note(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(4, 1)));
        noteElements1.add(new Note(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 6)));
        TupletElement tupletElement1 = new TupletElement(tupletSpec1, noteElements1);

        NthRepeat twiceRepeat = new NthRepeat(2);

        elements1.add(multiNote1);
        elements1.add(openRepeatBar1);
        elements1.add(tupletElement1);
        elements1.add(closeRepeatBar1);
        elements1.add(twiceRepeat);


        List<Element> elements = new ArrayList<>();

        List<Note> notes = new ArrayList<>();
        notes.add(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        notes.add(new Note(Rest.getInstance(), new NoteLength(1, 1)));
        NoteElement multiNote = new MultiNote(notes);

        Barline openRepeatBar = new Barline(Barline.Type.OPEN_REPEAT_BAR);
        Barline closeRepeatBar = new Barline(Barline.Type.CLOSE_REPEAT_BAR);

        TupletSpec tupletSpec = new TupletSpec(3);
        List<NoteElement> noteElements = new ArrayList<>();
        noteElements.add(new Note(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        noteElements.add(new Note(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        noteElements.add(new Note(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty()), new NoteLength(1, 1)));
        TupletElement tupletElement = new TupletElement(tupletSpec, noteElements);

        elements.add(multiNote);
        elements.add(openRepeatBar);
        elements.add(tupletElement);
        elements.add(closeRepeatBar);
        elements.add(twiceRepeat);

        AbcLine elementLine = new ElementLine(elements1);
        AbcLine fieldVoice = new FieldVoice(" The field voice");
        AbcLine comment = new Comment(" this is a comment");
        AbcLine elementLine1 = new ElementLine(elements);

        List<AbcLine> abcLines = new ArrayList<>();
        abcLines.add(elementLine);
        abcLines.add(fieldVoice);
        abcLines.add(comment);
        abcLines.add(elementLine1);

        assertEquals(new AbcMusic(abcLines), parser.expectAbcMusic());

    }

    @Test
    public void testfieldNumber() throws Exception
    {
        Parser parser = getParser("X: 12\n");
        assertEquals(new FieldNumber(12), parser.expectFieldNumber());

        parser = getParser("X:1\n");
        assertEquals(new FieldNumber(1), parser.expectFieldNumber());

        parser = getParser("X:2 \n");
        assertEquals(new FieldNumber(2), parser.expectFieldNumber());

        parser = getParser("X: 3 \n");
        assertEquals(new FieldNumber(3), parser.expectFieldNumber());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testExpectFieldNumberFail() throws Exception
    {
        getParser("X: \n").expectFieldNumber();
    }

    @Test
    public void testFieldComposer() throws Exception
    {
        Parser parser = getParser("C:Abc\n");
        assertEquals(new FieldComposer("Abc"), parser.expectFieldComposer());

        parser = getParser("C: abc\n");
        assertEquals(new FieldComposer(" abc"), parser.expectFieldComposer());

        parser = getParser("C:2 \n");
        assertEquals(new FieldComposer("2 "), parser.expectFieldComposer());

        parser = getParser("C: xyz's \n");
        assertEquals(new FieldComposer(" xyz's "), parser.expectFieldComposer());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testExpectFieldComposerFail() throws Exception
    {
        getParser("X: \n").expectFieldNumber();
    }


    @Test
    public void testNoteLengthStrict() throws Exception
    {
        Parser parser = getParser("3/4");


    }

    public Parser getParser(String str)
    {
        return new Parser(new Lexer(str));
    }
}
