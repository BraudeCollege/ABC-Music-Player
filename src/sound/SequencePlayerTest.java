package sound;

import org.junit.Test;
import static org.junit.Assert.*;

public class SequencePlayerTest
{

    @Test
    public void testPlay() throws Exception
    {
        SequencePlayer sp = new SequencePlayer(300, 2);
        sp.addNote(new Pitch('C').toMidiNote(), 0, 1);
        sp.addNote(new Pitch('C').toMidiNote(), 1, 1);
        sp.addNote(new Pitch('C').toMidiNote(), 2, 1);
        sp.addNote(new Pitch('C').toMidiNote(), 3, 1);
        sp.addNote(new Pitch('C').toMidiNote(), 4, 1);
        sp.addNote(new Pitch('C').toMidiNote(), 5, 1);
        sp.addNote(new Pitch('C').toMidiNote(), 6, 1);
        sp.addNote(new Pitch('C').toMidiNote(), 7, 1);

        sp.addNote(new Pitch('C').toMidiNote(), 8, 3);
        sp.addNote(new Pitch('C').toMidiNote(), 11, 1);
        sp.addNote(new Pitch('C').toMidiNote(), 12, 3);
        sp.addNote(new Pitch('C').toMidiNote(), 15, 1);

        sp.play();

        assertEquals("Event: NOTE_ON  Pitch: 60  Tick: 0\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 1\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 1\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 2\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 2\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 3\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 3\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 4\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 4\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 5\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 5\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 6\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 6\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 7\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 7\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 8\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 8\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 11\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 11\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 12\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 12\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 15\n" +
                "Event: NOTE_ON  Pitch: 60  Tick: 15\n" +
                "Event: NOTE_OFF Pitch: 60  Tick: 16\n" +
                "***** End of track *****   Tick: 16\n", sp.toString());
    }

}
