package utils.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class C7 {
    /** find missing number (starts from 1 to n) */
    public static int findMissingNum(int[] arr){
        int n = arr.length + 1;
        int xor = 0;
        for (int i = 0; i <= n; i ++) {
            xor ^= i;
        }
        for (int num : arr) xor ^= num;
        return xor;
    }
    @Test
    public void findMissingNumTest(){
        int[] arr = new int[]{1, 2, 4};
        System.out.println(findMissingNum(arr));
    }
    /** find unique number (all numbers dup 2 times) */
    public static int findUnique(int[] arr){
        int xor = 0;
        for (int num : arr) xor ^= num;
        return xor;
    }
    @Test
    public void findUnique(){
        int[] arr = new int[]{1, 1, 2, 3, 3};
        System.out.println(findUnique(arr));
    }
    /** find first Unique */
    public static int find1stUnique(int[] arr){
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : arr) {
            map.merge(num, 1, (a, b) -> a + b);
        }
        // map.merge(key, valueAddOn, BiFunction(v1, v2){return v1 + v2});
        // 若map.get(key)为null, 那么map.put(key, valueAddOn)
        // 否则map.put(key, BiFunction(原值, valueAddOn))
        for (int i = 0; i < arr.length; i ++) {
            if (map.get(arr[i]) == 1) return i;
        }
        return -1;
    }
    @Test
    public void find1stUniqueTest(){
        int[] arr = new int[]{1, 1, 2, 2, 3, 5, 7, 7};
        System.out.println(find1stUnique(arr));
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 1);
        map.put("b", 2);
        map.merge("a", 3, (a, b) -> a + b);
        map.forEach((k, v) -> System.out.println(k + " : " + v));
    }
    /** common numbers */
    public static List<Integer> commonNumbers(int[] a, int[] b){
        List<Integer> list = new ArrayList<>();
        int i = 0;
        int j = 0;
        while (i < a.length && j < b.length) {
            if (a[i] == b[j]) {
                list.add(a[i]);
                i ++;
                j ++;
            } else if (a[i] < b[j]) {
                i ++;
            } else {
                j ++;
            }
        }
        return list;
    }
    public static List<Integer> commonNumbers(int[] a, int []b, int[]c) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0, k = 0;
        while (i < a.length && j < b.length && k < c.length) {
            if (a[i] == b[j] && b[j] == c[k]) {
                result.add(a[i]);
                i ++;
                j ++;
                k ++;
            } else if (a[i] <= b[j] && a[i] <= c[k]) {
                i ++;
            } else if (b[j] <= a[i] && b[j] <= c[k]) {
                j ++;
            } else {
                k ++;
            }
        }
        return result;
    }
    @Test
    public void commonNumbersTest(){
        int[] a = new int[]{1, 3, 4, 5, 7};
        int[] b = new int[]{2, 4, 5, 7, 10};
        int[] c = new int[]{4, 7, 8, 10};
        System.out.println(commonNumbers(a, b));
        System.out.println(commonNumbers(a, b, c));
    }

    /** remove every char in s from str */
    public static String removeChars(String str, String s){
        if (str == null || str.length() == 0) return str;
        char[] chars = str.toCharArray();
        Set<Character> set = new HashSet<>();
        for (char c : s.toCharArray()) set.add(c);
        int slow = 0, fast = 0;
        while (fast < chars.length) {
            if (! set.contains(chars[fast])) {
                chars[slow ++] = chars[fast];
            }
            fast ++;
        }
        return new String(chars, 0, slow);
    }
    @Test
    public void removeCharsTest(){
        System.out.println(removeChars("abcabc", "ab"));
    }

    /** remove every s from str */
    public static String removeS(String str, String s) {
        if (str == null || str.length() == 0) return str;
        char[] chars = str.toCharArray();
        int slow = 0, fast = 0;
        while (fast < chars.length) {
            if (equals(chars, s, fast)) {
                fast += s.length();
            } else {
                chars[slow ++] = chars[fast ++];
            }
        }
        return new String(chars, 0, slow);
    }
    private static boolean equals(char[] chars, String s, int fast) {
        char[] chars1 = s.toCharArray();
        for (int j = 0; j < chars1.length; j ++) {
            if (chars[fast + j] != chars1[j]) return false;
        }
        return true;
    }
    @Test
    public void removeSTest(){
        System.out.println(removeS("we are ready to go", "ready"));
    }

    /** remove leading, trailing and duplicate spaces */
    public static String removeDupSpaces(String str){
        if (str == null || str.length() == 0) return str;
        int slow = 0, fast = 0;
        char[] chars = str.toCharArray();
        while (fast < chars.length) {
            if (chars[fast] == ' ' && (fast == 0 || chars[fast - 1] == ' ')) {
                fast ++;
                continue;
            }
            chars[slow ++] = chars[fast ++];
        }
        if (slow > 0 && chars[slow - 1] == ' ') {
            return new String(chars, 0, slow - 1);
        } else {
            return new String(chars, 0, slow);
        }
    }
    @Test
    public void removeDupSpacesTest(){
        System.out.println(removeDupSpaces("   hello     world    "));
    }
    /** remove all spaces */
    public static String removeAllSpaces(String str){
        if (str == null || str.length() == 0) return str;
        char[] chars = str.toCharArray();
        int slow = 0, fast = 0;
        while(fast < chars.length) {
            if (chars[fast] != ' ') {
                chars[slow ++] = chars[fast];
            }
            fast ++;
        }
        return new String(chars, 0, slow);
    }
    @Test
    public void removeAllSpacesTest(){
        System.out.println(removeAllSpaces("   hello     world    "));
    }

    /** remove neighbors but saving n chars (n >= 1)*/
    public static String removeNei(String str, int n){
        if (str == null || str.length() <= n) return str;
        char[] chars = str.toCharArray();
        int slow = n - 1, fast = n;
        while (fast < chars.length) {
            if (chars[fast] != chars[slow - (n - 1)]) {
                chars[++ slow] = chars[fast];
            }
            fast ++;
        }
        return new String(chars, 0, slow + 1);
    }
    @Test
    public void removeNeiTest(){
        System.out.println(removeNei("aaaabbbbbbdccccc", 2));
    }
    /** remove remote neighbors if possible
     * "abbbaaccz" → "aaaccz" → "ccz" → "z"
     * "aabccdc" → "bccdc" → "bdc"
     * */
    public static String removeRemoteNei(String str){
        if (str == null || str.length() <= 1) return str;
        int slow = -1, fast = 0;
        char[] chars = str.toCharArray();
        while (fast < chars.length) {
            if (slow == -1 || chars[slow] != chars[fast]) {
                chars[++ slow] = chars[fast];
            } else {
                while (fast + 1 < chars.length && chars[fast] == chars[fast + 1]) {
                    fast ++;
                }
                slow --;
            }
            fast ++;
        }
        return new String(chars, 0, slow + 1);
    }
    @Test
    public void removeRemoteNeiTest(){
        System.out.println(removeRemoteNei("abbbaaccz"));
    }

    /** determine if one string is another's substring */
    public static int strStr(String l, String s){
        if (l.length() < s.length()) return -1;
        if (s.length() == 0) return 0;
        for (int i = 0; i <= l.length() - s.length(); i ++) {
            if (equals(l, s, i)) {
                return i;
            }
        }
        return -1;
    }
    private static boolean equals(String l, String s, int i) {
        for (int j = 0; j < s.length(); j ++) {
            if (l.charAt(j + i) != s.charAt(j)) return false;
        }
        return true;
    }
    @Test
    public void strStrTest(){
        System.out.println(strStr("hello world", "llo"));
    }
}
