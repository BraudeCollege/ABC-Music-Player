package player.visitor;

import player.ast.*;

/**
 * Performs semantic validator on ABC Abstract Syntax Tree
 * TODO: warning about correctness of calculated bpm
 * TODO: correctness of note-length
 */
class AbcValidateVisitor implements AbcVisitor<Void>
{
    /**
     * @param ast root of abstract syntax tree
     */
    public AbcValidateVisitor(AbstractSyntaxTree ast)
    {
        ast.accept(this);
    }

    @Override
    public Void on(AbcTune tune)
    {
        return null;
    }

    @Override
    public Void on(AbcHeader header)
    {
        return null;
    }

    @Override
    public Void on(AbcMusic body)
    {
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
        return null;
    }

    @Override
    public Void on(FieldMeter field)
    {
        return null;
    }

    @Override
    public Void on(FieldTempo field)
    {
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
}
