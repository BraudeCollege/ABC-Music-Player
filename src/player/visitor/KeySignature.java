package player.visitor;

import player.ast.Keynote;
import player.ast.ModeMinor;

public enum KeySignature
{
    C  (new char[] {}, true),
    G  (new char[] { 'F' }, true),
    D  (new char[] { 'F', 'C'}, true),
    A  (new char[] { 'F', 'C', 'G'}, true),
    E  (new char[] { 'F', 'C', 'G', 'D'}, true),
    B  (new char[] { 'F', 'C', 'G', 'D', 'A'}, true),
    Fs (new char[] { 'F', 'C', 'G', 'D', 'A', 'E'}, true),
    Cs (new char[] { 'F', 'C', 'G', 'D', 'A', 'E', 'B'}, true),

    Am (new char[] {}, true),
    Em (new char[] { 'F' }, true),
    Bm (new char[] { 'F', 'C'}, true),
    Fsm(new char[] { 'F', 'C', 'G'}, true),
    Csm(new char[] { 'F', 'C', 'G', 'D'}, true),
    Gsm(new char[] { 'F', 'C', 'G', 'D', 'A'}, true),
    Dsm(new char[] { 'F', 'C', 'G', 'D', 'A', 'E'}, true),
    Asm(new char[] { 'F', 'C', 'G', 'D', 'A', 'E', 'B'}, true),

    F  ( new char[] { 'B' }, false ),
    Bb ( new char[] { 'B', 'E' }, false ),
    Eb ( new char[] { 'B', 'E', 'A' }, false ),
    Ab ( new char[] { 'B', 'E', 'A', 'D' }, false ),
    Db ( new char[] { 'B', 'E', 'A', 'D', 'G' }, false ),
    Gb ( new char[] { 'B', 'E', 'A', 'D', 'G', 'C' }, false ),
    Cb ( new char[] { 'B', 'E', 'A', 'D', 'G', 'C', 'F' }, false ),

    Dm ( new char[] {  'B' }, false ),
    Gm ( new char[] {  'B', 'E' }, false ),
    Cm ( new char[] {  'B', 'E', 'A' }, false ),
    Fm ( new char[] {  'B', 'E', 'A', 'D' }, false ),
    Bbm( new char[] {  'B', 'E', 'A', 'D', 'G' }, false ),
    Ebm( new char[] {  'B', 'E', 'A', 'D', 'G', 'C' }, false ),
    Abm( new char[] {  'B', 'E', 'A', 'D', 'G', 'C', 'F' }, false );

    private char[] notes;

    private boolean isSharp;

    private KeySignature(char[] notes, boolean isSharp)
    {
        this.notes = notes;
        this.isSharp = isSharp;
    }

    /**
     * @param keynote
     * @param minor
     * @return a KeySignature depends on the given keynote and ModeMinor
     *         if no KeySignature is found, key C major is returned
     */
    public static KeySignature getKey(Keynote keynote, ModeMinor minor)
    {
        StringBuilder keyStrBuilder = new StringBuilder();

        char basenote = Character.toUpperCase(keynote.getBasenote().getSymbol());
        keyStrBuilder.append(basenote);

        switch (keynote.getKeyAccidental().getType()) {
            case SHARP:
                keyStrBuilder.append('s'); break;
            case FLAT:
                keyStrBuilder.append('b'); break;
        }

        if (minor.getType() != ModeMinor.Type.NONE)
            keyStrBuilder.append('m');

        try {
            return KeySignature.valueOf(keyStrBuilder.toString());
        } catch (IllegalArgumentException e) {
            return C;
        }
    }

    /**
     * @param noteSymbol
     * @return how many semitones up / down for noteSymbol in this key signature
     */
    public int getAccidental(char noteSymbol)
    {
        char upperCaseSymbol = Character.toUpperCase(noteSymbol);

        int factor = isSharp ? 1 : -1;

        for (char symbol : notes)
        {
            if (symbol == upperCaseSymbol)
                return factor;
        }

        return 0;
    }
}
