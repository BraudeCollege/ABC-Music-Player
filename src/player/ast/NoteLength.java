package player.ast;

/**
 * Represents a note length Exp: "2/4", "4", "/", "4/", "/4"
 */
public class NoteLength implements AbstractSyntaxTree
{

    private int multiplier;

    private int divider;

    /**
     * Create a Instance of NoteLenght with a multiplier and a divide as integers
     * @param multiplier
     * @param divider
     */
    public NoteLength(int multiplier, int divider)
    {
        this.divider = divider;
        this.multiplier = multiplier;
    }

    /**
     *
     * @return Get the divider
     */
    public int getDivider()
    {
        return divider;
    }

    /**
     *
     * @return Get the Multiplier
     */
    public int getMultiplier()
    {
        return multiplier;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteLength that = (NoteLength) o;

        if (multiplier != that.multiplier) return false;
        return divider == that.divider;

    }

    @Override
    public int hashCode()
    {
        int result = multiplier;
        result = 31 * result + divider;
        return result;
    }

    @Override
    public String toString()
    {
        return "NoteLength{" +
                "divider=" + divider +
                ", multiplier=" + multiplier +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
