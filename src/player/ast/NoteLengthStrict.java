package player.ast;

/**
 * Created by DucNguyenMinh on 8/24/15.
 */
public class NoteLengthStrict implements AbstractSyntaxTree
{
    private int upper;

    private int lower;

    public NoteLengthStrict(int upper, int lower)
    {
        this.upper = upper;
        this.lower = lower;
    }

    public int getLower()
    {
        return lower;
    }

    public int getUpper()
    {
        return upper;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NoteLengthStrict that = (NoteLengthStrict) o;

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
        return "NoteLengthStrict{" +
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
