package player;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * Lexer performs lexical analysis for an ABC-Notation text
 * and provides tokens to parser
 */
public class Lexer
{
    /**
     * Text to be lexed
     */
    private String text;

    private Queue<Token> tokens;

    public Lexer(String text)
    {
        this.text = text;
        this.tokens = new ArrayDeque<>();
        initLexer();
    }

    /**
     * @return true if there are tokens left; otherwise false
     */
    public boolean hasNext()
    {
        return tokens.isEmpty();
    }

    /**
     * This Method read the text of lexer and create some token
     *
     * @return nextToken if there're still some left
     */
    public Token nextToken()
    {
        return null;
    }


    /**
     * Tokenize the text of the lexer
     */
    private void tokenize()
    {

    }

    /**
     * Initialize all states of the lexer at the begining
     */
    private void initLexer()
    {
        tokenize();
    }

}
