package player.ast;

public class TupletSpec
{
    private final int count;

    /**
     * @param count
     */
    public TupletSpec(int count)
    {
        this.count = count;
    }

    /**
     * @return number of note-elements in a tuplet
     */
    public int getCount()
    {
        return count;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TupletSpec that = (TupletSpec) o;

        return count == that.count;

    }

    @Override
    public int hashCode()
    {
        return count;
    }

    @Override
    public String toString()
    {
        return "TupletSpec{" +
                "count=" + count +
                '}';
    }
}
