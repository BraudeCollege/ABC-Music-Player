package player;

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

    public Lexer(String text) {
        this.text = text;
    }

    /**
     *
     * @return true if there are tokens left; otherwise false
     */
    public boolean hasNext(){
        return false;
    }

    /**
     * This Method read the text of lexer and create some token
     * @return nextToken if there're still some left
     */
    public Token nextToken(){
        return null;
    }

}
