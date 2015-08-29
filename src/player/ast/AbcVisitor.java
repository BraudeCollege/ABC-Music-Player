package player.ast;

/**
 * Visitor interface of Abc data structure
 *
 * @param <R> return type
 */
public interface AbcVisitor<R>
{
    public R on(AbcTune tune);

    public R on(AbcHeader header);

    public R on(AbcMusic body);

    public R on(FieldNumber field);

    public R on(FieldTitle field);

    public R on(FieldKey field);

    public R on(FieldVoice field);

    public R on(FieldComposer field);

    public R on(FieldDefaultLength field);

    public R on(FieldMeter field);

    public R on(FieldTempo field);

    public R on(Comment c);

    public R on(ElementLine line);

    public R on(Element element);

    public R on(NthRepeat repeat);

    public R on(Barline bar);

    public R on(TupletElement element);

    public R on(MultiNote note);

    public R on(NoteLength noteLength);

    public R on(Rest rest);

    public R on(Pitch pitch);

    public R on(Basenote basenote);

    public R on(Accidental acc);

    public R on(Octave octave);

    public R on(KeyAccidental keyAccidental);

    public R on(Keynote keynote);

    public R on(NoteLengthStrict noteLengthStrict);

    public R on(ModeMinor modeMinor);

    public R on(MeterFraction meterFraction);

    public R on(MeterCPipe meterCPipe);

    public R on(MeterC meterC);

    public R on(Key key);
}
