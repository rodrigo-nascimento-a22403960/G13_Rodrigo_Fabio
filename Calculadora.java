import java.util.Scanner;
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

    public static double calcular(String expr) {
        expr = expr.replace(" ", "");

        ArrayList<Double> numeros = new ArrayList<>();
        ArrayList<Character> ops = new ArrayList<>();

        StringBuilder temp = new StringBuilder();
        char[] chars = expr.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            if (Character.isDigit(c) || c == '.' || (c == '-' && (i == 0 || "+-*/".indexOf(chars[i - 1]) != -1))) {
                temp.append(c);
            } else {
                if (temp.length() == 0) return Double.NaN;
                numeros.add(Double.parseDouble(temp.toString()));
                temp.setLength(0);
                ops.add(c);
            }
        }
        if (temp.length() > 0) numeros.add(Double.parseDouble(temp.toString()));

        for (int i = 0; i < ops.size(); i++) {
            char op = ops.get(i);
            if (op == '*' || op == '/') {
                ArrayList<Double> a = new ArrayList<>();
                a.add(numeros.get(i));
                a.add(numeros.get(i + 1));
                double r = (op == '*') ? multiplicar(a) : dividir(a);
                numeros.set(i, r);
                numeros.remove(i + 1);
                ops.remove(i);
                i--;
            }
        }

        while (!ops.isEmpty()) {
            ArrayList<Double> a = new ArrayList<>();
            a.add(numeros.get(0));
            a.add(numeros.get(1));
            double r = (ops.get(0) == '+') ? adicionar(a) : subtracao(a);
            numeros.set(0, r);
            numeros.remove(1);
            ops.remove(0);
        }

        return numeros.isEmpty() ? Double.NaN : numeros.get(0);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        while (sc.hasNextLine()) {
            String entrada = sc.nextLine();
            if (entrada == null || entrada.trim().isEmpty()) continue;
            double resultado = calcular(entrada);
            if (Double.isNaN(resultado)) {
                System.out.println("Expressão inválida");
            } else {
                if (resultado == (long) resultado) System.out.println((long) resultado);
                else System.out.println(resultado);
            }
        }
        sc.close();
    }
}
