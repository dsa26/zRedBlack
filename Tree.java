public class Tree<K, V> {
    private Node<K, V> ROOT;

    private final LessThan<K> lessThan;
    private final EqualTo<K> equalTo;

    public Tree(LessThan<K> lessThan, EqualTo<K> equalTo) {
        this.ROOT = null;
        this.lessThan = lessThan;
        this.equalTo = equalTo;
    }

    public boolean isEmpty() {
        return ROOT == null;
    }

    public void put(K key, V value) {
        this.ROOT = put(key, value, ROOT, null);
    }

    private Node<K, V> put(K key, V value, Node<K, V> node, Node<K, V> parent) {
        if (node == null)
            return new Node<K, V>(key, value, parent); // Has to be at top to prevent NullPointerException
        if (equalTo.f(node.key, key))
            throw new IllegalArgumentException("Key already exists in tree");

        if (lessThan.f(node.key, key)) {
            node.right = put(key, value, node.right, node);
            return node;
        } else {
            node.left = put(key, value, node.left, node);
            return node;
        }
    }

    @FunctionalInterface
    public interface LessThan<K> { // Something called a Functional Interface -- Enables generic class structure
        boolean f(K a, K b);
    }

    @FunctionalInterface
    public interface EqualTo<K> { // Two separate interfaces to keep them functional
        boolean f(K a, K b);
    }
}
