package player.visitor;

import player.RationalNumber;
import player.ast.*;

import java.util.HashSet;
import java.util.Set;

class TupletNoteLengthVisitor implements AbcVisitor<Void>
{
    private Set<RationalNumber> noteLengths;

    private RationalNumber defaultNoteLength;

    public TupletNoteLengthVisitor(TupletElement tuplet, RationalNumber defaultNoteLength)
    {
        noteLengths = new HashSet<>();

        this.defaultNoteLength = defaultNoteLength;

        tuplet.accept(this);
    }

    public Set<RationalNumber> getNoteLengths()
    {
        return new HashSet<>(noteLengths);
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
    public Void on(TupletElement tuplet)
    {
        tuplet.getNoteElements().stream().forEach(noteElement -> noteElement.accept(this));
        return null;
    }

    @Override
    public Void on(MultiNote multiNote)
    {
        multiNote.getNotes().stream().forEach(note -> note.accept(this));
        return null;
    }

    @Override
    public Void on(NoteLength length)
    {
        RationalNumber rationalLength = new RationalNumber(length.getUpper(), length.getLower());

        RationalNumber realNoteLength = rationalLength.multiply(defaultNoteLength);

        // save the real note length
        noteLengths.add(realNoteLength);

        return null;
    }

    @Override
    public Void on(Rest rest)
    {
        rest.getNoteLength().accept(this);
        return null;
    }

    @Override
    public Void on(Pitch pitch)
    {
        pitch.getNoteLength().accept(this);
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
