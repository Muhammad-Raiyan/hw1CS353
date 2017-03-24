import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ishmam on 3/19/2017.
 *
 * @author ishmam
 */
public class TFIDFCalculator {

    private final HashMap<String, Double> idfMap;

    public TFIDFCalculator() {
        idfMap = null;
    }

    public TFIDFCalculator(HashMap<String, Double> idfMap) {
        this.idfMap = idfMap;
    }

    public double tf(DataModel dm, String term) {
        double result = 0;

        if(dm.getTfMap().get(term)!=null){
            return dm.getTfMap().get(term);
        }

        List<String> file = dm.getContent();
        for (String word : file) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
        dm.getTfMap().put(term, result/file.size());

        return result / file.size();
    }


    public double idf(ArrayList<DataModel> docs, String term) {

        double n = 0;
        //try {
            for (DataModel dm : docs) {

                ArrayList<String> content = dm.getContent();
                try {
                    for (String word : content) {
                        if (term.equalsIgnoreCase(word)) {
                            n++;
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        //}
       /* catch (NullPointerException e){
            e.printStackTrace();
        }*/

        return Math.log(docs.size() / n);
    }


    public double tfIdf(DataModel file, ArrayList<DataModel> docs, String term) {
        double tfValue = tf(file, term);
        double idfValue = 0.0;
        if(idfMap.get(term)==null){
            idfValue = idf(docs, term);
            idfMap.put(term, idfValue);
        }
        else {
            idfValue = idfMap.get(term);
        }

        return tfValue*idfValue;

    }

    public HashMap<String, Double> getIdfMap() {
        return idfMap;
    }
}
