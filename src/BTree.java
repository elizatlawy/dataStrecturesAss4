import java.io.*;

public class BTree {
    private int t;
    private BTreeNode root;


    public BTree(String t) {
        if (t == null || Integer.parseInt(t) < 1)
            throw new IllegalArgumentException("t must be a positive integer");
        this.t = Integer.parseInt(t);
        root = null;
    }

    public void insert(String key) {
        if (key == null)
            throw new NullPointerException("string is null");

        if (root == null)
            root = new BTreeNode(t, true);

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

    public String toString() {
        if (root == null)
            return "";
        else
            return root.DFS;
    }

    public void createFullTree(String inputPath) {
        File inputFile = new File(inputPath);
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
} // end of class


