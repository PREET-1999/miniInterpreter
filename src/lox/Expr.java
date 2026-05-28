package src.lox;

import java.util.List;
import java.util.ArrayList;
abstract class Expr {
    interface Visitor<R> {
        R taskOnBinaryExpr(Binary expr);
        R taskOnLogicalExpr(Logical expr);
        R taskOnGroupingExpr(Grouping expr);
        R taskOnLiteralExpr(Literal expr);
        R taskOnUnaryExpr(Unary expr);
        R taskOnAssignExpr(Assign expr);
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
    static class Logical extends Expr {
        final Expr left;
        final Token operator;
        final Expr right;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnLogicalExpr(this);
        }
        Logical(Expr left, Token operator, Expr right) {
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
    static class Assign extends Expr {
        final Token leftId;
        final Expr rhsExpr;


        @Override
        public <R> R accept(Visitor<R> visitor) {
            return visitor.taskOnAssignExpr(this);
        }
        Assign(Token leftId, Expr rhsExpr) {
            this.leftId = leftId;
            this.rhsExpr = rhsExpr;
        }
    }
}
