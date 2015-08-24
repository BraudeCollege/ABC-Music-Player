package player.ast;

public class Barline implements AbstractSyntaxTree
{
    public enum Type
    {
        SINGLE_BAR, DOUBLE_BAR, OPEN_BAR, CLOSE_BAR, OPEN_REPEAT_BAR, CLOSE_REPEAT_BAR
    }

    /**
     * type of accidental
     */
    private final Type type;

    private static Barline singleBar;
    private static Barline doubleBar;
    private static Barline openBar;
    private static Barline closeBar;
    private static Barline closeRepeatBar;
    private static Barline openRepeatBar;

    public Barline(Type type)
    {
        this.type = type;
    }

    public static Barline getInstance(Type type)
    {
        switch (type) {
            case SINGLE_BAR:
                return singleBar = (singleBar == null) ? new Barline(Type.SINGLE_BAR) : singleBar;
            case DOUBLE_BAR:
                return doubleBar = (doubleBar == null) ? new Barline(Type.DOUBLE_BAR) : doubleBar;
            case OPEN_BAR:
                return openBar = (openBar == null) ? new Barline(Type.OPEN_BAR) : openBar;
            case CLOSE_BAR:
                return closeBar = (closeBar == null) ? new Barline(Type.CLOSE_BAR) : closeBar;
            case OPEN_REPEAT_BAR:
                return openRepeatBar = (openRepeatBar == null) ? new Barline(Type.OPEN_REPEAT_BAR) : openRepeatBar;
            case CLOSE_REPEAT_BAR:
                return closeRepeatBar = (closeRepeatBar == null) ? new Barline(Type.CLOSE_REPEAT_BAR) : closeRepeatBar;
        }

        throw new IllegalArgumentException();
    }

    /**
     * @return type of Barline
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

        Barline that = (Barline) o;

        return type == that.type;

    }


    @Override
    public int hashCode()
    {
        return type.hashCode();
    }

    @Override
    public String toString()
    {
        return "Barline{" +
                "type=" + type +
                '}';
    }
}
