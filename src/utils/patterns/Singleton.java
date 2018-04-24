package utils.patterns;

public class Singleton {
}
/** Singleton Pattern */
final class EagerSingleton {
    private static final EagerSingleton instance = new EagerSingleton();
    private EagerSingleton(){}
    public static EagerSingleton getInstance (){
        return instance;
    }
}
final class LazySingleton {
    private static volatile LazySingleton instance = null;
    private LazySingleton(){}
    public static LazySingleton getInstance () {
        if (instance == null) {
            synchronized (LazySingleton.class) {
                if (instance == null) {
                    instance = new LazySingleton();
                }
            }
        }
        return instance;
    }
}
