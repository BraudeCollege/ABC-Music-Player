package player.ast;

public class Rest implements Note
{
    private final NoteLength noteLength;

    public Rest(NoteLength noteLength)
    {
        this.noteLength = noteLength;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Rest rest = (Rest) o;

        return !(noteLength != null ? !noteLength.equals(rest.noteLength) : rest.noteLength != null);

    }

    @Override
    public int hashCode()
    {
        return noteLength != null ? noteLength.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "Rest{" +
                "noteLength=" + noteLength +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }

    @Override
    public NoteLength getNoteLength()
    {
        return noteLength;
    }
}
