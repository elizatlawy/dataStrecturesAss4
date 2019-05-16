import java.io.IOException;
import java.nio.file.*;

public class BloomFilter {
    private int p = 15486907;
    private int m1;
    public int[] table; // the table of the bloom filter
    private String functions[]; // array of the hash functions alpha and beta parameters

    public BloomFilter(String m1, String hash_functions_path) {
        this.m1 = HelperFunctions.stringToInt(m1);
        table = new int[this.m1]; // java initializing an int array to be zero in all cells as default
        String hash_functions = HelperFunctions.readFileAsString(hash_functions_path); // reading the hash_functions txt file
        //saving all of the hash functions alpha and beta parameters in an array
        functions = hash_functions.split("\\r?\\n"); // splits the String to array by down line
    }

     public void updateTable(String path){
         int[] encodedKeys = HelperFunctions.keyPharser(path);
        // adding all the keys to the bloom filter table
        for (int i = 0; i<encodedKeys.length; i++)
            add(encodedKeys[i]);
    }

    /**
     *
     * @param key the value we check if exist in the bloom filter table
     * @return true if exist, false otherwise
     */
    private boolean contains (int key) {
        boolean contains = true;
        for (int i = 0; i < functions.length & contains; i++) {
            // calculating the current hash function
            int h = hashing(functions[i], key);
            if(table[h] != 1)
                contains = false;
        }
        return contains;
    }

    /**
     * adding a new value to the bloom filter table
     * @param key the value to be stored in the bloom filter table
     */
    private void add (int key){
        // going over all of the hash functions in the array and applying them on the key value
        for (int i = 0; i<functions.length; i++){
            int h = hashing(functions[i], key);
            // updating the current index in the array according to the hash function result to be 1
            table[h] = 1;
        }
    }
    /**
     *
     * @param function String that contains the alpha & beta parameters
     * @param key the key to insert
     * @return h the hashing result
     */
    private int hashing(String function, int key){
        // calculating the current hash function
        int alpha = function.charAt(0)-'0';
        int beta = function.charAt(2)-'0';
        int h = ((alpha * key + beta) % p) % m1;
        return h;
    }

    public String getFalsePositivePercentage(HashTable hashTable, String path){
        int[] encodedKeys = HelperFunctions.keyPharser(path);
        double falseRejcetion = 0; double trueRejection = 0;
        for (int i = 0; i<encodedKeys.length; i++){
            if(contains(encodedKeys[i]) & !hashTable.contains(encodedKeys[i]))
                falseRejcetion++;
            if(hashTable.contains(encodedKeys[i]))
                trueRejection++;
        }
        double notRejected = encodedKeys.length - trueRejection;
        double FalsePositivePercentage = falseRejcetion/notRejected;
        String Percentage = Double.toString(FalsePositivePercentage);
        return Percentage;
    }

    public String getRejectedPasswordsAmount(String path){
        int[] encodedKeys = HelperFunctions.keyPharser(path);
        int rejectedPasswordsAmount = 0;
        for (int encodedKey : encodedKeys) {
            if (contains(encodedKey))
                rejectedPasswordsAmount++;

        }
        return Double.toString(rejectedPasswordsAmount);
    }

}


