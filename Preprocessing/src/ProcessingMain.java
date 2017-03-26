import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by ishmam on 3/24/2017.
 *
 * @author ishmam
 */
public class ProcessingMain {

    private static String filter = "[^a-zA-Z\\s]";
    private static final String positiveDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\pos";
    private static final String negativeDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\neg";

    private static ArrayList<Path> pathList = new ArrayList<>();
    static ArrayList<DataModel> fileList = new ArrayList<>();


    //static HashMap<Path, HashMap<String, Double>> tfMap = new HashMap<Path, HashMap<String, Double>>();

    public static void main(String[] args) {

        ReadFile readFile = new ReadFile();
        pathList.addAll(readFile.loadFileNames(Paths.get(positiveDir)));
        pathList.addAll(readFile.loadFileNames(Paths.get(negativeDir)));
        if(promptAboutDataMod() == 1) filter = "";
        Preprocess preprocess = new Preprocess();

        for(Path path: pathList){
            String content = readFile.loadFromFile(path);
            content = content.replace("\r", "").replace("\n", "");
            content = content.replaceAll(filter, "");

            DataModel dm;
            if(String.valueOf(path).contains("pos")){
                dm = new DataModel(path, true);

            } else{
                dm = new DataModel(path, false);
            }
            String sArray[] = content.split(" ");
            dm.setContent(new ArrayList<>(Arrays.asList(sArray)));
            dm.setTfMap(preprocess.calculateTF(sArray));
            fileList.add(dm);

            //tfMap.put(path, preprocess.calculateTF(content));
        }

        long seed = System.nanoTime();
        Collections.shuffle(fileList, new Random(seed));
        preprocess.setFileList(fileList);
        do {
            int selector = promptAlgorithm();
            if (selector == 2) {
                startNaiveBayes(preprocess);
            } else if (selector == 1) {
                startPerceptron(preprocess);
            } else {
                System.exit(1);
            }
        }while(true);

    }

    private static void startPerceptron(Preprocess preprocess) {
        HashMap<String, Double> weightVector;
        HashMap<String, Double> idfMap;
        ArrayList<DataModel> trainingDataList;
        ArrayList<DataModel> testingDataList;
        double accuracy = 0, precision = 0, recall = 0;
        for(int i =0; i<5; i++) {
            preprocess.runCrossValidation(i);
            weightVector = preprocess.getWeightVector();
            idfMap = new HashMap<>();
            trainingDataList = (ArrayList<DataModel>) fileList.stream().filter(DataModel::isTrainingData).collect(Collectors.toList());
            testingDataList = (ArrayList<DataModel>) fileList.stream().filter(DataModel::isTestData).collect(Collectors.toList());
            System.out.print("Prepreprocessing: ");
            int count = 0;

            for(DataModel dm: trainingDataList){
                HashMap<String, Double> featureVector = new HashMap<>();
                count++;
                for(String key : dm.getContent()) {
                    if(!idfMap.containsKey(key)){
                        double idf = preprocess.calculateIDF(trainingDataList, key);
                        idfMap.put(key, idf);
                    }
                    if(!featureVector.containsKey(key)){
                        double tfIdf = dm.getTfMap().get(key)*idfMap.get(key);
                        featureVector.put(key, tfIdf);
                    }
                }
                dm.setFeaturevector(featureVector);
                if(count%160==0){
                    System.out.print(count/16.00 + "% -> ");
                }
            }

            PerceptronController perceptronController = new PerceptronController(weightVector, trainingDataList, testingDataList);
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

    private static void startNaiveBayes(Preprocess preprocess) {
        HashMap<String, Double> bayesResultHash;
        double accuracy = 0, precision = 0, recall = 0;
        for(int i =0; i<5; i++) {
            preprocess.runCrossValidation(i);
            NaiveBayesController naiveBayesController = new NaiveBayesController(fileList, preprocess);
            naiveBayesController.startTraining();
            bayesResultHash = naiveBayesController.startTesting();
            accuracy += bayesResultHash.get("Accuracy");
            precision += bayesResultHash.get("Precision");
            recall += bayesResultHash.get("Recall");
        }

        System.out.println("Average Accuracy: " + String.format("%.3f", accuracy/5.0));
        System.out.println("Average Precision: " + String.format("%.3f", precision/5.0));
        System.out.println("Average Recall: " + String.format("%.3f", recall/5.0));
    }

    private static int promptAlgorithm() {
        System.out.println("1.Perceptron \n2.Naive Bayes\n3.Exit ");
        System.out.print("Select: ");
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }

    private static int promptAboutDataMod() {
        System.out.print("1. No modification \n2. Only words without punctuation or numerals\nSelect: ");
        Scanner sc = new Scanner(System.in);
        System.out.println();
        return sc.nextInt();
    }
}
