package utils.common;

public class CopierTutorial {
    public static void main(String[] args) {
        Car car = new Car("civic", new String[]{"door", "roof", "chasis", "light"});

        Car car1 = (Car)Copier.shallowCopy(car);
        System.out.println(car1 == car);  // false, two different car
        System.out.println(car1.parts[0] == car.parts[0]);  // true, means the reference of elements of the array not changed

        Car car2 = (Car)Copier.deepCopy(car);
        System.out.println(car2 == car);  // false, two different car
        System.out.println(car2.parts[0] == car.parts[0]);  // false, means the reference of elements of the array changed
    }
}

class Car extends Copier.CopyableClass{
    String name;
    String[] parts;
    public Car(String name, String[] parts){
        this.name = name;
        this.parts = parts;
    }
}
