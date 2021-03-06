import java.util.ArrayList;
import java.util.List;

/**
 * Created by ishmam on 3/19/2017.
 *
 * @author ishmam
 */
public class TFIDFCalculator {

    public double tf(DataModel dm, String term) {
        double result = 0;

        List<String> file = dm.getContent();
        for (String word : file) {
            if (term.equalsIgnoreCase(word))
                result++;
        }
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
        return tf(file, term) * idf(docs, term);

    }
}
