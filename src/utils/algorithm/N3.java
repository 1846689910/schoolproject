package utils.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class N3 {
    /** 2 Sum
     * time O(n), space O(1)
     * */
    public static boolean twoSum(int[] arr, int target){
        if (arr == null || arr.length <= 1) return false;
        Arrays.sort(arr);
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int cur = arr[left] + arr[right];
            if (cur == target) {
                return true;
            } else if (cur < target) {
                left ++;
            } else {
                right --;
            }
        }
        return false;
    }
    public static boolean twoSum1(int[] arr, int target){
        if (arr == null || arr.length <= 1) return false;
        Set<Integer> set = new HashSet<>();
        for (int num : arr) {
            if (set.contains(target - num)) return true;
            set.add(num);
        }
        return false;
    }
    @Test
    public void twoSumTest(){
        int[] arr = new int[]{1, 2, 3, 5, 7, 9};
        System.out.println(twoSum(arr, 9));
        System.out.println(twoSum(arr, 101));
        System.out.println(twoSum1(arr, 9));
        System.out.println(twoSum1(arr, 101));
    }

    /** 2 sum all pair 1(all possible indices combination) */
    public static List<List<Integer>> twoSumAllPair1(int[] arr, int target){
        List<List<Integer>> result = new ArrayList<>();
        Map<Integer, List<Integer>> map = new HashMap<>();
        for (int i = 0; i < arr.length; i ++) {
            List<Integer> indices = map.get(target - arr[i]);
            if (indices != null) {
                for (int j : indices) {
                    result.add(Arrays.asList(j, i));
                }
            }
            if (! map.containsKey(arr[i])) {
                map.put(arr[i], new ArrayList<>());
            }
            map.get(arr[i]).add(i);
        }
        return result;
    }
    @Test
    public void twoSumAllPair1Test(){
        int[] arr = new int[]{1, 3, 5, 7, 9};
        System.out.println(twoSumAllPair1(arr, 10));
        System.out.println(twoSumAllPair1(arr, 11));
    }
    /** two sum all pair 2 (all non-duplicate value combination) */
    public static List<List<Integer>> twoSumAllPair2(int[] arr, int target){
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(arr);
        int left = 0, right = arr.length - 1;
        while (left < right) {
            if (left > 0 && arr[left] == arr[left - 1]) {
                left ++;
                continue;
            }
            int cur = arr[left] + arr[right];
            if (cur == target) {
                result.add(Arrays.asList(arr[left ++], arr[right]));
                while (left < right && arr[left] == arr[left - 1]) left ++;
            } else if (cur < target) {
                left ++;
            } else {
                right --;
            }
        }
        return result;
    }
    @Test
    public void twoSumAllPair2Test(){
        int[] arr = new int[]{1, 3, 5, 7, 9};
        System.out.println(twoSumAllPair2(arr, 10));
        System.out.println(twoSumAllPair2(arr, 11));
    }
}
