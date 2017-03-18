import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.stream.Stream;

/**
 * Created by ishmam on 3/17/2017.
 *
 * @author ishmam
 */
public class InputData {
    HashMap<String, Integer> inputMap = new HashMap<String, Integer>();

    public InputData(Path folder) {

        try(Stream<Path> paths = Files.walk(folder)) {
            paths.forEach(filePath -> {
                if(Files.isRegularFile(filePath)){
                    System.out.println("Path: " + filePath);
                    readFile(filePath);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(Path file){
        try(BufferedReader br = new BufferedReader(new FileReader(String.valueOf(file)))) {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while(line != null){
                sb.append(line);
                sb.append(System.lineSeparator());
                line = br.readLine();
            }
            String fullFile = sb.toString();
            stringToHash(fullFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stringToHash(String input){
        String[] words = input.split(" ");

        for(int i = 0; i<words.length; i++){
            if(inputMap.get(words[i]) == null){
                inputMap.put(words[i], 1);
            }
            else {
                int newValue = Integer.valueOf(String.valueOf(inputMap.get(words[i])));
                newValue++;
                inputMap.put(words[i], newValue);
            }
        }
    }

    public HashMap<String, Integer> getInputMap() {
        return inputMap;
    }
}
