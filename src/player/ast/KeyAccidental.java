package player.ast;

public class KeyAccidental implements AbstractSyntaxTree
{

    public enum Type
    {
        FLAT, SHARP, NONE
    }

    private final Type type;

    private KeyAccidental(Type type)
    {
        this.type = type;
    }

    /**
     * Factory method for a sharp key-accidental
     * @return an KeyAccidental object
     */
    public static KeyAccidental getSharp()
    {
        return new KeyAccidental(Type.SHARP);
    }

    /**
     * Factory method for a flat key-accidental
     * @return an KeyAccidental object
     */
    public static KeyAccidental getFlat()
    {
        return new KeyAccidental(Type.FLAT);
    }

    /**
     * @return Null Object of KeyAccidental
     */
    public static KeyAccidental getNone()
    {
        return new KeyAccidental(Type.NONE);
    }
    /**
     * @return type of key-accidental
     */
    public Type getType()
    {
        return type;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        KeyAccidental that = (KeyAccidental) o;

        return type == that.type;
    }

    @Override
    public int hashCode()
    {
        return type != null ? type.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "KeyAccidental{" +
                "type=" + type +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
