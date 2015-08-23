package player.ast;

/**
 * Represent a base note
 */
public class Basenote implements AbstractSyntaxTree
{
    /**
     * Symbol of basenote
     */
    private final char symbol;

    public Basenote(char symbol)
    {
        this.symbol = symbol;
    }

    /**
     * @return symbol that the basenote represent
     */
    public char getSymbol()
    {
        return symbol;
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

    @Override
    public String toString()
    {
        return "Basenote{" +
                "symbol=" + symbol +
                '}';
    }
}
