package player.ast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TupletElement implements Element
{
    private TupletSpec tupletSpec;
    private List<NoteElement> noteElements;

    /**
     * @param tupletSpec
     * @param noteElements length of noteElements must equals tupletSpec.getCount()
     * @throws IllegalArgumentException if noteElements.size() != tupletSpec.getCount()
     */
    public TupletElement(TupletSpec tupletSpec, List<NoteElement> noteElements)
    {
        this.tupletSpec = tupletSpec;

        if (noteElements.size() != tupletSpec.getCount())
            throw new IllegalArgumentException("Number of note elements must be " + tupletSpec.getCount() + " but received " + noteElements.size());

        this.noteElements = new ArrayList<>(noteElements);
    }

    /**
     * @return _TODO: iteration ???
     */
    public List<NoteElement> getNoteElements()
    {
        return new ArrayList(noteElements);
    }

    /**
     * @return number of note elements in this tuplet
     */
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
