package player.ast;

public class FieldComposer implements MidTuneField
{
    /**
     * name the composer
     */
    private final String name;

    /**
     * @param name != null
     */
    public FieldComposer(String name)
    {
        if (name == null)
            throw new NullPointerException();
        this.name = name;
    }

    @Override
    public String toString()
    {
        return "FieldComposer{" +
                "name='" + name + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldComposer that = (FieldComposer) o;

        return !(name != null ? !name.equals(that.name) : that.name != null);
    }

    @Override
    public int hashCode()
    {
        return name != null ? name.hashCode() : 0;
    }

    public String getName()
    {
        return name;
    }
}
