package player.ast;

public class ModeMinor implements AbstractSyntaxTree
{
    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }

    public enum Type
    {
        MODE_MINOR, NONE
    }

    /**
     * The only ModeMinor object
     */
    private static ModeMinor instance;

    /**
     * The only Null Object of ModeMinor
     */
    private static ModeMinor noneInstance;

    private final Type type;

    private ModeMinor(Type type)
    {
        this.type = type;
    }

    /**
     * @return the sole instance of ModeMinor
     */
    public static ModeMinor getInstance()
    {
        if (instance == null)
            instance = new ModeMinor(Type.MODE_MINOR);

        return instance;
    }

    /**
     * @return Null Object of ModeMinor
     */
    public static ModeMinor getNone()
    {
        if (noneInstance == null)
            noneInstance = new ModeMinor(Type.NONE);

        return noneInstance;
    }

    /**
     * @return type of mode minor (real or null object)
     */
    public Type getType()
    {
        return type;
    }

    @Override
    public String toString()
    {
        return "ModeMinor{" +
                "type=" + type +
                '}';
    }
}
