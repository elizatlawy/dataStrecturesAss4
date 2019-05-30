public class HashList {
    // ---------------------- fields ----------------------
    private HashListElement first;

    // ---------------------- constructor ----------------------
    public HashList() {
        first = null;
    }
    // ---------------------- methods ----------------------

    //Adds key to the beginning of this list
    public void addFirst(int key) {
        first = new HashListElement(key, first);
    }


    //Returns true if this list contains the specified key
    public boolean contains(int key) {
        boolean output = false;
        for (HashListElement curr = first; curr != null & !output; curr = curr.getNext())
            output = key == curr.getData();
        return output;
    }

    //Returns a String representing this object
    public String toString() {
        String output = "<";
        HashListElement current = first;
        while (current != null) {
            output = output + current.toString();
            current = current.getNext();
            if (current != null)
                output = output + ", ";
        }
        output = output + ">";
        return output;
    }
}
