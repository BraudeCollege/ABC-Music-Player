package player.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * // _TODO: traverse notes
 */
public class MultiNote implements NoteElement
{
    private List<Note> notes;

    /**
     * construct a multi-note element from a list of notes
     * @param notes there must be at least one Note in notes
     */
    public MultiNote(List<Note> notes)
    {
        if (notes.isEmpty())
            throw new IllegalArgumentException("There is at least one Note in notes");

        this.notes = new ArrayList<>(notes);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MultiNote multiNote = (MultiNote) o;

        return !(notes != null ? !notes.equals(multiNote.notes) : multiNote.notes != null);

    }

    @Override
    public int hashCode()
    {
        return notes != null ? notes.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "MultiNote{" +
                "notes=" + notes +
                '}';
    }


    public List<Note> getNotes()
    {
        return notes;
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
