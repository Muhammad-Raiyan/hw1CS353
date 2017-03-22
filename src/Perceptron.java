import java.util.HashMap;

/**
 * Created by ishmam on 3/19/2017.
 *
 * @author ishmam
 */
public class Perceptron {
    HashMap<String, Double> inputVector;
    HashMap<String, Double> wightVector;
    double bias;
    double actualResponse, desiredResponse, learningRate;

    public Perceptron(HashMap<String, Double> inputVector, HashMap<String, Double> wightVector, boolean isPos) {
        this.inputVector = inputVector;
        this.wightVector = wightVector;
        this.desiredResponse = isPos? 1 : -1;
        init();
    }

    private void init() {
    }
}
