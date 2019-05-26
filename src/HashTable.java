import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HashTable {
    private int p = 15486907;
    private HashList[] table;
    private int m2;


    public HashTable(String m2){
        this.m2 = HelperFunctions.stringToInt(m2);
        table = new HashList[this.m2];
    }
    public void updateTable(String path){
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
     *
     * @param key the value we check if exist in the hash table
     * @return true if exist, false otherwise
     */
    public boolean contains (int key) {
        boolean contains = true;
        int index = hashFunction(key);
        if(table[index] == null)
            contains = false;
        else
            contains = table[index].contains(key);
        return contains;
    }

    private int hashFunction(int key){
        // hashing by using the multiplication method
        double a = (Math.sqrt(5)-1)/2;
        return (int) (m2 * ((key * a) %1));

    }
}
