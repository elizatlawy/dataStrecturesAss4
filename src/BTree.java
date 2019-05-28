import java.io.*;

public class BTree {
    private int t;
    private BTreeNode root;


    public BTree(String t) {
        if (t == null || Integer.parseInt(t) < 1)
            throw new IllegalArgumentException("t must be a positive number");
        this.t = Integer.parseInt(t);
        root = null;
    }
    public void createFullTree(String path) {
        File inputFile = new File(path);
        try {
            FileInputStream stream = new FileInputStream(inputFile);
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(reader);
            String line = buffer.readLine();
            while (line != null) {
                this.insert(line);
                line = buffer.readLine();
            }
            buffer.close();
            reader.close();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void insert(String key) {
        if (key == null)
            throw new NullPointerException("string is null");

        if (root == null){
            root = new BTreeNode(t, true);
            root.insert(key);
        }
        else{
            key = key.toLowerCase();
            root = root.insert(key);
        }
    }

    public BTreeNode search(String key) {
        if (key == null)
            throw new NullPointerException("string is null");
        else if (root == null)
            return null;
        else{
            key = key.toLowerCase();
            return root.search(key);
        }
    }

    public void deleteKeysFromTree (String path){
        File inputFile = new File(path);
        try {
            FileInputStream stream = new FileInputStream(inputFile);
            InputStreamReader reader = new InputStreamReader(stream);
            BufferedReader buffer = new BufferedReader(reader);
            String line = buffer.readLine();

            while (line != null) {
                delete(line);
                line = buffer.readLine();
            }
            buffer.close();
            reader.close();
            stream.close();
        } catch (IOException e) {
            System.out.println("failed to read file");
        }
    }

    public void delete(String key){
        if(root != null){
           root.deleteKey(key);
            // If the root node has 0 keys, if it is a leaf set it to be null
           if(root.getNumberOfKeys() == 0){
               BTreeNode tmp = root;
               if(root.isLeaf())
                   root = null;
               // if the root has a child, make its first child as the new root
               else
                   root = root.children[0];
               tmp = null;
           }
        }

    }

    public String getSearchTime(String path){
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
                this.search(line);
                line = buffer.readLine();
            }
            // end of search
            endTime = System.nanoTime();
            buffer.close();
            reader.close();
            stream.close();
        } catch (IOException e) {
            System.out.println("failed to read file");
        }
        return HelperFunctions.estimatedRunTime(startTime,endTime);
    }
    public String toString() {
        if (root == null)
            return "Empty tree";
        else{
            String output = "";
            output = root.DFS(0, output);
            return output;
        }
    }
} // end of class


