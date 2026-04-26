package src.lox;

public class Token {
    final TokenType type;
    final int line;
    final String lexeme;
    final Object literal; // added for generic storage of info

    // the below signature didnt work when I needed to store the actual number in
    // the Token
    // so decided to go with storing the lexeme and a generic object that can
    // represent string or obj in a token...
    // Token(TokenType type, String lexeme, int line) {
    // this.type = type;
    // this.line = line;
    // this.lexeme = lexeme;
    // }
    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
