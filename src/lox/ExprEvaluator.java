package src.lox;

import static src.lox.TokenType.EQUAL;

import java.util.List;

public class ExprEvaluator implements Expr.Visitor<Object>, Stmt.Visitor<Void> {
    private static class ExprEvaluationError extends RuntimeException {
    }

    private ExprEvaluationError error(Token operator, String message) {
        Lox.error(operator, message);
        return new ExprEvaluationError();
    }

    private void checkNumberOperand(Token operator, Object operand) {
        if (operand instanceof Double)
            return;
        throw error(operator, "Operand must be a number.");
    }

    private void checkNumberOperands(Token operator,
            Object left, Object right) {
        if (left instanceof Double && right instanceof Double)
            return;

        throw error(operator, "Operands must be numbers.");
    }

    public Object interpret(Expr expr) {
        try {
            // System.out.println("Exp eval start....");
            // System.out.println( expr.accept(this) );
            return expr.accept(this);

        } catch (ExprEvaluationError error) {
            return null;
        }

    }

    public void interpret(List<Stmt> stmts) {
        try {
            System.out.println("Exp eval start....");
            // System.out.println( );
            for (var stmt : stmts) {
                stmt.accept(this);
            }

        } catch (ExprEvaluationError error) {
        }

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

    @Override
    public Void taskOnExpressionStmt(Stmt.Expression stmt) {
        interpret(stmt.expression);
        return null;
    }

    @Override
    public Void taskOnPrintStmt(Stmt.Print stmt) {
        System.out.println(interpret(stmt.expression));
        return null;
    }

    public Void taskOnVarDeclStmt(Stmt.VarDecl stmt) {
        System.out.print("var ");
        System.out.print(stmt.varId.lexeme);
        if (stmt.expression != null) {
            Object exprVal = interpret(stmt.expression);
            System.out.print(" = ");
            System.out.println(exprVal);

            // store this value to the symtab entry
            SymbolTable.putSymTabEntry(stmt.varId.lexeme, exprVal);
        }
        else{
            // store default value 0 to the symtab entry
            SymbolTable.putSymTabEntry(stmt.varId.lexeme, 0);
        }
        return null;
    }

    public Object taskOnBinaryExpr(Expr.Binary expr) {
        Object left = expr.left.accept(this);
        Object right = expr.right.accept(this);

        // System.out.println("left "+ left);
        // System.out.println("right "+right);

        // can I consistently process and keep as Double/String after fetching value???
        // if(left instanceof Token){
        // String lhsId = ((Token)left).lexeme;
        // left = SymbolTable.getSymTabEntry(lhsId);
        // }
        // if(right instanceof Token){
        // String rhsId = ((Token)right).lexeme;
        // right = SymbolTable.getSymTabEntry(rhsId);
        // }

        switch (expr.operator.type) {
            case NOT_EQUAL:
                return !isEqual(left, right);
            case EQUAL_EQUAL:
                return isEqual(left, right);
            case GREATER:
                checkNumberOperands(expr.operator, left, right);
                return (double) left > (double) right;
            case GREATER_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left >= (double) right;
            case LESS:
                checkNumberOperands(expr.operator, left, right);
                return (double) left < (double) right;
            case LESS_EQUAL:
                checkNumberOperands(expr.operator, left, right);
                return (double) left <= (double) right;
            case MINUS: {
                checkNumberOperands(expr.operator, left, right);
                return (double) left - (double) right;
            }
            case PLUS:
                if (left instanceof Double && right instanceof Double) {
                    return (double) left + (double) right;
                }

                if (left instanceof String && right instanceof String) {
                    return (String) left + (String) right;
                }
                throw error(expr.operator,
                        "Operands must be two numbers or two strings.");
            case SLASH:
                return (double) left / (double) right;
            case STAR:
                return (double) left * (double) right;
        }
        return 100;// will it even reach here?? (yes if it matches none of the above)

    }

    public Object taskOnUnaryExpr(Expr.Unary expr) {
        Object unaryObj = expr.right.accept(this);

        // if(unaryObj instanceof Token){
        // String unaryId = ((Token)unaryObj).lexeme;
        // unaryObj = SymbolTable.getSymTabEntry(unaryId);
        // }

        switch (expr.operator.type) {
            case NOT:
                return !isTruthy(unaryObj);
            case MINUS:
                checkNumberOperand(expr.operator, unaryObj);
                return -(double) unaryObj;
        }
        return unaryObj;
    }

    public Object taskOnLiteralExpr(Expr.Literal expr) {

        Object val = expr.value;

        // Probably just transforming the ifentifier to its value here at this point,
        // might work
        if (val instanceof Token) {
            String valId = ((Token) val).lexeme;
            val = SymbolTable.getSymTabEntry(valId);
        }
        System.out.println("In rtaskOnLiteral " + val);

        return val;

    }

    public Object taskOnGroupingExpr(Expr.Grouping expr) {
        Object exp = expr.expression.accept(this);
        return exp;
    }

    public Object taskOnAssignExpr(Expr.Assign expr) {
        System.out.println("ASSIGN KA KYA KRNA HAI?");
         Object rhsExprVal = interpret(expr.rhsExpr);
            // store this value to the symtab entry only if it was defined
            if(SymbolTable.containsSymbol(expr.leftId.lexeme))
            {
                        SymbolTable.putSymTabEntry(expr.leftId.lexeme, rhsExprVal);

            }
            else{
                //throw undefined variable error
                throw error(new Token(EQUAL,"equals","equals",-1) ,"Undefined variable");
            }
        return rhsExprVal; 
    }
}
