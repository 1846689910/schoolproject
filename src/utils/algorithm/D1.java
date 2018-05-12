package utils.algorithm;

import org.junit.Test;

import java.util.*;

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

    /** largest set of points with positive slope* */
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

    /** most points on a line */
    public static int mostPoints(Point[] points){
        int result = 0;
        for (int i = 0; i < points.length; i ++) {
            int same = 1, sameX = 0, most = 0;
            Map<Double, Integer> map = new HashMap<>();
            for (int j = 0; j < points.length; j ++) {
                if (i == j) continue;
                if (points[i].x == points[j].x && points[i].y == points[j].y) {
                    same ++;
                } else if (points[i].x == points[j].x) {
                    sameX ++;
                } else {
                    double slope = (points[j].y - points[i].y + 0.0) / (points[j].x - points[i].x);
                    map.merge(slope, 1, (a, b) -> a + b);
                    most = Math.max(most, map.get(slope));
                }
            }
            most = Math.max(most, sameX) + same;
            result = Math.max(result, most);
        }
        return result;
    }

    /** longest common substring */
    public static String longestCommonStr(String s1, String s2){
        if (s1.length() == 0 || s2.length() == 0) return "";
        int start = 0, longest = 0;
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        int[][] cur = new int[chars1.length + 1][chars2.length + 1];
        for (int i = 1; i <= chars1.length; i ++) {
            for (int j = 1; j <= chars2.length; j ++) {
                if (chars1[i - 1] == chars2[j - 1]) {
                    cur[i][j] = cur[i - 1][j - 1] + 1;
                    if (cur[i][j] > longest) {
                        longest = cur[i][j];
                        start = i - longest;
                    }
                }
            }
        }
        return new String(chars1, start, longest);
    }
    @Test
    public void longestCommonStrTest(){
        System.out.println(longestCommonStr("how are you", "are"));
    }

    /** longest common subsequence */
    public static int longestCommonSeq(String s1, String s2){
        if (s1.length() == 0 || s2.length() == 0) return 0;
        char[] chars1 = s1.toCharArray();
        char[] chars2 = s2.toCharArray();
        int[][] cur = new int[chars1.length + 1][chars2.length + 1];
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i <= chars1.length; i ++) {
            for (int j = 1; j <= chars2.length; j ++) {
                if (chars1[i - 1] == chars2[j - 1]) {
                    cur[i][j] = cur[i - 1][j - 1] + 1;
                    list.add(i - 1);
                } else {
                    cur[i][j] = Math.max(cur[i - 1][j], cur[i][j - 1]);
                }
            }
        }
        System.out.println(list);
        StringBuilder sb = new StringBuilder();
        for (int i : list) {
            sb.append(s1.charAt(i));
        }
        System.out.println(sb.toString());

        return cur[chars1.length][chars2.length];
    }
    @Test
    public void longestCommonSeqTest(){
        System.out.println(longestCommonSeq("how are you doing", "are doing"));
    }

    /** Array Hopper1 */
    public static boolean canJump(int[] arr) {
        boolean[] can = new boolean[arr.length];
        can[0] = true;
        for (int i = 1; i < arr.length; i ++) {
            for (int j = 0; j < i; j ++) {
                if (can[j] && arr[j] + j >= i) {
                    can[i] = true;
                    break;
                }
            }
        }
        return can[arr.length - 1];
    }
    /** Array Hopper2 */
    public static int minJump(int[] arr) {
        int[] min = new int[arr.length];
        min[0] = 0;
        for (int i = 1; i < arr.length; i ++) {
            min[i] = -1;
            for (int j = 0; j < i; j ++) {
                if (min[j] != -1 && arr[j] + j >= i) {
                    if (min[i] == -1 || min[i] > min[j] + 1) {
                        min[i] = min[j] + 1;
                    }
                }
            }
        }
        return min[arr.length - 1];
    }
    /** max product of cutting rope */
    public static int maxProduct(int length){
        if (length == 1) return 0;
        if (length == 2) return 1;
        int[] cur = new int[length + 1];
        for (int i = 3; i <= length; i ++) {
            for (int j = 1; j <= i / 2; j ++) {
                cur[i] = Math.max(cur[i], j * Math.max(i - j, cur[i - j]));
            }
        }
        return cur[length - 1];
    }
    /** min square number could make the number */
    public static int minNum(int num){
        int[] cur = new int[num + 1];
        cur[0] = 1;
        cur[1] = 1;
        for (int i = 2; i <= num; i ++) {
            int r = (int)Math.sqrt(i);
            if (r * r == i) {
                cur[i] = 1;
            } else {
                cur[i] = Integer.MAX_VALUE;
                for (int j = 1; j < i; j ++) {
                    cur[i] = Math.min(cur[i], cur[j] + cur[i - j]);
                }
            }
        }
        return cur[num];
    }
    /** Dictionary word */
    public static boolean dictWords(String input, String[] dict) {
        Set<String> set = new HashSet<>(Arrays.asList(dict));
        boolean[] canBreak = new boolean[input.length() + 1];
        for (int i = 1; i <= input.length(); i ++) {
            for (int j = 0; j < i; j ++) {
                if (set.contains(input.substring(j, i)) && canBreak[j]) {
                    canBreak[i] = true;
                    break;
                }
            }
        }
        return canBreak[input.length()];
    }
    /** interleave string */
    public static boolean canMerge(String a, String b, String c){
        int aLen = a.length(), bLen = b.length(), cLen = c.length();
        if (aLen + bLen != cLen) return false;
        boolean[][] can = new boolean[aLen + 1][bLen + 1];
        for (int i = 0; i <= aLen; i ++) {
            for (int j = 0; j <= bLen; j ++) {
                if (i == 0 && j == 0) {
                    can[i][j] = true;
                } else if (i > 0 && a.charAt(i - 1) == c.charAt(i + j - 1)) {
                    can[i][j] |= can[i - 1][j];
                } else if (j > 0 && b.charAt(j - 1) == c.charAt(i + j - 1)) {
                    can[i][j] |= can[i][j - 1];
                }
            }
        }
        return can[aLen][bLen];
    }
    /** minimum cuts to make palindrome */
    public static int minCutstoPalindrome(String input){
        char[] chars = input.toCharArray();
        boolean[][] isPalindrome = new boolean[chars.length + 1][chars.length + 1];
        int[] min = new int[chars.length + 1];
        for (int end = 1; end <= chars.length; end ++) {
            min[end] = end;
            for (int start = 1; start <= end; start ++) {
                if (chars[start - 1] == chars[end - 1]) {
                    isPalindrome[start][end] = end - start < 2 || isPalindrome[start + 1][end - 1];
                }
                if (isPalindrome[start][end]) {
                    min[end] = Math.min(min[end], 1 + min[start - 1]);
                }
            }
        }
        return min[chars.length] - 1;
    }
    /** edit distance */
    public static int editDistance(String s1, String s2){
        int len1 = s1.length();
        int len2 = s2.length();
        int[][] dist = new int[len1 + 1][len2 + 1];
        for (int i = 0; i <= len1; i ++) {
            for (int j = 0; j <= len2; j ++) {
                if (i == 0) {
                    dist[i][j] = j;
                } else if (j == 0) {
                    dist[i][j] = i;
                } else if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dist[i][j] = dist[i - 1][j - 1];
                } else {
                    dist[i][j] = Math.min(dist[i - 1][j - 1] + 1, Math.min(dist[i - 1][j] + 1, dist[i][j - 1] + 1));
                }
            }
        }
        return dist[len1][len2];
    }
    /** largest cross with all 1s */
    public static int largestCross(int[][] matrix) {
        int row = matrix.length;
        if (row == 0) return 0;
        int col = matrix[0].length;
        if (col == 0) return 0;
        int[][] leftUp = leftUp(matrix, row, col);
        int[][] rightDown = rightDown(matrix, row, col);
        return merge(leftUp, rightDown, row, col);
    }
    private static int[][] leftUp(int[][] matrix, int row, int col){
        int[][] left = new int[row][col];
        int[][] up = new int[row][col];
        for (int i = 0; i < row; i ++) {
            for (int j = 0; j < col; j ++) {
                if (matrix[i][j] == 1) {
                    if (i == 0 && j == 0) {
                        left[i][j] = 1;
                        up[i][j] = 1;
                    } else if (i == 0) {
                        left[i][j] = left[i][j - 1] + 1;
                        up[i][j] = 1;
                    } else if (j == 0) {
                        left[i][j] = 1;
                        up[i][j] = up[i - 1][j] + 1;
                    } else {
                        left[i][j] = left[i][j - 1] + 1;
                        up[i][j] = up[i - 1][j] + 1;
                    }
                }
            }
        }
        merge(left, up, row, col);
        return left;
    }
    private static int[][] rightDown(int[][] matrix, int row, int col){
        int[][] right = new int[row][col];
        int[][] down = new int[row][col];
        for (int i = row - 1; i >= 0; i --) {
            for (int j = col - 1; j >= 0; j --) {
                if (matrix[i][j] == 1) {
                    if (i == row - 1 && j == col - 1) {
                        right[i][j] = 1;
                        down[i][j] = 1;
                    } else if (i == row - 1) {
                        right[i][j] = right[i][j + 1] + 1;
                        down[i][j] = 1;
                    } else if (j == col - 1) {
                        right[i][j] = 1;
                        down[i][j] = down[i + 1][j] + 1;
                    } else {
                        right[i][j] = right[i][j + 1] + 1;
                        down[i][j] = down[i + 1][j] + 1;
                    }
                }
            }
        }
        merge(right, down, row, col);
        return right;
    }
    private static int merge(int[][] m1, int[][] m2, int row, int col) {
        int result = 0;
        for (int i = 0; i < row; i ++) {
            for (int j = 0; j < col; j ++) {
                m1[i][j] = Math.min(m1[i][j], m2[i][j]);
                result = Math.max(result, m1[i][j]);
            }
        }
        return result;
    }
    /** largest X with all 1s */
    public static int largestX (int[][] matrix){
        int m = matrix.length;
        if (m == 0) return 0;
        int n = matrix[0].length;
        if (n == 0) return 0;
        int[][] leftUp = leftUp1(matrix, m, n);
        int[][] rightDown = rightDown1(matrix, m, n);
        return merge(leftUp, rightDown, m, n);
    }
    private static int[][] leftUp1(int[][] matrix, int m, int n) {
        int[][] left = new int[m][n];
        int[][] up = new int[m][n];
        for (int i = 0; i < m; i ++) {
            for (int j = 0; j < n; j ++) {
                if (matrix[i][j] == 1) {
                    left[i][j] = getNum(matrix, m, n, i - 1, j + 1) + 1;
                    up[i][j] = getNum(matrix, m, n, i + 1, j + 1) + 1;
                }
            }
        }
        merge(left, up, m, n);
        return left;
    }
    private static int[][] rightDown1(int[][] matrix, int m, int n) {
        int[][] right = new int[m][n];
        int[][] down = new int[m][n];
        for (int i = m - 1; i >= 0; i --) {
            for (int j = n - 1; j >= 0; j --) {
                if (matrix[i][j] == 1) {
                    right[i][j] = getNum(matrix, m, n, i + 1, j + 1);
                    down[i][j] = getNum(matrix, m, n, i - 1, j - 1);
                }
            }
        }
        merge(right, down, m, n);
        return right;
    }
    private static int getNum(int[][] matrix, int m, int n, int i, int j){
        if (i < 0 || i >= m || j < 0 || j >= n) {
            return 0;
        }
        return matrix[i][j];
    }

    /** largest square of 1s */
    public static int largestSquare(int[][] matrix){
        if (matrix.length == 0 || matrix[0].length == 0) return 0;
        int n = matrix.length;
        int[][] largest = new int[n][n];
        int result = 0;
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < n; j ++) {
                if (i == 0 || j == 0) {
                    largest[i][j] = 0;
                } else if (matrix[i][j] == 1) {
                    largest[i][j] = Math.min(largest[i - 1][j - 1] + 1, Math.min(largest[i - 1][j] + 1, largest[i][j - 1] + 1));
                }
                result = Math.max(result, largest[i][j]);
            }
        }
        return result;
    }
    /** largest submatrix sum */
    public static int largestMatrixSum(int[][] matrix){
        int result = Integer.MIN_VALUE;
        for (int i = 0; i < matrix.length; i ++) {
            int[] cur = new int[matrix[0].length];
            for (int j = i; j < matrix[0].length; j ++) {
                for (int k = 0; k < cur.length; k ++) {
                    cur[k] += matrix[j][k];
                }
                result = Math.max(result, largestSubArraySum(cur));
            }
        }
        return result;
    }

    /** spiral order traverse */
    public static List<Integer> spiralTraverse(int[][] matrix){
        List<Integer> result = new ArrayList<>();
        if (matrix.length == 0) return result;
        int left = 0, right = matrix[0].length - 1, up = 0, down = matrix.length - 1;
        while (left < right && up < down) {
            for (int i = left; i <= right; i ++) {
                result.add(matrix[up][i]);
            }
            for (int i = up + 1; i <= down - 1; i ++) {
                result.add(matrix[i][right]);
            }
            for (int i = right; i >= left; i --) {
                result.add(matrix[down][i]);
            }
            for (int i = down - 1; i >= up + 1; i --) {
                result.add(matrix[i][left]);
            }
            left ++; right --; up ++; down --;
        }
        if (left > right || up > down) {
            return result;
        }
        if (left == right) {
            for (int i = up; i <= down; i ++) result.add(matrix[i][left]);
        } else if (up == down) {
            for (int i = left; i <= right; i ++) result.add(matrix[up][i]);
        }
        return result;
    }
    @Test
    public void spiralTraverseTest(){
        int[][] matrix = {
                {1, 3, 5},
                {2, 4, 6},
                {7, 8, 9}
        };
        System.out.println(spiralTraverse(matrix));
    }
    /** spiral order generate */
    public static int[][] spiralGenerate(int m, int n){
        if (m == 0) return new int[m][n];
        int[][] matrix = new int[m][n];
        int left = 0, right = n - 1, up = 0, down = m - 1;
        int num = 1;
        while (left < right && up < down) {
            for (int i = left; i <= right; i ++) matrix[up][i] = num ++;
            for (int i = up + 1; i <= down - 1; i ++) matrix[i][right] = num ++;
            for (int i = right; i >= left; i --) matrix[down][i] = num ++;
            for (int i = down - 1; i >= up + 1; i --) matrix[i][left] = num ++;
            left ++; right --; up ++; down --;
        }
        if (left > right || up > down) {
            return matrix;
        } else if (left == right) {
            for (int i = up; i <= down; i ++) matrix[i][up] = num ++;
        } else if (up == down) {
            for (int i = left; i <= right; i ++) matrix[up][i] = num ++;
        }
        return matrix;
    }
    @Test
    public void spiralGenerateTest(){
        int[][] matrix = spiralGenerate(5, 6);
        for (int[] arr : matrix) {
            System.out.println(Arrays.toString(arr));
        }
    }
    /** Abbreviation Matching */
    public static boolean match(String s, String t) {
        return matchHelper(s, t, 0, 0);
    }
    private static boolean matchHelper(String s, String t, int si, int ti) {
        if (si == s.length() && ti == t.length()) return true;
        if (si >= s.length() || ti >= t.length()) return false;
        if (! Character.isDigit(t.charAt(ti))) {
            if (s.charAt(si) == t.charAt(ti)) {
                return matchHelper(s, t, si + 1, ti + 1);
            }
            return false;
        }
        int count = 0;
        while (ti < t.length() && Character.isDigit(t.charAt(ti))) {
            count = count * 10 + (t.charAt(ti) - '0');
            ti ++;
        }
        return matchHelper(s, t, si + count, ti);
    }
    public static boolean match1(String s, String t){
        int si = 0, ti = 0;
        while (si < s.length() && ti < t.length()) {
            if (! Character.isDigit(t.charAt(ti))) {
                if (s.charAt(si) != t.charAt(ti)) {
                    return false;
                }
                si ++;
                ti ++;
            } else {
                int count = 0;
                while (ti < t.length() && Character.isDigit(t.charAt(ti))) {
                    count = count * 10 + (t.charAt(ti) - '0');
                    ti ++;
                }
                si += count;
            }
        }
        return si == s.length() && ti == t.length();
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
