package player.ast;

import player.ast.*;

import javax.sound.midi.MidiUnavailableException;

/**
 * Visitor interface of Abc data structure
 * @param <R> return type
 */
public interface AbcVisitor<R>
{
    public R on(AbcTune tune);
    public R on(AbcHeader header);
    public R on(AbcMusic body);
    public R on(FieldNumber field);
    public R on(FieldTitle field);
    public R on(FieldKey field);
    public R on(FieldVoice field);
    public R on(Comment c);
    public R on(ElementLine line);
    public R on(Element element);
    public R on(NthRepeat repeat);
    public R on(Barline bar);
    public R on(TupletElement element);
    public R on(Note note);
    public R on(MultiNote note);
    public R on(NoteLength noteLength);
    public R on(Rest rest);
    public R on(Pitch pitch);
    public R on(Basenote basenote);
}
