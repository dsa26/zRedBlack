public class Node<K, V> {
    public enum Color {
        BLACK,
        RED
    }

    public Node<K, V> parent;
    public K key;
    public V value;
    public Node<K, V> left;
    public Node<K, V> right;
    public Color color;

    public Node(K key, V value, Node<K, V> parent) {
        this.parent = parent;
        this.key = key;
        this.value = value;
        this.color = Color.RED;
    }
}