package player.ast;

/**
 * Created by DucNguyenMinh on 8/24/15.
 */
public class NoteLengthStrict
{
    private int multiplier;

    private int divider;

    public NoteLengthStrict(int multiplier, int divider)
    {
        this.multiplier = multiplier;
        this.divider = divider;
    }

    public int getDivider()
    {
        return divider;
    }

    public int getMultiplier()
    {
        return multiplier;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteLengthStrict that = (NoteLengthStrict) o;

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
        return "NoteLengthStrict{" +
                "divider=" + divider +
                ", multiplier=" + multiplier +
                '}';
    }
}
