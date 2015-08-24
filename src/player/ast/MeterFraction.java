package player.ast;

public class MeterFraction
{
    private final int multiplier;
    private final int divider;

    /**
     * @param multiplier > 0
     * @param divider > 0
     */
    public MeterFraction(int multiplier, int divider)
    {
        this.multiplier = multiplier;
        this.divider = divider;
    }

    /**
     * @return number before the slash
     */
    public int getMultiplier()
    {
        return multiplier;
    }

    /**
     * @return number before the after
     */
    public int getDivider()
    {
        return divider;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MeterFraction that = (MeterFraction) o;

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
        return "MeterFraction{" +
                "multiplier=" + multiplier +
                ", divider=" + divider +
                '}';
    }
}
