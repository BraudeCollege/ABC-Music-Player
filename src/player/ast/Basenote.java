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
}
