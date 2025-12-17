package src;

import java.util.ArrayList;

public class Potencia extends Operacao {
    @Override
    public double executar(ArrayList<Double> valores) {
        return Math.pow(valores.get(0), valores.get(1));
    }
}
