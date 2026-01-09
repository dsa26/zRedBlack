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
        this.ROOT = fix(put(key, value, ROOT, null));
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

    private Node<K, V> fix(Node<K, V> node) {
        return fixDouble(fixRight(fixSplit(node)));
    }

    private Node<K, V> fixRight(Node<K, V> node) { // Using return type to avoid tracking parent -- Hope to potentially
                                                   // remove parent tracking
        if (node == null)
            return null;
        if (node.right != null) {
            if (node.right.color == Node.Color.RED) {
                Node<K, V> pivot = node.right;
                node.right = pivot.left;
                pivot.left = node;
                pivot.right = fixRight(pivot.right);
                return pivot;
            }
        }
        node.left = fixRight(node.left);
        node.right = fixRight(node.right);
        return node;
    }

    private Node<K, V> fixSplit(Node<K, V> node) {
        if (node == null)
            return null;
        if (node.left != null && node.right != null) {
            if (node.left.color == Node.Color.RED && node.right.color == Node.Color.RED) {
                node.left.color = Node.Color.BLACK;
                node.right.color = Node.Color.BLACK;
                node.color = Node.Color.RED; // Assuming current is black because previous iterations should've caught
                                             // that
            }
        }
        node.left = fixSplit(node.left);
        node.right = fixSplit(node.right);
        return node;
    }

    private Node<K, V> fixDouble(Node<K, V> node) {
        if (node == null)
            return null;
        if (node.left != null) {
            if (node.color == Node.Color.RED && node.left.color == Node.Color.RED) {
                Node<K, V> pivot = node;
                node.left.color = Node.Color.BLACK;
                node.color = Node.Color.BLACK;
                node.parent.left = node.right; // Safe assuming left here because rights would already have been caught
                pivot.right = node.parent;
                return pivot;
            }
        }
        node.left = fixDouble(node.left);
        node.right = fixDouble(node.right);
        return node;
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
