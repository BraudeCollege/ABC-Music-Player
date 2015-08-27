package player.ast;

public class MeterFraction implements Meter
{
    private final int upper;
    private final int lower;

    /**
     * @param upper > 0
     * @param lower > 0
     */
    public MeterFraction(int upper, int lower)
    {
        this.upper = upper;
        this.lower = lower;
    }

    /**
     * @return number before the slash
     */
    public int getUpper()
    {
        return upper;
    }

    /**
     * @return number before the after
     */
    public int getLower()
    {
        return lower;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeterFraction that = (MeterFraction) o;

        if (upper != that.upper) return false;
        return lower == that.lower;

    }

    @Override
    public int hashCode()
    {
        int result = upper;
        result = 31 * result + lower;
        return result;
    }

    @Override
    public String toString()
    {
        return "MeterFraction{" +
                "upper=" + upper +
                ", lower=" + lower +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
