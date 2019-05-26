import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelperFunctions {

    private static int p = 15486907;

    public static int[] keyPharser(String path){
        String keys = readFileAsString(path);
        // saving all the keys in an array
        String[] keysArray = keys.split("\\r?\\n");
        // encoding the keys using horner's rule
        return keysEncoding(keysArray);

    }

    /**
     * returns all the text from filePath as String
     * @param filePath the path of the file that the function will read from
     * @return a new string of the file content
     */
    public static String readFileAsString(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    /**
     * encoding all the keys from string to encoded numbers
     * @param keys array of the keys as strings
     * @return new array of the keys as encoded numbers
     */
    public static int[] keysEncoding(String[] keys){
        int[] encodedKeys = new int[keys.length];
        for(int i = 0; i < keys.length; i++){
            encodedKeys[i] = horners(keys[i]);
        }
        return encodedKeys;
    }

    /**
     * encoding a single string key to a number according to Horner's rule
     * @param key the string to be encoded
     * @return new int of the key as encoded number
     */
    private static int horners(String key){
        long result = key.charAt(0);
        for (int i = 1; i < key.length(); i++){
            result = (key.charAt(i) + 256*result)%p;
        }
        return (int)result;
    }
    /**
     * Convert String representing a decimal number to int
     *
     * @param str is a string representing a positive decimal number
     * @return output
     */
    public static int stringToInt(String str) {
        int output = 0;
        for (int i = 0; i < str.length(); i++) {
            output = output * 10;
            output = output + str.charAt(i) - '0';

        }
        return output;
    }
}
