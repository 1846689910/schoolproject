package utils.algorithm;

public class C8 {
    /** reverse a String */
    public static String reverse(String str){
        if (str == null || str.length() == 0) return str;
        char[] chars = str.toCharArray();
        int left = 0, right = chars.length - 1;
        while (left < right) {
            char c = chars[left];
            chars[left] = chars[right];
            chars[right] = c;
        }
        return new String(chars);
    }

}
