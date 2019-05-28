class BTreeNode {

    protected String[] keys;
    protected BTreeNode[] children;
    private int numberOfKeys;
    private boolean isLeaf;
    private static int LOWER_BOUND_KEYNUM;
    private static int UPPER_BOUND_KEYNUM;

    protected BTreeNode(int t, boolean isLeaf){
        keys =  new String[2*t-1];
        children = new  BTreeNode[2*t];
        numberOfKeys = 0;
        this.isLeaf = isLeaf;
        LOWER_BOUND_KEYNUM = t-1;
        UPPER_BOUND_KEYNUM = 2*t - 1;

    }
    protected BTreeNode insert(String key) {
        // if root is full split it
        if(numberOfKeys == keys.length){
            BTreeNode newRoot = new BTreeNode(children.length/2, false);
            newRoot.children[0] = this;
            newRoot.splitChild(0);
            // continue the insert
            newRoot.insertNonFull(key);
            return newRoot;
        }
        // if root is not full
        else
            insertNonFull(key);
            return this;
    }
    // insert when root is not full
    private void insertNonFull (String key){
        int i = this.numberOfKeys-1;
        if(isLeaf)
            insertToLeaf(i,key);
        // the node is not s leaf
        else
            insertToNonLeaf(i,key);
    }
    private void insertToLeaf (int i, String key){
        while (i >= 1 && key.compareTo(keys[i]) < 0){
            keys[i+1] = keys[i];
            i--;
        }
        keys[i+1] = key;
        numberOfKeys++;
    }
    private void insertToNonLeaf (int i, String key){
        while (i >= 1 && key.compareTo(keys[i]) < 0)
            i--;
        // moving on forward to the right child
        i++;
        // check if the child is full
        if(children[i].numberOfKeys == keys.length){
            this.splitChild(i);
            // if the key we split id smaller then key, need to move one index
            if(key.compareTo(keys[i]) > 0)
                i++;
        }
        this.children[i].insertNonFull(key);
    }

    private void splitChild(int splitIndex){
        int t = this.children.length/2;
        BTreeNode toSplit = this.children[splitIndex];
        BTreeNode newChild = new BTreeNode(t,toSplit.isLeaf);
        newChild.numberOfKeys = t-1;
        // moving the last t keys from toSplit to newChild
        toSplit.copyHalfKeys(t, newChild);
        // if toSplit is not a leaf, moving his last t childrens to newChild
        if(!toSplit.isLeaf)
            toSplit.copyHalfChildren(t, newChild);
        // inserting the newChild to current node
        this.insertNewChild(splitIndex, newChild);
        toSplit.numberOfKeys = t-1;
        for( int i = splitIndex; i < this.numberOfKeys; i++)
            this.keys[i+1] = keys[i];
        this.keys[splitIndex] = toSplit.keys[t-1];
        // update the numbers of keys of toSplit & current node
        this.numberOfKeys++;
    }
    // copy values of this.keys from cell t to the beginning newChild.keys
    private void copyHalfKeys (int t, BTreeNode newChild){
        for(int j = 0; j < t-1; j++)
            newChild.keys[j] = this.keys[j+t];
    }
    // copy values of this.children from cell t to the beginning newChild.children
    private void copyHalfChildren (int t, BTreeNode newChild){
        for(int j = 0; j < t; j++)
            newChild.children[j] = this.children[j+t];
    }
    private void insertNewChild(int splitIndex, BTreeNode newChild){
        for (int i = splitIndex+1; i <= numberOfKeys; i++)
            this.children[i+1] = this.children[i];
        this.children[splitIndex+1] = newChild;
    }


    protected BTreeNode search(String key){
        int i = 0;
        // //keep incrementing in node while key > current value.
        while (i < numberOfKeys && key.compareTo(keys[i]) > 0)
            i++;
        if(i == numberOfKeys)
            i--;
        if(key.compareTo(keys[i]) == 0)
            return this;
        else if (!this.isLeaf)
            children[i].search(key);
        // the node is a leaf, the key not found
        return null;
    }
    // A function to remove the key k from the sub-tree rooted with this node
    protected void deleteKey(String key) {
        int index = searchKeyIndex(key);
        // The key to be removed is present in this node
        if (index > 0) {
            if (this.isLeaf)
                this.removeFromLeaf(index);
            else
                this.removeFromNonLeaf(index);
        }
        // need to continue the search in the sub-tree
        else {
            //  If this node is a leaf node, then the key is not exist in tree, stop teh function
            if(this.isLeaf)
                return;
            // else it is not a leaf and we continue the search
            else {
                // The flag indicates if the key is present in the sub-tree rooted with the last child of this node
                boolean isinLastChild = (index == numberOfKeys);

            }




        }




        }

    } // end of class

    private void removeFromLeaf(int index){

    }

    private void removeFromNonLeaf(int index){

    }

    //
    //returns the index of the first key that is greater than or equal to k
    private int searchKeyIndex(String key) {
        int i = 0;
        //keep incrementing in node while key > current value.
        while (i < numberOfKeys && key.compareTo(keys[i]) > 0)
            i++;
        return i;
    }

    protected String DFSHelper(int depth, String output){
        if(!isLeaf){
            for( int i = 0; i <= numberOfKeys; i++){
                output = this.children[i].DFS(depth+1, output);
                if(i < numberOfKeys)
                    output = output + keys[i] + "_" + depth +",";
            }
        }
        else {
            for( int i = 0; i < numberOfKeys; i++){
                output = output + keys[i] + "_" + depth +",";

            }
        }
        return output;
    }
    protected String DFS(int depth, String output){
         output = DFSHelper(depth, output);
         // cut the last ","
         output = output.substring(0,output.length()-1);
         return output;
    }

    public int getNumberOfKeys(){
        return this.numberOfKeys;
    }
    public boolean isLeaf(){
        return this.isLeaf;
    }

} // end of class


