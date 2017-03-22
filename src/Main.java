import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static final String positiveDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\pos";
    public static final String negativeDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\neg";
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

        double error = 0.0;
        TFIDFCalculator tfidfCalculator = new TFIDFCalculator(idfMap);
        ArrayList<DataModel> trainingDataList = (ArrayList<DataModel>) inputData.getFileList().stream().filter(DataModel::isTrainingData).collect(Collectors.toList());

        boolean initial = true;

        for(int epoch = 0; epoch <1000; epoch++) {
            int i=0;
            for (DataModel dm : trainingDataList) {
                i++;
                if (initial) {
                    double sum = 0;
                    dm.setFeaturevector(defaultFeatureVector);

                    for (String word : dm.getContent()) {
                        double temp = tfidfCalculator.tfIdf(dm, trainingDataList, word);
                        dm.getFeaturevector().put(word, temp);
                        sum += temp;
                    }
                }

                Perceptron perceptron = new Perceptron(dm.getFeaturevector(), weightVector, dm.isPos(), dm);

                error += Math.abs(perceptron.train());
                //System.out.println(++i + "th sum: " + sum + " idfMapSize: " + idfMap.size());
                //temp =  (error / i);
                if (i % 10 == 0) System.out.println(i + "th error: " + error / i + " epoch: " + epoch);
            }
            if(epoch==0) initial = false;
            double result = error/1600;
            System.out.println("Error: "+ result + " Epoch: " + epoch + " T: " + result);
            if(result<1.5) break;
            error = 0.0;
        }


        System.out.println("Perceptron Finished");
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
