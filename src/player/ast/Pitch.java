package player.ast;

/**
 * Created by DucNguyenMinh on 8/23/15.
 */
public class Pitch implements AbstractSyntaxTree
{
    /**
     * accidental object of this Pitch
     */
    private Accidental accidental;

    /**
     * accidental object of this Pitch
     */
    private Basenote basenote;

    /**
     * accidental object of this Pitch
     */
    private Octave octave;

    /**
     * Create a Pitch with basenote and optional accidental anf octave
     *
     * @param accidental Accidental of the pitch if there is no accidental then use Accidental.getEmptyObj()
     * @param basenote basenote of the pitch
     * @param octave octabe of the pitch if there is no accidental then use Accidental.getEmptyObj()
     *
     * @requires basenote != null
     *           accidental != null
     *           octave != null
     */
    public Pitch(Accidental accidental, Basenote basenote, Octave octave)
    {
        if(basenote == null || accidental == null || octave == null)
            throw new NullPointerException("basenote must ");

        this.accidental = accidental;
        this.basenote = basenote;
        this.octave = octave;
    }
}
