public class WorkingTree<K, V> {
    private Node<K, V> ROOT;

    private final LessThan<K> lessThan;
    private final EqualTo<K> equalTo;

    public WorkingTree(EqualTo<K> equalTo, LessThan<K> lessThan) {
        this.ROOT = null;
        this.lessThan = lessThan;
        this.equalTo = equalTo;
    }

    public boolean isEmpty() {
        return ROOT == null;
    }

    public void put(K key, V value) {
        this.ROOT = put(key, value, ROOT);
        // while (!check(this.ROOT)) {
        this.ROOT = fix(this.ROOT);
        // }
        this.ROOT.color = Node.Color.BLACK; // Better than making BLACK at insertion because rotations might change this
    }

    private Node<K, V> put(K key, V value, Node<K, V> node) {
        if (node == null)
            return new Node<K, V>(key, value); // Has to be at top to prevent NullPointerException
        if (equalTo.f(node.key, key))
            throw new IllegalArgumentException("Key already exists in tree");

        if (lessThan.f(node.key, key)) {
            node.right = put(key, value, node.right); // So basically only the ROOT will be BLACK as
                                                      // soon as added
            return node;
        } else {
            node.left = put(key, value, node.left);
            return node;
        }
    }

    private boolean check(Node<K, V> node) {
        if (node == null)
            return true;

        if (node.left != null && node.right != null && node.left.color == Node.Color.RED
                && node.right.color == Node.Color.RED)
            return false;

        if (node.right != null && node.right.color == Node.Color.RED)
            return false;

        if (node.left != null && node.left.left != null && node.left.color == Node.Color.RED
                && node.left.left.color == Node.Color.RED)
            return false;

        return check(node.left) && check(node.right);
    }

    private Node<K, V> fix(Node<K, V> node) {
        if (node == null)
            return null;
        node.left = fix(node.left);
        node.right = fix(node.right);
        return fixSplit(fixDouble(fixRight(node))); // Lot of debate about void vs return node
        // Split needs to be after Double, and Double needs to be after Right
    }

    private Node<K, V> fixSplit(Node<K, V> node) {
        if (node == null)
            return null;
        if (node.left != null && node.right != null) {
            if (node.left.color == Node.Color.RED && node.right.color == Node.Color.RED) {
                node.left.color = Node.Color.BLACK;
                node.right.color = Node.Color.BLACK;
                // node.color = Node.Color.RED; // Assuming current is black because previous
                // iterations should've caught
                // // that -- Not necessarily true in new model
                if (node.color == Node.Color.RED)
                    node.color = Node.Color.BLACK;
                else
                    node.color = Node.Color.RED;
            }
        }

        return node;
    }

    private Node<K, V> fixRight(Node<K, V> node) { // Using return type to avoid tracking parent -- Hope to potentially
                                                   // remove parent tracking
        if (node == null)
            return null;
        if (node.right != null) {
            if (node.right.color == Node.Color.RED) {
                Node<K, V> pivot = node.right;
                node.right = pivot.left; // What if this is RED?
                pivot.left = node;
                pivot.color = node.color;
                node.color = Node.Color.RED;
                // pivot.right = fix(pivot.right); // ---Not really needed because runs at every
                // insertion?---
                return pivot;
            }
        }

        return node;
    }

    private Node<K, V> fixDouble(Node<K, V> node) {
        if (node == null)
            return null;
        if (node.left != null && node.left.left != null) { // Safe assuming left here because rights would already have
                                                           // been caught
            if (node.left.color == Node.Color.RED && node.left.left.color == Node.Color.RED) {
                Node<K, V> pivot = node.left;
                // pivot.color = Node.Color.RED; // Not needed because split will run on the
                // pivot
                // pivot.left.color = Node.Color.BLACK;
                // node.color = Node.Color.BLACK; // IMPORTANT -- Had initially forgotten
                node.left = pivot.right;
                pivot.color = node.color;
                node.color = Node.Color.RED;
                pivot.right = node;
                // pivot.left = fix(pivot.left);
                return pivot;
            }
        }

        return node;
    }

    public void display() {
        preorder(ROOT);
    }

    private void preorder(Node<K, V> node) {
        if (node == null)
            return;
        debug(node);
        preorder(node.left);
        preorder(node.right);
    }

    @FunctionalInterface
    public interface LessThan<K> { // Something called a Functional Interface -- Enables generic class structure
        boolean f(K a, K b);
    }

    @FunctionalInterface
    public interface EqualTo<K> { // Two separate interfaces to keep them functional
        boolean f(K a, K b);
    }

    private void debug(Node<K, V> node) {
        System.out.print(node);
        System.out.print(" ");
        System.out
                .println("Run: " + node.key + " Left: " + node.left + " Right: "
                        + node.right + " Color: " + node.color);
    }
}
