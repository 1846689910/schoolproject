package utils.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
/**
 * JProperties extends Properties and include all the methods that Properties has
 * */
public class JProperties extends Properties {
    String path;
    public JProperties(String path){
        super();
        this.path = path;
    }
    public boolean storeProperties(String comments){
        try {
            FileOutputStream fos = new FileOutputStream(new File(path));
            super.store(fos, comments);
            return true;
        }catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void loadProperties () {
        try {
            FileInputStream fis = new FileInputStream(new File(path));
            super.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object set(String key, String value) {
        Object o = super.setProperty(key, value);
        storeProperties("");
        return o;
    }

    public Object set(String key, String value, String comments){
        Object o = super.setProperty(key, value);
        storeProperties(comments);
        return o;
    }

    public String get(String key) {
        return super.getProperty(key);
    }

}
