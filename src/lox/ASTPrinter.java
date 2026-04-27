package src.lox;

public class ASTPrinter implements Expr.Visitor<String> {
    public String print(Expr expr){
            return expr.accept(this);

    }
    public String wrapInParantheses(String... names) {
        StringBuilder builder = new StringBuilder();
        builder.append("(");
        for (var name : names) {
            builder.append(" ");
            builder.append(name);
        }
        builder.append(")");

        return builder.toString();

    }

    public String taskOnBinaryExpr(Expr.Binary expr) {
        String tokenName = expr.operator.lexeme;
        String leftExprName = expr.left.accept(this);
        String RightExprName = expr.right.accept(this);
        return wrapInParantheses(tokenName, leftExprName, RightExprName);
    }

    public String taskOnUnaryExpr(Expr.Unary expr) {
        String tokenName = expr.operator.lexeme;
        String exprName = expr.right.accept(this);
        return wrapInParantheses(tokenName, exprName);
    }

    public String taskOnLiteralExpr(Expr.Literal expr) {
        String literalName;
        Object literalToken = expr.value;
        if (literalToken == null)
            literalName = "nil";
        else
            literalName = expr.value.toString();
        return wrapInParantheses(literalName);

    }

    public String taskOnGroupingExpr(Expr.Grouping expr) {
        String exprName = expr.expression.accept(this);
        return wrapInParantheses("group",exprName);

    }
}
