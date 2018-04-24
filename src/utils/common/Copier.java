package utils.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class Copier {

    /** Any class needs to be deep copied, should implements Serializable interface or extends CopyableClass
     * Any class needs to be shallow copied, should extends CopyableClass
     * (if the class implements Cloneable itself, then it should call obj.clone() itself)
     * */
    public abstract static class CopyableClass implements Serializable, Cloneable {
        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    /** the object that needs to be deep copied should implements Serializable interface */
    public static Object deepCopy (Serializable obj) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            byte[] buf = baos.toByteArray();
            ByteArrayInputStream bais = new ByteArrayInputStream(buf);
            return new ObjectInputStream(bais).readObject();
            // after you get object, then convert (someClass)deepCopy(obj);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /** the object that needs to be shallow copied should implements Cloneable interface */
    public static Object shallowCopy (CopyableClass obj) {
        try {
            return obj.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
