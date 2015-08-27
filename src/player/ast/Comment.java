package player.ast;

public class Comment implements AbcLine, OtherField
{
    private final String text;

    /**
     * @param text
     */
    public Comment(String text)
    {
        this.text = (text == null) ? "" : text;
    }

    @Override
    public String toString()
    {
        return "Comment{" +
                "text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Comment that = (Comment) o;

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


