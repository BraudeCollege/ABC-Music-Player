package player.compiler;

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
        Parser parser = getParser("B/2");
        assertEquals(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1,2)), parser.expectPitch());

        NoteLength noteLength = new NoteLength(1,1);

        parser = getParser("__c");
        assertEquals(new Pitch(new Basenote('c'), Accidental.getInstance(Accidental.Type.DOUBLE_FLAT), Octave.getEmpty(), noteLength), parser.expectPitch());

        parser = getParser("^A,");
        assertEquals(new Pitch(new Basenote('A'), Accidental.getInstance(Accidental.Type.SHARP), Octave.getDown(1), noteLength), parser.expectPitch());

        parser = getParser("B'");
        assertEquals(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getUp(1), noteLength), parser.expectPitch());
    }

    @Test
    public void testExpectRest() throws Exception
    {
        Parser parser = getParser("z2");
        assertEquals(new Rest(new NoteLength(2,1)), parser.expectRest());
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
    public void testNote() throws Exception
    {
        Parser parser = getParser("A");
        assertEquals(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)), parser.expectNote());

        parser = getParser("^^A,2/3");
        assertEquals(new Pitch(new Basenote('A'), Accidental.getInstance(Accidental.Type.DOUBLE_SHARP), Octave.getDown(1), new NoteLength(2, 3)), parser.expectNote());

        parser = getParser("^B2/4");
        assertEquals(new Pitch(new Basenote('B'), Accidental.getInstance(Accidental.Type.SHARP), Octave.getEmpty(), new NoteLength(2, 4)), parser.expectNote());

        parser = getParser("C2/");
        assertEquals(new Pitch(new Basenote('C'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(2, 2)), parser.expectNote());

        parser = getParser("z/3");
        assertEquals(new Rest(new NoteLength(1, 3)), parser.expectNote());

    }

    @Test
    public void testMultinote() throws Exception

    {
        Parser parser = getParser("[A1/2 z/3 B _C']");

        ArrayList<Note> notes = new ArrayList<>();
        notes.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 2)));
        notes.add(new Rest(new NoteLength(1, 3)));
        notes.add(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        notes.add(new Pitch(new Basenote('C'), Accidental.getInstance(Accidental.Type.FLAT), Octave.getUp(1), new NoteLength(1, 1)));

        assertEquals(new MultiNote(notes), parser.expectMultiNote());
    }

    @Test
    public void testNoteElement() throws Exception
    {
        Parser parser = getParser("[A1/2 z/3]");

        ArrayList<Note> notes = new ArrayList<>();
        notes.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 2)));
        notes.add(new Rest(new NoteLength(1, 3)));

        assertEquals(new MultiNote(notes), parser.expectNoteElement());

        parser = getParser("A1/2");
        assertEquals(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 2)), parser.expectNoteElement());
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
        notes.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 2)));
        ArrayList<Note> mulNotes = new ArrayList<>();
        mulNotes.add(new Pitch(new Basenote('C'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        mulNotes.add(new Pitch(new Basenote('D'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        mulNotes.add(new Pitch(new Basenote('F'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        notes.add(new MultiNote(mulNotes));
        notes.add(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));

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
        notes.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 2)));
        notes.add(new Rest(new NoteLength(1, 3)));
        assertEquals(new MultiNote(notes), parser.expectElement());

        parser = getParser("(3 A1/2 [CDF] E");

        ArrayList<NoteElement> notes2 = new ArrayList<>();
        notes2.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 2)));

        ArrayList<Note> mulNotes = new ArrayList<>();
        mulNotes.add(new Pitch(new Basenote('C'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        mulNotes.add(new Pitch(new Basenote('D'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        mulNotes.add(new Pitch(new Basenote('F'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        notes2.add(new MultiNote(mulNotes));
        notes2.add(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));

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
        notes.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        notes.add(new Rest(new NoteLength(1, 1)));
        NoteElement multiNote = new MultiNote(notes);

        Barline openRepeatBar = new Barline(Barline.Type.OPEN_REPEAT_BAR);
        Barline closeRepeatBar = new Barline(Barline.Type.CLOSE_REPEAT_BAR);

        TupletSpec tupletSpec = new TupletSpec(3);
        List<NoteElement> noteElements = new ArrayList<>();
        noteElements.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        noteElements.add(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        noteElements.add(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
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
        notes.add(new Pitch(new Basenote('C'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        notes.add(new Rest(new NoteLength(3, 4)));
        NoteElement multiNote = new MultiNote(notes);

        Barline openRepeatBar = new Barline(Barline.Type.OPEN_REPEAT_BAR);
        Barline closeRepeatBar = new Barline(Barline.Type.CLOSE_REPEAT_BAR);

        TupletSpec tupletSpec = new TupletSpec(3);
        List<NoteElement> noteElements = new ArrayList<>();
        noteElements.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 2)));
        noteElements.add(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(4, 1)));
        noteElements.add(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 6)));
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
        notes1.add(new Pitch(new Basenote('C'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        notes1.add(new Rest(new NoteLength(3, 4)));
        NoteElement multiNote1 = new MultiNote(notes1);

        Barline openRepeatBar1 = new Barline(Barline.Type.OPEN_REPEAT_BAR);
        Barline closeRepeatBar1 = new Barline(Barline.Type.CLOSE_REPEAT_BAR);

        TupletSpec tupletSpec1 = new TupletSpec(3);
        List<NoteElement> noteElements1 = new ArrayList<>();
        noteElements1.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 2)));
        noteElements1.add(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(4, 1)));
        noteElements1.add(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 6)));
        TupletElement tupletElement1 = new TupletElement(tupletSpec1, noteElements1);

        NthRepeat twiceRepeat = new NthRepeat(2);

        elements1.add(multiNote1);
        elements1.add(openRepeatBar1);
        elements1.add(tupletElement1);
        elements1.add(closeRepeatBar1);
        elements1.add(twiceRepeat);


        List<Element> elements = new ArrayList<>();

        List<Note> notes = new ArrayList<>();
        notes.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        notes.add(new Rest(new NoteLength(1, 1)));
        NoteElement multiNote = new MultiNote(notes);

        Barline openRepeatBar = new Barline(Barline.Type.OPEN_REPEAT_BAR);
        Barline closeRepeatBar = new Barline(Barline.Type.CLOSE_REPEAT_BAR);

        TupletSpec tupletSpec = new TupletSpec(3);
        List<NoteElement> noteElements = new ArrayList<>();
        noteElements.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        noteElements.add(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        noteElements.add(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
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
    public void testExpectFieldNumber() throws Exception
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
    public void testFieldTitle() throws Exception
    {
        Parser parser = getParser("T:Abc\n");
        assertEquals(new FieldTitle("Abc"), parser.expectFieldTitle());

        parser = getParser("T: abc\n");
        assertEquals(new FieldTitle(" abc"), parser.expectFieldTitle());

        parser = getParser("T:2 \n");
        assertEquals(new FieldTitle("2 "), parser.expectFieldTitle());

        parser = getParser("T: xyz's \n");
        assertEquals(new FieldTitle(" xyz's "), parser.expectFieldTitle());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testExpectFieldTitleFail() throws Exception
    {
        getParser("C:\n").expectFieldTitle();
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
        getParser("C:\n").expectFieldComposer();
    }

    @Test
    public void testKeyAccidental() throws Exception
    {
        assertEquals(KeyAccidental.getFlat(), getParser("b").expectKeyAccidental());
        assertEquals(KeyAccidental.getSharp(), getParser("#").expectKeyAccidental());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testKeyAccidentalFails() throws Exception
    {
        getParser("|").expectKeyAccidental();
    }

    @Test
    public void testExpectKeynote() throws Exception
    {
        assertEquals(new Keynote(new Basenote('C'), KeyAccidental.getSharp()), getParser("C#").expectKeynote());
        assertEquals(new Keynote(new Basenote('b'), KeyAccidental.getFlat()), getParser("bb").expectKeynote());
        assertEquals(new Keynote(new Basenote('A'), KeyAccidental.getNone()), getParser("A").expectKeynote());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testExpectKeynoteFails() throws Exception
    {
        getParser("||").expectKeynote();
    }

    @Test
    public void testExpectModeMinor() throws Exception
    {
        assertEquals(ModeMinor.getInstance(), getParser("m").expectModeMinor());
    }
    
    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testExpectModeMinorFails() throws Exception
    {
        getParser("M").expectModeMinor();
    }

    @Test
    public void testExpectKey() throws Exception
    {
        assertEquals(new Key(new Keynote(new Basenote('C'), KeyAccidental.getSharp()), ModeMinor.getInstance()), getParser("C#m").expectKey());
        assertEquals(new Key(new Keynote(new Basenote('C'), KeyAccidental.getSharp()), ModeMinor.getNone()), getParser("C#").expectKey());
    }

    @Test
    public void testExpectFieldKey() throws Exception
    {
        Key key = new Key(new Keynote(new Basenote('C'), KeyAccidental.getSharp()), ModeMinor.getInstance());
        assertEquals(new FieldKey(key), getParser("K:C#m\n").expectFieldKey());
        assertEquals(new FieldKey(key), getParser("K: C#m\n").expectFieldKey());
        assertEquals(new FieldKey(key), getParser("K:C#m  \n").expectFieldKey());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testExpectFieldKeyFails() throws Exception
    {
        getParser("K:  \n").expectFieldKey();
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testExpectFieldKeyFailsTwo() throws Exception
    {
        getParser("T: C\n").expectFieldKey();
    }

    @Test
    public void testExpectFieldTempo() throws Exception
    {
        assertEquals(new FieldTempo(120), getParser("Q:120\n").expectFieldTempo());
        assertEquals(new FieldTempo(120), getParser("Q: 120 \n").expectFieldTempo());
        assertEquals(new FieldTempo(120), getParser("Q:120  \n").expectFieldTempo());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testExpectFieldTempoFails() throws Exception
    {
        getParser("Q:  \n").expectFieldTempo();
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testExpectFieldTempoFailsTwo() throws Exception
    {
        getParser("T: 120\n").expectFieldTempo();
    }

    @Test
    public void testMeterFraction() throws Exception
    {
        assertEquals(new MeterFraction(2,4), getParser("2/4").expectMeterFraction());
        assertEquals(new MeterFraction(5,10), getParser("5/10").expectMeterFraction());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testMeterFractionFails() throws Exception
    {
        getParser("5/ #").expectMeterFraction();
    }

    @Test
    public void testMeterC() throws Exception
    {
        assertEquals(MeterC.getInstance(), getParser("C").expectC());
    }

    @Test
    public void testMeterCPipe() throws Exception
    {
        assertEquals(MeterCPipe.getInstance(), getParser("C|").expectCPipe());
    }

    @Test
    public void testNoteLengthStrict() throws Exception
    {
        Parser parser = getParser("3/4");
        assertEquals(new NoteLengthStrict(3, 4), parser.expectNoteLengthStrict());

        parser = getParser("1/4");
        assertEquals(new NoteLengthStrict(1, 4), parser.expectNoteLengthStrict());

        parser = getParser("2/2");
        assertEquals(new NoteLengthStrict(2, 2), parser.expectNoteLengthStrict());
    }

    @Test
    public void testExpectMeter() throws Exception
    {
        assertEquals(new MeterFraction(2,4), getParser("2/4").expectMeter());
        assertEquals(MeterC.getInstance(), getParser("C").expectMeter());
        assertEquals(MeterCPipe.getInstance(), getParser("C|").expectMeter());
    }

    @Test
    public void testExpectFieldMeter() throws Exception
    {
        assertEquals(new FieldMeter(new MeterFraction(2,4)), getParser("M:2/4\n").expectFieldMeter());
        assertEquals(new FieldMeter(MeterCPipe.getInstance()), getParser("M: C|\n").expectFieldMeter());
        assertEquals(new FieldMeter(MeterC.getInstance()), getParser("M: C  \n").expectFieldMeter());
    }

    @Test
    public void testExpectFieldDefaultLength() throws Exception
    {
        FieldDefaultLength f = new FieldDefaultLength(new NoteLengthStrict(2,4));
        assertEquals(f, getParser("L:2/4\n").expectFieldDefaultLength());
        assertEquals(f, getParser("L: 2/4 \n").expectFieldDefaultLength());
        assertEquals(f, getParser("L:2/4  \n").expectFieldDefaultLength());
    }

    @Test
    public void testExpectOtherField() throws Exception
    {
        assertEquals(new Comment(" comment field"), getParser("% comment field\n").expectOtherField());
        assertEquals(new FieldDefaultLength(new NoteLengthStrict(2, 4)), getParser("L:2/4\n").expectOtherField());
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testNoteLengthStrictFails() throws Exception
    {
        getParser("/").expectNoteLengthStrict();
    }

    @Test(expected = Parser.UnexpectedTokenException.class)
    public void testNoteLengthStrictFailsTwo() throws Exception
    {
        getParser("/2").expectNoteLengthStrict();
    }

    @Test
    public void testExpectAbcHeader() throws Exception
    {
        List<OtherField> fields = new ArrayList<>();
        fields.add(new FieldComposer("Beethoven"));
        fields.add(new FieldDefaultLength(new NoteLengthStrict(2, 4)));
        fields.add(new FieldTempo(120));
        fields.add(new FieldMeter(MeterC.getInstance()));
        fields.add(new FieldVoice("Voice1"));

        Key key = new Key(new Keynote(new Basenote('C'), KeyAccidental.getSharp()), ModeMinor.getInstance());

        AbcHeader header = new AbcHeader(new FieldNumber(1), new FieldTitle("ABC Title"), fields, new FieldKey(key));

        assertEquals(header, getParser(
                "X:1\n" +
                "%Comment 1\n" +
                "%Comment 2\n" +
                "T:ABC Title\n" +
                "C:Beethoven\n" +
                "L:2/4\n" +
                "Q:120\n" +
                "M:C\n" +
                "V:Voice1\n" +
                "K:C#m\n"
        ).expectAbcHeader());
    }

    @Test
    public void testExpectAbcTune() throws Exception
    {
        List<OtherField> fields = new ArrayList<>();
        fields.add(new FieldComposer("Beethoven"));
        fields.add(new FieldDefaultLength(new NoteLengthStrict(2, 4)));
        fields.add(new FieldTempo(120));
        fields.add(new FieldMeter(MeterC.getInstance()));
        fields.add(new FieldVoice("Voice1"));

        Key key = new Key(new Keynote(new Basenote('C'), KeyAccidental.getSharp()), ModeMinor.getInstance());

        AbcHeader header = new AbcHeader(new FieldNumber(1), new FieldTitle("ABC Title"), fields, new FieldKey(key));

        Parser parser = getParser(
                "X:1\n" +
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
                "[A z] |: (3 A B E :|[2\n"
        );

        List<Element> elements1 = new ArrayList<>();

        List<Note> notes1 = new ArrayList<>();
        notes1.add(new Pitch(new Basenote('C'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        notes1.add(new Rest(new NoteLength(3, 4)));
        NoteElement multiNote1 = new MultiNote(notes1);

        Barline openRepeatBar1 = new Barline(Barline.Type.OPEN_REPEAT_BAR);
        Barline closeRepeatBar1 = new Barline(Barline.Type.CLOSE_REPEAT_BAR);

        TupletSpec tupletSpec1 = new TupletSpec(3);
        List<NoteElement> noteElements1 = new ArrayList<>();
        noteElements1.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 2)));
        noteElements1.add(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(4, 1)));
        noteElements1.add(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 6)));
        TupletElement tupletElement1 = new TupletElement(tupletSpec1, noteElements1);

        NthRepeat twiceRepeat = new NthRepeat(2);

        elements1.add(multiNote1);
        elements1.add(openRepeatBar1);
        elements1.add(tupletElement1);
        elements1.add(closeRepeatBar1);
        elements1.add(twiceRepeat);


        List<Element> elements = new ArrayList<>();

        List<Note> notes = new ArrayList<>();
        notes.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        notes.add(new Rest(new NoteLength(1, 1)));
        NoteElement multiNote = new MultiNote(notes);

        Barline openRepeatBar = new Barline(Barline.Type.OPEN_REPEAT_BAR);
        Barline closeRepeatBar = new Barline(Barline.Type.CLOSE_REPEAT_BAR);

        TupletSpec tupletSpec = new TupletSpec(3);
        List<NoteElement> noteElements = new ArrayList<>();
        noteElements.add(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        noteElements.add(new Pitch(new Basenote('B'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
        noteElements.add(new Pitch(new Basenote('E'), Accidental.getEmpty(), Octave.getEmpty(), new NoteLength(1, 1)));
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

        AbcMusic body = new AbcMusic(abcLines);

        assertEquals(new AbcTune(header, body), parser.expectAbcTune());
    }

    public Parser getParser(String str)
    {
        return new Parser(new Lexer(str));
    }
}
