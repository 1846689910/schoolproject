package utils.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;


/**
 * any class needs to be copied (either deepCopy or shallowCopy) in the future needs to implements Copyable
 * interface and override clone() method:
 * @Override
 * public Object clone() throws CloneNotSupportedException
 * */
public interface Copyable<E> extends Serializable, Cloneable {

    Object clone() throws CloneNotSupportedException;

    default Object deepCopy(){
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
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

    default Object shallowCopy () {
        try {
            return this.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            return null;
        }
    }
}
