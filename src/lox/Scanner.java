package src.lox;

import src.lox.Token;
import static src.lox.TokenType.*; //static added to be able to use without ToeknType.PLUS""

import java.util.ArrayList;
import java.util.List;

public class Scanner {
    private final String source;
    private final List<Token> tokens = new ArrayList<>();

    private int start = 0;
    private int current = 0;
    private int lineNumber = 1;

    Scanner(String source) {
        this.source = source;

    }

    private char getCharacterFromSourceAndStep() {
        char currCharacter = source.charAt(current);
        current++;
        return currCharacter;
    }

    private boolean match(char expected) {
        if (reachedEOF())
            return false;
        if (source.charAt(current) != expected)
            return false;

        // only consume this if we got what we expected
        current++;
        return true;
    }

    /*
     * “Always safe to call, even at EOF”
     * So even if someone forgets reacjedEOF() outside, peek() won’t crash [as we
     * added 1st condition].
     */
    private char peek() {
        if (reachedEOF())
            return '\0';
        return source.charAt(current);
    }

    private void checkForStringLiteral() {
        while (!reachedEOF()) {
            if (peek() == '"') {
                getCharacterFromSourceAndStep();
                String literal = source.substring(start, current);
                // System.out.println("Found string literal "+ literal);
                addToken(STRING , literal);
                return;
            }
            // System.out.println(peek()); // just to see what all Im consuming
            getCharacterFromSourceAndStep();
        }
        Lox.reportError(lineNumber, "Unterminated String.");
        // char c = getCharacterFromSourceAndStep();
    }

    private void scanToken() {
        char c = getCharacterFromSourceAndStep();
        switch (c) {
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;

            // double characters
            case '!':
                addToken(match('=') ? NOT_EQUAL : NOT);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;

            // case '/':{
            // if(match('/')){ //a comment, consume the whole line
            // while(getCharacterFromSourceAndStep() != '\n' );
            // }
            // }
            case '/': {
                if (match('/')) { // a comment, consume the whole line
                    while (!reachedEOF() && peek() != '\n') {
                        getCharacterFromSourceAndStep();
                    }
                } else {
                    addToken(SLASH);
                }
                break;
            }
            case '"':
                checkForStringLiteral();
                break;

            case ' ':
            case '\r':
            case '\t':
                // Ignore whitespace.
                break;

            case '\n':
                lineNumber++;
                break;
            default:
                Lox.reportError(lineNumber, "Unexpected character."); // the erroneous character is still consumed by
                                                                      // the earlier call to
                                                                      // getCharacterFromSourceAndStep()
                break;

        }
    }

    void addToken(TokenType tokenType) {
        Token token = new Token(tokenType, "", lineNumber);
        tokens.add(token);
    }

    void addToken(TokenType tokenType, String lexeme) {
        Token token = new Token(tokenType, lexeme, lineNumber);
        tokens.add(token);
    }

    List<Token> scanTokens() {

        while (!reachedEOF()) {
            // We are at the beginning of the next lexeme.
            start = current;
            scanToken();
        }

        // sorce ko spserate by whitespace, and forEach word[s]-> check if
        // ya line se bhi sepertae krte wakt uss token ka line number dena;
        /*
         * 1. its resrved
         * 2. if its a single charcter by expicit comparision
         * 3. if its 2 character like == or != or >=
         * 4.else tag it as IDENTIFIER
         */
        return tokens;
    }

    private boolean reachedEOF() {
        return current >= source.length();
    }
}
