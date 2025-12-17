package src;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class CalculadoraTest {

    @Test
    public void testeAdicao() {
        double r = Calculadora.calcular("2 + 3");
        assertEquals(5.0, r);
    }

    @Test
    public void testeSubtracao() {
        double r = Calculadora.calcular("10 - 4");
        assertEquals(6.0, r);
    }

    @Test
    public void testeMultiplicacao() {
        double r = Calculadora.calcular("2 * 5");
        assertEquals(10.0, r);
    }

    @Test
    public void testeDivisao() {
        double r = Calculadora.calcular("20 / 4");
        assertEquals(5.0, r);
    }

    @Test
    public void testePotencia() {
        double r = Calculadora.calcular("2 ^ 3");
        assertEquals(8.0, r);
    }

    @Test
    public void testePrioridadeOperacoes() {
        double r = Calculadora.calcular("2 + 3 * 4");
        assertEquals(14.0, r);
    }

    @Test
    public void testeEspacos() {
        double r = Calculadora.calcular("  2   +   3 ");
        assertEquals(5.0, r);
    }

    @Test
    public void testeNumeroNegativo() {
        double r = Calculadora.calcular("-2 + 5");
        assertEquals(3.0, r);
    }

    @Test
    public void testeOperacaoDesconhecida() {
        assertThrows(IllegalArgumentException.class, () -> {
            Calculadora.calcular("2 X 10");
        });
    }

    @Test
    public void testeExpressaoInvalida() {
        assertThrows(IllegalArgumentException.class, () -> {
            Calculadora.calcular("2 + ");
        });
    }
}
