import java.util.Scanner;

public class LLMCalculatorGame {

    String askUserForExpression() {
        System.out.println("Escreva a expressão a calcular (+ - * / ^):");
        Scanner sc = new Scanner(System.in);
        String expr = sc.nextLine();

        while (expr == null || expr.trim().isEmpty()) {
            System.out.println("Expressão vazia. Tente novamente:");
            expr = sc.nextLine();
        }

        return expr.trim();
    }

    public void execute() {
        String expr = askUserForExpression();

        try {
            double resultado = Calculadora.calcular(expr);
            System.out.println("Resultado: " + resultado);
        } catch (Exception e) {
            System.out.println("Erro no cálculo: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LLMCalculatorGame app = new LLMCalculatorGame();
        app.execute();
    }
}
