package player.ast;

/**
 * Created by hieusun on 23.08.15.
 */
public class Accidental implements AbstractSyntaxTree
{
    public enum Type {
        SHARP, DOUBLE_SHARP, FLAT, DOUBLE_FLAT, NEUTRAL;
    }

    public final Type type;

    public Accidental(Type type)
    {
        this.type = type;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Accidental that = (Accidental) o;

        return type == that.type;

    }

    @Override
    public int hashCode()
    {
        return type.hashCode();
    }
}
