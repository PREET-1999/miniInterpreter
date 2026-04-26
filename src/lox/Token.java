package src.lox;

public class Token {
    final TokenType type;
    final int line;
    final String lexeme;

    Token(TokenType type, String lexeme, int line) {
        this.type = type;
        this.line = line;
        this.lexeme = lexeme;
    }

    public String toString() {
        return type + " ";
    }
}
