public class HashListElement {
    // ---------------------- fields ----------------------
    private int data;
    private HashListElement next;

    // ---------------------- constructors ----------------------
    public HashListElement(int data, HashListElement next) {
        this.data = data;
        this.next = next;
    }
    public HashListElement(int data) {
        this(data, null);
    }

    // ---------------------- Methods ----------------------
    public HashListElement getNext() {
        return next;
    }

    public void setNext(HashListElement next) {
        this.next = next;
    }

    public int getData() {
        return data;
    }

    public int setData(int data) {
        int tmp = this.data;
        this.data = data;
        return tmp;
    }
}
