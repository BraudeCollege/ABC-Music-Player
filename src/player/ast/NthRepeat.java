package player.ast;

public class NthRepeat implements AbstractSyntaxTree
{
    /**
     * number of repeats
     */
    private final int times;

    public NthRepeat(int times)
    {
        this.times = times;
    }

    public int getTimes()
    {
        return times;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NthRepeat nthRepeat = (NthRepeat) o;

        return times == nthRepeat.times;

    }

    @Override
    public int hashCode()
    {
        return times;
    }

    @Override
    public String toString()
    {
        return "NthRepeat{" +
                "times=" + times +
                '}';
    }
}
