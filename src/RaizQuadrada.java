package src;

import java.util.ArrayList;

public class RaizQuadrada extends Operacao {
    @Override
    public double executar(ArrayList<Double> valores) {
        return Math.sqrt(valores.get(0));
    }
}
