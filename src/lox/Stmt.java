package src.lox;

import java.util.List;
import java.util.ArrayList;
abstract class Stmt {
    interface Visitor<R> {
        R taskOnExpressionStmt(Expression stmt);
        R taskOnPrintStmt(Print stmt);
        R taskOnVarDeclStmt(VarDecl stmt);
        R taskOnBlockStmt(Block stmt);
        R taskOnIfStmt(If stmt);
        R taskOnWhileStmt(While stmt);
    }

    abstract <R> R accept(Visitor<R> visitor);

    static class Expression extends Stmt {
        final Expr expression;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnExpressionStmt(this);
        }
        Expression(Expr expression) {
            this.expression = expression;
        }
    }
    static class Print extends Stmt {
        final Expr expression;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnPrintStmt(this);
        }
        Print(Expr expression) {
            this.expression = expression;
        }
    }
    static class VarDecl extends Stmt {
        final Expr expression;
        final Token varId;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnVarDeclStmt(this);
        }
        VarDecl(Expr expression, Token varId) {
            this.expression = expression;
            this.varId = varId;
        }
    }
    static class Block extends Stmt {
        final List<Stmt> blockStmts;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnBlockStmt(this);
        }
        Block(List<Stmt> blockStmts) {
            this.blockStmts = blockStmts;
        }
    }
    static class If extends Stmt {
        final Expr expression;
        final Stmt takenStmt;
        final Stmt notTakenStmt;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnIfStmt(this);
        }
        If(Expr expression, Stmt takenStmt, Stmt notTakenStmt) {
            this.expression = expression;
            this.takenStmt = takenStmt;
            this.notTakenStmt = notTakenStmt;
        }
    }
    static class While extends Stmt {
        final Expr expression;
        final Stmt whileStmt;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnWhileStmt(this);
        }
        While(Expr expression, Stmt whileStmt) {
            this.expression = expression;
            this.whileStmt = whileStmt;
        }
    }
}
