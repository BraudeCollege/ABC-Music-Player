package player.ast;

public class Keynote implements AbstractSyntaxTree
{
    private final Basenote basenote;

    private final KeyAccidental keyAccidental;

    /**
     * @param basenote != null
     * @param keyAccidental != null
     */
    public Keynote(Basenote basenote, KeyAccidental keyAccidental)
    {
        this.basenote = basenote;
        this.keyAccidental = keyAccidental;
    }

    /**
     * @return Basenote
     */
    public Basenote getBasenote()
    {
        return basenote;
    }

    /**
     * @return KeyAccidental
     */
    public KeyAccidental getKeyAccidental()
    {
        return keyAccidental;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Keynote keynote = (Keynote) o;

        if (basenote != null ? !basenote.equals(keynote.basenote) : keynote.basenote != null) return false;
        return !(keyAccidental != null ? !keyAccidental.equals(keynote.keyAccidental) : keynote.keyAccidental != null);

    }

    @Override
    public int hashCode()
    {
        int result = basenote != null ? basenote.hashCode() : 0;
        result = 31 * result + (keyAccidental != null ? keyAccidental.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Keynote{" +
                "basenote=" + basenote +
                ", keyAccidental=" + keyAccidental +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
