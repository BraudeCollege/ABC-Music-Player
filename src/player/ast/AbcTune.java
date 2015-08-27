package player.ast;

/**
 * Created by hieusun on 26.08.15.
 */
public class AbcTune implements AbstractSyntaxTree
{
    private final AbcHeader header;
    private final AbcMusic body;

    /**
     * @param header Abc music header
     * @param body Abc music body
     */
    public AbcTune(AbcHeader header, AbcMusic body)
    {
        this.header = header;
        this.body = body;
    }

    /**
     * @return Abc Music Header
     */
    public AbcHeader getHeader()
    {
        return header;
    }

    /**
     * @return Abc Music Body
     */
    public AbcMusic getBody()
    {
        return body;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbcTune abcTune = (AbcTune) o;

        if (header != null ? !header.equals(abcTune.header) : abcTune.header != null) return false;
        return !(body != null ? !body.equals(abcTune.body) : abcTune.body != null);

    }

    @Override
    public int hashCode()
    {
        int result = header != null ? header.hashCode() : 0;
        result = 31 * result + (body != null ? body.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "AbcTune{" +
                "header=" + header +
                ", body=" + body +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
