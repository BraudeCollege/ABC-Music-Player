package player.ast;

public class FieldVoice implements MidTuneField, OtherField
{
    /**
     * text of field voice
     */
    private final String text;

    /**
     * @param text != null
     */
    public FieldVoice(String text)
    {
        if (text == null)
            throw new NullPointerException();
        this.text = text;
    }

    @Override
    public String toString()
    {
        return "FieldVoice{" +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldVoice that = (FieldVoice) o;

        return !(text != null ? !text.equals(that.text) : that.text != null);
    }

    @Override
    public int hashCode()
    {
        return text != null ? text.hashCode() : 0;
    }

    public String getText()
    {
        return text;
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
