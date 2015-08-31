package player.visitor;

import player.RationalNumber;
import player.ast.*;
import sound.SequencePlayer;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.HashMap;


/**
 * Visitor that traverses the ast and plays the notes
 */
class AbcPlayVisitor implements AbcVisitor<Void>
{
    /**
     * SequencePlayer plays pitches and rest
     */
    private final SequencePlayer sequencePlayer;

    /**
     * Store accidentals while playing
     */
    private final HashMap<Character, Integer> tempAccidental;

    /**
     * count the ticks of the player
     */
    private int currentTick;

    /**
     * contains information about the current abc tune
     */
    private AbcInfoVisitor abcInfo;

    /**
     * @param ast root of ABC AbstractSyntaxTree != null
     * @throws InvalidMidiDataException
     * @throws MidiUnavailableException
     */
    public AbcPlayVisitor(AbstractSyntaxTree ast, AbcInfoVisitor abcInfo) throws InvalidMidiDataException, MidiUnavailableException
    {
        // abc information collector
        this.abcInfo = abcInfo;

        // start with tick 0
        currentTick = 0;

        // initialize sequence player with appropriate BPM and ticksPerQuaterNote
        sequencePlayer = new SequencePlayer(abcInfo.getBpm(), abcInfo.getTicksPerQuarterNote());

        tempAccidental = new HashMap<Character, Integer>();

        // traverse the ast
        ast.accept(this);
    }


    /**
     * plays the notes
     * @throws MidiUnavailableException
     */
    public void play() throws MidiUnavailableException
    {
        sequencePlayer.play();
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
        // ignore, doesn't matter while playing
        return null;
    }

    @Override
    public Void on(FieldTitle field)
    {
        // ignore, doesn't matter while playing
        return null;
    }

    @Override
    public Void on(FieldKey field)
    {
        // ignore, doesn't matter
        return null;
    }

    @Override
    public Void on(FieldVoice field)
    {
        // ignore, doesn't matter
        return null;
    }

    @Override
    public Void on(FieldComposer field)
    {
        // ignore, doesn't matter while playing
        return null;
    }

    @Override
    public Void on(FieldDefaultLength field)
    {
        // ignore, doesn't matter
        return null;
    }

    @Override
    public Void on(FieldMeter field)
    {
        // ignore, doesn't matter
        return null;
    }

    @Override
    public Void on(FieldTempo field)
    {
        // ignore, doesn't matter anymore while playing
        return null;
    }

    @Override
    public Void on(Comment c)
    {
        // ignore, doesn't matter
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
        // clear all accidentals at barline
        tempAccidental.clear();
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
        PlayChordVisitor playChord = new PlayChordVisitor(mnote);
        currentTick += playChord.getElapsedTicks();

        return null;
    }

    @Override
    public Void on(NoteLength noteLength)
    {
        // doesn't matter while playing
        return null;
    }

    @Override
    public Void on(Rest rest)
    {
        // calculate and add the amount of ticks should the rest be played
        currentTick += getTicksToPlay(rest.getNoteLength());

        return null;
    }

    @Override
    public Void on(Pitch pitch)
    {
        // calculate how many ticks should the note be played
        int ticks = getTicksToPlay(pitch.getNoteLength());

        // create a sound pitch
        char symbol = Character.toUpperCase(pitch.getBasenote().getSymbol());
        sound.Pitch soundPitch = new sound.Pitch(symbol);

        // transpose appropriate amount of octaves
        soundPitch = soundPitch.octaveTranspose(calculateOctaves(pitch));

        // transpose appropriate amount accidentals
        soundPitch = soundPitch.accidentalTranspose(calculateAccidental(pitch));

        sequencePlayer.addNote(soundPitch.toMidiNote(), currentTick, ticks);

        currentTick += ticks;

        return null;
    }

    /**
     * calculate and return how many ticks should the note be played
     *
     * @param nl
     * @return number of ticks
     */
    private int getTicksToPlay(NoteLength nl)
    {
        RationalNumber rationalNoteLength = new RationalNumber(nl.getUpper(), nl.getLower());

        RationalNumber realNoteLength = rationalNoteLength.multiply(abcInfo.getDefaultNoteLength());

        int ticks = realNoteLength.divide(abcInfo.getUnitNoteLength()).getNumerator();

        return ticks;
    }

