package player;

import player.ast.AbstractSyntaxTree;
import player.ast.Basenote;

/**
 * Perform semantic analysis and produce an abstract syntax tree
 */
public class Parser
{

    private final Lexer lex;

    public Parser(Lexer lex)
    {
        this.lex = lex;
    }

    /**
     * @return
     */
    public AbstractSyntaxTree expectBasenote()
    {
        // exception if !hasNext

        Token token =  lex.nextToken();

        switch (token.getType()) {
            case A: case B: case C: case D: case E: case F: case G:
            case a: case b: case c: case d: case e: case f: case g:
                return new Basenote(token.getValue().charAt(0));
        }

        return null;
    }

    static class UnexpectedTokenException extends RuntimeException{

    }
}
