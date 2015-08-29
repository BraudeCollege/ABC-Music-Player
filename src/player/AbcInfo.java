package player;


import player.ast.*;

public class AbcInfo
{
    class InfoVisitor implements AbcVisitor<Void>
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
            id = field.getNum();
            return null;
        }

        @Override
        public Void on(FieldTitle field)
        {
            title = field.getTitle();
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
            composer = field.getName();
            return null;
        }

        @Override
        public Void on(FieldDefaultLength field)
        {
            field.getNoteLengthStrict().accept(this);

            return null;
        }

        @Override
        public Void on(FieldMeter field)
        {
            field.getMeter().accept(this);
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
            // ignore this node, no information
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
            // ignore this node, no information
            return null;
        }

        @Override
        public Void on(Barline bar)
        {
            // ignore this node, no information
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
            RationalNumber rationalLength = new RationalNumber(length.getMultiplier(), length.getDivider());

            RationalNumber realNoteLength = rationalLength.multiply(defaultNoteLength);

            if (minNoteLength == null || realNoteLength.compareTo(minNoteLength) < 0)
                minNoteLength = realNoteLength;

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
            // ignore this node, no information
            return null;
        }

        @Override
        public Void on(Accidental acc)
        {
            // ignore this node, no information
            return null;
        }

        @Override
        public Void on(Octave octave)
        {
            // ignore this node, no information
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
        public Void on(ModeMinor modeMinor)
        {
            return null;
        }

        @Override
        public Void on(Key key)
        {
            return null;
        }

        @Override
        public Void on(NoteLengthStrict noteLengthStrict)
        {
            defaultNoteLength = new RationalNumber(noteLengthStrict.getUpper(), noteLengthStrict.getLower());
            return null;
        }

        @Override
        public Void on(MeterFraction meterFraction)
        {
            meter = meterFraction;
            return null;
        }

        @Override
        public Void on(MeterCPipe meterCPipe)
        {
            meter = meterCPipe;
            return null;
        }

        @Override
        public Void on(MeterC meterC)
        {
            meter = meterC;
            return null;
        }
    }

    /**
     * ID - Value of X: field
     */
    private int id;

    /**
     * Title specified in abc header
     */
    private String title = "Unknown";

    /**
     * Composer specified in abc header; default: "Unknown"
     */
    private String composer = "Unknown";


    /**
     * meter specified in abc header; default: 4/4
     */
    private Meter meter = new MeterFraction(4, 4);

    /**
     * The number of default-length notes per minute; default: 100
     */
    private int tempo = 100;

    /**
     * Default note length; default: 1/8 (quavier)
     */
    private RationalNumber defaultNoteLength = new RationalNumber(1, 8);

    /**
     * minimum length of all the notes
     */
    private RationalNumber minNoteLength;

    /**
     * visitor that traverse the abc tree and extract it's information
     */
    private InfoVisitor infoVisitor;

    public AbcInfo(AbstractSyntaxTree root)
    {
        infoVisitor = new InfoVisitor();

        root.accept(infoVisitor);
    }

    /**
     * @return smallest note length in abc file
     */
    public RationalNumber getMinNoteLength()
    {
        return minNoteLength;
    }

    /**
     * @return Default note length
     */
    public RationalNumber getDefaultNoteLength()
    {
        return defaultNoteLength;
    }

    /**
     * @return the tempo specified in abc header
     */
    public int getTempo()
    {
        return tempo;
    }

    /**
     * @return meter specified in abc header
     */
    public Meter getMeter()
    {
        return meter;
    }

    /**
     * @return title specified in abc header
     */
    public String getTitle()
    {
        return title;
    }

    /**
     * @return composer specified in abc header
     */
    public String getComposer()
    {
        return composer;
    }

    /**
     * @return value of X: field in abc header
     */
    public int getId()
    {
        return id;
    }
}
