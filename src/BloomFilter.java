public class BloomFilter {
    // ---------------------- fields ----------------------
    private int p = 15486907;
    private int m1;
    protected int[] table; // the table of the bloom filter
    private String[] functions; // array of the hash functions alpha and beta parameters

    // ---------------------- constructor ----------------------
    public BloomFilter(String m1, String hash_functions_path) {
        if (m1 == null || Integer.parseInt(m1) < 1)
            throw new IllegalArgumentException("m1 must be a positive number");
        this.m1 = Integer.parseInt(m1);
        table = new int[this.m1]; // java initializing an int array to be zero in all cells as default
        String hash_functions = HelperFunctions.readFileAsString(hash_functions_path); // reading the hash_functions txt file
        //saving all of the hash functions alpha and beta parameters in an array
        functions = hash_functions.split("\\r?\\n"); // splits the String to array by down line
    }

    // ---------------------- Methods ----------------------
    public void updateTable(String path) {
        int[] encodedKeys = HelperFunctions.keyParser(path);
        // adding all the keys to the bloom filter table
        for (int encodedKey : encodedKeys) add(encodedKey);
    }

    /**
     * @param key the value we check if exist in the bloom filter table
     * @return true if exist, false otherwise
     */
    private boolean contains(int key) {
        boolean contains = true;
        for (int i = 0; i < functions.length & contains; i++) {
            // calculating the current hash function
            int h = hashing(functions[i], key);
            if (table[h] != 1)
                contains = false;
        }
        return contains;
    }

    /**
     * adding a new value to the bloom filter table
     *
     * @param key the value to be stored in the bloom filter table
     */
    private void add(int key) {
        // going over all of the hash functions in the array and applying them on the key value
        for (String function : functions) {
            int h = hashing(function, key);
            // updating the current index in the array according to the hash function result to be 1
            table[h] = 1;
        }
    }

    /**
     * @param function String that contains the alpha & beta parameters separated by underscore
     * @param key      the key to insert
     * @return int of the hashing result
     */
    private int hashing(String function, int key) {
        int underscoreIndex = function.indexOf("_");
        String alphaStr = function.substring(0, underscoreIndex);
        String betaStr = function.substring(underscoreIndex + 1);
        // check if alpha & beta are legal parameters
        if (!alphaStr.matches("[0-9]+") || !betaStr.matches("[0-9]+")) {
            throw new IllegalArgumentException("alpha & beta  must be a positive numbers");
        }
        int alpha = Integer.parseInt(alphaStr);
        int beta = Integer.parseInt(betaStr);
        // calculating the current hash function
        return ((alpha * key + beta) % p) % m1;
    }

    public String getFalsePositivePercentage(HashTable hashTable, String path) {
        int[] encodedKeys = HelperFunctions.keyParser(path);
        double falseRejection = 0;
        double trueRejection = 0;
        for (int encodedKey : encodedKeys) {
            if (contains(encodedKey) & !hashTable.contains(encodedKey))
                falseRejection++;

            if (hashTable.contains(encodedKey))
                trueRejection++;
        }
        double notRejected = encodedKeys.length - trueRejection;
        double FalsePositivePercentage = falseRejection / notRejected;
        return Double.toString(FalsePositivePercentage);
    }

    public String getRejectedPasswordsAmount(String path) {
        int[] encodedKeys = HelperFunctions.keyParser(path);
        int rejectedPasswordsAmount = 0;
        for (int encodedKey : encodedKeys) {
            if (contains(encodedKey))
                rejectedPasswordsAmount++;
        }
        return Integer.toString(rejectedPasswordsAmount);
    }

}


