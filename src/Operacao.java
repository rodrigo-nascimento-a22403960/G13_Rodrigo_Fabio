package src;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class Operacao implements Serializable {
    public abstract double executar(ArrayList<Double> valores);
}
