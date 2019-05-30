import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Tests {
    public static void main(String[] args) {

        //long startTime = System.currentTimeMillis();
//        System.out.println(startTime);
//        startTime = startTime /1000000;
//        System.out.println(startTime);

//        String str1 = "0.1"; // 0.1000
//        String str2 = "0.123"; // 0.1230
//        String str3  = "123.1"; // "123.1000"
//        String str4 = "10.123456789"; // "10/1234"
//        System.out.println(estimatedRunTime(str1));
//        System.out.println(estimatedRunTime(str2));
//        System.out.println(estimatedRunTime(str3));
//        System.out.println(estimatedRunTime(str4));

        BTreeTests();
        BloomFilterTests();

//        // ++++++++++++++++++ writing function +++++++++++++++
//        // The name of the file to open.
//        String fileName = System.getProperty("user.dir")+"/test.txt";
//
//        try {
//            // Assume default encoding.
//            FileWriter fileWriter =
//                    new FileWriter(fileName);
//
//            // Always wrap FileWriter in BufferedWriter.
//            BufferedWriter bufferedWriter =
//                    new BufferedWriter(fileWriter);
//
//            // Note that write() does not automatically
//            // append a newline character.
//            bufferedWriter.write(Arrays.toString(b1.table));
//
//            // Always close files.
//            bufferedWriter.close();
//        } catch (IOException ex) {
//            System.out.println(
//                    "Error writing to file '"
//                            + fileName + "'");
//            // Or we could just do this:
//            // ex.printStackTrace();
//
//            // ++++++++++++++++++ writing function +++++++++++++++
//
//        }
    } // end of main

//    public static String estimatedRunTime(String estimatedTime){
//        int indexOfDot = estimatedTime.indexOf('.');
//        int afterFurDigits = estimatedTime.length() - (indexOfDot + 1);
//        // if the is more than 4 digits after the dot, cut the rest
//        if(afterFurDigits > 4 )
//            estimatedTime = estimatedTime.substring(0,indexOfDot + 5);
//        // if there is less than 4 digits add zeros to the result
//        if(afterFurDigits < 4){
//            while (afterFurDigits < 4){
//                estimatedTime = estimatedTime + "0";
//                afterFurDigits++;
//            }
//        }
//
//        return estimatedTime;
//    }

    public static void BloomFilterTests() {

        //Create the Bloom Filter.
        for (int i = 1; i <= 1; i++) {
            String s = "" + i;
            BloomFilter bloomFilter = contructBloomFilter("1000000");
            //Create the Hash Table.
            HashTable hashTable = contructHashTable("1000000");
            //Find the percentage of false-positives
//            String falsePositivesPercent = bloomFilter.getFalsePositivePercentage(hashTable, System.getProperty("user.dir")+"/requested_passwords.txt");
//            System.out.println(falsePositivesPercent);
//
//            //Find the number of rejected passwords
//            String rejectedPasswordsAmount = bloomFilter.getRejectedPasswordsAmount(System.getProperty("user.dir")+"/requested_passwords.txt");
//            System.out.println("rejected by BF: " + rejectedPasswordsAmount);
//            System.out.println("table size: " + i);

            String hashTableSearchTime = hashTable.getSearchTime(System.getProperty("user.dir") + "/10k-most-common.txt");
            System.out.println(hashTableSearchTime);
        }
    }

    public static void BTreeTests() {
        //Create the B tree using the t value and the path to the bad_passwords file.
        BTree btree = createTree("100");

        //Get the DFS representation of the btree
        //String treeLayout = btree.toString();
        //System.out.println(treeLayout);
        String btreeSearchTime = btree.getSearchTime(System.getProperty("user.dir") + "/10k-most-common.txt");
        System.out.println(btreeSearchTime);

        //Get the DFS representation of the btree, after performing deletions
        //String treeLayoutAfterDeletions = deleteKeysFromTree(btree);
        //System.out.println(treeLayoutAfterDeletions);

    }

    private static String deleteKeysFromTree(BTree btree) {
        btree.deleteKeysFromTree(System.getProperty("user.dir") + "/10k-most-common.txt");
        return btree.toString();
    }

    // Create the BTree using the t value, and the friends file.
    // Insert the bad passwords into the tree.
    private static BTree createTree(String tVal) {
        BTree btree = new BTree(tVal);
        btree.createFullTree(System.getProperty("user.dir") + "/password-list-top-100000.txt");
        return btree;
    }

    private static BloomFilter contructBloomFilter(String m1) {
        BloomFilter bloomFilter = new BloomFilter(m1, System.getProperty("user.dir") + "/hash_functions.txt");
        //update the Bloom Filter's table with the bad passwords
        bloomFilter.updateTable(System.getProperty("user.dir") + "/password-list-top-100000.txt");
        return bloomFilter;
    }

    private static HashTable contructHashTable(String m2) {
        HashTable hashTable = new HashTable(m2);
        //update the Hash Table with the bad passwords
        hashTable.updateTable(System.getProperty("user.dir") + "/password-list-top-100000.txt");
        return hashTable;
    }


    public static int horners(String key) {
        int p = 15486907;
        long result = key.charAt(0);
        for (int i = 1; i < key.length(); i++) {
            result = (key.charAt(i) + 256 * result) % p;
        }
        return (int) result;
    }


    private static String readFileAsString(String filePath) {
        String content = "";
        try {
            content = new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}

