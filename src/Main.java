import java.nio.file.Paths;
import java.util.*;

public class Main {

    public static final String positiveDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\pos";
    public static final String negativeDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\neg";
    private static HashMap<String, Double> weightVector;
    private static HashMap<String, Double> defaultFeatureVector;
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

        for(DataModel dm : inputData.getFileList()){
            TFIDFCalculator tfidfCalculator = new TFIDFCalculator();
            double sum = 0;
            for(String word: dm.getContent()){
                double temp = tfidfCalculator.tfIdf(dm, dataProcessing.getTrainingDocs(), word);
                defaultFeatureVector.put(word, temp);
            }
            System.out.println("File: " + dm.getPath());
        }
        /*trainingData = dataProcessing.getTrainingData();
        testingData = dataProcessing.getTestingData();
        Map<String, Integer> sortedTrainingData = sortMapByValue(trainingData);
        Map<String, Integer> sortedTestingData = sortMapByValue(testingData);*/
        System.out.println("Hello");
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
