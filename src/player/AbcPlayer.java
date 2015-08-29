package player;

import player.ast.*;
import sound.SequencePlayer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;

public class AbcPlayer
{
    class PlayVisitor implements AbcVisitor<Void>
    {
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
            line.getElements().stream().forEach(element -> element.accept(this));
            return null;
        }

        @Override
        public Void on(Element element)
        {
            element.accept(this);
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
        public Void on(MultiNote mnote)
        {
            mnote.getNotes().stream().forEach(note -> note.accept(this));
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
            NoteLength nl = pitch.getNoteLength();

            RationalNumber rationalNoteLength = new RationalNumber(nl.getMultiplier(), nl.getDivider());

            RationalNumber realNoteLength = rationalNoteLength.multiply(abcInfo.getDefaultNoteLength());

            int ticks = realNoteLength.divide(abcInfo.getMinNoteLength()).getNumerator();

            sound.Pitch soundPitch = new sound.Pitch(pitch.getBasenote().getSymbol());

            sequencePlayer.addNote(soundPitch.toMidiNote(), currentTick, ticks);

            currentTick += ticks;

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

    /**
     * Note length of a quarter note
     */
    private static final RationalNumber QUARTER_NOTE_LENGTH = new RationalNumber(1, 4);

    /**
     * SequencePlayer plays pitches and rest
     */
    private final SequencePlayer sequencePlayer;

    /**
     * count the ticks of the player
     */
    private int currentTick;

    /**
     * contains information about the current abc tune
     */
    private AbcInfo abcInfo;

    /**
     * visitor that traverses the ast and plays the notes
     */
    private final PlayVisitor playVisitor;


    /**
     * @param ast root of ABC AbstractSyntaxTree != null
     * @param abcInfo != null
     * @throws InvalidMidiDataException
     * @throws MidiUnavailableException
     */
    public AbcPlayer(AbstractSyntaxTree ast, AbcInfo abcInfo) throws InvalidMidiDataException, MidiUnavailableException
    {
        // abc information collector
        this.abcInfo = abcInfo;

        // start with tick 0
        currentTick = 0;

        // visitor that actually plays the notes
        playVisitor = new PlayVisitor();

        // initialize sequence player with appropriate BPM and ticksPerQuaterNote
        sequencePlayer = new SequencePlayer(getBpm(), getTicksPerQuarterNote());

        // start traverse the ast
        ast.accept(playVisitor);
    }

    /**
     * @return ticks per quarter note
     */
    private int getTicksPerQuarterNote()
    {
        int ticksPerQuarterNote = 1;

        RationalNumber minNoteLength = abcInfo.getMinNoteLength();

        if (QUARTER_NOTE_LENGTH.compareTo(minNoteLength) > 0) {
            // TODO: round it up!!!
            ticksPerQuarterNote = QUARTER_NOTE_LENGTH.divide(minNoteLength).getNumerator();
        }

        return ticksPerQuarterNote;
    }

    /**
     * @return beats per minute, where each beat equals a quarter note
     */
    private int getBpm()
    {
        RationalNumber tempo = new RationalNumber(abcInfo.getTempo(), 1);

        RationalNumber bpm = tempo.multiply(abcInfo.getDefaultNoteLength()).divide(QUARTER_NOTE_LENGTH);

        return Math.floorDiv(bpm.getNumerator(), bpm.getDenominator());
    }

    public void play() throws MidiUnavailableException
    {
        sequencePlayer.play();
    }

    @Override
    public String toString()
    {
        return sequencePlayer.toString();
    }
}
