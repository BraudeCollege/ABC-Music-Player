package player;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public Lexer(String text) throws TokenType.UnknownTokenException
    {
        this.text = text;
        this.tokens = new ArrayDeque<>();

        tokenize();
    }

    /**
     * @return true if there are tokens left; otherwise false
     */
    public boolean hasNext()
    {
        return !tokens.isEmpty();
    }

    /**
     * This Method read the text of lexer and create some token
     *
     * @return nextToken if there're still some left
     */
    public Token nextToken()
    {
        if(!hasNext()) throw new RunOutOfTokenException();
        return tokens.remove();
    }


    /**
     * Tokenize the text of the lexer
     * Modifies tokens by adding tokens to it
     */
    private void tokenize() throws TokenType.UnknownTokenException
    {
        TokenType[] types = TokenType.values();

        ArrayList<String> patterns = new ArrayList<>();

        for (TokenType t : types) {
            patterns.add(t.getRegex());
        }

        String regex = "(" + String.join("|", patterns) + ")";

        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(text);

        while (m.find()) {

            String tokenText = m.group(1);
            TokenType tokenType = TokenType.identify(tokenText);

            if (tokenType == null)
                throw new TokenType.UnknownTokenException();

            tokens.add(new Token(tokenType, tokenText));
        }
    }

    @Override
    public String toString()
    {
        return "Lexer{ " + tokens + " }";
    }


    static class RunOutOfTokenException extends RuntimeException
    {

    }
}
