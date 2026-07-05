package src.lox;

import static src.lox.TokenType.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

/*
program        → declaration* EOF ;

declaration    → varDecl
               | statement ;
               
varDecl        → "var" IDENTIFIER ( "=" expression )? ";" ;

statement      → exprStmt
               | printStmt ;
               | block
               | ifStmt
               | whileStmt
               | forStmt

forStmt        → "for" "(" (varDecl | exprStmt | ";")  expression? ";" expression? ")"statement ;  //exprStmt hai expression nai

whileStmt      → "while" "(" expression ")" statement



ifStmt         → "if" "(" expression ")" statement
               ( "else" statement )? ;

exprStmt       → expression ";" ;
printStmt      → "print" expression ";" ;
block          → "{" declaration* "}" ";"  ;            

expression     → assignment;
assignment     → IDENTIFIER "=" assignment
               | logic_or;
logic_or       → logic_and ("or" logic_and)*;
logic_and      →  equality ( "and" equality)*;
equality       → comparison ( ( "!=" | "==" ) comparison )* ;
comparison     → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
term           → factor ( ( "-" | "+" ) factor )* ;
factor         → unary ( ( "/" | "*" ) unary )* ;
unary          → ( "!" | "-" ) unary
               | primary ;
primary        → NUMBER | STRING | "true" | "false" | "nil"
               | "(" expression ")" 
               | IDENTIFIER;
*/
// Each grammar rule becomes a method inside this new class:

// Each method for parsing a grammar rule produces a syntax tree for that rule 
// and returns it to the caller

public class Parser {

    private static class ParseError extends RuntimeException {
    } // later check why this way

    private final List<Token> tokens;
    private int current = 0;
    List<Stmt> stmtList;

    Parser(List<Token> tokens) {
        this.tokens = tokens;
        stmtList = new ArrayList<>();
    }

    // before statements were added
    // Expr parse() {
    // try {
    // return expression();
    // } catch (ParseError error) {
    // return null;
    // }
    // }

    // now since it has the correct starting symbol in grammer
    List<Stmt> parse() {
        try {
            return program();
        } catch (ParseError error) {
            return null;
        }
    }

    private List<Stmt> program() {
        while (!reachedEndOfTokens()) {
            stmtList.add(declarationStmt());
        }
        return stmtList;
    }

    private Stmt declarationStmt() {

        if (matchThenStep(VAR)) {
            // System.out.println("var toh mila hai");
            return varDecl();
        }
        return statement();

    }

    private Stmt varDecl() {

        Token id = consume(IDENTIFIER, "Expect variable name.");
        Expr expr = null;
        if (matchThenStep(EQUAL)) {

            expr = expression();

        }
        consume(SEMICOLON, "Expect ';' after value.");

        return new Stmt.VarDecl(expr, id);

    }
    // // The below one didnt work as it didnt handle just the decl-> var a ;

    // private Stmt varDecl() {
    // if (matchThenStep(IDENTIFIER)) {
    // System.out.print("ID toh mila hai");

    // Token id = previous();
    // System.out.println(id);

    // if (matchThenStep(EQUAL)) {
    // System.out.println("= bhi mila hai");

    // Expr expr = expression();
    // consume(SEMICOLON, "Expect ';' after value.");

    // return new Stmt.VarDecl(expr, id);
    // }
    // }
    // return null;
    // }

    private Stmt statement() {
        if (matchThenStep(PRINT))
            return printStatement();

        if (matchThenStep(LEFT_BRACE)) {
            return blockStatement();
        }
        if (matchThenStep(IF)) {
            return ifStatement();

        }

        if (matchThenStep(WHILE)) {
            return whileStatement();

        }

        if (matchThenStep(FOR)) {
            return forStatement();
        }
        return expressionStatement();
    }

    private Stmt forStatement() {
        consume(LEFT_PAREN, "Expected opening brace.");
        Stmt initializer = null;
        if (matchThenStep(SEMICOLON)) {
            // empoty initilizer
        } else {
            if (matchThenStep(VAR)) {
                initializer = varDecl();
            } else { // it must be an expr stmt
                initializer = expressionStatement();
            }
            // consume(SEMICOLON, "Expected seperator.");

        }
        System.out.println("Initializer zaala");
        Expr condition = null;
        if (matchThenStep(SEMICOLON)) {
            // empoty consition
        } else {
            System.out.println("found some condition");

            condition = expression();
            consume(SEMICOLON, "Expected seperator.");//as expression doesnt consume ';'

        }
        System.out.println("Condition zaala");
        Expr increment = null;

        if (matchThenStep(RIGHT_PAREN)) {
            // empoty increment
        } else {
            System.out.println("increment cha else");
            increment = expression();
            // consume(SEMICOLON, "Expected seperator.");

        consume(RIGHT_PAREN, "Expected closing brace.");

        }
        // consume(RIGHT_PAREN, "Expected closing brace.");
        System.out.println("Increment zaala");

        Stmt stmt = statement();
        if(stmt instanceof Stmt.Block){
            System.out.println("Block hai ab");
            System.out.println(((Stmt.Block)stmt).blockStmts.size());
            if(increment!=null)
            ((Stmt.Block)stmt).blockStmts.add(new Stmt.Expression(increment));
            System.out.println(((Stmt.Block)stmt).blockStmts.size());


        }
        else{

        }

        Stmt forBlock = stmt;
        if(initializer!=null){
                    List<Stmt> blockstmts = new ArrayList<>();
                    blockstmts.add(initializer);
                    blockstmts.add(new Stmt.While(condition, stmt));
                    forBlock = new Stmt.Block(blockstmts);
        }
       
        return forBlock;
    }

