package player.ast;

public class FieldKey implements AbstractSyntaxTree
{
    private final Key key;

    /**
     * @param key != null
     */
    public FieldKey(Key key)
    {
        this.key = key;
    }

    /**
     * @return Key in field-key
     */
    public Key getKey()
    {
        return key;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldKey fieldKey = (FieldKey) o;

        return !(key != null ? !key.equals(fieldKey.key) : fieldKey.key != null);

    }

    @Override
    public int hashCode()
    {
        return key != null ? key.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "FieldKey{" +
                "key=" + key +
                '}';
    }
}
