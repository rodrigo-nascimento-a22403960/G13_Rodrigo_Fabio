import java.util.ArrayList;

public class Calculadora {

    public static double adicionar(ArrayList<Double> a) {
        double sum = 0;
        for (double v : a) sum += v;
        return sum;
    }

    public static double subtracao(ArrayList<Double> a) {
        double sum = a.get(0);
        for (int i = 1; i < a.size(); i++) sum -= a.get(i);
        return sum;
    }

    public static double multiplicar(ArrayList<Double> a) {
        double sum = 1;
        for (double v : a) sum *= v;
        return sum;
    }

    public static double dividir(ArrayList<Double> a) {
        double sum = a.get(0);
        for (int i = 1; i < a.size(); i++) sum /= a.get(i);
        return sum;
    }

    public static double potencia(ArrayList<Double> a) {
        return Math.pow(a.get(0), a.get(1));
    }

    public static double calcular(String expr) {
        if (expr == null) throw new IllegalArgumentException("Expressão nula");
        expr = expr.replace(" ", "");
        ArrayList<Double> nums = new ArrayList<>();
        ArrayList<Character> ops = new ArrayList<>();
        String tmp = "";
        char[] chars = expr.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isDigit(c) || c == '.' || (c == '-' && (i == 0 || "+-*/^".indexOf(chars[i - 1]) != -1))) {
                tmp += c;
            } else if ("+-*/^".indexOf(c) != -1) {
                if (tmp.isEmpty()) throw new IllegalArgumentException("Erro de sintaxe");
                nums.add(Double.parseDouble(tmp));
                tmp = "";
                ops.add(c);
            } else {
                throw new IllegalArgumentException("Operação desconhecida: " + c);
            }
        }

        if (tmp.isEmpty()) throw new IllegalArgumentException("Erro de sintaxe");
        nums.add(Double.parseDouble(tmp));

        for (int i = 0; i < ops.size(); i++) {
            char op = ops.get(i);
            if (op == '*' || op == '/' || op == '^') {
                ArrayList<Double> v = new ArrayList<>();
                v.add(nums.get(i));
                v.add(nums.get(i + 1));
                double r;
                if (op == '*') r = multiplicar(v);
                else if (op == '/') r = dividir(v);
                else r = potencia(v);
                nums.set(i, r);
                nums.remove(i + 1);
                ops.remove(i);
                i--;
            }
        }

        while (!ops.isEmpty()) {
            ArrayList<Double> v = new ArrayList<>();
            v.add(nums.get(0));
            v.add(nums.get(1));
            double r;
            if (ops.get(0) == '+') r = adicionar(v);
            else r = subtracao(v);
            nums.set(0, r);
            nums.remove(1);
            ops.remove(0);
        }

        return nums.get(0);
    }
}
