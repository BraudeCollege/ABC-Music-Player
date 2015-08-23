package player.ast;

/**
 * Created by hieusun on 23.08.15.
 */
public class Rest implements AbstractSyntaxTree
{

    private static Rest instance;

    private Rest() {}

    public static Rest getInstance() {

        if (instance == null)
            instance = new Rest();

        return instance;
    }
}
