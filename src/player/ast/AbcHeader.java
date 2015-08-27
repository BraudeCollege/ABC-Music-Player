package player.ast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hieusun on 24.08.15.
 */
public class AbcHeader implements AbstractSyntaxTree
{
    private final FieldNumber fieldNumber;
    private final FieldTitle fieldTitle;
    private final List<OtherField> otherFields;
    private final FieldKey fieldKey;

    /**
     * @param fieldNumber != null
     * @param fieldTitle != null
     * @param otherFields != null
     * @param fieldKey != null
     */
    public AbcHeader(FieldNumber fieldNumber, FieldTitle fieldTitle, List<OtherField> otherFields, FieldKey fieldKey)
    {
        this.fieldNumber = fieldNumber;
        this.fieldTitle = fieldTitle;
        this.otherFields = new ArrayList<>(otherFields);
        this.fieldKey = fieldKey;
    }

    /**
     * @return field number
     */
    public FieldNumber getFieldNumber()
    {
        return fieldNumber;
    }

    /**
     * @return field title
     */
    public FieldTitle getFieldTitle()
    {
        return fieldTitle;
    }

    /**
     * @return other fields
     */
    public List<OtherField> getOtherFields()
    {
        return new ArrayList<>(otherFields);
    }

    /**
     * @return field key
     */
    public FieldKey getFieldKey()
    {
        return fieldKey;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbcHeader abcHeader = (AbcHeader) o;

        if (fieldNumber != null ? !fieldNumber.equals(abcHeader.fieldNumber) : abcHeader.fieldNumber != null)
            return false;
        if (fieldTitle != null ? !fieldTitle.equals(abcHeader.fieldTitle) : abcHeader.fieldTitle != null) return false;
        if (otherFields != null ? !otherFields.equals(abcHeader.otherFields) : abcHeader.otherFields != null)
            return false;
        return !(fieldKey != null ? !fieldKey.equals(abcHeader.fieldKey) : abcHeader.fieldKey != null);

    }

    @Override
    public int hashCode()
    {
        int result = fieldNumber != null ? fieldNumber.hashCode() : 0;
        result = 31 * result + (fieldTitle != null ? fieldTitle.hashCode() : 0);
        result = 31 * result + (otherFields != null ? otherFields.hashCode() : 0);
        result = 31 * result + (fieldKey != null ? fieldKey.hashCode() : 0);
        return result;
    }

    @Override
    public String toString()
    {
        return "AbcHeader{" +
                "fieldNumber=" + fieldNumber +
                ", fieldTitle=" + fieldTitle +
                ", otherFields=" + otherFields +
                ", fieldKey=" + fieldKey +
                '}';
    }

    @Override
    public <R> R accept(AbcVisitor<R> visitor)
    {
        return visitor.on(this);
    }
}
