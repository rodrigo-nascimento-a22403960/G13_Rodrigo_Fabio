import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.basic.BasicScrollBarUI;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

/**
 * Interface gráfica profissional da Calculadora LLM.
 * Design moderno com tema escuro, animações e integração LLM.
 *
 * @author Projeto LP2
 * @version 2.0
 */
public class CalculadoraGUI extends JFrame {

    // ==================== TEMA E CORES ====================

    // Paleta de cores principal
    private static final Color BACKGROUND_DARK = new Color(13, 17, 23);
    private static final Color BACKGROUND_CARD = new Color(22, 27, 34);
    private static final Color BACKGROUND_ELEVATED = new Color(33, 38, 45);
    private static final Color BACKGROUND_HOVER = new Color(48, 54, 61);

    // Cores de destaque
    private static final Color ACCENT_PRIMARY = new Color(88, 166, 255);
    private static final Color ACCENT_SECONDARY = new Color(165, 120, 255);
    private static final Color ACCENT_SUCCESS = new Color(63, 185, 80);

    // Cores de texto
    private static final Color TEXT_PRIMARY = new Color(240, 246, 252);
    private static final Color TEXT_SECONDARY = new Color(139, 148, 158);
    private static final Color TEXT_MUTED = new Color(110, 118, 129);

    // Gradientes
    private static final Color GRADIENT_ORANGE_START = new Color(255, 149, 0);
    private static final Color GRADIENT_ORANGE_END = new Color(255, 94, 58);
    private static final Color GRADIENT_BLUE_START = new Color(0, 212, 255);
    private static final Color GRADIENT_BLUE_END = new Color(88, 166, 255);
    private static final Color GRADIENT_PURPLE_START = new Color(165, 120, 255);
    private static final Color GRADIENT_PURPLE_END = new Color(255, 120, 200);

    // Dimensões
    private static final int BORDER_RADIUS = 16;
    private static final int PADDING_LARGE = 24;
    private static final int PADDING_MEDIUM = 16;
    private static final int BUTTON_HEIGHT = 64;

    // ==================== COMPONENTES ====================

    private ModernDisplay display;
    private JPanel mainPanel;
    private JPanel calculatorPanel;
    private LLMChatPanel chatPanel;
    private CardLayout cardLayout;
    private String historico = "";
    private LLMInteractionEngine engine;

    // ==================== CONSTRUTOR ====================

    public CalculadoraGUI() {
        initEngine();
        setupFrame();
        createMainPanel();
        addKeyboardSupport();
        setVisible(true);
    }

    private void initEngine() {
        try {
            engine = new LLMInteractionEngine();
        } catch (Exception e) {
            System.err.println("Aviso: LLM engine não inicializado: " + e.getMessage());
        }
    }

    private void setupFrame() {
        setTitle("Calculadora LLM Pro");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(BACKGROUND_DARK);
        setIconImage(createAppIcon());
    }

