package player.ast;

/**
 * Created by hieusun on 24.08.15.
 */
public class Key
{
    private final Keynote keynote;
    private final ModeMinor modeMinor;

    /**
     * @param keynote != null
     * @param modeMinor != null
     */
    public Key(Keynote keynote, ModeMinor modeMinor)
    {
        this.keynote = keynote;
        this.modeMinor = modeMinor;
    }

    /**
     * @return Keynote of the key
     */
    public Keynote getKeynote()
    {
        return keynote;
    }

    /**
     * @return ModeMinor (if there is no ModeMinor: modeMinor.getType() == ModeMinor.Type.NONE)
     */
    public ModeMinor getModeMinor()
    {
        return modeMinor;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Key key = (Key) o;

        if (keynote != null ? !keynote.equals(key.keynote) : key.keynote != null) return false;
        return !(modeMinor != null ? !modeMinor.equals(key.modeMinor) : key.modeMinor != null);

    }

    @Override
    public int hashCode()
    {
        int result = keynote != null ? keynote.hashCode() : 0;
        result = 31 * result + (modeMinor != null ? modeMinor.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "Key{" +
                "keynote=" + keynote +
                ", modeMinor=" + modeMinor +
                '}';
    }
}
