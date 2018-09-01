package utils.algorithm;

import org.junit.Test;
import scala.Char;

import java.util.*;

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
    public static void swap(int[] arr, int i, int j){
        int num = arr[i];
        arr[i] = arr[j];
        arr[j] = num;
    }
    public static void swap(char[] arr, int i, int j) {
        char c = arr[i];
        arr[i] = arr[j];
        arr[j] = c;
    }
    public static void reverse(int[] arr, int i, int j) {
        while (i < j) {
            swap(arr, i ++, j --);
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

    /** right shift by N chars
     * right shift abcde by 2 chars -> deabc
     * */
    public static String rightShift(String s, int n){
        if (s == null || s.length() <= 1) return s;
        char[] chars = s.toCharArray();
        n %= chars.length;
        reverse(chars, 0, chars.length - n - 1);
        reverse(chars, chars.length - n, chars.length - 1);
        reverse(chars, 0, chars.length - 1);
        return new String(chars);
    }
    @Test
    public void rightShiftTest(){
        System.out.println(rightShift("abcde", 2));
    }

    /** String replace
     * replace all s with t in str
     * */
    public static String replace(String str, String s, String t) {
        StringBuilder sb = new StringBuilder();
        int from = 0;
        int idx = str.indexOf(s, from);
        while (idx != -1) {
            sb.append(str, from, idx).append(t);
            from = idx + s.length();
            idx = str.indexOf(s, from);
        }
        sb.append(str, from, str.length());
        return sb.toString();
    }
    @Test
    public void replaceTest(){
        System.out.println(replace("hello world", "ll", "l"));
    }

    /** array reorder */
    public static int[] arrayReorder(int[] arr){
        if (arr == null) return null;
        if (arr.length % 2 == 0) {
            arrayReorderHelper(arr, 0, arr.length - 1);
        } else {
            arrayReorderHelper(arr, 0, arr.length - 2);
        }
        return arr;
    }
    private static void arrayReorderHelper(int[] arr, int left, int right){
        int len = right - left + 1;
        if (len <= 2) return;
        int lmid = left + len / 4, mid = left + len / 2, rmid = left + len * 3 / 4;
        reverse(arr, lmid, mid - 1);
        reverse(arr, mid, rmid - 1);
        reverse(arr, lmid, rmid - 1);
        arrayReorderHelper(arr, left, left + (lmid - left) * 2 - 1);
        arrayReorderHelper(arr, left + (lmid - left) * 2, right);
    }
    @Test
    public void arrayReorderTest(){
        System.out.println(Arrays.toString(arrayReorder(new int[]{1, 2, 3, 4, 5, 6, 7})));
    }

    /** decompress a string */
    public static String decompress(String str) {
        if (str == null || str.length() == 0) return str;
        char[] chars = str.toCharArray();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < chars.length; i ++) {
            char c = chars[i ++];
            int count = (int)(chars[i] - '0');
            while (count > 0) {
                sb.append(c);
                count --;
            }
        }
        return sb.toString();
    }
    @Test
    public void decompressTest(){
        String s = "a1b2c3";
        System.out.println(decompress(s));
    }

    /** longest substring without repeated character or with n repeated characters*/
    public static int longestSubstring(String str, int n){
        if (str == null || str.length() == 0) return 0;
        char[] chars = str.toCharArray();
        Map<Character, Integer> map = new HashMap<>();
        int slow = 0, fast = 0, longest = 0, soluStart = 0, soluEnd = 0;
        while (fast < chars.length) {
            if (map.containsKey(chars[fast]) && map.get(chars[fast]) == n) {
                map.remove(chars[slow ++]);
            } else {
                Integer count = map.get(chars[fast]);
                if (count == null) {
                    map.put(chars[fast ++], 1);
                } else {
                    map.put(chars[fast ++], count + 1);
                }
                if (longest < fast - slow) {
                    longest = fast - slow;
                    soluStart = slow;
                    soluEnd = fast;
                }
            }
        }
        System.out.println(new String(chars, soluStart, longest));
        return longest;
    }
    @Test
    public void longestSubstringTest(){
        String s = "aaaabbbcde";
        System.out.println(longestSubstring(s, 1));
    }
    /** smallest window
     * smallest window in s containing all letters in t
     * */
    public static int smallestWindow(String s, String t){
        if (s.length() < t.length()) return -1;
        if (t.length() == 0) return 0;
        int shortest = s.length();
        int start = -1, end = -1, solutStart = -1, solutEnd = -1;
        Map<Character, List<Integer>> map = new HashMap<>();
        for (char c : t.toCharArray()) {
            List<Integer> list = map.get(c);
            if (list == null) {
                map.put(c, Arrays.asList(1, 1));
            } else {
                Integer n = list.get(0);
                map.put(c, Arrays.asList(n, n));
            }
        }
        for (int i = 0; i <= s.length() - t.length(); i ++) {
            for (int j = i; j < s.length(); j ++) {
                char c = s.charAt(j);
                List<Integer> list = map.get(c);
                if (list != null && list.get(1) > 0) {
                    list.set(1, list.get(1) - 1);
                }
                if (allZero(map)) {
                    start = i;
                    end = j;
                    if (shortest >= end - start + 1) {
                        shortest = end - start + 1;
                        solutStart = start;
                        solutEnd = end;
                    }
                    reset(map);
                    break;
                }
            }
        }
        if (start == -1 || end == -1) return -1;
        System.out.println(solutStart + " : " + solutEnd);
        System.out.println(s.substring(solutStart, solutEnd + 1));
        return shortest;
    }
    public static boolean allZero(Map<Character, List<Integer>> map) {
        for (Map.Entry<Character, List<Integer>> entry : map.entrySet()) {
            if (entry.getValue().get(1) > 0) return false;
        }
        return true;
    }
    public static void reset(Map<Character, List<Integer>> map) {
        map.forEach((k, v) -> {
            v.set(1, v.get(0));
        });
    }

    @Test
    public void smallestWindowTest(){
        System.out.println(smallestWindow("hello world", "ow"));
    }

    /** find all anagrams */
    public static List<Integer> allAnagrams(String s, String l){
        List<Integer> starts = new ArrayList<>();
        if (l.length() < s.length() || l.length() == 0) return starts;
        Map<Character, Integer> map = new HashMap<>();
        for (char c : s.toCharArray()) {
            map.merge(c, 1, (v1, v2) -> v1 + v2);
        }
        int match = 0;
        char[] chars = l.toCharArray();
        for (int i = 0; i < chars.length; i ++) {
            Integer count = map.get(chars[i]);
            if (count != null) {
                map.put(chars[i], count - 1);
                if (count == 1) match ++;
            }
            if (i >= s.length()) {
                char c = chars[i - s.length()];
                count = map.get(c);
                if (count != null) {
                    map.put(c, count + 1);
                    if (count == 0) match --;
                }
            }
            if (match == map.size()) starts.add(i - s.length() + 1);
        }
        return starts;
    }
    @Test
    public void allAnagramsTest(){
        System.out.println(allAnagrams("abc", "dcbaacbecab"));
    }
    /** is anagram of another string */
    public static boolean isAnagram(String s, String t){
        if (s.length() != t.length()) return false;
        int[] count = new int[26];
        for (char c : s.toCharArray()) {
            count[c - 'a'] ++;
        }
        for (char c : t.toCharArray()) {
            count[c - 'a'] --;
        }
        for (int num : count) {
            if (num != 0) return false;
        }
        return true;
    }
    @Test
    public void isAnagramTest(){
        System.out.println(isAnagram("abc", "cab"));
    }
}
