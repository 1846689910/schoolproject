package utils.algorithm;

import org.junit.Test;

import java.util.Arrays;
import java.util.Dictionary;

public class C2 {
    /** get result of a ^ b
     * time O(logb)
     * */
    public static long pow(int a, int b){
        if (a == 0) return 0;
        if (b == 0) return 1;
        long half = pow(a, b / 2);
        if (b % 2 == 0) {
            return half * half;
        } else {
            return half * half * a;
        }
    }
    public static long pow1(int a, int b) {
        if (a == 0) return 0;
        if (b == 0) return 1;
        return b * pow(a, b - 1);
    }
    @Test
    public void powTest(){
        System.out.println(pow(2, 3));
        System.out.println(pow1(3, 3));
    }

    /**Binary Search
     * */
    public static int binarySearch(int[] arr, int target){
        if (arr == null || arr.length == 0) return -1;
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] > target) {
                right = mid - 1;
            } else if (arr[mid] < target) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }
    public static int binarySearch1(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;
        int left = 0, right = arr.length - 1;
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid;
            } else {
                right = mid;
            }
        }
        if (arr[left] == target) return left;
        if (arr[right] == target) return right;
        return -1;
    }
    @Test
    public void binarySearchTest(){
        int[] arr = {1, 5, 9, 10, 12, 15};
        System.out.println(binarySearch(arr, 12));
        System.out.println(binarySearch1(arr, 12));
    }

    /**
     * Dictionary Search
     * search in an unknown size array (very large array)
     * */
    public static int dictSearch (Dictionary dict, int target) {
        int left = 0, right = 1;
        while (dict.get(right) != null && (int)dict.get(right) < target) {
            right *= 2;
        }
        return binarySearch(dict, target, left, right);
    }
    public static int binarySearch(Dictionary dict, int target, int left, int right) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (dict.get(mid) == null || (int) dict.get(mid) > target) {
                right = mid - 1;
            } else if ((int) dict.get(mid) < target) {
                left = mid + 1;
            } else {
                return mid;
            }
        }
        return -1;
    }

    /**search in 2D matrix
     * */
    public static int[] matrixSearch(int[][] matrix, int target){
        int rows = matrix.length, cols = matrix[0].length, left = 0, right = rows * cols - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            int row = mid / cols, col = mid % cols;
            if (matrix[row][col] == target) {
                return new int[]{row, col};
            } else if (matrix[row][col] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return new int[]{-1, -1};
    }
    @Test
    public void matrixSearchTest(){
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        System.out.println(Arrays.toString(matrixSearch(matrix, 8)));
    }

    /**
     * find smallest in rotated(shifted) array
     * {8, 9, 10, 1, 2, 3}
     * Assume: there is only one smallest value, no duplicate smallest
     * */
    public static int findSmallestInRotatedArr(int[] arr){
        if (arr == null || arr.length == 0) return -1;
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[left] < arr[right]) return left;
            if (arr[left] == arr[mid] && arr[mid] == arr[right]) {
                right --;
                continue;
            }
            if (arr[mid] > arr[right]) {
                left = mid + 1;
            } else if (arr[mid] < arr[right]) {
                right = mid;
            } else {
                right = mid - 1;
            }
        }
        return left;
    }
    @Test
    public void findSmallestInRotatedArrTest(){
        int[] arr = new int[]{1, 4, 5, -10, -7, - 3};
        System.out.println(findSmallestInRotatedArr(arr));
    }

    /**
     * rotated array search target
     * */
    public static int rotatedSearch(int[] arr, int target){
        if (arr == null || arr.length == 0) return -1;
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) return mid;
            if (arr[left] == arr[mid] && arr[mid] == arr[right]) {
                right --;
                continue;
            }
            if (arr[mid] < arr[left] || arr[mid] < arr[right]) {
                if (target > arr[mid] && target <= arr[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            } else if (arr[mid] > arr[left] || arr[mid] > arr[right]) {
                if (target >= arr[left] && target < arr[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
        }
        return -1;
    }
    @Test
    public void rotatedSearchTest(){
        int[] arr = new int[]{4, 8, 10, 1, 2, 3};
        System.out.println(rotatedSearch(arr, 9));
    }

}

