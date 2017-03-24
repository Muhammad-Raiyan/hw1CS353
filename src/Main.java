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
    private static ArrayList<DataModel> trainingDataList;
    private static ArrayList<DataModel> testingDataList;

    public static void main(String[] args) {

        InputData inputData = new InputData();
        inputData.loadFileNames(Paths.get(positiveDir), true);
        inputData.loadFileNames(Paths.get(negativeDir), false);
        inputData.shuffle();
        double accuracy = 0, precision = 0, recall = 0;

        DataProcessing dataProcessing = new DataProcessing(inputData);

        for(int i=0; i<5; i++) {
            System.out.println("N: " + i);
            dataProcessing.runCrossValidation(i);
            weightVector = dataProcessing.getWeightVector();
            defaultFeatureVector = new HashMap<>(weightVector);
            idfMap = new HashMap<>();
            initPreprocess(inputData, dataProcessing);
            PerceptronController perceptronController = new PerceptronController(inputData, weightVector, trainingDataList, testingDataList);
            perceptronController.startTraining();
            HashMap<String, Double> tempHash = perceptronController.startTesting();
            accuracy += tempHash.get("Accuracy");
            precision += tempHash.get("Precision");
            recall += tempHash.get("Recall");
        }

        System.out.println("Average Accuracy: " + String.format("%.3f", accuracy/5.0));
        System.out.println("Average Precision: " + String.format("%.3f", precision/5.0));
        System.out.println("Average Recall: " + String.format("%.3f", recall/5.0));
    }

    private static void initPreprocess(InputData inputData, DataProcessing dataProcessing) {
        TFIDFCalculator tfidfCalculator = new TFIDFCalculator(idfMap);
        trainingDataList = (ArrayList<DataModel>) inputData.getFileList().stream().filter(DataModel::isTrainingData).collect(Collectors.toList());
        testingDataList = (ArrayList<DataModel>) inputData.getFileList().stream().filter(DataModel::isTestData).collect(Collectors.toList());

        System.out.print("Prepreprocessing: ");
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

            if(count%160 == 0) System.out.print((count/1600.00)*100 + "% -> ");

        }

        for(DataModel dm: testingDataList){
            HashMap<String, Double> testingData = dataProcessing.getTestingData(dm);
            dm.setFeaturevector(testingData);
        }
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
