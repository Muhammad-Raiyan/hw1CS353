import java.util.*;

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
    HashMap<String, Double> testingData = new HashMap<>();
    HashMap<String, Double> weightVector = new HashMap<>();

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

    public ArrayList<DataModel> getTrainingDocs (){
        ArrayList<DataModel> trainingDocs = new ArrayList<>();
        for(DataModel file: fileList)
        {
            if(!file.isTestData()){
                trainingDocs.add(file);
            }
        }

        return trainingDocs;
    }

    public HashMap<String, Double> getTestingData (DataModel dm){

        String content = inputData.readFile(dm.getPath());
        return stringToHash(content);
    }

    public HashMap<String, Double> getWeightVector(){
        for(DataModel file: fileList)
        {
            if(!file.isTestData()){
                String review = inputData.readFile(file.getPath());
                String[] words = review.split(" ");
                for(int i=0; i<words.length; i++){
                    words[i] = words[i].replace("\r", "").replace("\n", "");
                    if(weightVector.get(words[i])==null){
                        weightVector.put(words[i], 0.0);
                    }
                }
                file.setContent(new ArrayList<String>(Arrays.asList(words)));
            }
        }
        return weightVector;
    }

    public HashMap<String, Double> stringToHash(String input){
        String[] words = input.split(" ");
        HashMap<String, Double> hash = new HashMap<>();

        for(int i = 0; i<words.length; i++){
            words[i] = words[i].replace("\r", "").replace("\n", "");
            if(hash.get(words[i]) == null){
                hash.put(words[i], 1.0);
            }
            else {
                double newValue = Double.valueOf(String.valueOf(hash.get(words[i])));
                newValue+=1.0;
                hash.put(words[i], newValue);
            }
        }
        return hash;
    }

}
