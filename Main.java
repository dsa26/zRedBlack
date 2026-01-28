public class Main {
    public static void main(String[] args) {
        Tree<String, Boolean> tree = new Tree<>((a, b) -> a.compareTo(b) == 0,
                (a, b) -> a.compareTo(b) < 0);
        tree.put("S", true);
        tree.display();
        tree.put("E", true);
        tree.display();
        tree.put("A", true);
        tree.display();
        tree.put("R", true);
        tree.display();
        tree.put("C", true);
        tree.display();
        tree.put("H", true);
        tree.display();
        tree.put("X", true);
        tree.display();
        tree.put("M", true);
        tree.display();
        tree.put("P", true);
        tree.display();
        tree.put("L", true);
        tree.display();
    }
}
