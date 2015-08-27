package player.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Represent a collection of elements
 */
public class ElementLine implements AbcLine
{
    private final List<Element> elements;

    /**
     * @param elements != null
     */
    public ElementLine(List elements)
    {
        this.elements = new ArrayList<>(elements);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ElementLine that = (ElementLine) o;

        return elements.equals(that.elements);

    }

    public List<Element> getElements()
    {
        return elements;
    }

    @Override
    public int hashCode()
    {
        return elements.hashCode();
    }

    @Override
    public String toString()
    {
        return "ElementLine{" +
                "elements=" + elements +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
