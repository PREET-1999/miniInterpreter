package src.lox;

abstract class Expr {
    interface Visitor<R> {
        R taskOnBinaryExpr(Binary expr);
        R taskOnGroupingExpr(Grouping expr);
        R taskOnLiteralExpr(Literal expr);
        R taskOnUnaryExpr(Unary expr);
    }

    abstract <R> R accept(Visitor<R> visitor);

    static class Binary extends Expr {
        final Expr left;
        final Token operator;
        final Expr right;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnBinaryExpr(this);
        }
        Binary(Expr left, Token operator, Expr right) {
            this.left = left;
            this.operator = operator;
            this.right = right;
        }
    }
    static class Grouping extends Expr {
        final Expr expression;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnGroupingExpr(this);
        }
        Grouping(Expr expression) {
            this.expression = expression;
        }
    }
    static class Literal extends Expr {
        final Object value;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnLiteralExpr(this);
        }
        Literal(Object value) {
            this.value = value;
        }
    }
    static class Unary extends Expr {
        final Token operator;
        final Expr right;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnUnaryExpr(this);
        }
        Unary(Token operator, Expr right) {
            this.operator = operator;
            this.right = right;
        }
    }
}
