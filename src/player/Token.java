package player;

/**
 * Represent a token produced by a lexer
 */
public class Token
{
    private final TokenType type;
    private final String value;

    public Token(TokenType type, String value)
    {
        this.type = type;
        this.value = value;
    }

    public TokenType getType()
    {
        return type;
    }

    public String getValue()
    {
        return value;
    }

    public boolean equals(Object o)
    {
        if (this.getClass() != o.getClass())
            return false;

        Token aToken = (Token) o;

        return type.equals(aToken.type) && value.equals(aToken.value);
    }

    public String toString()
    {
        return "Token(" + type + ","+ value +")";
    }
}
