package src;

import java.util.ArrayList;

public class Divisao extends Operacao {
    @Override
    public double executar(ArrayList<Double> valores) {
        double r = valores.get(0);
        for (int i = 1; i < valores.size(); i++) r /= valores.get(i);
        return r;
    }
}

