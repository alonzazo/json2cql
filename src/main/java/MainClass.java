import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.util.Scanner;

public class MainClass {

    public static void main(String[] args){

        try {
            String tablename;
            String path;
            if (args.length == 1){
                tablename = "Reviews";
                path = args[0];
            }
            else if (args.length == 2){
                tablename = args[0];
                path = args[1];
            }
            else
                throw new Exception();

            File fileInput = new File(path);
            ProgressCounter progressCounter = new ProgressCounter(fileInput.length());

            InputStream inputStream = new FileInputStream(path);
            Scanner scanner = new Scanner(inputStream);

            File file = new File("out.sql");
            file.createNewFile();

            FileWriter fileWriter = new FileWriter("out.sql",true);

            progressCounter.start();

            while (scanner.hasNextLine()){

                String line = scanner.nextLine();

                JSONParser jsonParser = new JSONParser();
                JSONObject row = (JSONObject) jsonParser.parse(line);

                String cqlLine = Json2CqlConverter.convertJSON(tablename, row);

                fileWriter.append(cqlLine + "\n");
                fileWriter.flush();

                progressCounter.advance(line.length());

            }

            fileWriter.close();

            progressCounter.finish();

        }catch (Exception e){
            e.printStackTrace();

        } finally {

        }


    }

    private static class ProgressCounter {
        private long currentProgress;
        private long total;
        private PrintStream printStream;

        public ProgressCounter(long total){
            currentProgress = 0;
            this.total = total;
            printStream = System.out;
        }

        public ProgressCounter(long total, PrintStream printStream){
            currentProgress = 0;
            this.total = total;
            this.printStream = printStream;
        }

        public void start(){
            printStream.println("This process has been started!");
        }

        public void advance(long quantity){
            currentProgress += quantity;
            report();
        }

        public void setProgress(long progressPoint){
            currentProgress = progressPoint;
            report();
        }

        public void setTotal(long total){
            this.total = total;
            report();
        }

        public void finish(){
            printStream.println("The process has been finished!");
        }

        private void report(){
            printStream.println("Current progress: " + (currentProgress * 100) / total + "%");
        }
    }
}