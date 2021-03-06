package player.ast;

import java.util.ArrayList;
import java.util.List;

public class AbcMusic implements AbstractSyntaxTree
{
    private final List<AbcLine> lines;

    /**
     * @param lines collection AbcLine objects != null
     */
    public AbcMusic(List<AbcLine> lines)
    {
        this.lines = new ArrayList<>(lines);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbcMusic abcMusic = (AbcMusic) o;

        return !(lines != null ? !lines.equals(abcMusic.lines) : abcMusic.lines != null);

    }

    public List<AbcLine> getLines()
    {
        return lines;
    }

    @Override
    public int hashCode()
    {
        return lines != null ? lines.hashCode() : 0;
    }

    @Override
    public String toString()
    {
        return "AbcMusic{" +
                "lines=" + lines +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
