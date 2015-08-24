package player.ast;

public class FieldNumber implements AbstractSyntaxTree
{
    /**
     * Reference number in the Field-number
     */
    private final int num;

    public FieldNumber(int num)
    {
        this.num = num;
    }

    public int getNum()
    {
        return num;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldNumber that = (FieldNumber) o;

        return num == that.num;

    }

    @Override
    public int hashCode()
    {
        return num;
    }

    @Override
    public String toString()
    {
        return "FieldNumber{" +
                "num=" + num +
                '}';
    }
}
