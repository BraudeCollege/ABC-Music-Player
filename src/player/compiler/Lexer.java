package player.compiler;

import java.util.*;
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

    private Deque<Token> tokens;
    private Stack<Token> logger;

    public Lexer(String text) throws TokenType.UnknownTokenException
    {
        this.text = text;
        this.tokens = new ArrayDeque<>();
        this.logger = new Stack<>();

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
        if (!hasNext()) throw new RunOutOfTokenException();

        Token token = tokens.remove();

        logger.push(token);

        return token;
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

    /**
     * Backtrack the most recently returned token
     * Modifies the object by putting the old token to the first
     * @return true if backtrack succeeds otherwise false
     */
    public boolean backtrack()
    {
        if (logger.size() == 0)
            return false;

        tokens.offerFirst(logger.pop());

        return true;
    }

    public void backtrack(int steps)
    {
        for (int i = 0; i < steps ; i++) {
            backtrack();
        }
    }


    static class RunOutOfTokenException extends RuntimeException
    {

    }
}
