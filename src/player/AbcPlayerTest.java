package player;

import org.junit.Test;
import player.ast.Accidental;
import player.ast.Basenote;
import player.ast.Octave;
import player.ast.Pitch;
import sound.SequencePlayer;

import static org.junit.Assert.*;

public class AbcPlayerTest
{

    @Test
    public void testPlayPitch() throws Exception
    {
        AbcPlayer player = new AbcPlayer(new SequencePlayer(120, 2));

        player.on(new Pitch(new Basenote('A'), Accidental.getEmpty(), Octave.getEmpty()));

        assertEquals("Event: NOTE_ON  Pitch: 60  Tick: 0\n" +
                "***** End of track *****Tick:1", player.toString());
    }
}
