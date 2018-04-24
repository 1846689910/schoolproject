package utils.io;

public class JPropertiesTutorial {
    public static void main(String[] args) {
        JProperties properties = new JProperties("D:\\a.txt");
        properties.set("a", "123");
        properties.set("b", "456");
        System.out.println(properties.get("a"));
        properties.set("c", "789", "add a property c");
        System.out.println(properties.get("c"));
    }
}
