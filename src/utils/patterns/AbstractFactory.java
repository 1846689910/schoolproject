package utils.patterns;

public class AbstractFactory {
    public static void main(String[] args) {
        Honda hondaJapan = new Honda();
        Toyota toyotaAmerica = new Toyota();
        ICar car = hondaJapan.produceCar();
        Civic civic = (Civic)car;
    }
}
/** Abstract Factory Pattern:
 * define an interface or abstract class for creating families of related (or dependent) objects
 * but without specifying their concrete sub-classes.
 * */
interface ICar{
    public static final String PATTERN = "Abstract Pattern";
    public abstract void run();
}
interface ICarMaker{
    public abstract ICar produceCar();
}
class Civic implements ICar {
    @Override
    public void run(){}
}
class Rav4 implements ICar {
    @Override
    public void run(){}
}
class Honda implements ICarMaker {
    @Override
    public ICar produceCar(){
        return new Civic();
    }
}
class Toyota implements ICarMaker {
    @Override
    public ICar produceCar(){
        return new Rav4();
    }
}
