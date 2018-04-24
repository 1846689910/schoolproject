package utils.patterns;

public class Decorator {
    public static void main(String[] args) {
        Child child = new Child();
        child.basicWork();
        Mother mother = new Mother(child);
        mother.basicWork();
        Father father = new Father(mother);
        father.basicWork();
    }
}
/** Decorator Pattern:
 * 增强一个类的基本功能，可以让这些类互相装饰
 * 1 装饰类的内部维护一个被装饰类的引用
 * 2 让装饰类有一个共同的父类或者父接口
 * */
interface BasicWork{
    public void basicWork();
}

class Child implements BasicWork {
    @Override
    public void basicWork(){
        System.out.println("draw a sketch");
    }
}

class Mother implements BasicWork {
    private BasicWork basicWork;  // 需要被增强的类
    public Mother(BasicWork basicWork) {
        this.basicWork = basicWork;
    }
    @Override
    public void basicWork(){
        basicWork.basicWork();
        System.out.println("fill sketch with color");
    }
}

class Father implements BasicWork {
    private BasicWork basicWork;  // 需要被增强的类
    public Father(BasicWork basicWork){
        this.basicWork = basicWork;
    }
    @Override
    public void basicWork(){
        basicWork.basicWork();
        System.out.println("hang the picture on wall");
    }
}
