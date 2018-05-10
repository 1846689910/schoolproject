package utils.algorithm;

import org.junit.Test;

import static utils.algorithm.C6.swap;

public class C8 {
    /** reverse a String */
    public static String reverse(String str){
        if (str == null || str.length() == 0) return str;
        char[] chars = str.toCharArray();
        int left = 0, right = chars.length - 1;
        while (left < right) {
            swap(chars, left ++, right --);
        }
        return new String(chars);
    }

    public static String reverse(String str, int i, int j) {
        if (str == null || str.length() == 0) return str;
        char[] chars = str.toCharArray();
        while (i < j) {
            swap (chars, i ++, j --);
        }
        return new String(chars);
    }
    @Test
    public void reverseTest(){
        System.out.println(reverse("hello"));
        System.out.println(reverse("hello", 1, 3));
    }

    public static void reverse(char[] chars, int i, int j) {
        while (i < j) {
            swap(chars, i ++, j --);
        }
    }

    /** reverse words */
    public static String reverseWords(String str) {
        if (str == null || str.length() == 0) return str;
        char[] chars = str.toCharArray();
        int start = 0;
        for (int i = 0; i < chars.length; i ++) {
            if (chars[i] != ' ' && (i == 0 || chars[i - 1] == ' ')) {
                start = i;
            }
            if (chars[i] != ' ' && (i == chars.length - 1 || chars[i + 1] == ' ')) {
                reverse(chars, start, i);
            }
        }
        reverse(chars, 0, chars.length - 1);
        return new String(chars);
    }
    @Test
    public void reverseWordsTest(){
        System.out.println(reverseWords("Hello how are you"));
    }
}
