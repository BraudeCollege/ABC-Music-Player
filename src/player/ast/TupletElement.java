package player.ast;

import java.util.List;

/**
 *
 */
public class TupletElement implements AbstractSyntaxTree
{

    private TupletSpec tupletSpec;

    private List<NoteElement> noteElements;

    public TupletElement(TupletSpec tupletSpec, List<NoteElement> noteElements)
    {
        this.tupletSpec = tupletSpec;
        this.noteElements = noteElements;
    }

    public List<NoteElement> getNoteElements()
    {
        return noteElements;
    }

    public TupletSpec getTupletSpec()
    {
        return tupletSpec;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TupletElement that = (TupletElement) o;

        if (tupletSpec != null ? !tupletSpec.equals(that.tupletSpec) : that.tupletSpec != null) return false;
        return !(noteElements != null ? !noteElements.equals(that.noteElements) : that.noteElements != null);

    }

    @Override
    public int hashCode()
    {
        int result = tupletSpec != null ? tupletSpec.hashCode() : 0;
        result = 31 * result + (noteElements != null ? noteElements.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "TupletElement{" +
                "noteElements=" + noteElements +
                ", tupletSpec=" + tupletSpec +
                '}';
    }
}
