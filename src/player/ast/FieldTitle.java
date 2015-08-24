package player.ast;

public class FieldTitle implements AbstractSyntaxTree
{
    /**
     * title of the file
     */
    private final String title;

    /**
     * @param title != null
     */
    public FieldTitle(String title)
    {
        if (title == null)
            throw new NullPointerException();
        this.title = title;
    }

    @Override
    public String toString()
    {
        return "FieldTitle{" +
                "title='" + title + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldTitle that = (FieldTitle) o;

        return !(title != null ? !title.equals(that.title) : that.title != null);
    }

    @Override
    public int hashCode()
    {
        return title != null ? title.hashCode() : 0;
    }

    public String getTitle()
    {
        return title;
    }
}
