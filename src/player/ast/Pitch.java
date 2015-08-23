package player.ast;

public class Pitch implements NoteOrRest
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

    /**
     * Create a Pitch with basenote and optional accidental anf octave
     *
     * @param basenote basenote of the pitch
     * @param accidental Accidental of the pitch if there is no accidental then use Accidental.getEmpty()
     * @param octave octabe of the pitch if there is no accidental then use Octave.getEmpty()
     *
     * @requires basenote != null
     *           accidental != null
     *           octave != null
     */
    public Pitch(Basenote basenote, Accidental accidental, Octave octave)
    {
        if(basenote == null || accidental == null || octave == null)
            throw new NullPointerException("Basenote, Accidental and Octave mustn't be null");

        this.accidental = accidental;
        this.basenote = basenote;
        this.octave = octave;
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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Pitch pitch = (Pitch) o;

        return accidental.equals(pitch.accidental) && octave.equals(pitch.octave) && basenote.equals(pitch.basenote);

    }

    @Override
    public int hashCode()
    {
        int result = accidental.hashCode();
        result = 31 * result + basenote.hashCode();
        result = 31 * result + octave.hashCode();
        return result;
    }


    @Override
    public String toString()
    {
        return "Pitch{" +
                "accidental=" + accidental +
                ", basenote=" + basenote +
                ", octave=" + octave +
                '}';
    }
}
