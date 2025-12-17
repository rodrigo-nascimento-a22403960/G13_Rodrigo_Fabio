import java.util.ArrayList;
import java.io.Serializable;

public abstract class Operacao implements Serializable {
    public abstract double executar(ArrayList<Double> valores);
}
