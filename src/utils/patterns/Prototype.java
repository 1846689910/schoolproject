package utils.patterns;

import java.util.HashMap;
import java.util.Map;

public class Prototype {

}
/** Prototype Pattern:
 * cloning of an existing object instead of creating new one and can also be customized as per the requirement
 * 读取对象或者数据结构比较耗时的情况下，可以通过cache保存一份数据，需要创建类似数据时直接使用clone
 * 不过可以尝试让该类implements Copyable并重写clone()方法，之后就可以直接用obj.deepCopy()和obj.shallowCopy()
 * */
abstract class Shape implements Cloneable {
    public abstract void draw();
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
class Circle extends Shape {
    @Override
    public void draw(){
        System.out.println("draw circle");
    }
}
class Square extends Shape {
    @Override
    public void draw(){
        System.out.println("draw square");
    }
}
class ShapeCache {
    Map<String, Shape> shapeCache;
    public ShapeCache(){
        this.shapeCache = new HashMap<>();
        this.shapeCache.put("circle", new Circle());
        this.shapeCache.put("square", new Square());
    }
    public Shape getShape(String type) {
        Shape shape = shapeCache.get(type);
        if (shape == null) {
            return null;
        } else {
            return (Shape)shape.clone();
        }
    }
}