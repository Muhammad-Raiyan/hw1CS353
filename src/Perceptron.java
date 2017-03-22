import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ishmam on 3/19/2017.
 *
 * @author ishmam
 */
public class Perceptron {

    public static HashMap<String, Double> weightVector;
    HashMap<String, Double> inputVector;
    double bias;
    double actualResponse, desiredResponse, learningRate;
    DataModel dm;

    public Perceptron(HashMap<String, Double> inputVector, HashMap<String, Double> weightVector, boolean isPos, DataModel dm) {
        this.inputVector = inputVector;
        this.weightVector = weightVector;
        this.desiredResponse = isPos? 1 : -1;
        this.dm = dm;
        init();
    }

    private void init() {
        bias = 0.0;
        learningRate = .25;
    }

    public double train(){
        actualResponse = signum();
        double error = updateWeight(actualResponse);
        return error;
    }

    private double updateWeight(double actualResponse) {
        double error = 0.0;
        for(String key:dm.getContent()){
            error += (desiredResponse - actualResponse);
            double delta = (desiredResponse-actualResponse)*inputVector.get(key);
            double newWeight = weightVector.get(key)+delta;
            weightVector.put(key, newWeight);
        }
        return error;
    }

    private double signum() {
        double sum = 0.0;

        for(String key:dm.getContent()){
            sum += (inputVector.get(key)*weightVector.get(key));
        }

        return sum;
    }

    public double test(ArrayList<String> testingVector){
        double sum = 0.0;
        for (String key:testingVector){
            if(weightVector.containsKey(key)){
                sum+= (weightVector.get(key));
            }
        }
        return sum;
    }
}
