| Tema | Calculadora |
| ------------- | ------ |
| **Grupo** | Rodrigo (a22403960) e Fábio (a22407696) |
| **Funcionalidade baseada em LLMInteractionEngine** | O utilizador pede operações matemáticas simples em linguagem natural (ex.: “faz a soma de 1+1”) e o LLMInteractionEngine interpreta e devolve o resultado correto. |
| **Prompts esperadas para a func** | “Faz a soma de 1+1” / “Faz a subtração de 10-4” / “Faz a multiplicação de 3*5” / “Faz a divisão de 20/4” |
| **Classes esperadas e sua responsabilidade** | **`Soma`** – Executa a operação de soma entre dois números. <br> **`Subtracao`** – Executa a operação de subtração entre dois números. <br> **`Multiplicacao`** – Executa a operação de multiplicação entre dois números. <br> **`Divisao`** – Executa a operação de divisão entre dois números, validando divisão por zero. <br> **`Menu`** – Mostra o menu ao utilizador, recolhe os dados e invoca as classes de operação. |
