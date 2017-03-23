import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ishmam on 3/22/2017.
 *
 * @author ishmam
 */
public class PerceptronController {

    private InputData inputData;
    private HashMap<String, Double> weightVector;

    private ArrayList<DataModel> trainingDataList;
    private ArrayList<DataModel> testingDataList;

    private  Perceptron perceptron;

    public PerceptronController(InputData inputData, HashMap<String, Double> weightVector, ArrayList<DataModel> trainingDataList, ArrayList<DataModel> testingDataList) {
        this.inputData = inputData;
        this.weightVector = weightVector;
        this.trainingDataList = trainingDataList;
        this.testingDataList = testingDataList;
    }

    public void startTraining(){
        double error = 0.0;
        for(int epoch = 0; epoch <1000; epoch++) {
            int i=0;
            for (DataModel dm : trainingDataList) {
                i++;
                perceptron = new Perceptron(dm.getFeaturevector(), weightVector, dm.isPos(), dm);
                error += Math.abs(perceptron.train());
                //if (i % 100 == 0) System.out.println(i + "th error: " + error / i + " epoch: " + epoch);
            }
            double result = error/1600;
            if(result == 0.0) {
                System.out.println("Underflow");
                continue;
            }
            System.out.println();
            startTesting();
            System.out.println("Error: "+ result + " Epoch: " + epoch);

            if(result<3 || epoch>50) {
                break;
            }

            error = 0.0;
        }
    }

    public void startTesting(){
        double tp = 0, fp = 0, tn = 0, fn = 0;
        for(DataModel dm: testingDataList){
            double result = perceptron.test(dm);
            if(result > 0){
                if(dm.isPos()) tp++;
                else fp++;
            }
            else {
                if(dm.isPos()) fn++;
                else tn++;
            }
            /*System.out.println(perceptron.test(dm));
            System.out.println(dm.getPath());
            System.out.println(dm.isPos()? "Positive" : "Negative");*/
        }
        double precisionP = tp/(tp+fp);
        double precisionN = tn/(tn+fp);
        double recallP = tp / (tp+fn);
        double recallN = tn / (tn+fp);
        double accuracy = (tp+tn)/(tp+tn+fp+fn);
        double precision = (precisionP+precisionN)/2.0;
        double recall = (recallN + recallP) / 2.0;
        //if(accuracy<.6) return;
        System.out.println("Accuracy: " + accuracy*100 + "%");
        System.out.println("Precision: " + precision*100 + "%");
        System.out.println("Recall: " + recall*100 + "%");
    }
}
