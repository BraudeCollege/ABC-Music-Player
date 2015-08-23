package player.ast;

/**
 * Represent a base note
 */
public class Basenote implements AbstractSyntaxTree
{
    /**
     * Symbol of basenote
     */
    public final char symbol;

    public Basenote(char symbol)
    {
        this.symbol = symbol;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Basenote basenote = (Basenote) o;

        return symbol == basenote.symbol;

    }

    @Override
    public int hashCode()
    {
        return (int) symbol;
    }
}
