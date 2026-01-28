public class Tree<K, V> {
    private Node<K, V> ROOT;

    private final LessThan<K> lessThan;
    private final EqualTo<K> equalTo;

    public Tree(EqualTo<K> equalTo, LessThan<K> lessThan) {
        this.ROOT = null;
        this.lessThan = lessThan;
        this.equalTo = equalTo;
    }

    public boolean isEmpty() {
        return ROOT == null;
    }

    public int size() {
        return size(ROOT);
    }

    private int size(Node<K, V> node) {
        if (node == null)
            return 0;
        if (node.left == null && node.right == null)
            return 1;
        return 1 + size(node.left) + size(node.right);
    }

    public void put(K key, V value) {
        this.ROOT = put(key, value, ROOT);
        this.ROOT = fix(this.ROOT);
        this.ROOT.color = Node.Color.BLACK; // Better than making BLACK at insertion because rotations might change this
    }

    private Node<K, V> put(K key, V value, Node<K, V> node) {
        if (node == null)
            return new Node<K, V>(key, value); // Has to be at top to prevent NullPointerException
        if (equalTo.f(node.key, key))
            throw new IllegalArgumentException("Key already exists in tree");

        if (lessThan.f(node.key, key)) {
            node.right = put(key, value, node.right);
            return node;
        } else {
            node.left = put(key, value, node.left);
            return node;
        }
    }

    private Node<K, V> fix(Node<K, V> node) {
        if (node == null)
            return null;
        node.left = fix(node.left);
        node.right = fix(node.right);
        return fixSplit(fixDouble(fixRight(node))); // Lot of debate about void vs return node
        // Split needs to be after Double, and Double needs to be after Right
    }

    private Node<K, V> fixRight(Node<K, V> node) { // Using return type to avoid tracking parent
        if (node == null)
            return null;
        if (node.right != null) {
            if (node.right.color == Node.Color.RED) {
                Node<K, V> pivot = node.right;
                node.right = pivot.left; // What if this is RED? -- Explained in video
                pivot.left = node;
                pivot.color = node.color; // Ensures recursion works
                node.color = Node.Color.RED;
                return pivot;
            }
        }

        return node;
    }

    private Node<K, V> fixDouble(Node<K, V> node) {
        if (node == null)
            return null;
        if (node.left != null && node.left.left != null) {
            if (node.left.color == Node.Color.RED && node.left.left.color == Node.Color.RED) {
                Node<K, V> pivot = node.left;
                node.left = pivot.right;
                pivot.right = node; // Will be RED, but pivot.left is also RED, so split will handle
                pivot.color = node.color;
                node.color = Node.Color.RED;
                return pivot;
            }
        }

        return node;
    }

    private Node<K, V> fixSplit(Node<K, V> node) {
        if (node == null)
            return null;
        if (node.left != null && node.right != null) {
            if (node.left.color == Node.Color.RED && node.right.color == Node.Color.RED) {
                node.left.color = Node.Color.BLACK;
                node.right.color = Node.Color.BLACK;
                if (node.color == Node.Color.RED)
                    node.color = Node.Color.BLACK;
                else
                    node.color = Node.Color.RED;
            }
        }

        return node;
    }

    public void display() {
        System.out.println("----------");
        preorder(ROOT);
        System.out.println("----------");
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

    public Node<K, V> get(K key) {
        Node<K, V> current = ROOT;
        while (current != null) {
            if (equalTo.f(current.key, key)) {
                return current;
            } else if (lessThan.f(current.key, key)) {
                current = current.right;
            } else {
                current = current.left;
            }
        }

        return null;
    }

    public boolean contains(K key) {
        return get(key) != null;
    }

    public Node<K, V> min() {
        return min(ROOT);
    }

    private Node<K, V> min(Node<K, V> node) {
        if (node == null)
            return null;
        Node<K, V> current = node;
        while (current.left != null)
            current = current.left;
        return current;
    }

    public Node<K, V> max() {
        return max(ROOT);
    }

    private Node<K, V> max(Node<K, V> node) {
        if (node == null)
            return null;
        Node<K, V> current = node;
        while (current.right != null)
            current = current.right;
        return current;
    }

    public Node<K, V> floor(K key) {
        Node<K, V> current = ROOT;
        Node<K, V> floor = null;
        while (current != null) {
            if (lessThan.f(current.key, key)) {
                floor = current;
                current = current.right;
            } else {
                current = current.left;
            }
        }
        return floor;
    }

    public Node<K, V> ceiling(K key) {
        Node<K, V> current = ROOT;
        Node<K, V> ceiling = null;
        while (current != null) {
            if (lessThan.f(current.key, key)) {
                current = current.right;
            } else {
                ceiling = current;
                current = current.left;
            }
        }
        return ceiling;
    }

    public int height() {
        return height(ROOT);
    }

    private int height(Node<K, V> node) {
        if (node == null)
            return 0;
        if (node.left == null && node.right == null)
            return 1;
        return 1 + Math.max(height(node.left), height(node.right)); // Gets the length of the maximal path to a leaf
    }

    public String toString() {
        return toString(ROOT);
    }

    private String toString(Node<K, V> node) {
        if (node == null)
            return "";
        return toString(node.left) + node.key + " " + toString(node.right);
    }
}
