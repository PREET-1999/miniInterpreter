package src.lox;

abstract class Stmt {
    interface Visitor<R> {
        R taskOnExpressionStmt(Expression stmt);
        R taskOnPrintStmt(Print stmt);
        R taskOnVarDeclStmt(VarDecl stmt);
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
}
