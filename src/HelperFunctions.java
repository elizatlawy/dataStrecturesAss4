import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelperFunctions {

    private static int p = 15486907;

    public static int[] keyPharser(String path) {
        String keys = readFileAsString(path);
        // saving all the keys in an array
        String[] keysArray = keys.split("\\r?\\n");
        // encoding the keys using horner's rule
        return keysEncoding(keysArray);

    }

    /**
     * returns all the text from filePath as String
     *
     * @param filePath the path of the file that the function will read from
     * @return a new string of the file content
     */
    public static String readFileAsString(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            System.out.println("failed to read file");
        }
        return content;
    }

    /**
     * encoding all the keys from string to encoded numbers
     *
     * @param keys array of the keys as strings
     * @return new array of the keys as encoded numbers
     */
    private static int[] keysEncoding(String[] keys) {
        int[] encodedKeys = new int[keys.length];
        for (int i = 0; i < keys.length; i++) {
            encodedKeys[i] = horners(keys[i]);
        }
        return encodedKeys;
    }

    /**
     * encoding a single string key to a number according to Horner's rule
     *
     * @param key the string to be encoded
     * @return new int of the key as encoded number
     */
    public static int horners(String key) {
        long result = key.charAt(0);
        for (int i = 1; i < key.length(); i++) {
            result = (key.charAt(i) + 256 * result) % p;
        }
        return (int) result;
    }

    /**
     * @param startTime the start time of what we want to measure
     * @param endTime   the end time of what we want to measure
     * @return the estimated run time in millisecond, in a format of 4 digits after the dot
     */
    public static String estimatedRunTime(double startTime, double endTime) {
        double runTime = (endTime - startTime) / 1000000;
        String estimatedTime = Double.toString(runTime);
        int indexOfDot = estimatedTime.indexOf('.');
        int afterFurDigits = estimatedTime.length() - (indexOfDot + 1);
        // if the is more than 4 digits after the dot, cut the rest
        if (afterFurDigits > 4)
            estimatedTime = estimatedTime.substring(0, indexOfDot + 5);
        // if there is less than 4 digits add zeros to the result
        if (afterFurDigits < 4) {
            while (afterFurDigits < 4) {
                estimatedTime = estimatedTime + "0";
                afterFurDigits++;
            }
        }

        return estimatedTime;

    }
} // end of class
