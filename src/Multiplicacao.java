package src;

import java.util.ArrayList;

public class Multiplicacao extends Operacao {
    @Override
    public double executar(ArrayList<Double> valores) {
        double r = 1;
        for (double v : valores) r *= v;
        return r;
    }
}
