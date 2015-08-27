package player.ast;

public class FieldDefaultLength implements OtherField
{
    private final NoteLengthStrict noteLengthStrict;

    /**
     * @param noteLengthStrict
     */
    public FieldDefaultLength(NoteLengthStrict noteLengthStrict)
    {
        this.noteLengthStrict = noteLengthStrict;
    }

    public NoteLengthStrict getNoteLengthStrict()
    {
        return noteLengthStrict;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldDefaultLength that = (FieldDefaultLength) o;

        return !(noteLengthStrict != null ? !noteLengthStrict.equals(that.noteLengthStrict) : that.noteLengthStrict != null);

    }

    @Override
    public int hashCode()
    {
        return noteLengthStrict != null ? noteLengthStrict.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "FieldDefaultLength{" +
                "noteLengthStrict=" + noteLengthStrict +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
