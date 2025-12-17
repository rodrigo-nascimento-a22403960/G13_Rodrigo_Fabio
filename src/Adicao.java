package src;

import java.util.ArrayList;

public class Adicao extends Operacao {
    @Override
    public double executar(ArrayList<Double> valores) {
        double s = 0;
        for (double v : valores) s += v;
        return s;
    }
}
