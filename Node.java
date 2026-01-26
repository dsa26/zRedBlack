public class Node<K, V> {
    public enum Color {
        BLACK,
        RED
    }

    public K key;
    public V value;
    public Node<K, V> left;
    public Node<K, V> right;
    public Color color;

    public Node(K key, V value) {
        this.color = Node.Color.RED;
        this.key = key;
        this.value = value;
    }
}