package src;

import java.util.ArrayList;

public class Quadrado extends Operacao {
    @Override
    public double executar(ArrayList<Double> valores) {
        double v = valores.get(0);
        return v * v;
    }
}
