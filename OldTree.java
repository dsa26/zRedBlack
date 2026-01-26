public class OldTree<K, V> {
    private Node<K, V> ROOT;

    private final LessThan<K> lessThan;
    private final EqualTo<K> equalTo;

    public OldTree(EqualTo<K> equalTo, LessThan<K> lessThan) {
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
                pivot.right = fixRight(pivot.right); // Not really needed because runs at every insertion?
                // fixParents(pivot); // two-line fix, but using method for consistency
                pivot.parent = node.parent;
                node.parent = pivot;
                node.right.parent = node;
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
                node.left.color = Node.Color.BLACK;
                node.color = Node.Color.BLACK;
                node.parent.left = node.right;
                // Safe assuming left here because rights would already have been caught
                // /* */ node.parent.left.parent = node.parent; // Didn't realize need for this
                // until debugging
                // /* */ node.parent = node.parent.parent;
                node.right = node.parent;
                node.parent = node.parent.parent;
                // fixParents(node);
                // /* */ node.right.parent = node;
                node.right.parent = node;
                node.left.parent = node;
                if (node.right.left != null)
                    node.right.left.parent = node.right;
            }
        }
        System.out
                .println("Run: " + node.key + " Parent: " + node.parent + " Left: " + node.left + " Right: "
                        + node.right);
        node.left = fixDouble(node.left);
        node.right = fixDouble(node.right);
        return node;
    }

    private void fixParents(Node<K, V> node) {
        if (node == null)
            return;
        if (node.left != null)
            node.left.parent = node;
        if (node.right != null)
            node.right.parent = node;
        fixParents(node.left);
        fixParents(node.right);
    }

    public void display() {
        levelDisplay(ROOT, 0);
    }

    private void levelDisplay(Node<K, V> node, int ite) {
        if (node == null || ite > 10)
            return;
        levelDisplay(node.left, ite + 1);
        System.out.println("Key: " + node.key + " Color: " + node.color);
        levelDisplay(node.right, ite + 1);
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
