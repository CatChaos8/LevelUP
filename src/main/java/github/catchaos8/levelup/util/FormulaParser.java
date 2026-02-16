package github.catchaos8.levelup.util;

public class FormulaParser {

    private String expression;
    private int pos;

    private FormulaParser(String expression) {
        // Remove all whitespace
        this.expression = expression.replaceAll("\\s+", "");
        this.pos = 0;
    }

    public static double evaluate(String formula, double x) {
        try {
            // Replace variable x with its value
            String prepared = formula.replaceAll("\\s+", "")
                    .replace("x", "(" + x + ")");
            return new FormulaParser(prepared).parseExpression();
        } catch (Exception e) {
            // Fallback
            return 0.5 * Math.pow(2, x) + 25;
        }
    }

    // Handles + and -
    private double parseExpression() {
        double result = parseTerm();
        while (pos < expression.length()) {
            char op = expression.charAt(pos);
            if (op == '+') {
                pos++;
                result += parseTerm();
            } else if (op == '-') {
                pos++;
                result -= parseTerm();
            } else {
                break;
            }
        }
        return result;
    }

    // Handles * and /
    private double parseTerm() {
        double result = parsePower();
        while (pos < expression.length()) {
            char op = expression.charAt(pos);
            if (op == '*') {
                pos++;
                result *= parsePower();
            } else if (op == '/') {
                pos++;
                result /= parsePower();
            } else {
                break;
            }
        }
        return result;
    }

    // Handles ^ (right associative)
    private double parsePower() {
        double base = parseFactor();
        if (pos < expression.length() && expression.charAt(pos) == '^') {
            pos++;
            double exponent = parsePower(); // right associative
            return Math.pow(base, exponent);
        }
        return base;
    }

    // Handles numbers, parentheses, unary minus
    private double parseFactor() {
        char c = expression.charAt(pos);

        // Unary minus
        if (c == '-') {
            pos++;
            return -parseFactor();
        }

        // Parentheses
        if (c == '(') {
            pos++; // skip '('
            double result = parseExpression();
            pos++; // skip ')'
            return result;
        }

        // Number
        int start = pos;
        while (pos < expression.length() &&
                (Character.isDigit(expression.charAt(pos)) || expression.charAt(pos) == '.')) {
            pos++;
        }
        return Double.parseDouble(expression.substring(start, pos));
    }
}