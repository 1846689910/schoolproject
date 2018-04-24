package utils.patterns;

public class Factory {
}
/** Factory Pattern:
 * define an interface or abstract class for creating an object
 * but let the subclasses decide which class to instantiate
 * */
interface IPet{
    public static final String PATTERN = "Factory";
    public abstract void play();
}
class Dog implements IPet {
    @Override
    public void play(){}
}
class Cat implements IPet {
    @Override
    public void play(){}
}
class Factory1 {
    public IPet getPet(String name) {
        switch (name) {
            case "dog":
                return new Dog();
            case "cat":
                return new Cat();
            default:
                return () -> {
                    System.out.println("default pet");
                };
        }
    }
}

