package player.ast;

/**
 * Created by DucNguyenMinh on 8/23/15.
 */
public class Octave implements AbstractSyntaxTree
{

    public enum Type
    {
        UP, DOWN, NONE
    }

    /**
     * levels of octave to be alternated
     */
    private final int levels;

    /**
     * type of octave
     */
    private final Type type;

    private Octave(Type type, int levels)
    {
        this.levels = levels;
        this.type = type;
    }

    /**
     * Get an Up Octave with a given levels
     * @param levels
     * @return an Octave of type Type.UP
     */
    public static Octave getUp(int levels)
    {
        return new Octave(Type.UP, levels);
    }

    /**
     * Get an Up Octave with a given levels
     * @param levels
     * @return an Octave of type Type.DOWN
     */
    public static Octave getDown(int levels)
    {
        return new Octave(Type.DOWN, levels);
    }

    /**
     * @return null object of Octave
     */
    public static Octave getEmpty()
    {
        return new Octave(Type.NONE, 0);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Octave octave = (Octave) o;

        return levels == octave.levels && type == octave.type;

    }

    @Override
    public int hashCode()
    {
        int result = levels;
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "Octave{" +
                "levels=" + levels +
                ", type=" + type +
                '}';
    }
}
