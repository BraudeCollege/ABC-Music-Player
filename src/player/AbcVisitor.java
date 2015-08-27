package player;

import player.ast.*;

import javax.sound.midi.MidiUnavailableException;

public interface AbcVisitor
{
    public void on(AbcTune tune);
    public void on(AbcHeader header);
    public void on(AbcMusic body);
    public void on(FieldNumber field);
    public void on(FieldTitle field);
    public void on(FieldKey field);
    public void on(FieldVoice field);
    public void on(Comment c);
    public void on(ElementLine line);
    public void on(Element element);
    public void on(NthRepeat repeat);
    public void on(Barline bar);
    public void on(TupletElement element);
    public void on(Note note);
    public void on(MultiNote note);
    public void on(NoteLength noteLength);
    public void on(Rest rest);
    public void on(Pitch pitch);
    public void on(Basenote basenote);
}
