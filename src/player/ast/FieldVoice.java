package player.ast;

/**
 * Created by DucNguyenMinh on 8/24/15.
 */
public class FieldVoice implements MidTuneField
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
}
