import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

/**
 * Created by ishmam on 3/17/2017.
 *
 * @author ishmam
 */

public class InputData {
    ArrayList<DataModel> fileList = new ArrayList<>();

    public InputData() {
    }

    public void loadFileNames(Path folder, boolean isPos){

        try(Stream<Path> paths = Files.walk(folder)) {
            paths.forEach(filePath -> {
                if(Files.isRegularFile(filePath)){
                    //System.out.println("Path: " + filePath);
                    DataModel dm = new DataModel(filePath, isPos);
                    fileList.add(dm);
                    //readFile(filePath);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String readFile(Path file){
        String fullFile = null;
        try(BufferedReader br = new BufferedReader(new FileReader(String.valueOf(file)))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line != null){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            fullFile = sb.toString();
            //stringToHash(fullFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullFile;
    }


    public ArrayList<DataModel> getFileList() {
        return fileList;
    }
}
