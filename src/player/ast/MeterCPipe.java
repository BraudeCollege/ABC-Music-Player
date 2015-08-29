package player.ast;

public class MeterCPipe implements Meter
{
    private static MeterCPipe instance;

    private MeterCPipe()
    {
    }

    /**
     * @return the sole instance of MeterC
     */
    public static MeterCPipe getInstance()
    {
        if (instance == null)
            instance = new MeterCPipe();

        return instance;
    }

    @Override
    public String toString()
    {
        return "MeterC";
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }

    @Override
    public int getUpper()
    {
        return 2;
    }

    @Override
    public int getLower()
    {
        return 2;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;

        if (o == null) return false;

        Meter meter = (Meter) o;

        return getLower() == meter.getLower() && getUpper() == meter.getUpper();
    }

    @Override
    public int hashCode()
    {
        return 31 * getLower() + getUpper();
    }
}
