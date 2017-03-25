import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by ishmam on 3/24/2017.
 *
 * @author ishmam
 */
public class ProcessingMain {

    private static String filter = "[^a-zA-Z\\s]";
    private static final String positiveDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\pos";
    private static final String negativeDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\neg";
    private static final File target = new File("C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\preprocess.json");
    private static ArrayList<Path> pathList = new ArrayList<>();
    static HashMap<Path, HashMap<String, Double>> tfMap = new HashMap<Path, HashMap<String, Double>>();
    static ArrayList<DataModel> fileList = new ArrayList<>();
    public static void main(String[] args) {


        ReadFile readFile = new ReadFile();
        pathList.addAll(readFile.loadFileNames(Paths.get(positiveDir)));
        pathList.addAll(readFile.loadFileNames(Paths.get(negativeDir)));
        if(prompt() == 1) filter = "";
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


        /*BayesClassifier bayesClassifier = new BayesClassifier(fileList);
        bayesClassifier.train();
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
        System.out.println("Recall: " + String.format("%.4f", recall));*/
        //writeTOJson();
    }

    private static int prompt() {
        System.out.print("1. No modification \n2. Only words without punctuation or numerals\nSelect: ");
        Scanner sc = new Scanner(System.in);
        System.out.println();
        return sc.nextInt();
    }

    private static void writeTOJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try(OutputStream out = new FileOutputStream(target)) {
            JsonGenerator generator = objectMapper.getFactory().createGenerator(out);
            generator.setPrettyPrinter(new DefaultPrettyPrinter());
            objectMapper.writeValue(target, tfMap);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
