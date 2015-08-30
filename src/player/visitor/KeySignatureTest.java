package player.visitor;

import org.junit.Test;
import player.ast.Basenote;
import player.ast.KeyAccidental;
import player.ast.Keynote;
import player.ast.ModeMinor;

import static org.junit.Assert.*;

public class KeySignatureTest
{
    @Test
    public void testGetKey()
    {
        KeySignature key = KeySignature.getKey(new Keynote(new Basenote('F'), KeyAccidental.getSharp()), ModeMinor.getNone());
        assertEquals(KeySignature.Fs, key);

        key = KeySignature.getKey(new Keynote(new Basenote('B'), KeyAccidental.getFlat()), ModeMinor.getInstance());
        assertEquals(KeySignature.Bbm, key);

        key = KeySignature.getKey(new Keynote(new Basenote('B'), KeyAccidental.getSharp()), ModeMinor.getInstance());
        assertEquals(KeySignature.C, key);
    }

    @Test
    public void testGetAccidental() throws Exception
    {
        assertEquals(1, KeySignature.B.getAccidental('a'));
        assertEquals(1, KeySignature.B.getAccidental('A'));
        assertEquals(1, KeySignature.B.getAccidental('G'));
        assertEquals(1, KeySignature.Asm.getAccidental('B'));
        assertEquals(1, KeySignature.Asm.getAccidental('b'));

        assertEquals(-1, KeySignature.Cb.getAccidental('D'));
        assertEquals(-1, KeySignature.Cb.getAccidental('F'));

        assertEquals(0, KeySignature.C.getAccidental('F'));
        assertEquals(0, KeySignature.Am.getAccidental('F'));
    }
}
