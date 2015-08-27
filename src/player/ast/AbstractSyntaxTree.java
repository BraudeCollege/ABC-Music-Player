package player.ast;

/**
 * An Abstract Syntax Tree represents a structures objects represents ABC objects
 */
public interface AbstractSyntaxTree
{
    public <R> R accept(AbcVisitor<R> visitor);
}
