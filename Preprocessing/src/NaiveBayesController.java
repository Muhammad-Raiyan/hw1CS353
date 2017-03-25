import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ishmam on 3/25/2017.
 *
 * @author ishmam
 */
public class NaiveBayesController {
    static ArrayList<DataModel> fileList;
    static BayesClassifier bayesClassifier;
    static Preprocess preprocess;

    public NaiveBayesController(ArrayList<DataModel> fileList, Preprocess preprocess) {
        this.fileList = fileList;
        this.preprocess = preprocess;
        bayesClassifier = new BayesClassifier(this.fileList);
    }

    public void startTraining(){
        preprocess.setFileList(fileList);


        bayesClassifier = new BayesClassifier(fileList);
        bayesClassifier.train();

    }

    public HashMap<String, Double> startTesting(){
        double tp = 0.0, fp = 0.0, tn = 0.0, fn = 0.0;
        for(DataModel dm:fileList){
            boolean actual = true;
            if(dm.isTestData()){
                actual = bayesClassifier.test(dm);
                if(actual == true){
                    if(dm.isPos()) tp++;
                    else fp++;
                }
                else{
                    if(!dm.isPos()) tn++;
                    else fn++;
                }
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
        HashMap<String, Double> temp = new HashMap<>();
        temp.put("Accuracy", accuracy);
        temp.put("Precision", precision);
        temp.put("Recall", recall);
        return temp;
    }
}
