import java.io.IOException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;

public class Tests {
    public static void main(String[] args) {
//        int m1 = 1548673;
//        BloomFilter b1 = new BloomFilter(m1, System.getProperty("user.dir") + "/hash_functions.txt");
//        b1.updateTable(System.getProperty("user.dir") + "/bad_passwords.txt");

        //Create the Bloom Filter.
        for(int i = 1; i<=1; i++){
            String s = "" + i;
            BloomFilter bloomFilter = contructBloomFilter("32");
            //Create the Hash Table.
            HashTable hashTable = contructHashTable("32");
            //Find the percentage of false-positives
            String falsePositivesPercent = bloomFilter.getFalsePositivePercentage(hashTable, System.getProperty("user.dir")+"/requested_passwords.txt");
            System.out.println(falsePositivesPercent);

            //Find the number of rejected passwords
            String rejectedPasswordsAmount = bloomFilter.getRejectedPasswordsAmount(System.getProperty("user.dir")+"/requested_passwords.txt");
            System.out.println("rejected by BF: " + rejectedPasswordsAmount);
            System.out.println("table size: " + i);
        }

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

    public static void BTreeTests(){

    }

    private static BloomFilter contructBloomFilter(String m1) {
        BloomFilter bloomFilter = new BloomFilter(m1, System.getProperty("user.dir")+"/hash_functions.txt");
        //update the Bloom Filter's table with the bad passwords
        bloomFilter.updateTable(System.getProperty("user.dir")+"/bad_passwords.txt");
        return bloomFilter;
    }

    private static HashTable contructHashTable(String m2) {
        HashTable hashTable = new HashTable(m2);
        //update the Hash Table with the bad passwords
        hashTable.updateTable(System.getProperty("user.dir")+"/bad_passwords.txt");
        return hashTable;
    }


        public static int horners (String key){
            int p = 15486907;
            long result = key.charAt(0);
            for (int i = 1; i < key.length(); i++) {
                result = (key.charAt(i) + 256 * result) % p;
            }
            return (int) result;
        }


        private static String readFileAsString (String filePath){
            String content = "";
            try {
                content = new String(Files.readAllBytes(Paths.get(filePath)));
            } catch (IOException e) {
                e.printStackTrace();
            }
            return content;
        }
    }

