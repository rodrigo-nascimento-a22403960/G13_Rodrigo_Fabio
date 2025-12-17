# Mini-Projeto LP2 - Calculadora LLM

## Tabela de Resumo

| Campo | Descrição |
|-------|-----------|
| **Tema** | 1 - Calculadora Avançada |
| **Grupo** | Rodrigo Nascimento (a22403960) e Fabio Farhat (a22407696) |
| **Funcionalidade baseada em LLM** | O LLM responde a perguntas matemáticas em linguagem natural (ex: "Quanto é 15% de 200?", "Resolve x² - 5x + 6 = 0"). Expressões puras são calculadas localmente pela classe Calculadora. |
| **Prompts esperadas** | "És um assistente de matemática. Responde de forma clara em português. Pergunta: {input}" |
| **Prompts usadas** | "És um assistente de matemática. Responde de forma clara em português. Pergunta: {input}" |
| **Classes esperadas e responsabilidades** | **Calculadora** - Avalia expressões matemáticas (+, -, *, /, ^). **CalculadoraGUI** - Interface gráfica moderna com Swing. **LLMInteractionEngine** - Comunicação HTTP com a API do LLM. **LLMCalculatorGame** - Controlo que decide se usa calculadora local ou LLM. **Operacao** - Classe abstrata base para operações. **Adicao** - Operação de soma. **Subtracao** - Operação de subtração. **Multiplicacao** - Operação de multiplicação. **Divisao** - Operação de divisão. **Potencia** - Operação de potência. **Quadrado** - Elevar ao quadrado. **RaizQuadrada** - Raiz quadrada. **Utils** - Leitura de input. |
