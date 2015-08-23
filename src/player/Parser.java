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
     * @return an ast with the root is a basenote
     * @throws player.Parser.UnexpectedTokenException if no basenote found
     */
    public AbstractSyntaxTree expectBasenote()
    {
        Token token =  lex.nextToken();

        switch (token.getType()) {
            case A: case B: case C: case D: case E: case F: case G:
            case a: case b: case c: case d: case e: case f: case g:
                return new Basenote(token.getValue().charAt(0));
        }

        throw new UnexpectedTokenException("Unexpected token '"+ token.getValue() +"', expect a Basenote");
    }

    static class UnexpectedTokenException extends RuntimeException {

        public UnexpectedTokenException(String message)
        {
            super(message);
        }
    }
}
