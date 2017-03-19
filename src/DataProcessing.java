import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by ishmam on 3/18/2017.
 *
 * @author ishmam
 */
public class DataProcessing {

    private final int crossValidationFold = 5;
    private ArrayList<DataModel> fileList;
    private InputData inputData;
    HashMap<String, Integer> trainingData = new HashMap<String, Integer>();
    HashMap<String, Integer> testingData = new HashMap<String, Integer>();

    public DataProcessing(InputData inputData) {
        this.fileList = inputData.getFileList();
        this.inputData = inputData;
    }

    public void runCrossValidation(int N){
        long seed = System.nanoTime();
        Collections.shuffle(fileList, new Random(seed));

        int[] begin = {0, 400, 800, 1200, 1600};
        int[] end = {399, 799, 1199, 1599, 2000};

        // set all files as training data
        for(DataModel x: fileList){
            x.setTestData(false);
        }

        // change selected ones as testing data
        for(int i=begin[N]; i<=end[N]; i++){
            DataModel dm = fileList.get(i);
            dm.setTestData(true);
            fileList.set(i, dm);
        }
    }

    public HashMap<String, Integer> getTrainingData (){
        for(DataModel file: fileList)
        {
            if(!file.isTestData()){
                String review = inputData.readFile(file.getPath());
                stringToHash(review, trainingData);
            }
        }

        return trainingData;
    }

    public HashMap<String, Integer> getTestingData (){

        fileList.forEach(file ->{
            if(file.isTestData()){
                String review = inputData.readFile(file.getPath());
                stringToHash(review, testingData);
            }
        });

        return testingData;
    }

    public void stringToHash(String input, HashMap<String, Integer> hash){
        String[] words = input.split(" ");

        for(int i = 0; i<words.length; i++){
            if(hash.get(words[i]) == null){
                hash.put(words[i], 1);
            }
            else {
                int newValue = Integer.valueOf(String.valueOf(hash.get(words[i])));
                newValue++;
                hash.put(words[i], newValue);
            }
        }
    }

}
