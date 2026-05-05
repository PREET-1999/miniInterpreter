package src.lox;

public class ExprEvaluator implements Expr.Visitor<Object> {
    public Object interpret(Expr expr) {
        return expr.accept(this);

    }

    public static void printObjectType(Object obj) {
        if (obj instanceof Double) {
            System.out.println("Double: " + obj);
        } else if (obj instanceof String) {
            System.out.println("String: " + obj);
        } else if (obj instanceof Boolean) {
            System.out.println("Boolean: " + obj);
        } else if (obj == null) {
            System.out.println("nil (null)");
        } else {
            System.out.println("Unknown type: " + obj.getClass().getSimpleName());
        }
    }

    private boolean isTruthy(Object object) {
        if (object == null)
            return false;
        if (object instanceof Boolean)
            return (boolean) object;
        return true;
    }

    private boolean isEqual(Object a, Object b) {
        if (a == null && b == null)
            return true;
        if (a == null)
            return false;

        return a.equals(b);
    }

    public Object evaluate(Expr expr) {
        return interpret(expr);
    }

    public Object taskOnBinaryExpr(Expr.Binary expr) {
        Object left = expr.left.accept(this);
        Object right = expr.right.accept(this);

        switch (expr.operator.type) {
            case NOT_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case GREATER:
                return (double) left > (double) right;
            case GREATER_EQUAL:
                return (double) left >= (double) right;
            case LESS:
                return (double) left < (double) right;
            case LESS_EQUAL:
                return (double) left <= (double) right;
            case MINUS:
                return (double) left - (double) right;
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }

                break;
            case SLASH:
                return (double) left / (double) right;
            case STAR:
                return (double) left * (double) right;
        }
        return null;// will lit even reach here??

    }

    public Object taskOnUnaryExpr(Expr.Unary expr) {
        Object unaryObj = expr.right.accept(this);
        switch (expr.operator.type) {
            case NOT:
                return !isTruthy(unaryObj);
            case MINUS:
                return -(double) unaryObj;
        }
        return unaryObj;
    }

    public Object taskOnLiteralExpr(Expr.Literal expr) {

        Object val = expr.value;
        return val;

    }

    public Object taskOnGroupingExpr(Expr.Grouping expr) {
        Object exp = expr.expression.accept(this);
        return exp;
    }
}
