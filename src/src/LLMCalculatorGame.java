import java.util.Scanner;

public class LLMCalculatorGame {

    private final LLMInteractionEngine engine;

    public LLMCalculatorGame(LLMInteractionEngine engine) {
        this.engine = engine;
    }

    String askUserInput() {
        System.out.println("Escreva uma expressão ou uma pergunta de matemática:");
        Scanner sc = new Scanner(System.in);
        String input = sc.nextLine();

        while (input == null || input.trim().isEmpty()) {
            System.out.println("Entrada vazia. Tente novamente:");
            input = sc.nextLine();
        }

        return input.trim();
    }

    boolean isPureExpression(String s) {
        return s.matches("[0-9\\.\\-\\+\\*\\/\\^\\s]+");
    }

    public void execute() {
        String input = askUserInput();

        // CASO 1: expressão pura → calculadora local
        if (isPureExpression(input)) {
            try {
                double r = Calculadora.calcular(input);
                System.out.println("Resultado: " + r);
            } catch (Exception e) {
                System.out.println("Erro no cálculo: " + e.getMessage());
            }
            return;
        }

        // CASO 2: pergunta → LLM
        try {
            System.out.println("<O LLM está a pensar...>");

            String prompt =
                    "Responde apenas com o resultado numérico. "
                            + "Pergunta de matemática: " + input.replace("\"", "");

            String json = engine.sendPrompt(prompt);

            String resposta = JSONUtils.getJsonString(json, "text");
            if (resposta == null || resposta.isEmpty()) {
                resposta = JSONUtils.getJsonString(json, "content");
            }

            if (resposta == null) {
                System.out.println("Erro: resposta inválida do LLM");
            } else {
                System.out.println("Resposta do LLM:");
                System.out.println(resposta.trim());
            }

        } catch (Exception e) {
            System.out.println("Erro ao comunicar com o LLM: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        LLMInteractionEngine engine = new LLMInteractionEngine();
        LLMCalculatorGame app = new LLMCalculatorGame(engine);
        app.execute();
    }
}
