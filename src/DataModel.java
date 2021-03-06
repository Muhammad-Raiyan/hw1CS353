import java.nio.file.Path;
import java.util.ArrayList;

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

    public DataModel(Path path, boolean isPos) {
        this.path = path;
        this.isPos = isPos;
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

    public void setTestData(boolean testData) {
        isTestData = testData;
    }
}
