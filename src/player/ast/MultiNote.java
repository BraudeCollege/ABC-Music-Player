package player.ast;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class MultiNote implements NoteElement
{
   private List<Note> notes;

    public MultiNote()
    {
        this.notes = new ArrayList<>();
    }

    /**
     * Add a note to the note list
     * @param note
     */
    public void addNote(Note note)
    {
        this.notes.add(note);
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
}
