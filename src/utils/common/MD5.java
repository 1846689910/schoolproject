package utils.common;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
public class MD5 {
    public static void main(String[] args) {
        String s = "hello world";
        System.out.println(getMD5(s.getBytes()));
        String s1 = "hello world1";
        System.out.println(getMD5(s1.getBytes()));
    }
    public static String getMD5(byte[] buf) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(buf);
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
