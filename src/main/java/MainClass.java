import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class MainClass {

    public static void main(String[] args){

        try {

            InputStream inputStream = new FileInputStream(args[0]);
            Scanner scanner = new Scanner(inputStream);

            File file = new File("out.sql");
            file.createNewFile();

            FileWriter fileWriter = new FileWriter("out.sql",true);

            while (scanner.hasNextLine()){

                String line = scanner.nextLine();

                JSONParser jsonParser = new JSONParser();
                JSONObject row = (JSONObject) jsonParser.parse(line);

                String cqlLine = Json2CqlConverter.convertJSON(row);

                fileWriter.append(cqlLine + "\n");
                fileWriter.flush();

            }
            fileWriter.close();

        }catch (Exception e){
            e.printStackTrace();

        } finally {

        }


    }
}