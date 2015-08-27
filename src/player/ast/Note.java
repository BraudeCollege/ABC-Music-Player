package player.ast;

/**
 * Represent a note, which either is a pitch or a rest
 * played with a particular length
 */
public class Note implements NoteElement
{
    private final NoteLength noteLength;
    private final NoteOrRest noteOrRest;

    /**
     * @param noteOrRest a Pitch or a Rest
     * @param noteLength note length that this note played with
     */
    public Note(NoteOrRest noteOrRest, NoteLength noteLength)
    {
        this.noteLength = noteLength;
        this.noteOrRest = noteOrRest;
    }

    /**
     * @return note length that this note played with
     */
    public NoteLength getNoteLength()
    {
        return noteLength;
    }

    /**
     * @return a Pitch or a Rest
     */
    public NoteOrRest getNoteOrRest()
    {
        return noteOrRest;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Note note = (Note) o;

        if (!noteLength.equals(note.noteLength)) return false;
        return noteOrRest.equals(note.noteOrRest);

    }

    @Override
    public int hashCode()
    {
        int result = noteLength.hashCode();
        result = 31 * result + noteOrRest.hashCode();
        return result;
    }

    @Override
    public String toString()
    {
        return "Note{" +
                "noteLength=" + noteLength +
                ", noteOrRest=" + noteOrRest +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
