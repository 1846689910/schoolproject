package utils.common;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JDate {
    final public static String stdFormat = "yyyy-MM-dd HH:mm:ss";

    public static void main(String[] args) {
        System.out.println(getStdTime());
    }

    public static String getStdTime(){
        return new SimpleDateFormat(stdFormat).format(new Date());
    }
    public static String getStdTime(long time) {return new SimpleDateFormat(stdFormat).format(new Date(time));}
    public static String getFormatedTime(String format){return new SimpleDateFormat(format).format(new Date());}
}
