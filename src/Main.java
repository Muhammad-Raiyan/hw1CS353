import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final String positiveDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\pos";
    public static final String negativeDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\neg";

    private static Perceptron perceptron;
    private static HashMap<String, Double> weightVector;
    private static HashMap<String, Double> defaultFeatureVector;
    public  static HashMap<String, Double> idfMap;
    private ArrayList<DataModel> trainingDocs;
    private ArrayList<DataModel> testingDocs;

    public static void main(String[] args) {

        InputData inputData = new InputData();
        inputData.loadFileNames(Paths.get(positiveDir), true);
        inputData.loadFileNames(Paths.get(negativeDir), false);

        DataProcessing dataProcessing = new DataProcessing(inputData);
        dataProcessing.runCrossValidation(1);
        weightVector = dataProcessing.getWeightVector();
        defaultFeatureVector = new HashMap<>(weightVector);
        idfMap = new HashMap<>();


        TFIDFCalculator tfidfCalculator = new TFIDFCalculator(idfMap);
        ArrayList<DataModel> trainingDataList = (ArrayList<DataModel>) inputData.getFileList().stream().filter(DataModel::isTrainingData).collect(Collectors.toList());
        ArrayList<DataModel> testingDataList = (ArrayList<DataModel>) inputData.getFileList().stream().filter(DataModel::isTestData).collect(Collectors.toList());

        // preprocessing
        System.out.println("Prepreprocessing: ");
        int count = 0;
        for (DataModel dm : trainingDataList) {
            count++;

            double sum = 0;
            dm.setFeaturevector(defaultFeatureVector);
            for (String word : dm.getContent()) {
                double temp = tfidfCalculator.tfIdf(dm, trainingDataList, word);
                dm.getFeaturevector().put(word, temp);
                sum += temp;
            }

            if(count%160 == 0) System.out.print((count/1600.00)*100 + "% ->");

        }

        // perceptron training
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
            System.out.println("Error: "+ result + " Epoch: " + epoch + " T: " + result);
            if(result<1.5) break;
            error = 0.0;
        }

        System.out.println("Training Finished");

        double tp = 0, fp = 0, tn = 0, fn = 0;
        for(DataModel dm: testingDataList){
            HashMap<String, Double> testingData = dataProcessing.getTestingData(dm);
            ArrayList<String> content =  new ArrayList<>(testingData.keySet());
            dm.setContent(content);
            HashMap<String, Double> temp = new HashMap<>();
            dm.setFeaturevector(testingData);
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
        System.out.println("Accuracy: " + accuracy*100 + "%");
        System.out.println("Precision: " + precision*100 + "%");
        System.out.println("Recall: " + recall*100 + "%");

    }

    private static Map<String, Integer> sortMapByValue(Map<String, Integer> unsortedMap){
        List<Map.Entry<String, Integer>> list = new LinkedList<Map.Entry<String, Integer>>(unsortedMap.entrySet());

        Collections.sort(list, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<String, Integer> sortedMap = new LinkedHashMap<String, Integer>();
        for(Map.Entry<String, Integer> entry: list){
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}