    /**
     * Calculate and return amount of accidental to adjust the note
     * @param pitch
     * @return
     */
    private int calculateAccidental(Pitch pitch) {

        char noteSymbol = Character.toUpperCase(pitch.getBasenote().getSymbol());

        Accidental acc = pitch.getAccidental();

        // add new accidental if needed
        if (acc.getType() != Accidental.Type.NONE)
            tempAccidental.put(noteSymbol, acc.getType().getAmount());

        // if there is already an accidental for this type of note, override the key signature
        if (tempAccidental.containsKey(noteSymbol))
            return tempAccidental.get(noteSymbol);

        KeySignature key = abcInfo.getKeySignature();

        int accidental = key.getAccidental(noteSymbol);

        return accidental;
    }

    /**
     * @return number of octaves to play the note
     *         < 0 means octaves down
     *         > 0 means octaves up
     *         = 0 means nothing changes
     */
    private int calculateOctaves(Pitch pitch)
    {
        int octaves = 0;

        Basenote basenote = pitch.getBasenote();

        // lower case symbol means 1 octave higer
        if (Character.isLowerCase(basenote.getSymbol()))
            octaves += 1;

        Octave octave = pitch.getOctave();
        switch (octave.getType()) {
            case UP:
                octaves += octave.getLevels();
                break;
            case DOWN:
                octaves -= octave.getLevels();
                break;
        }

        return octaves;
    }

    @Override
    public Void on(Basenote basenote)
    {
        // doesn't get called while playing
        return null;
    }

    @Override
    public Void on(Accidental acc)
    {
        // doesn't get called while playing
        return null;
    }

    @Override
    public Void on(Octave octave)
    {
        // doesn't get called while playing
        return null;
    }

    @Override
    public Void on(KeyAccidental keyAccidental)
    {
        // doesn't get called while playing
        return null;
    }

    @Override
    public Void on(Keynote keynote)
    {
        // doesn't get called while playing
        return null;
    }

    @Override
    public Void on(NoteLengthStrict noteLengthStrict)
    {
        // doesn't get called while playing
        return null;
    }

    @Override
    public Void on(ModeMinor modeMinor)
    {
        // doesn't get called while playing
        return null;
    }

    @Override
    public Void on(MeterFraction meterFraction)
    {
        // doesn't get called while playing
        return null;
    }

    @Override
    public Void on(MeterCPipe meterCPipe)
    {
        // doesn't get called while playing
        return null;
    }

    @Override
    public Void on(MeterC meterC)
    {
        // doesn't get called while playing
        return null;
    }

    @Override
    public Void on(Key key)
    {
        // doesn't get called while playing
        return null;
    }

    @Override
    public String toString()
    {
        return sequencePlayer.toString();
    }


    /**
     * Visitor that play the notes of a chord/multinote at the same times
     * Assume that the notes have equal note length
     */
    class PlayChordVisitor implements AbcVisitor<Void>
    {
        /**
         * ticks that the notes has been played
         */
        private int elapsedTicks;

        /**
         * @param mnote
         */
        public PlayChordVisitor(MultiNote mnote)
        {
            this.elapsedTicks = 0;
            mnote.accept(this);
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
            if (elapsedTicks == 0)
                elapsedTicks = getTicksToPlay(rest.getNoteLength());

            return null;
        }

        @Override
        public Void on(Pitch pitch)
        {
            // calculate how many ticks should the note be played
            int ticks = getTicksToPlay(pitch.getNoteLength());

            // create a sound pitch
            char symbol = Character.toUpperCase(pitch.getBasenote().getSymbol());
            sound.Pitch soundPitch = new sound.Pitch(symbol);

            // transpose appropriate amount of octaves
            soundPitch = soundPitch.octaveTranspose(calculateOctaves(pitch));

            // transpose appropriate amount accidentals
            soundPitch = soundPitch.accidentalTranspose(calculateAccidental(pitch));

            sequencePlayer.addNote(soundPitch.toMidiNote(), currentTick, ticks);

            if (elapsedTicks == 0)
                elapsedTicks = ticks;

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
         * @return current tick of the track
         */
        public int getElapsedTicks()
        {
            return elapsedTicks;
        }
    }
}
