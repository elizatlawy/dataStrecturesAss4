public class BTreeNode {

    private String[] keys;
    private BTreeNode[] children;
    private int numberOfKeys;
    private boolean isLeaf;

    protected BTreeNode(int t, boolean isLeaf){
        keys =  new String[2*t-1];
        children = new  BTreeNode[2*t];
        numberOfKeys = 0;
        this.isLeaf = isLeaf;

    }
    protected BTreeNode insert(String key) {
        // if root is full split it
        if( numberOfKeys == keys.length){
            BTreeNode newRoot = new BTreeNode(children.length/2, false);
            newRoot.children[0] = this;
            splitChild(0);
            // continue the insert
            insertNonFull(key);
            return newRoot;
        }
        // if root is not full
        else
            insertNonFull(key);
            return this;
    }
    // insert when root is not full
    private void insertNonFull (String key){
        int i = this.numberOfKeys;
        if(isLeaf){
            while (i >= 1 && key.compareTo(keys[i]) < 0){
                keys[i+1] = keys[i];
                i--;
            }
            keys[i+1] = key;
            numberOfKeys++;
        }
        // the node is not s leaf
        else{
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
    }
    private void splitChild(int splitIndex){
        int t = this.children.length/2;
        BTreeNode toSplit = this.children[splitIndex];
        BTreeNode newChild = new BTreeNode(t,toSplit.isLeaf);
        newChild.numberOfKeys = t-1;
        // moving the last t keys from toSplit to newChild
        for(int j = 0; j <= t-1; j++)
            newChild.keys[j] = toSplit.keys[j+t];
        // if toSplit is not a leaf, moving his last t childrens to newChild
        if(!toSplit.isLeaf){
            for(int j = 0; j <= t; j++)
                newChild.children[j] = toSplit.children[j+t];
        }
        // update the numbers of keys of toSplit
        toSplit.numberOfKeys = t-1;
        // inserting the newChild to current node
        for (int i = splitIndex+1; i < numberOfKeys; i++)
            this.children[i+1] = this.children[i];
        this.children[splitIndex+1] = newChild;
        // inserting the middle key of toSplit to current node
        for( int i = splitIndex; i < this.numberOfKeys; i++)
            this.keys[i+1] = keys[i];
        this.keys[splitIndex] = toSplit.keys[t];
        this.numberOfKeys++;
    }

    protected BTreeNode search(String key){
        int i = 0;
        while (i <= numberOfKeys && key.compareTo(keys[i]) > 0)
            i++;
        if(key.compareTo(keys[i]) == 0)
            return this;
        else if (!this.isLeaf)
            children[i].search(key);
        // the node is a leaf, the key not found
        return null;
    }

} // end of class


