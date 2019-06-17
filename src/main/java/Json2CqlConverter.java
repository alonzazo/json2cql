import org.json.simple.JSONObject;

import java.util.Collection;
import java.util.Set;

public class Json2CqlConverter {

    public static String convertJSON(JSONObject json) {

        return  "INSERT INTO Reviews (" + keysSeparatedByComma(json.keySet()) + ") VALUES (" + valuesSeparatedByComma(json.values()) + ");";

    }

    public static String convertJSON(String tableName, JSONObject json) {

        return  "INSERT INTO " + tableName + " (" + keysSeparatedByComma(json.keySet()) + ") VALUES (" + valuesSeparatedByComma(json.values()) + ");";

    }

    private static String keysSeparatedByComma(Set set){
        String result = "";
        for (Object key: set){
            String keyString = (String) key;
            result += keyString + ",";
        }

        return result.substring(0,result.length()-1);
    }

    private static String valuesSeparatedByComma(Collection collection){
        String result = "";

        for (Object value: collection){
            String valueString = value.toString();

            if (isHelpfulField(valueString))
                valueString = convertHelpful(valueString);

            if (isWeirdDate(valueString))
                valueString = convertDate(valueString);

            if (!isNumeric(valueString))
                valueString = "\"" + valueString + "\"";

            result += valueString + ",";
        }

        return result.substring(0,result.length()-1);
    }

    //FILTROS

    private static boolean isNumeric(String string){
        return string.matches("-?[0-9]+(\\.[0-9]+)?");
    }

    private static boolean isWeirdDate(String string) {return string.matches("[0-9]{1,2} [0-9]{1,2}, [0-9]{4}");}

    private static String convertDate(String string) {
        String[] result;

        result = string.split(" ");
        return result[2] + "-" + result[0] + "-" + result[1].substring(0,result[1].length()-1);

    }

    private static boolean isHelpfulField(String string) {return string.matches("\\[[0-9]+,[0-9]+\\]");}

    private static String convertHelpful(String string) {
        String[] result;

        result = string.split(",");

        Float a = Float.parseFloat(result[0].substring(1));

        Float b = Float.parseFloat(result[1].substring(0,result[1].length()-1));

            if (a.equals(b) && b == 0.0)
                return "0.0";

        Float helpfulFloat =  a / b;

        return helpfulFloat.toString();
    }
}
