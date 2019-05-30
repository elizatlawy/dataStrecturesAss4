import java.util.Arrays;

class BTreeNode {
    // ---------------------- fields ----------------------
    protected String[] keys;
    protected BTreeNode[] children;
    private int numberOfKeys;
    private boolean isLeaf;
    private static int t;
    private static int lowerBoundKeyNumber;
    private static int upperBoundKeyNumber;

    // ---------------------- constructor ----------------------
    protected BTreeNode(int t, boolean isLeaf) {
        this.t = t;
        keys = new String[2 * t - 1];
        children = new BTreeNode[2 * t];
        numberOfKeys = 0;
        this.isLeaf = isLeaf;
        lowerBoundKeyNumber = t - 1;
        upperBoundKeyNumber = 2 * t - 1;

    }

    // ---------------------- Methods ----------------------
    // ---------------------- Insert Methods ----------------------
    protected BTreeNode insert(String key) {
        // if root is full split it
        if (numberOfKeys == upperBoundKeyNumber) {
            BTreeNode newRoot = new BTreeNode(t, false);
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
    private void insertNonFull(String key) {
        int i = this.numberOfKeys - 1;
        if (isLeaf)
            insertToLeaf(i, key);
            // the node is not s leaf
        else
            insertToNonLeaf(i, key);
    }

    private void insertToLeaf(int i, String key) {
        while (i >= 1 && key.compareTo(keys[i]) < 0) {
            keys[i + 1] = keys[i];
            i--;
        }
        keys[i + 1] = key;
        numberOfKeys++;
    }

    private void insertToNonLeaf(int i, String key) {
        while (i >= 1 && key.compareTo(keys[i]) < 0)
            i--;
        // moving on forward to the right child
        i++;
        // check if the child is full
        if (children[i].numberOfKeys == upperBoundKeyNumber) {
            this.splitChild(i);
            // if the key we split id smaller then key, need to move one index
            if (key.compareTo(keys[i]) > 0)
                i++;
        }
        this.children[i].insertNonFull(key);
    }

    private void splitChild(int splitIndex) {
        BTreeNode toSplit = this.children[splitIndex];
        BTreeNode newChild = new BTreeNode(t, toSplit.isLeaf);
        newChild.numberOfKeys = lowerBoundKeyNumber;
        // moving the last t keys from toSplit to newChild
        toSplit.copyHalfKeys(t, newChild);
        // if toSplit is not a leaf, moving his last t childrens to newChild
        if (!toSplit.isLeaf)
            toSplit.copyHalfChildren(t, newChild);
        // inserting the newChild to current node
        this.insertNewChild(splitIndex, newChild);
        // insert the leftmost key of toSplit to current node
        for (int i = splitIndex; i < this.numberOfKeys; i++)
            this.keys[i + 1] = keys[i];
        this.keys[splitIndex] = toSplit.keys[t - 1];
        // update the numbers of keys of toSplit & current node
        this.numberOfKeys++;
        toSplit.numberOfKeys = lowerBoundKeyNumber;
    }

    // copy values of this.keys from cell t to the beginning newChild.keys
    private void copyHalfKeys(int t, BTreeNode newChild) {
        for (int j = 0; j < t - 1; j++)
            newChild.keys[j] = this.keys[j + t];
    }

    // copy values of this.children from cell t to the beginning newChild.children
    private void copyHalfChildren(int t, BTreeNode newChild) {
        for (int j = 0; j < t; j++)
            newChild.children[j] = this.children[j + t];
    }

    private void insertNewChild(int splitIndex, BTreeNode newChild) {
        for (int i = splitIndex + 1; i <= numberOfKeys; i++)
            this.children[i + 1] = this.children[i];
        this.children[splitIndex + 1] = newChild;
    }
    // ---------------------- End of Insert Methods ----------------------


    protected BTreeNode search(String key) {
        int i = 0;
        // //keep incrementing in node while key > current value.
        while (i < numberOfKeys && key.compareTo(keys[i]) > 0)
            i++;
        if (i == numberOfKeys)
            i--;
        if (key.compareTo(keys[i]) == 0)
            return this;
        else if (!this.isLeaf)
            children[i].search(key);
        // the node is a leaf, the key not found
        return null;
    }

    // ---------------------- Remove Methods ----------------------
    // A function to remove the key k from the sub-tree rooted with this node
    protected void deleteKey(String key) {
        int index = searchKeyIndex(key);
        // The key to be removed is present in this node
        if (index < numberOfKeys && this.keys[index].compareTo(key) == 0) {
            if (this.isLeaf)
                this.removeFromLeaf(index);
            else
                this.removeFromNonLeaf(index);
        }
        // else if this node is not a leaf, need to continue the search in the sub-tree
        else if (!this.isLeaf) {
            // isInLastChild indicates if the key is supposed to exist in the
            // sub-tree rooted with the last child of this node
            boolean isInLastChild = (index == numberOfKeys);
            // If the child where the key is supposed to exist has less that t keys,
            // we fill that child with a key to reach t keys
            if (children[index].numberOfKeys == lowerBoundKeyNumber)
                fillChild(index);
            // If the last child has been merged, it must have merged with the previous
            // child so we continue the search on the (idx-1)th child.
            if (isInLastChild && index > numberOfKeys)
                children[index - 1].deleteKey(key);
                // Else, we continue to the (idx)th child which now has at least t keys
            else
                children[index].deleteKey(key);
        }
        // else it is a leaf and the key is not exist in the tree
    } // end of deleteKey function

    private void fillChild(int index) {
        // If children[index - 1] as more than t-1 keys. borrow a key from it
        if (index > 0 && children[index - 1].numberOfKeys > lowerBoundKeyNumber)
            borrowKeyFromPrev(index);
            // If children[index + 1] as more than t-1 keys. borrow a key from it
        else if (index < numberOfKeys && children[index + 1].numberOfKeys > lowerBoundKeyNumber)
            borrowKeyFromNext(index);
            // If both child have less then t keys, merge it with it's right sibling
            // or it's left sibling in case children[index] is the last child
        else {
            if (index < numberOfKeys)
                merge(index);
                // it is the last child
            else
                merge(index - 1);
        }
    }

    private void removeFromLeaf(int index) {
        for (int i = index + 1; i < numberOfKeys; i++)
            keys[i - 1] = keys[i];
        numberOfKeys--;
    }

    private void removeFromNonLeaf(int index) {
        // Case 1: the left sibling has more than t-1
        if (children[index].numberOfKeys > lowerBoundKeyNumber) {
            String prevKey = getPrevKey(index);
            keys[index] = prevKey;
            children[index].deleteKey(prevKey);
        }
        // Case 2: the right sibling has more than t-1
        else if (children[index + 1].numberOfKeys > lowerBoundKeyNumber) {
            String nextKey = getNextKey(index);
            keys[index] = nextKey;
            children[index + 1].deleteKey(nextKey);
        }
        // Case 3: both right & left siblings as lass than t keys,
        // merge the two siblings. Add the key at the middle of the new node.
        // And recursively delete the key from children[index]
        else {
            String toDelete = this.keys[index];
            merge(index);
            children[index].deleteKey(toDelete);
        }
    }

    private void merge(int index) {
        BTreeNode leftChild = children[index];
        BTreeNode rightChild = children[index + 1];
        // Taking the key from the parent node
        leftChild.keys[t - 1] = this.keys[index];
        // Coping the keys from rightChild to leftChild
        for (int i = 0; i < rightChild.numberOfKeys; i++)
            leftChild.keys[i + t] = rightChild.keys[i];
        // Coping the children pointers of rightChild if it is not a leaf
        if (!leftChild.isLeaf) {
            for (int i = 0; i <= rightChild.numberOfKeys; i++)
                leftChild.children[i + t] = rightChild.children[i];
        }
        // Moving all the keys of this node one step left
        for (int i = index; i < numberOfKeys - 1; i++)
            this.keys[i] = this.keys[i + 1];
        // Moving the children pointers of this node from index + 1 one step left
        for (int i = index + 1; i < numberOfKeys; i++)
            this.children[i] = this.children[i + 1];
        //Updating the number of keys of this node & leftChild
        this.numberOfKeys--;
        leftChild.numberOfKeys = leftChild.numberOfKeys + rightChild.numberOfKeys + 1;
    }

    private void borrowKeyFromPrev(int index) {
        BTreeNode child = children[index];
        BTreeNode leftChild = children[index - 1];
        // Moving all key in child one step forward
        for (int i = child.numberOfKeys; i >= 0; i--)
            child.keys[i + 1] = child.keys[i];
        // if child is not a leaf, moving is children pointers one step forward
        if (!child.isLeaf) {
            for (int i = child.numberOfKeys; i >= 0; i--)
                child.children[i + 1] = child.children[i];
        }
        // copy the key at index - 1 from this node to the child
        child.keys[0] = this.keys[index - 1];
        // if leftChild is not a leaf moving is last children to be the first children of child
        if (!leftChild.isLeaf) {
            child.children[0] = leftChild.children[leftChild.numberOfKeys];
        }
        // moving the rightest key of leftChild to the parent (this node)
        this.keys[index - 1] = leftChild.keys[leftChild.numberOfKeys - 1];
        // updating the number of keys of child & leftChild
        child.numberOfKeys++;
        leftChild.numberOfKeys--;
    }

    private void borrowKeyFromNext(int index) {
        BTreeNode child = children[index];
        BTreeNode rightChild = children[index + 1];
        // keys[index] is inserted as the last key in child
        child.keys[child.numberOfKeys] = this.keys[index];
        if (!rightChild.isLeaf)
            child.children[child.numberOfKeys + 1] = rightChild.children[0];
        // The first key from rightChild is inserted into the parent keys[index]
        this.keys[index] = rightChild.keys[0];
        // Moving all keys in rightChild one step left
        for (int i = 1; i < rightChild.numberOfKeys; i++)
            rightChild.keys[i - 1] = rightChild.keys[i];
        // if rightChild is not a leaf, moving is children pointers one step left
        if (!rightChild.isLeaf) {
            for (int i = 1; i <= rightChild.numberOfKeys; i++)
                rightChild.children[i - 1] = rightChild.children[i];
        }
        // updating the number of keys of child & leftChild
        child.numberOfKeys++;
        rightChild.numberOfKeys--;
    }

    private String getPrevKey(int index) {
        BTreeNode current = this.children[index];
        while (!current.isLeaf)
            current = current.children[current.numberOfKeys];
        // Return the last key of the leaf
        return current.keys[current.numberOfKeys - 1];

    }

    private String getNextKey(int index) {
        BTreeNode current = this.children[index + 1];
        while (!current.isLeaf)
            current = current.children[0];
        // Return the first key of the leaf
        return current.keys[0];
    }

    //returns the index of the first key that is greater than or equal to k
    private int searchKeyIndex(String key) {
        int i = 0;
        //keep incrementing in node while key > current value.
        while (i < numberOfKeys && key.compareTo(keys[i]) > 0)
            i++;
        return i;
    }
    // ---------------------- End of Remove Methods ----------------------

    private String DFSHelper(int depth, String output) {
        if (!isLeaf) {
            for (int i = 0; i <= numberOfKeys; i++) {
                output = this.children[i].DFSHelper(depth + 1, output);
                if (i < numberOfKeys)
                    output = output + keys[i] + "_" + depth + ",";
            }
        } else {
            for (int i = 0; i < numberOfKeys; i++) {
                output = output + keys[i] + "_" + depth + ",";

            }
        }
        return output;
    }

    protected String DFS(int depth, String output) {
        output = DFSHelper(depth, output);
        // cut the last ","
        return output.substring(0, output.length() - 1);
    }

    // ---------------------- Getters Methods ----------------------
    public int getNumberOfKeys() {
        return this.numberOfKeys;
    }

    public String[] getKeys() {
        return keys;
    }

    public boolean getIsLeaf() {
        return isLeaf;
    }

    public BTreeNode[] getChildren() {
        return children;
    }

} // end of class


