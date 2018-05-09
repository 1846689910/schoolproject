package utils.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
}
