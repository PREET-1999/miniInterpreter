package src.lox;

import static src.lox.TokenType.*;
import java.util.List;
/*
expression     → equality ;
equality       → comparison ( ( "!=" | "==" ) comparison )* ;
comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term           → factor ( ( "-" | "+" ) factor )* ;
factor         → unary ( ( "/" | "*" ) unary )* ;
unary          → ( "!" | "-" ) unary
               | primary ;
primary        → NUMBER | STRING | "true" | "false" | "nil"
               | "(" expression ")" ;
*/
// Each grammar rule becomes a method inside this new class:

// Each method for parsing a grammar rule produces a syntax tree for that rule 
// and returns it to the caller

public class Parser {

    private static class ParseError extends RuntimeException {
    } // later check why this way

    private final List<Token> tokens;
    private int current = 0;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
    }

    Expr parse() {
        try {
            return expression();
        } catch (ParseError error) {
            return null;
        }
    }

    private Expr expression() {
        return equality();
    }

    // equality → comparison ( ( "!=" | "==" ) comparison )* ;
    private Expr equality() {
        Expr expr = comparision();

        while (matchThenStep(NOT_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparision();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    // comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
    private Expr comparision() {
        Expr expr = term();

        while (matchThenStep(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        Expr expr = factor();

        while (matchThenStep(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor() {
        Expr expr = unary();

        while (matchThenStep(SLASH, STAR)) {
            Token operator = previous();
            Expr right = unary();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    // unary → ( "!" | "-" ) unary
    // | primary ;
    private Expr unary() {
        if (matchThenStep(NOT, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        return primary();
    }

    // primary → NUMBER | STRING | "true" | "false" | "nil"
    // | "(" expression ")" ;
    private Expr primary() {
        if (matchThenStep(FALSE))
            return new Expr.Literal(false);
        if (matchThenStep(TRUE))
            return new Expr.Literal(true);
        if (matchThenStep(NIL))
            return new Expr.Literal(null);

        if (matchThenStep(NUMBER, STRING)) {
            return new Expr.Literal(previous().literal);// as already had stepped
        }

        if (matchThenStep(LEFT_PAREN)) {
            Expr expr = expression();
            consume(RIGHT_PAREN, "Expect ')' after expression.");
            return new Expr.Grouping(expr);
        }
        return null;//I dont think this is right to return
    }

    private Token consume(TokenType type, String message) {
        if (checkTokenType(type))
            return getTokenAndStep();
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Lox.reportError(token.line, message);
        return new ParseError();
    }

    private boolean matchThenStep(TokenType... types) {
        for (TokenType type : types) {
            if (checkTokenType(type)) {
                getTokenAndStep();
                return true;
            }
        }

        return false;
    }

    private boolean checkTokenType(TokenType type) {
        if (reachedEndOfTokens())
            return false;
        return peek().type == type;
    }

    private Token getTokenAndStep() {
        return tokens.get(current++);
    }

    private Token previous() {
        return tokens.get(current - 1);
    }

    private Token peek() {
        if (reachedEndOfTokens())
            return null;
        return tokens.get(current);
    }

    private boolean reachedEndOfTokens() {
        // return peek().type == EOF;
        return current >= tokens.size();
    }
}
