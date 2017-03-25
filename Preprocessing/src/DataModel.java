import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ishmam on 3/18/2017.
 *
 * @author ishmam
 */
public class DataModel {
    private Path path;
    private boolean isPos;
    private boolean isTestData;
    private ArrayList<String> content;
    private HashMap<String, Double> featurevector;
    private HashMap<String, Double> tfMap;

    public DataModel(Path path, boolean isPos) {
        this.path = path;
        this.isPos = isPos;
        tfMap = new HashMap<>();
    }

    public HashMap<String, Double> getFeaturevector() {
        return featurevector;
    }

    public void setFeaturevector(HashMap<String, Double> featurevector) {
        this.featurevector = featurevector;
    }

    public ArrayList<String> getContent() {
        return content;
    }

    public void setContent(ArrayList<String> content) {
        this.content = content;
    }
    public Path getPath() {
        return path;
    }

    public void setPath(Path file) {
        this.path = file;
    }

    public boolean isPos() {
        return isPos;
    }

    public void setPos(boolean pos) {
        isPos = pos;
    }

    public boolean isTestData() {
        return isTestData;
    }

    public boolean isTrainingData() {
        return !isTestData;
    }

    public void setTestData(boolean testData) {
        isTestData = testData;
    }

    public HashMap<String, Double> getTfMap() {
        return tfMap;
    }

    public void setTfMap(HashMap<String, Double> tfMap) {
        this.tfMap = tfMap;
    }

}
