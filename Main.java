public class Main {
    public static void main(String[] args) {
        WorkingTree<String, Boolean> tree = new WorkingTree<>((a, b) -> a.compareTo(b) == 0, (a, b) -> a.compareTo(b) < 0);
        System.out.println("----------");
        tree.put("S", true);
        tree.display();
        System.out.println("----------");
        tree.put("E", true);
        tree.display();
        System.out.println("----------");
        tree.put("A", true);
        tree.display();
        System.out.println("----------");
        tree.put("R", true);
        tree.display();
        System.out.println("----------");
        tree.put("C", true);
        tree.display();
        System.out.println("----------");
        tree.put("H", true);
        tree.display();
        System.out.println("----------");
        tree.put("X", true);
        tree.display();
        System.out.println("----------");
        tree.put("M", true);
        tree.display();
        System.out.println("----------");
        tree.put("P", true);
        tree.display();
        System.out.println("----------");
        tree.put("L", true);
        tree.display();
        System.out.println("----------");
    }
}
