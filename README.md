| Tema | Calculadora |
| ------------- | ------ |
| **Grupo** | Rodrigo (a22403960) e Fábio (a22407696) |
| **Funcionalidade baseada em LLM** | O utilizador escreve pedidos de cálculo em linguagem natural (por exemplo: “faz a soma de 1+1”) e o LLM devolve o resultado do cálculo. |
| **Prompts esperadas para a func** | “Faz a soma de 1+1” / “Faz a multiplicação de 5*3” / “Calcula quanto é (10 - 4) + 6” |
| **Classes esperadas e sua responsabilidade** | **`Calculadora`** – Executa operações matemáticas básicas. <br> **`Historico`** – Regista e apresenta o histórico de cálculos. <br> **`InterfaceUtilizador`** – Gere o menu, input e output. <br> **`LLMExplicador`** – Envia o pedido ao LLM e recebe o resultado. <br> **`Main`** – Classe principal que inicia e coordena o programa. |