    private Image createAppIcon() {
        int size = 64;
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        applyRenderingHints(g2);

        GradientPaint gradient = new GradientPaint(0, 0, GRADIENT_PURPLE_START, size, size, GRADIENT_BLUE_END);
        g2.setPaint(gradient);
        g2.fillRoundRect(0, 0, size, size, 16, 16);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 36));
        FontMetrics fm = g2.getFontMetrics();
        String text = "=";
        g2.drawString(text, (size - fm.stringWidth(text)) / 2, (size + fm.getAscent()) / 2 - 4);
        g2.dispose();
        return img;
    }

    private void createMainPanel() {
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        mainPanel.setBackground(BACKGROUND_DARK);

        calculatorPanel = createCalculatorPanel();
        chatPanel = new LLMChatPanel(engine, () -> cardLayout.show(mainPanel, "calculator"));

        mainPanel.add(calculatorPanel, "calculator");
        mainPanel.add(chatPanel, "chat");

        add(mainPanel);
        pack();
        setLocationRelativeTo(null);
    }

    private JPanel createCalculatorPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 12));
        panel.setBackground(BACKGROUND_DARK);
        panel.setBorder(new EmptyBorder(20, 20, 20, 20));

        panel.add(createHeader(), BorderLayout.NORTH);
        display = new ModernDisplay();
        panel.add(display, BorderLayout.CENTER);
        panel.add(createButtonsPanel(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel title = new JLabel("⚡ Calculadora Pro");
        title.setFont(createFont("SF Pro Display", Font.BOLD, 22));
        title.setForeground(TEXT_PRIMARY);

        JButton llmButton = createLLMButton();

        header.add(title, BorderLayout.WEST);
        header.add(llmButton, BorderLayout.EAST);

        return header;
    }

    private JButton createLLMButton() {
        JButton btn = new JButton("🤖 Chat LLM") {
            private float hoverProgress = 0;
            private Timer timer = new Timer(16, e -> repaint());
            {
                addMouseListener(new MouseAdapter() {
                    public void mouseEntered(MouseEvent e) { timer.start(); }
                    public void mouseExited(MouseEvent e) { hoverProgress = 0; repaint(); }
                });
            }

            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                applyRenderingHints(g2);

                if (getMousePosition() != null && hoverProgress < 1) hoverProgress = Math.min(1, hoverProgress + 0.1f);

                Color start = blendColors(GRADIENT_PURPLE_START, Color.WHITE, hoverProgress * 0.2f);
                Color end = blendColors(GRADIENT_PURPLE_END, Color.WHITE, hoverProgress * 0.15f);

                g2.setPaint(new GradientPaint(0, 0, start, getWidth(), 0, end));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                        (getHeight() + fm.getAscent()) / 2 - 3);
                g2.dispose();
            }
        };
        btn.setFont(createFont("SF Pro Text", Font.BOLD, 14));
        btn.setPreferredSize(new Dimension(130, 40));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> cardLayout.show(mainPanel, "chat"));
        return btn;
    }

    private JPanel createButtonsPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setOpaque(false);
        panel.setBorder(new EmptyBorder(12, 0, 0, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.weightx = 1;
        gbc.weighty = 1;

        // Linha 0
        addButton(panel, gbc, 0, 0, "C", ButtonStyle.FUNCTION, this::clear);
        addButton(panel, gbc, 1, 0, "⌫", ButtonStyle.FUNCTION, this::backspace);
        addButton(panel, gbc, 2, 0, "√", ButtonStyle.FUNCTION, this::squareRoot);
        addButton(panel, gbc, 3, 0, "÷", ButtonStyle.OPERATION, () -> appendOperator("/"));

        // Linha 1
        addButton(panel, gbc, 0, 1, "7", ButtonStyle.NUMBER, () -> appendDigit("7"));
        addButton(panel, gbc, 1, 1, "8", ButtonStyle.NUMBER, () -> appendDigit("8"));
        addButton(panel, gbc, 2, 1, "9", ButtonStyle.NUMBER, () -> appendDigit("9"));
        addButton(panel, gbc, 3, 1, "×", ButtonStyle.OPERATION, () -> appendOperator("*"));

        // Linha 2
        addButton(panel, gbc, 0, 2, "4", ButtonStyle.NUMBER, () -> appendDigit("4"));
        addButton(panel, gbc, 1, 2, "5", ButtonStyle.NUMBER, () -> appendDigit("5"));
        addButton(panel, gbc, 2, 2, "6", ButtonStyle.NUMBER, () -> appendDigit("6"));
        addButton(panel, gbc, 3, 2, "−", ButtonStyle.OPERATION, () -> appendOperator("-"));

        // Linha 3
        addButton(panel, gbc, 0, 3, "1", ButtonStyle.NUMBER, () -> appendDigit("1"));
        addButton(panel, gbc, 1, 3, "2", ButtonStyle.NUMBER, () -> appendDigit("2"));
        addButton(panel, gbc, 2, 3, "3", ButtonStyle.NUMBER, () -> appendDigit("3"));
        addButton(panel, gbc, 3, 3, "+", ButtonStyle.OPERATION, () -> appendOperator("+"));

        // Linha 4
        addButton(panel, gbc, 0, 4, "x²", ButtonStyle.FUNCTION, this::square);
        addButton(panel, gbc, 1, 4, "0", ButtonStyle.NUMBER, () -> appendDigit("0"));
        addButton(panel, gbc, 2, 4, ".", ButtonStyle.NUMBER, () -> appendDigit("."));
        addButton(panel, gbc, 3, 4, "=", ButtonStyle.SPECIAL, this::calculate);

        // Linha 5
        addButton(panel, gbc, 0, 5, "^", ButtonStyle.FUNCTION, () -> appendOperator("^"));
        addButton(panel, gbc, 1, 5, "(", ButtonStyle.FUNCTION, () -> appendDigit("("));
        addButton(panel, gbc, 2, 5, ")", ButtonStyle.FUNCTION, () -> appendDigit(")"));
        addButton(panel, gbc, 3, 5, "±", ButtonStyle.FUNCTION, this::toggleSign);

        return panel;
    }

    private void addButton(JPanel panel, GridBagConstraints gbc, int x, int y,
                           String text, ButtonStyle style, Runnable action) {
        gbc.gridx = x;
        gbc.gridy = y;
        ModernButton button = new ModernButton(text, style);
        button.addActionListener(e -> action.run());
        panel.add(button, gbc);
    }

    private void addKeyboardSupport() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED && calculatorPanel.isShowing()) {
                char c = e.getKeyChar();
                int code = e.getKeyCode();

                if (Character.isDigit(c)) appendDigit(String.valueOf(c));
                else if (c == '.') appendDigit(".");
                else if (c == '+') appendOperator("+");
                else if (c == '-') appendOperator("-");
                else if (c == '*') appendOperator("*");
                else if (c == '/') appendOperator("/");
                else if (c == '^') appendOperator("^");
                else if (c == '(' || c == ')') appendDigit(String.valueOf(c));
                else if (code == KeyEvent.VK_ENTER) calculate();
                else if (code == KeyEvent.VK_BACK_SPACE) backspace();
                else if (code == KeyEvent.VK_ESCAPE) clear();
                else if (code == KeyEvent.VK_DELETE) clear();
            }
            return false;
        });
    }

    // ==================== AÇÕES ====================

    private void appendDigit(String digit) { display.appendDigit(digit); }
    private void appendOperator(String op) { display.appendOperator(op); }
    private void clear() { display.clear(); }
    private void backspace() { display.backspace(); }

    private void calculate() {
        String expr = display.getExpressao();
        if (expr.isEmpty()) return;

        try {
            String calcExpr = expr.replace("×", "*").replace("÷", "/").replace("−", "-");
            double resultado = Calculadora.calcular(calcExpr);
            String resultStr = formatResult(resultado);
            historico += expr + " = " + resultStr + "\n";
            display.setHistorico(historico);
            display.setResultado(resultStr);
        } catch (Exception e) {
            display.setResultado("Erro");
        }
    }

    private void squareRoot() {
        try {
            double valor = Double.parseDouble(display.getExpressao());
            if (valor >= 0) {
                String resultStr = formatResult(Math.sqrt(valor));
                historico += "√" + display.getExpressao() + " = " + resultStr + "\n";
                display.setHistorico(historico);
                display.setResultado(resultStr);
            } else display.setResultado("Erro");
        } catch (Exception e) { display.setResultado("Erro"); }
    }

    private void square() {
        try {
            double valor = Double.parseDouble(display.getExpressao());
            String resultStr = formatResult(valor * valor);
            historico += display.getExpressao() + "² = " + resultStr + "\n";
            display.setHistorico(historico);
            display.setResultado(resultStr);
        } catch (Exception e) { display.setResultado("Erro"); }
    }

    private void toggleSign() {
        String expr = display.getExpressao();
        if (expr.isEmpty() || expr.equals("0")) return;
        display.setExpressao(expr.startsWith("-") ? expr.substring(1) : "-" + expr);
    }

    private String formatResult(double valor) {
        if (valor == (long) valor) return String.valueOf((long) valor);
        return String.format("%.10f", valor).replaceAll("0+$", "").replaceAll("\\.$", "");
    }

    // ==================== UTILITÁRIOS ====================

    private static void applyRenderingHints(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }

    private static Font createFont(String name, int style, int size) {
        Font font = new Font(name, style, size);
        if (!font.getFamily().equals(name)) {
            for (String fallback : new String[]{"Segoe UI", "Helvetica Neue", "Arial"}) {
                font = new Font(fallback, style, size);
                if (font.getFamily().equals(fallback)) break;
            }
        }
        return font;
    }

    private static Color blendColors(Color c1, Color c2, float ratio) {
        float ir = 1.0f - ratio;
        return new Color(
                Math.min(255, (int)(c1.getRed() * ir + c2.getRed() * ratio)),
                Math.min(255, (int)(c1.getGreen() * ir + c2.getGreen() * ratio)),
                Math.min(255, (int)(c1.getBlue() * ir + c2.getBlue() * ratio))
        );
    }

    // ==================== ENUM BUTTON STYLE ====================

    private enum ButtonStyle { NUMBER, OPERATION, FUNCTION, SPECIAL, LLM }

    // ==================== CLASSE MODERN BUTTON ====================

    private class ModernButton extends JButton {
        private ButtonStyle style;
        private float hoverProgress = 0f, pressProgress = 0f;
        private boolean isHovered = false, isPressed = false;
        private Color baseColor, hoverColor, gradientStart, gradientEnd;
        private boolean hasGradient;
        private Timer hoverTimer, pressTimer;

        public ModernButton(String text, ButtonStyle style) {
            super(text);
            this.style = style;
            setupStyle();
            setupButton();
            setupAnimations();
        }

        private void setupStyle() {
            switch (style) {
                case NUMBER:
                    baseColor = BACKGROUND_ELEVATED;
                    hoverColor = BACKGROUND_HOVER;
                    hasGradient = false;
                    setForeground(TEXT_PRIMARY);
                    break;
                case OPERATION:
                    gradientStart = GRADIENT_ORANGE_START;
                    gradientEnd = GRADIENT_ORANGE_END;
                    hasGradient = true;
                    setForeground(Color.WHITE);
                    break;
                case FUNCTION:
                    baseColor = new Color(55, 60, 68);
                    hoverColor = new Color(70, 75, 83);
                    hasGradient = false;
                    setForeground(TEXT_PRIMARY);
                    break;
                case SPECIAL:
                    gradientStart = GRADIENT_BLUE_START;
                    gradientEnd = GRADIENT_BLUE_END;
                    hasGradient = true;
                    setForeground(Color.WHITE);
                    break;
                case LLM:
                    gradientStart = GRADIENT_PURPLE_START;
                    gradientEnd = GRADIENT_PURPLE_END;
                    hasGradient = true;
                    setForeground(Color.WHITE);
                    break;
            }
        }

        private void setupButton() {
            setFont(createFont("SF Pro Text", Font.BOLD, 22));
            setFocusPainted(false);
            setBorderPainted(false);
            setContentAreaFilled(false);
            setOpaque(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setPreferredSize(new Dimension(BUTTON_HEIGHT, BUTTON_HEIGHT));
        }

        private void setupAnimations() {
            hoverTimer = new Timer(16, e -> {
                if (isHovered && hoverProgress < 1f) hoverProgress = Math.min(1f, hoverProgress + 0.15f);
                else if (!isHovered && hoverProgress > 0f) hoverProgress = Math.max(0f, hoverProgress - 0.15f);
                else ((Timer) e.getSource()).stop();
                repaint();
            });

            pressTimer = new Timer(16, e -> {
                if (isPressed && pressProgress < 1f) pressProgress = Math.min(1f, pressProgress + 0.3f);
                else if (!isPressed && pressProgress > 0f) pressProgress = Math.max(0f, pressProgress - 0.2f);
                else ((Timer) e.getSource()).stop();
                repaint();
            });

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { isHovered = true; if (!hoverTimer.isRunning()) hoverTimer.start(); }
                public void mouseExited(MouseEvent e) { isHovered = false; if (!hoverTimer.isRunning()) hoverTimer.start(); }
                public void mousePressed(MouseEvent e) { isPressed = true; if (!pressTimer.isRunning()) pressTimer.start(); }
                public void mouseReleased(MouseEvent e) { isPressed = false; if (!pressTimer.isRunning()) pressTimer.start(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            applyRenderingHints(g2);

            int w = getWidth(), h = getHeight();
            double scale = 1.0 - (pressProgress * 0.05);
            int sw = (int)(w * scale), sh = (int)(h * scale);
            int ox = (w - sw) / 2, oy = (h - sh) / 2;

            // Sombra glow para botões com gradiente
            if (hasGradient && hoverProgress > 0) {
                g2.setColor(new Color(gradientStart.getRed(), gradientStart.getGreen(),
                        gradientStart.getBlue(), (int)(40 * hoverProgress)));
                g2.fillRoundRect(ox - 2, oy + 4, sw + 4, sh, BORDER_RADIUS + 2, BORDER_RADIUS + 2);
            }

            // Fundo
            if (hasGradient) {
                Color start = blendColors(gradientStart, Color.WHITE, hoverProgress * 0.15f);
                Color end = blendColors(gradientEnd, Color.WHITE, hoverProgress * 0.1f);
                g2.setPaint(new GradientPaint(ox, oy, start, ox, oy + sh, end));
            } else {
                g2.setColor(blendColors(baseColor, hoverColor, hoverProgress));
            }
            g2.fillRoundRect(ox, oy, sw, sh, BORDER_RADIUS, BORDER_RADIUS);

            // Brilho no topo
            if (hasGradient) {
                g2.setColor(new Color(255, 255, 255, 30));
                g2.fillRoundRect(ox + 2, oy + 2, sw - 4, sh / 3, BORDER_RADIUS - 2, BORDER_RADIUS - 2);
            }

            // Texto
            g2.setFont(getFont());
            g2.setColor(getForeground());
            FontMetrics fm = g2.getFontMetrics();
            String text = getText();
            int tx = ox + (sw - fm.stringWidth(text)) / 2;
            int ty = oy + (sh - fm.getHeight()) / 2 + fm.getAscent();

            if (hasGradient) {
                g2.setColor(new Color(0, 0, 0, 50));
                g2.drawString(text, tx, ty + 1);
                g2.setColor(getForeground());
            }
            g2.drawString(text, tx, ty);
            g2.dispose();
        }
    }

    // ==================== CLASSE MODERN DISPLAY ====================

    private class ModernDisplay extends JPanel {
        private String expressao = "", resultado = "0", historico = "";
        private boolean showingResult = false;

        public ModernDisplay() {
            setOpaque(false);
            setPreferredSize(new Dimension(360, 180));
        }

        public void setExpressao(String e) { expressao = e; showingResult = false; repaint(); }
        public void setResultado(String r) { resultado = r; showingResult = true; repaint(); }
        public void setHistorico(String h) { historico = h; repaint(); }
        public String getExpressao() { return expressao; }

        public void clear() { expressao = ""; resultado = "0"; showingResult = false; repaint(); }

        public void appendDigit(String digit) {
            if (showingResult) { expressao = digit; showingResult = false; }
            else if (expressao.equals("0") && !digit.equals(".")) expressao = digit;
            else expressao += digit;
            repaint();
        }

        public void appendOperator(String op) {
            if (!expressao.isEmpty()) {
                char last = expressao.charAt(expressao.length() - 1);
                if ("+-*/^".indexOf(last) == -1) { expressao += op; showingResult = false; repaint(); }
            }
        }

        public void backspace() {
            if (!expressao.isEmpty() && !showingResult) {
                expressao = expressao.substring(0, expressao.length() - 1);
                repaint();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            applyRenderingHints(g2);

            int w = getWidth(), h = getHeight(), r = BORDER_RADIUS, p = PADDING_LARGE;

            // Fundo glassmorphism
            g2.setColor(new Color(30, 35, 42, 200));
            g2.fillRoundRect(0, 0, w, h, r, r);

            // Borda sutil
            g2.setColor(new Color(255, 255, 255, 15));
            g2.drawRoundRect(0, 0, w - 1, h - 1, r, r);

            // Brilho no topo
            g2.setPaint(new GradientPaint(0, 0, new Color(255, 255, 255, 20), 0, h / 3, new Color(255, 255, 255, 0)));
            g2.fillRoundRect(1, 1, w - 2, h / 3, r - 1, r - 1);

            // Histórico
            if (!historico.isEmpty()) {
                g2.setFont(createFont("SF Pro Text", Font.PLAIN, 12));
                g2.setColor(TEXT_MUTED);
                FontMetrics fm = g2.getFontMetrics();
                String[] linhas = historico.split("\n");
                int y = p;
                for (int i = Math.max(0, linhas.length - 2); i < linhas.length; i++) {
                    String linha = linhas[i];
                    if (linha.length() > 35) linha = "..." + linha.substring(linha.length() - 32);
                    g2.drawString(linha, p, y + fm.getAscent());
                    y += fm.getHeight() + 2;
                }
            }

            // Expressão (quando mostrar resultado)
            String displayExpr = expressao;
            if (!displayExpr.isEmpty() && showingResult) {
                g2.setFont(createFont("SF Pro Text", Font.PLAIN, 18));
                g2.setColor(TEXT_SECONDARY);
                FontMetrics fm = g2.getFontMetrics();
                while (fm.stringWidth(displayExpr) > w - 2 * p && displayExpr.length() > 1)
                    displayExpr = "..." + displayExpr.substring(4);
                g2.drawString(displayExpr, w - p - fm.stringWidth(displayExpr), h - p - 50);
            }

            // Resultado principal
            String displayText = showingResult ? resultado : (expressao.isEmpty() ? "0" : expressao);
            int fontSize = 48;
            if (displayText.length() > 10) fontSize = 36;
            if (displayText.length() > 14) fontSize = 28;
            if (displayText.length() > 18) fontSize = 22;

            g2.setFont(createFont("SF Pro Display", Font.BOLD, fontSize));
            FontMetrics fm = g2.getFontMetrics();
            while (fm.stringWidth(displayText) > w - 2 * p && displayText.length() > 1)
                displayText = displayText.substring(1);

            int tx = w - p - fm.stringWidth(displayText);
            int ty = h - p - 5;

            g2.setColor(new Color(0, 0, 0, 30));
            g2.drawString(displayText, tx + 1, ty + 2);
            g2.setColor(TEXT_PRIMARY);
            g2.drawString(displayText, tx, ty);

            g2.dispose();
        }
    }

    // ==================== CLASSE LLM CHAT PANEL ====================

    private class LLMChatPanel extends JPanel {
        private JPanel chatArea;
        private JScrollPane scrollPane;
        private JTextField inputField;
        private JButton sendButton;
        private ArrayList<String[]> messages = new ArrayList<>();
        private LLMInteractionEngine engine;
        private Runnable onBack;

        public LLMChatPanel(LLMInteractionEngine engine, Runnable onBack) {
            this.engine = engine;
            this.onBack = onBack;
            setLayout(new BorderLayout());
            setBackground(BACKGROUND_DARK);
            setBorder(new EmptyBorder(16, 16, 16, 16));

            createHeader();
            createChatArea();
            createInputArea();

            addBotMessage("Olá! 👋 Sou o assistente de matemática.\n\nPodes perguntar-me:\n• \"Quanto é a raiz de 144?\"\n• \"Resolve x² - 5x + 6 = 0\"\n• \"Quanto é 15% de 200?\"");
        }

        private void createHeader() {
            JPanel header = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    applyRenderingHints(g2);
                    g2.setPaint(new GradientPaint(0, 0, GRADIENT_PURPLE_START, getWidth(), 0, GRADIENT_PURPLE_END));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
                    g2.dispose();
                }
            };
            header.setOpaque(false);
            header.setPreferredSize(new Dimension(0, 60));
            header.setBorder(new EmptyBorder(12, 16, 12, 16));

            JButton back = new JButton("← Voltar");
            back.setFont(createFont("SF Pro Text", Font.BOLD, 14));
            back.setForeground(Color.WHITE);
            back.setContentAreaFilled(false);
            back.setBorderPainted(false);
            back.setFocusPainted(false);
            back.setCursor(new Cursor(Cursor.HAND_CURSOR));
            back.addActionListener(e -> onBack.run());

            JLabel title = new JLabel("🤖 Assistente LLM", SwingConstants.CENTER);
            title.setFont(createFont("SF Pro Display", Font.BOLD, 18));
            title.setForeground(Color.WHITE);

            JLabel status = new JLabel("● Online");
            status.setFont(createFont("SF Pro Text", Font.PLAIN, 12));
            status.setForeground(new Color(150, 255, 150));

            header.add(back, BorderLayout.WEST);
            header.add(title, BorderLayout.CENTER);
            header.add(status, BorderLayout.EAST);

            add(header, BorderLayout.NORTH);
        }

        private void createChatArea() {
            chatArea = new JPanel();
            chatArea.setLayout(new BoxLayout(chatArea, BoxLayout.Y_AXIS));
            chatArea.setBackground(BACKGROUND_DARK);
            chatArea.setBorder(new EmptyBorder(16, 0, 16, 0));

            scrollPane = new JScrollPane(chatArea);
            scrollPane.setBackground(BACKGROUND_DARK);
            scrollPane.setBorder(null);
            scrollPane.getViewport().setBackground(BACKGROUND_DARK);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            scrollPane.getVerticalScrollBar().setUnitIncrement(16);
            scrollPane.getVerticalScrollBar().setUI(new BasicScrollBarUI() {
                @Override protected void configureScrollBarColors() { thumbColor = BACKGROUND_HOVER; }
                @Override protected JButton createDecreaseButton(int o) { return createZeroButton(); }
                @Override protected JButton createIncreaseButton(int o) { return createZeroButton(); }
                private JButton createZeroButton() { JButton b = new JButton(); b.setPreferredSize(new Dimension(0, 0)); return b; }
                @Override protected void paintThumb(Graphics g, JComponent c, Rectangle r) {
                    Graphics2D g2 = (Graphics2D) g; g2.setColor(thumbColor);
                    g2.fillRoundRect(r.x + 2, r.y, r.width - 4, r.height, 8, 8);
                }
                @Override protected void paintTrack(Graphics g, JComponent c, Rectangle r) {}
            });

            add(scrollPane, BorderLayout.CENTER);
        }

        private void createInputArea() {
            JPanel inputPanel = new JPanel(new BorderLayout(10, 0)) {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    applyRenderingHints(g2);
                    g2.setColor(BACKGROUND_CARD);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), BORDER_RADIUS, BORDER_RADIUS);
                    g2.dispose();
                }
            };
            inputPanel.setOpaque(false);
            inputPanel.setBorder(new EmptyBorder(16, 16, 16, 16));

            inputField = new JTextField() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    applyRenderingHints(g2);
                    g2.setColor(BACKGROUND_ELEVATED);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.dispose();
                    super.paintComponent(g);
                }
            };
            inputField.setOpaque(false);
            inputField.setFont(createFont("SF Pro Text", Font.PLAIN, 15));
            inputField.setForeground(TEXT_PRIMARY);
            inputField.setCaretColor(ACCENT_PRIMARY);
            inputField.setBorder(new EmptyBorder(12, 16, 12, 16));
            inputField.setPreferredSize(new Dimension(0, 48));
            inputField.setText("Escreve a tua pergunta...");
            inputField.setForeground(TEXT_MUTED);

            inputField.addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    if (inputField.getText().equals("Escreve a tua pergunta...")) {
                        inputField.setText("");
                        inputField.setForeground(TEXT_PRIMARY);
                    }
                }
                public void focusLost(FocusEvent e) {
                    if (inputField.getText().isEmpty()) {
                        inputField.setText("Escreve a tua pergunta...");
                        inputField.setForeground(TEXT_MUTED);
                    }
                }
            });
            inputField.addActionListener(e -> sendMessage());

            sendButton = new JButton("➤") {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    applyRenderingHints(g2);
                    g2.setPaint(new GradientPaint(0, 0, GRADIENT_BLUE_START, getWidth(), getHeight(), GRADIENT_BLUE_END));
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                    g2.setColor(Color.WHITE);
                    g2.setFont(getFont());
                    FontMetrics fm = g2.getFontMetrics();
                    g2.drawString(getText(), (getWidth() - fm.stringWidth(getText())) / 2,
                            (getHeight() + fm.getAscent()) / 2 - 3);
                    g2.dispose();
                }
            };
            sendButton.setFont(createFont("SF Pro Text", Font.BOLD, 18));
            sendButton.setPreferredSize(new Dimension(48, 48));
            sendButton.setContentAreaFilled(false);
            sendButton.setBorderPainted(false);
            sendButton.setFocusPainted(false);
            sendButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
            sendButton.addActionListener(e -> sendMessage());

            inputPanel.add(inputField, BorderLayout.CENTER);
            inputPanel.add(sendButton, BorderLayout.EAST);

            JPanel wrapper = new JPanel(new BorderLayout());
            wrapper.setOpaque(false);
            wrapper.setBorder(new EmptyBorder(16, 0, 0, 0));
            wrapper.add(inputPanel);

            add(wrapper, BorderLayout.SOUTH);
        }

        private void addBotMessage(String text) { addMessageBubble(text, false); }
        private void addUserMessage(String text) { addMessageBubble(text, true); }

        private void addMessageBubble(String text, boolean isUser) {
            JPanel wrapper = new JPanel(new FlowLayout(isUser ? FlowLayout.RIGHT : FlowLayout.LEFT, 0, 4));
            wrapper.setOpaque(false);
            wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

            JPanel bubble = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    applyRenderingHints(g2);
                    if (isUser) g2.setPaint(new GradientPaint(0, 0, GRADIENT_BLUE_START, getWidth(), getHeight(), GRADIENT_BLUE_END));
                    else g2.setColor(BACKGROUND_ELEVATED);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.dispose();
                }
            };
            bubble.setLayout(new BorderLayout());
            bubble.setOpaque(false);
            bubble.setBorder(new EmptyBorder(12, 16, 12, 16));

            JTextArea ta = new JTextArea(text);
            ta.setFont(createFont("SF Pro Text", Font.PLAIN, 14));
            ta.setForeground(isUser ? Color.WHITE : TEXT_PRIMARY);
            ta.setOpaque(false);
            ta.setEditable(false);
            ta.setLineWrap(true);
            ta.setWrapStyleWord(true);
            ta.setBorder(null);
            ta.setSize(280, Short.MAX_VALUE);
            ta.setPreferredSize(new Dimension(Math.min(280, ta.getPreferredSize().width), ta.getPreferredSize().height));

            bubble.add(ta);
            wrapper.add(bubble);
            chatArea.add(wrapper);
            chatArea.revalidate();

            SwingUtilities.invokeLater(() -> {
                JScrollBar sb = scrollPane.getVerticalScrollBar();
                sb.setValue(sb.getMaximum());
            });
        }

        private void showLoading() {
            JPanel wrapper = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 4));
            wrapper.setOpaque(false);
            wrapper.setName("loading");

            JPanel bubble = new JPanel() {
                private int dots = 0;
                { new Timer(400, e -> { dots = (dots + 1) % 4; repaint(); }).start(); }
                @Override
                protected void paintComponent(Graphics g) {
                    Graphics2D g2 = (Graphics2D) g.create();
                    applyRenderingHints(g2);
                    g2.setColor(BACKGROUND_ELEVATED);
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                    g2.setColor(TEXT_SECONDARY);
                    g2.setFont(createFont("SF Pro Text", Font.BOLD, 20));
                    g2.drawString("•".repeat(dots + 1), 16, getHeight() / 2 + 7);
                    g2.dispose();
                }
            };
            bubble.setOpaque(false);
            bubble.setPreferredSize(new Dimension(60, 40));
            wrapper.add(bubble);
            chatArea.add(wrapper);
            chatArea.revalidate();
        }

        private void hideLoading() {
            for (Component c : chatArea.getComponents())
                if ("loading".equals(c.getName())) { chatArea.remove(c); break; }
            chatArea.revalidate();
            chatArea.repaint();
        }

        private void sendMessage() {
            String text = inputField.getText().trim();
            if (text.isEmpty() || text.equals("Escreve a tua pergunta...")) return;

            addUserMessage(text);
            inputField.setText("");
            inputField.setForeground(TEXT_PRIMARY);
            inputField.setEnabled(false);
            sendButton.setEnabled(false);
            showLoading();

            new Thread(() -> {
                String resposta;
                try {
                    if (text.matches("[0-9\\.\\-\\+\\*\\/\\^\\s\\(\\)]+")) {
                        double r = Calculadora.calcular(text);
                        resposta = "O resultado de " + text + " é:\n\n" + formatResult(r);
                    } else {
                        String prompt = "És um assistente de matemática. Responde de forma clara em português. Pergunta: " + text;
                        String json = engine.sendPrompt(prompt);
                        resposta = extractResponse(json);
                    }
                } catch (Exception e) {
                    resposta = "❌ Erro: " + e.getMessage();
                }

                final String r = resposta;
                SwingUtilities.invokeLater(() -> {
                    hideLoading();
                    addBotMessage(r);
                    inputField.setEnabled(true);
                    sendButton.setEnabled(true);
                    inputField.requestFocus();
                });
            }).start();
        }

        private String extractResponse(String json) {
            for (String key : new String[]{"text", "content", "message"}) {
                int i = json.indexOf("\"" + key + "\"");
                if (i != -1) {
                    int s = json.indexOf("\"", i + key.length() + 3) + 1;
                    int e = json.indexOf("\"", s);
                    if (s > 0 && e > s) return json.substring(s, e).replace("\\n", "\n").replace("\\\"", "\"");
                }
            }
            return json;
        }
    }

    // ==================== MAIN ====================

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.setProperty("awt.useSystemAAFontSettings", "on");
            System.setProperty("swing.aatext", "true");
        } catch (Exception e) {}

        SwingUtilities.invokeLater(CalculadoraGUI::new);
    }
}