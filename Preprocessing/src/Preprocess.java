import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ishmam on 3/24/2017.
 *
 * @author ishmam
 */
public class Preprocess {

    private ArrayList<DataModel> fileList;

    public HashMap<String, Double> calculateTF(String[] content){

        String temp[] = content;
        HashMap<String, Double> tfMap = new HashMap<>();
        for(int i=0; i<temp.length; i++){
            if(!tfMap.containsKey(temp[i])) {
                tfMap.put(temp[i], tf(temp, temp[i]));
            }
        }

        return tfMap;
    }

    private Double tf(String[] content, String s) {
        Double result = 0.0;

        for(int i=0; i<content.length; i++){
            if(s.equalsIgnoreCase(content[i])){
                result++;
            }
        }
        return result/content.length;
    }

    public void runCrossValidation(int N){

        int[] begin = {0, 400, 800, 1200, 1600};
        int[] end = {399, 799, 1199, 1599, 1999};

        // set all files as training data
        for(DataModel x: fileList){
            x.setTestData(false);
        }

        // change selected ones as testing data
        for(int i=begin[N]; i<=end[N]; i++){
            DataModel dm = fileList.get(i);
            dm.setTestData(true);
            dm.setFeaturevector(null);
            fileList.set(i, dm);
        }
    }


    public void setFileList(ArrayList<DataModel> fileList) {
        this.fileList = fileList;
    }
}
