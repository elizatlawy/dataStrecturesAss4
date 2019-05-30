import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HashTable {
    // ---------------------- fields ----------------------
    private int p = 15486907;
    private HashList[] table;
    private int m2;

    // ---------------------- constructor ----------------------
    public HashTable(String m2) {
        if (m2 == null || Integer.parseInt(m2) < 1)
            throw new IllegalArgumentException("m2 must be a positive number");
        this.m2 = Integer.parseInt(m2);
        table = new HashList[this.m2];
    }

    // ---------------------- Methods ----------------------
    public void updateTable(String path) {
        int[] encodedKeys = HelperFunctions.keyPharser(path);
        // adding all the keys to the bloom filter table
        for (int encodedKey : encodedKeys) {
            int index = hashFunction(encodedKey);
            // if we insert to that index for the first time, we need to create an empty list first
            if (table[index] == null)
                table[index] = new HashList();
            table[index].addFirst(encodedKey);
        }
    }

    /**
     * @param key the value we check if exist in the hash table
     * @return true if exist, false otherwise
     */
    public boolean contains(int key) {
        boolean contains;
        int index = hashFunction(key);
        if (table[index] == null)
            contains = false;
        else
            contains = table[index].contains(key);
        return contains;
    }

    private int hashFunction(int key) {
        // hashing by using the multiplication method
        double a = (Math.sqrt(5) - 1) / 2;
        return (int) (m2 * ((key * a) % 1));

    }

    public String getSearchTime(String path) {
        File inputFile = new File(path);
        double startTime = 0;
        double endTime = 0;
        try {
            FileInputStream stream = new FileInputStream(inputFile);
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(reader);
            String line = buffer.readLine();
            // start of search
            startTime = System.nanoTime();
            while (line != null) {
                this.contains(HelperFunctions.horners(line));
                line = buffer.readLine();
            }
            // end of search
            endTime = System.nanoTime();
            buffer.close();
            reader.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return HelperFunctions.estimatedRunTime(startTime, endTime);
    }


} // end of class
