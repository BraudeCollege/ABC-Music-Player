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
        Token aToken = (Token) o;
        return type.equals(aToken.type) && value.equals(aToken.value);
    }
    
    @Override
    public int hashCode()
    {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }
}
