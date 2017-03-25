import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ishmam on 3/24/2017.
 *
 * @author ishmam
 */
public class ProcessingMain {

    private static final String positiveDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\pos";
    private static final String negativeDir = "C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\neg";
    private static final File target = new File("C:\\Users\\ishmam\\Documents\\Programming\\hw1CS353\\data\\preprocess.json");
    private static ArrayList<Path> pathList = new ArrayList<>();
    static HashMap<Path, HashMap<String, Double>> tfMap = new HashMap<Path, HashMap<String, Double>>();
    static ArrayList<DataModel> fileList = new ArrayList<>();
    public static void main(String[] args) {

        ReadFile readFile = new ReadFile();
        pathList.addAll(readFile.loadFileNames(Paths.get(positiveDir)));
        pathList.addAll(readFile.loadFileNames(Paths.get(negativeDir)));

        Preprocess preprocess = new Preprocess();

        for(Path path: pathList){
            String content = readFile.loadFromFile(path);
            //String temp = content.replaceAll("[^a-zA-Z\\s]", "");
            //System.out.println(temp);

            DataModel dm;
            if(String.valueOf(path).contains("pos")){
                dm = new DataModel(path, true);

            } else{
                dm = new DataModel(path, false);
            }
            String sArray[] = content.split(" ");
            dm.setContent(new ArrayList<>(Arrays.asList(sArray)));
            dm.setTfMap(preprocess.calculateTF(sArray));
            fileList.add(dm);

            //tfMap.put(path, preprocess.calculateTF(content));
        }
        System.out.println(fileList.size());
        preprocess.setFileList(fileList);
        preprocess.runCrossValidation(1);

        writeTOJson();
    }

    private static void writeTOJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try(OutputStream out = new FileOutputStream(target)) {
            JsonGenerator generator = objectMapper.getFactory().createGenerator(out);
            generator.setPrettyPrinter(new DefaultPrettyPrinter());
            objectMapper.writeValue(target, tfMap);
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