    private Stmt whileStatement() {
        consume(LEFT_PAREN, "Expected opening brace.");
        Expr expr = expression();
        consume(RIGHT_PAREN, "Expected closng brace.");
        Stmt stmt = statement();
        return new Stmt.While(expr, stmt);
    }

    private Stmt ifStatement() {
        consume(LEFT_PAREN, "Expected opening brace.");
        Expr expr = expression();
        consume(RIGHT_PAREN, "Expected closng brace.");
        Stmt taken = statement();
        Stmt notTaken = null;
        if (matchThenStep(ELSE)) {
            notTaken = statement();
        }
        return new Stmt.If(expr, taken, notTaken);
    }

    private Stmt blockStatement() {
        List<Stmt> blockstmts = new ArrayList<>();
        while (!matchThenStep(RIGHT_BRACE)) {
            Stmt blockStmt = declarationStmt();
            blockstmts.add(blockStmt);
        }
        // consume(RIGHT_BRACE, "expected closing }");
        return new Stmt.Block(blockstmts);
    }

    private Stmt printStatement() {
        // print was consumed by statement() already
        Expr value = expression();
        consume(SEMICOLON, "Expect ';' after value.");
        return new Stmt.Print(value);
    }

    private Stmt expressionStatement() {
        Expr expr = expression();
        consume(SEMICOLON, "Expect ';' after expression.");
        return new Stmt.Expression(expr);
    }

    private Expr expression() {

        return assignment();
    }

    private Expr assignment() {
        // In a complex l-value, this might fail...
        if (checkTokenType(IDENTIFIER) && checkNextTokenType(EQUAL)) {
            Token id = consume(IDENTIFIER, "expected an IDENTIFIER");
            consume(EQUAL, "expected a =");
            Expr rhs = assignment(); // we didnt loop, because assignment is right associative (oooo)
            return new Expr.Assign(id, rhs);
        }
        // if(matchThenStep(IDENTIFIER)){
        // Token id = previous();
        // consume(EQUAL, "expected a =");
        // Expr rhs = assignment();
        // return new Expr.Assign(id, rhs);
        // }

        return logicalOr();
    }

    private Expr logicalOr() {
        Expr expr = logicalAnd();
        while (matchThenStep(OR)) {
            Token op = previous();
            Expr rightOperand = logicalAnd();
            expr = new Expr.Logical(expr, op, rightOperand);
        }
        return expr;
    }

    private Expr logicalAnd() {
        Expr expr = equality();
        while (matchThenStep(AND)) {
            Token op = previous();
            Expr rightOperand = equality();
            expr = new Expr.Logical(expr, op, rightOperand);
        }
        return expr;
    }

    // equality → comparison ( ( "!=" | "==" ) comparison )* ;
    private Expr equality() {
        // System.out.println("Equality -> I was called");
        Expr expr = comparision();

        // System.out.println(" Equality ==| !== ");
        while (matchThenStep(NOT_EQUAL, EQUAL_EQUAL)) {
            Token operator = previous();
            Expr right = comparision();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    // comparison → term ( ( ">" | ">=" | "<" | "<=" ) term )* ;
    private Expr comparision() {
        // System.out.println("Comparision -> I was called");

        Expr expr = term();

        while (matchThenStep(GREATER, GREATER_EQUAL, LESS, LESS_EQUAL)) {
            Token operator = previous();
            Expr right = term();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr term() {
        // System.out.println("term -> I was called");

        Expr expr = factor();

        while (matchThenStep(MINUS, PLUS)) {
            Token operator = previous();
            Expr right = factor();
            expr = new Expr.Binary(expr, operator, right);
        }

        return expr;
    }

    private Expr factor() {
        // System.out.println("factor -> I was called");

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
        // System.out.println("unary -> I was called");

        if (matchThenStep(NOT, MINUS)) {
            Token operator = previous();
            Expr right = unary();
            return new Expr.Unary(operator, right);
        }

        return primary();
    }

    // primary → NUMBER | STRING | "true" | "false" | "nil" | IDENTIFIER
    // | "(" expression ")" ;
    private Expr primary() {
        // System.out.println("primary -> I was called");

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
        if (matchThenStep(IDENTIFIER)) {
            // System.out.println("found Id" + previous());
            return new Expr.Literal(previous());
        }
        // System.out.println("Im Primary but couldnt find anything, returning null\n");
        // return null;// I dont think this is right to return...I was right, needed the
        // below stmt
        throw error(peek(), "Expected expression.");

    }

    private Token consume(TokenType type, String message) {
        if (checkTokenType(type))
            return getTokenAndStep();
        throw error(peek(), message);
    }

    private ParseError error(Token token, String message) {
        Lox.error(token, message);
        return new ParseError();
    }

    private boolean matchThenStep(TokenType... types) {
        for (TokenType type : types) {
            if (checkTokenType(type)) {
                // System.out.println("matching for" +type);
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

    private boolean checkNextTokenType(TokenType type) {
        if (reachedEndOfTokens())
            return false;
        return peekNext().type == type;
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

    private Token peekNext() {
        if (reachedEndOfTokens())
            return null;
        if (current + 1 > tokens.size())
            return null;
        return tokens.get(current + 1);
    }

    private boolean reachedEndOfTokens() {
        // return peek().type == EOF;
        return current >= tokens.size();
    }

    private void synchronize() {
        getTokenAndStep();

        while (!reachedEndOfTokens()) {
            if (previous().type == SEMICOLON)
                return;

            switch (peek().type) {
                case CLASS:
                case FUN:
                case VAR:
                case FOR:
                case IF:
                case WHILE:
                case PRINT:
                    return;
            }

            getTokenAndStep();
        }
    }
}
