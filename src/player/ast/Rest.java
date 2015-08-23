package player.ast;

public class Rest implements AbstractSyntaxTree, NoteOrRest
{

    private static Rest instance;

    private Rest() {}

    public static Rest getInstance() {

        if (instance == null)
            instance = new Rest();

        return instance;
    }
}
