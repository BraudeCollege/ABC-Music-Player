package player.ast;

public class Pitch implements Note
{
    /**
     * Rep Invariants:
     *   accidental != null
     *   octave != null
     *   basenote != null
     */

    /**
     * accidental of this Pitch
     */
    private Accidental accidental;

    /**
     * basenote of this Pitch
     */
    private Basenote basenote;

    /**
     * octave of this Pitch
     */
    private Octave octave;

    private NoteLength noteLength;

    /**
     * Create a Pitch with basenote and optional accidental anf octave
     *
     * @param basenote basenote of the pitch
     * @param accidental Accidental of the pitch if there is no accidental then use Accidental.getEmpty()
     * @param octave octabe of the pitch if there is no accidental then use Octave.getEmpty()
     *
     * @param noteLength
     * @requires basenote != null
     *           accidental != null
     *           octave != null
     *           noteLength != null
     */
    public Pitch(Basenote basenote, Accidental accidental, Octave octave, NoteLength noteLength)
    {
        if(basenote == null || accidental == null || octave == null || noteLength == null)
            throw new NullPointerException("Basenote, Accidental and Octave mustn't be null");

        this.accidental = accidental;
        this.basenote = basenote;
        this.octave = octave;
        this.noteLength = noteLength;
    }

    /**
     * @return accidental of the pitch
     */
    public Accidental getAccidental()
    {
        return accidental;
    }

    /**
     * @return basenote of the pitch
     */
    public Basenote getBasenote()
    {
        return basenote;
    }

    /**
     * @return octave of the pitch
     */
    public Octave getOctave()
    {
        return octave;
    }

    public NoteLength getNoteLength()
    {
        return noteLength;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pitch pitch = (Pitch) o;

        if (accidental != null ? !accidental.equals(pitch.accidental) : pitch.accidental != null) return false;
        if (basenote != null ? !basenote.equals(pitch.basenote) : pitch.basenote != null) return false;
        if (octave != null ? !octave.equals(pitch.octave) : pitch.octave != null) return false;
        return !(noteLength != null ? !noteLength.equals(pitch.noteLength) : pitch.noteLength != null);

    }

    @Override
    public int hashCode()
    {
        int result = accidental != null ? accidental.hashCode() : 0;
        result = 31 * result + (basenote != null ? basenote.hashCode() : 0);
        result = 31 * result + (octave != null ? octave.hashCode() : 0);
        result = 31 * result + (noteLength != null ? noteLength.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Pitch{" +
                "accidental=" + accidental +
                ", basenote=" + basenote +
                ", octave=" + octave +
                ", noteLength=" + noteLength +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}

