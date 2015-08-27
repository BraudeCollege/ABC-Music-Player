package player;


import player.ast.*;

public class AbcInfoCollector implements AbcVisitor<Void>
{

    /**
     * The number of default-length notes per minute.
     */
    private int tempo;

    /**
     * Default note length
     */
    private RationalNumber defaultNoteLength;

    /**
     * Beats per bar
     */
    private int beatsPerBar;

    /**
     * minimum length of all the notes
     */
    private RationalNumber minNoteLength;

    public AbcInfoCollector(AbstractSyntaxTree root)
    {
        // TODO: test the defaults
        // TODO: const or static variable
        defaultNoteLength = new RationalNumber(1,8);
        tempo = 100;
        minNoteLength = new RationalNumber(Integer.MIN_VALUE, 1);

        root.accept(this);
    }

    @Override
    public Void on(AbcTune tune)
    {
        tune.getHeader().accept(this);
        tune.getBody().accept(this);

        return null;
    }

    @Override
    public Void on(AbcHeader header)
    {
        header.getFieldNumber().accept(this);
        header.getFieldKey().accept(this);
        header.getFieldTitle().accept(this);
        header.getOtherFields().stream().forEach(field -> field.accept(this));

        return null;
    }

    @Override
    public Void on(AbcMusic body)
    {
        body.getLines().stream().forEach(line -> line.accept(this));

        return null;
    }

    @Override
    public Void on(FieldNumber field)
    {
        return null;
    }

    @Override
    public Void on(FieldTitle field)
    {
        return null;
    }

    @Override
    public Void on(FieldKey field)
    {
        return null;
    }

    @Override
    public Void on(FieldVoice field)
    {
        return null;
    }

    @Override
    public Void on(FieldComposer field)
    {
        return null;
    }

    @Override
    public Void on(FieldDefaultLength field)
    {
        NoteLengthStrict noteLengthStrict = field.getNoteLengthStrict();

        defaultNoteLength = new RationalNumber(noteLengthStrict.getMultiplier(), noteLengthStrict.getDivider());

        return null;
    }

    @Override
    public Void on(FieldMeter field)
    {
        Meter meter = field.getMeter();

        this.beatsPerBar = meter.getUpper();

        return null;
    }

    @Override
    public Void on(FieldTempo field)
    {
        tempo = field.getTempo();
        return null;
    }

    @Override
    public Void on(Comment c)
    {
        return null;
    }

    @Override
    public Void on(ElementLine line)
    {
        line.getElements().stream().forEach(element -> element.accept(this));
        return null;
    }

    @Override
    public Void on(Element element)
    {
        return null;
    }

    @Override
    public Void on(NthRepeat repeat)
    {
        return null;
    }

    @Override
    public Void on(Barline bar)
    {
        return null;
    }

    @Override
    public Void on(TupletElement element)
    {
        return null;
    }

    @Override
    public Void on(Note note)
    {
        NoteLength length = note.getNoteLength();
        RationalNumber rationalLength = new RationalNumber(length.getMultiplier(), length.getDivider());

        RationalNumber realNoteLength = rationalLength.multiply(defaultNoteLength);

        if (realNoteLength.compare(minNoteLength) < 0)
            minNoteLength = realNoteLength;

        return null;
    }

    @Override
    public Void on(MultiNote note)
    {
        return null;
    }

    @Override
    public Void on(NoteLength noteLength)
    {
        return null;
    }

    @Override
    public Void on(Rest rest)
    {
        return null;
    }

    @Override
    public Void on(Pitch pitch)
    {
        return null;
    }

    @Override
    public Void on(Basenote basenote)
    {
        return null;
    }

    @Override
    public Void on(Accidental acc)
    {
        return null;
    }

    @Override
    public Void on(Octave octave)
    {
        return null;
    }

    @Override
    public Void on(KeyAccidental keyAccidental)
    {
        return null;
    }

    @Override
    public Void on(Keynote keynote)
    {
        return null;
    }

    @Override
    public Void on(NoteLengthStrict noteLengthStrict)
    {
        return null;
    }

    @Override
    public Void on(ModeMinor modeMinor)
    {
        return null;
    }

    @Override
    public Void on(MeterFraction meterFraction)
    {
        return null;
    }

    @Override
    public Void on(MeterCPipe meterCPipe)
    {
        return null;
    }

    @Override
    public Void on(MeterC meterC)
    {
        return null;
    }

    @Override
    public Void on(Key key)
    {
        return null;
    }

    /**
     * @return beats per minute, where each beat equals a quarter note
     */
    public int getBpm()
    {
        RationalNumber quarterNoteLength = new RationalNumber(1, 4);
        RationalNumber tempo = new RationalNumber(this.tempo, 1);
        RationalNumber bpm = tempo.multiply(defaultNoteLength).divide(quarterNoteLength);

        return Math.floorDiv(bpm.getNumerator(), bpm.getDenominator());
    }

    public int getBeatsPerBar()
    {
        return beatsPerBar;
    }

    public RationalNumber getMinLength()
    {
        return minLength;
    }
}
