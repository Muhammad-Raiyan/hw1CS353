import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by ishmam on 3/25/2017.
 *
 * @author ishmam
 */
public class BayesClassifier {

    final int N = 1600;
    int posDoc;
    double priorP, priorN;
    ArrayList<DataModel> fileList;
    //int totalWordsP = 0, totalWordsN = 0;
    ArrayList<String> positiveWords, negativeWords;
    HashMap<String, Double> cProbPos, cProbN;
    public BayesClassifier(ArrayList<DataModel> fileList) {
        this.fileList = fileList;
        positiveWords = new ArrayList<>();
        negativeWords = new ArrayList<>();
        cProbPos = new HashMap<>();
        cProbN = new HashMap<>();

        posDoc = countPosDocs();
        priorP = posDoc*1.00/N;
        priorN = 1- priorP;

        for(DataModel dm: fileList){
            if(dm.isTrainingData()) {
                if (dm.isPos())
                    positiveWords.addAll(dm.getContent());
                else
                    negativeWords.addAll(dm.getContent());
            }
        }

    }

    public void train(){
        int i = 0, j = 0;
        System.out.print("Calculating conditional probability(positive): ");
        for(String term:positiveWords){
            i++;
            if(i%50000==0) System.out.print(".");

            if(!cProbPos.containsKey(term)){
                cProbPos.put(term, condProb(positiveWords, term));
            }
        }

        System.out.print("\nCalculating conditional probability(negative): ");
        for(String term:negativeWords){
            j++;
            if(j%50000==0) System.out.print(".");
            if(!cProbN.containsKey(term)){
                cProbN.put(term, condProb(negativeWords, term));
            }
        }
        System.out.println();
    }

    public boolean test(DataModel dm){
        double posteriorP = 0.0, posteriorN = 0.0;
        for(String term:dm.getContent()){
            if(cProbPos.containsKey(term) && cProbN.containsKey(term)){
                posteriorP += (Math.log(priorP) + cProbPos.get(term));
                posteriorN += (Math.log(priorN) + cProbN.get(term));
            }
        }
        return Math.log(posteriorP/posteriorN)<0? true: false;
    }

    int countPosDocs(){
        int i = 0;
        for(DataModel dm: fileList){
            if(dm.isPos() && dm.isTrainingData()) i++;
        }

        return i;
    }

    double condProb(ArrayList<String> words, String word){
        double result = 0.0;
        result = Collections.frequency(words, word)*1.00;
        result /= words.size();
        return Math.log(result);
    }
}
