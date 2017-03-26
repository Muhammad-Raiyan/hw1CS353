import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ishmam on 3/22/2017.
 *
 * @author ishmam
 */
public class PerceptronController {

    private HashMap<String, Double> weightVector;

    private ArrayList<DataModel> trainingDataList;
    private ArrayList<DataModel> testingDataList;

    private  Perceptron perceptron;

    public PerceptronController(HashMap<String, Double> weightVector, ArrayList<DataModel> trainingDataList, ArrayList<DataModel> testingDataList) {
        this.weightVector = weightVector;
        this.trainingDataList = trainingDataList;
        this.testingDataList = testingDataList;
    }

    public void startTraining(){
        double error = 0.0;
        for(int epoch = 0; epoch <50; epoch++) {
            int i=0;
            for (DataModel dm : trainingDataList) {
                i++;
                perceptron = new Perceptron(dm.getFeaturevector(), weightVector, dm.isPos(), dm);
                error += Math.abs(perceptron.train());
                //if (i % 100 == 0) System.out.println(i + "th error: " + error / i + " epoch: " + epoch);
            }
            double result = error;

            if(result< 80) {
                System.out.println();
                System.out.println("Error: "+ result + " Epoch: " + epoch);
                break;
            }
            error = 0.0;
        }
    }

    public HashMap<String, Double> startTesting(){
        double tp = 0, fp = 0, tn = 0, fn = 0;
        for(DataModel dm: testingDataList){
            double result = perceptron.test(dm);
            if(result > 3){
                if(dm.isPos()) tp++;
                else fp++;
            }
            else {
                if(dm.isPos()) fn++;
                else tn++;
            }
        }
        double precisionP = tp/(tp+fp);
        double precisionN = tn/(tn+fp);
        double recallP = tp / (tp+fn);
        double recallN = tn / (tn+fp);
        double accuracy = (tp+tn)/(tp+tn+fp+fn);
        double precision = (precisionP+precisionN)/2.0;
        double recall = (recallN + recallP) / 2.0;
        System.out.println("TP - FP - TN - FN: " + tp + " " + fp + " " + tn + " " + fn);
        System.out.println("Accuracy: " + String.format("%.4f", accuracy));
        System.out.println("Precision: " + String.format("%.4f", precision));
        System.out.println("Recall: " + String.format("%.4f", recall));
        System.out.println();
        HashMap<String, Double> res = new HashMap<>();
        res.put("Accuracy", accuracy);
        res.put("Precision", precision);
        res.put("Recall", recall);
        return res;
    }
}
