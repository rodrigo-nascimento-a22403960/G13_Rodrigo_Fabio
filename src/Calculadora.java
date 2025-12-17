package src;

import java.util.ArrayList;
import java.util.Scanner;

public class Calculadora {

    public static double calcular(String expr) {
        expr = expr.replace(" ", "");

        ArrayList<Double> nums = new ArrayList<>();
        ArrayList<Character> ops = new ArrayList<>();

        String tmp = "";
        char[] chars = expr.toCharArray();

        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];

            if (Character.isDigit(c) || c == '.' ||
                    (c == '-' && (i == 0 || "+-*/^".indexOf(chars[i - 1]) != -1))) {
                tmp += c;
            } else if ("+-*/^".indexOf(c) != -1) {
                if (tmp.isEmpty()) {
                    throw new IllegalArgumentException("Erro de sintaxe");
                }
                nums.add(Double.parseDouble(tmp));
                tmp = "";
                ops.add(c);
            } else {
                throw new IllegalArgumentException("Operação desconhecida: " + c);
            }
        }

        if (tmp.isEmpty()) {
            throw new IllegalArgumentException("Erro de sintaxe");
        }

        nums.add(Double.parseDouble(tmp));

        for (int i = 0; i < ops.size(); i++) {
            char op = ops.get(i);

            if (op == '*' || op == '/' || op == '^') {
                ArrayList<Double> v = new ArrayList<>();
                v.add(nums.get(i));
                v.add(nums.get(i + 1));

                Operacao o;
                if (op == '*') o = new Multiplicacao();
                else if (op == '/') o = new Divisao();
                else o = new Potencia();

                double r = o.executar(v);

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

            Operacao o;
            if (ops.get(0) == '+') o = new Adicao();
            else o = new Subtracao();

            double r = o.executar(v);

            nums.set(0, r);
            nums.remove(1);
            ops.remove(0);
        }

        return nums.get(0);
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.println("Calculadora simples");
        System.out.println("Operações possíveis:");
        System.out.println("+  adição");
        System.out.println("-  subtração");
        System.out.println("*  multiplicação");
        System.out.println("/  divisão");
        System.out.println("^  potência");
        System.out.println("Exemplo: 2 + 3 * 4 ^ 2");
        System.out.println("Digite uma expressão por linha:");

        while (sc.hasNextLine()) {
            String e = sc.nextLine();
            if (e.trim().isEmpty()) continue;

            try {
                System.out.println(calcular(e));
            } catch (Exception ex) {
                System.out.println("Erro: " + ex.getMessage());
            }
        }

        sc.close();
    }
}
