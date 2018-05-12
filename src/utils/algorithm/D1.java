package utils.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class D1 {

    /** fibonacci
     * find nth fibonacci number
     * */
    public static long fibonacci(int n) {  // Time: O(2^n), space: O(n)
        if (n == 0 || n == 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }
    public static long fibonacci1(int n) {  // Time: O(n), space: O(n)
        if (n == 0 || n == 1) return n;
        int[] arr = new int[n + 1];
        arr[0] = 0;
        arr[1] = 1;
        for (int i = 2; i <= n; i ++) {
            arr[i] = arr[i - 1] + arr[i - 2];
        }
        return arr[n];
    }
    public static long fibonacci2(int n) {  // Time: O(n), space: O(1)
        if (n == 0 || n == 1) return n;
        long a = 0, b = 1;
        while (n >= 2) {
            long temp = a + b;
            a = b;
            b = temp;
            n --;
        }
        return b;
    }
    @Test
    public void fibonacciTest(){
        System.out.println(fibonacci(9));
        System.out.println(fibonacci1(9));
        System.out.println(fibonacci2(9));
    }
    /** longest ascending subarray
     * base case: M[0] = 1, when there is only one element, the longest ascending subarray length is 1
     * induction rule: M[i] represents from 0th element to ith element, the length of longest ascending subarray
     *      M[i] = 1; if arr[i] < arr[i - 1]
     *      M[i] = M[i - 1] + 1; if arr[i] > arr[i - 1]
     * */
    public static int longestAsc(int[] arr){
        int cur = 1, result = 1, start = 0, end = 0, soluStart = 0, soluEnd = 0;
        for (int i = 1; i < arr.length; i ++) {
            if (arr[i] > arr[i - 1]) {
                cur ++;
                end = i;
            } else {
                cur = 1;
                start = i;
            }
            if (cur > result) {
                result = cur;
                soluStart = start;
                soluEnd = end;
            }
        }
        System.out.println(soluStart + " : " + soluEnd);
        return result;
    }
    @Test
    public void longestAscTest(){
        System.out.println(longestAsc(new int[]{5, 4, 1, 7, 8, 9, 3, 6}));
    }

    /** largest subarray sum
     *
     * */
    public static int largestSubArraySum(int[] arr){
        int cur = arr[0], result = arr[0], start = 0, end = 0, soluStart = 0, soluEnd = 0;
        for (int i = 1; i < arr.length; i ++) {
            if (cur > 0) {
                cur += arr[i];
                end = i;
            } else {
                cur = arr[i];
                start = i;
            }
            if (cur > result) {
                result = cur;
                soluStart = start;
                soluEnd = end;
            }
        }
        System.out.println(soluStart + " : " + soluEnd);
        return result;
    }
    @Test
    public void largestSubArraySumTest(){
        System.out.println(largestSubArraySum(new int[]{-10, 4, 5, 3, 7, 2}));
    }

    /** longest consecutive 1s */
    public static int longest1s(int[] arr){
        if (arr.length == 0) return 0;
        int cur = arr[0], result = arr[0], start = 0, end = 0, soluStart = 0, soluEnd = 0;
        for (int i = 1; i < arr.length; i ++) {
            if (arr[i] == 1) {
                cur ++;
                if (arr[i - 1] == 1) {
                    end = i;
                } else {
                    start = i;
                }
            } else {
                cur = 0;
            }
            if (cur > result) {
                result = cur;
                soluStart = start;
                soluEnd = end;
            }
        }
        System.out.println(soluStart + " : " + soluEnd);
        return result;
    }

    @Test
    public void longest1sTest(){
        System.out.println(longest1s(new int[]{1, 1, -1, 1, 0, 1, 1, 1, 0}));
    }

    /** longest ascending subsequence */
    public static int longestSeq(int[] arr){
        int[] cur = new int[arr.length];
        cur[0] = 1;
        int result = 1;
        for (int i = 1; i < arr.length; i ++) {
            cur[i] = 1;
            for (int j = 0; j < i; j ++) {
                if (arr[j] < arr[i]) {
                    cur[i] = Math.max(cur[i], cur[j] + 1);
                }
            }
            result = Math.max(cur[i], result);
        }
        return result;
    }
    public static int longestSeq1(int[] arr){
        int[] cur = new int[arr.length];
        cur[0] = 1;
        int result = 1, start = 0;
        for (int i = 1; i < arr.length; i ++) {
            cur[i] = 1;
            for (int j = 0; j < i; j ++) {
                if (arr[j] < arr[i]) {
                    if (cur[j] + 1 > cur[i]) {
                        cur[i] = cur[j] + 1;
                        start = j;
                    }
                }
            }
            if (result < cur[i]) {
                result = cur[i];
                System.out.println(start + " : " + i);
            }
        }
        return result;
    }
    @Test
    public void longestSeqTest(){
        System.out.println(longestSeq(new int[]{1, 4, 3, 2, 5, 7, 0}));
        System.out.println(longestSeq1(new int[]{1, 4, 3, 2, 5, 7, 0}));
    }

    /** largest set of points with positive slope
     *
     * */
    public static int largestSetPoints(Point[] points){
        if (points == null || points.length == 0) return 0;
        Arrays.sort(points, (p1, p2) -> {
            if (p1.x == p2.x) return 0;
            return p1.x < p2.x ? -1 : 1;
        });
        int[] cur = new int[points.length];
        cur[0] = 0;
        int result = 0;
        for (int i = 1; i < points.length; i ++) {
            cur[i] = 0;
            for (int j = 0; j < i; j ++) {
                if (points[j].y < points[i].y) {
                    cur[i] = Math.max(cur[i], cur[j] == 0 ? cur[j] + 2 : cur[j] + 1);
                }
            }
            result = Math.max(result, cur[i]);
        }
        return result;
    }
}
class Point{
    int x;
    int y;
    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
