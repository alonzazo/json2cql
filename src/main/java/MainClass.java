import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.Scanner;

import static java.lang.System.exit;

public class MainClass {

    public static void main(String[] args){

        try {
            String tablename;
            String path;
            if (args.length == 1){
                tablename = "Reviews";
                path = args[0];
            }
            if (args.length == 2){
                tablename = args[0];
                path = args[1];
            }
            else
                throw new Exception();

            InputStream inputStream = new FileInputStream(path);
            Scanner scanner = new Scanner(inputStream);

            File file = new File("out.sql");
            file.createNewFile();

            FileWriter fileWriter = new FileWriter("out.sql",true);

            while (scanner.hasNextLine()){

                String line = scanner.nextLine();

                JSONParser jsonParser = new JSONParser();
                JSONObject row = (JSONObject) jsonParser.parse(line);

                String cqlLine = Json2CqlConverter.convertJSON(tablename, row);

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