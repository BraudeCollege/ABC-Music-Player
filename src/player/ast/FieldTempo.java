package player.ast;

/**
 * Created by hieusun on 24.08.15.
 */
public class FieldTempo
{
    private final int tempo;

    /**
     * @param tempo must be positive ( tempo > 0 )
     */
    public FieldTempo(int tempo)
    {
        this.tempo = tempo;
    }

    /**
     * @return tempo in field-tempo
     */
    public int getTempo()
    {
        return tempo;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FieldTempo that = (FieldTempo) o;

        return tempo == that.tempo;

    }

    @Override
    public int hashCode()
    {
        return tempo;
    }

    @Override
    public String toString()
    {
        return "FieldTempo{" +
                "tempo=" + tempo +
                '}';
    }
}
