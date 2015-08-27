package player.ast;

public class FieldMeter implements OtherField
{
    private final Meter meter;

    /**
     * @param meter != null
     */
    public FieldMeter(Meter meter)
    {
        this.meter = meter;
    }

    /**
     * @return Meter in field-meter
     */
    public Meter getMeter()
    {
        return meter;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldMeter that = (FieldMeter) o;

        return !(meter != null ? !meter.equals(that.meter) : that.meter != null);

    }

    @Override
    public int hashCode()
    {
        return meter != null ? meter.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "FieldMeter{" +
                "meter=" + meter +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
