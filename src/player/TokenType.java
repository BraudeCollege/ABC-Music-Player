package player;

/**
 * Created by DucNguyenMinh on 8/18/15.
 */
public enum TokenType {
//
//    abc-tune ::= abc-header abc-music
//
//    abc-header ::= field-number comment* field-title other-fields* field-key
//
//    field-number ::= "X:" DIGIT+ end-of-line
//    field-title ::= "T:" text end-of-line
//    other-fields ::= field-composer | field-default-length | field-meter
//    | field-tempo | field-voice | comment
//    field-composer ::= "C:" text end-of-line
//    field-default-length ::= "L:" note-length-strict end-of-line
//    field-meter ::= "M:" meter end-of-line
//    field-tempo ::= "Q:" tempo end-of-line
//    field-voice ::= "V:" text end-of-line
//    field-key ::= "K:" key end-of-line
//
//    key ::= keynote [mode-minor]

//    keynote ::= _TODO_ basenote [key-accidental]

//    key-accidental ::= "#" | "b"
//    mode-minor ::= "m"
//
//    meter ::= "C" | "C|" | meter-fraction
//    meter-fraction ::= DIGIT+ "/" DIGIT+
//
//    tempo ::= DIGIT+



//    abc-music ::= abc-line+
//    abc-line ::= (element+ linefeed) | mid-tune-field | comment
//    element ::= note-element | tuplet-element | barline | nth-repeat | space
//
//    note-element ::= (note | multi-note)
//
//// note is either a pitch or a rest
//    note ::= note-or-rest [note-length]
//    note-or-rest ::= pitch | rest
//    pitch ::= [accidental] basenote [octave]
//    octave ::= ("'"+) | (","+)
//    note-length ::= [DIGIT+] ["/" [DIGIT+]]
//    note-length-strict ::= DIGIT+ "/" DIGIT+
//
//    ; "^" is sharp, "_" is flat, and "=" is neutral
//    accidental ::= "^" | "^^" | "_" | "__" | "="
//
//    basenote ::= "C" | "D" | "E" | "F" | "G" | "A" | "B"
//            | "c" | "d" | "e" | "f" | "g" | "a" | "b"
//
//    rest ::= "z"
//
//// tuplets
//    tuplet-element ::= tuplet-spec note-element+
//    tuplet-spec ::= "(" DIGIT
//
//// chords
//    multi-note ::= "[" note+ "]"
//
//    barline ::= "|" | "||" | "[|" | "|]" | ":|" | "|:"
//    nth-repeat ::= "[1" | "[2"
//
//    ; A voice field might reappear in the middle of a piece
//    ; to indicate the change of a voice
//    mid-tune-field- ::= field-voice
//
//    comment ::= "%" text linefeed
//    end-of-line ::= comment | linefeed
//    text ::= .*

    SPACE(" "),
    FIELD_X("X:"), FIELD_T("T:"), FIELD_C("C:"), FIELD_L("L:"),
    FIELD_M("M:"), FIELD_Q("Q:"), FIELD_V("V:"), FIELD_K("K:"),

    SHARP("#"),

    MODE_MINOR("m"),
    C_PIPE("C\\|"),


    C("C"), D("D"), E("E"), F("F"), G("G"), A("A"), B("B"),
    c("c"), d("d"), e("e"), f("f"), g("g"), a("a"), b("b"),
    REST("z"),

    SLASH("/"),

    OCTAVE_UP("'+"),
    OCTAVE_DOWN(",+"),

    ACC_SHARP_DOUBLE("\\^\\^"), ACC_SHARP("\\^"),
    ACC_FLAT_DOUBLE("__"), ACC_FLAT("_"),
    ACC_NEUTRAL("\\="),

    OPEN_PAREN("\\("),

    DOUBLE_BAR("\\|\\|"),CLOSE_BAR("\\|\\]"), SINGLE_BAR("\\|"),OPEN_BAR("\\[\\|"),
    OPEN_REPEAT_BAR("\\|:"), CLOSE_REPEAT_BAR(":\\|"),

    OPEN_BRACKET("\\["), CLOSE_BRACKET("\\]"),

    REPEAT_1("\\[1"),
    REPEAT_2("\\[2"),
    DIGIT("[0-9]"),
    COMMENT("%.+"),
    DOT_PLUS(".+"),
    LINEFEED("(?:\\n|\\n\\r)");


    static class UnknownTokenException extends RuntimeException { }

    /**
     * regular expression to match the token
     */
    private String regex;

    /**
     * initialize enum object with a regular expression
     * @param regex regular expression string
     */
    TokenType(String regex) {
        this.regex = regex;
    }

    /**
     * @return corresponding regular expression string
     */
    public String getRegex()
    {
        return regex;
    }

    /**
     * @param token Value of Token to be identified
     *
     * @return the type of the token given in the argument
     * null if no matching type found
     */
    public static TokenType identify(String token)
    {
        for (TokenType t : TokenType.values()) {
            if (token.matches(t.getRegex()))
                return t;
        }

        return null;
    }
}
