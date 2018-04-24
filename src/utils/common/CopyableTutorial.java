package utils.common;

public class CopyableTutorial {
    public static void main(String[] args) {
        Truck truck = new Truck("tundra", new String[]{"door", "roof", "chasis", "light"});

        Truck truck1 = (Truck)truck.shallowCopy();
        System.out.println(truck == truck1);  // false, two different object
        System.out.println(truck.parts[0] == truck1.parts[0]);  // true, means the reference of elements of the array not changed

        Truck truck2 = (Truck)truck.deepCopy();
        System.out.println(truck == truck2);  // false, two different object
        System.out.println(truck.parts[0] == truck2.parts[0]);  // false, means the reference of elements of the array changed
    }
}
class Truck implements Copyable<Truck> {
    String name;
    String[] parts;
    public Truck(String name, String[] parts){
        this.name = name;
        this.parts = parts;
    }
    @Override
    public Object clone() throws CloneNotSupportedException{
        return super.clone();
    }
}