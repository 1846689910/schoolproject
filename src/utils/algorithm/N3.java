package utils.algorithm;

import org.junit.Test;

import java.util.*;

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

    /** 3 sum */
    public static List<List<Integer>> threeSum(int[] arr, int target){
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(arr);
        for (int i = 0; i < arr.length - 2; i ++) {
            if (i > 0 && arr[i] == arr[i - 1]) {
                i ++;
            }
            int left = i + 1, right = arr.length - 1;
            while (left < right) {
                int cur = arr[left] + arr[right] + arr[i];
                if (cur == target) {
                    result.add(Arrays.asList(arr[left], arr[right], arr[i]));
                    left ++;
                    while (left < right && arr[left] == arr[left - 1]) left ++;
                } else if (cur < target) {
                    left ++;
                } else {
                    right --;
                }
            }
        }
        return result;
    }
    @Test
    public void threeSumTest(){
        int[] arr = new int[]{1, 3, 5, 7, 9};
        System.out.println(threeSum(arr, 10));
        System.out.println(threeSum(arr, 11));
    }
    /** four sum */
    public static boolean fourSum(int[] arr, int target){
        if (arr == null || arr.length <= 3) return false;
        Arrays.sort(arr);
        for (int i = 0; i < arr.length - 3; i ++) {
            for (int j = i + 1; j < arr.length - 2; j ++) {
                int left = j + 1, right = arr.length - 1;
                while (left < right) {
                    int cur = arr[left] + arr[right] + arr[i] + arr[j];
                    if (cur == target) {
                        return true;
                    } else if (cur < target) {
                        left ++;
                    } else {
                        right --;
                    }
                }
            }
        }
        return false;
    }
    public static boolean fourSum2(int[] arr, int target){
        if (arr == null || arr.length <= 3) return false;
        Map<Integer, Pair> map = new HashMap<>();
        for (int i = 1; i < arr.length; i ++) {
            for (int j = 0; j < i; j ++) {
                int cur = arr[i] + arr[j];
                if (map.containsKey(target - cur) && map.get(target - cur).right < j) {
                    return true;
                }
                if (! map.containsKey(cur)) {
                    map.put(cur, new Pair(j, i));
                }
            }
        }
        return false;
    }
    @Test
    public void fourSumTest(){
        int[] arr = new int[]{1, 3, 5, 7, 9, 11, 13, 15};
        System.out.println(fourSum(arr, 20));
        System.out.println(fourSum(arr, 11));
    }

    /** the largest rectangle in histogram */
    public static int largestRec(int[] arr){  // time O(n^2)
        int area = arr[0];
        for (int i = 0; i < arr.length; i ++) {
            int left = i, right = i, height = arr[i];
            while (left >= 0) {
                if (arr[left] < height) {
                    left ++;
                    break;
                }
                if (left > 0) {
                    left --;
                } else {
                    break;
                }
            }
            while (right < arr.length) {
                if (arr[right] < height) {
                    right --;
                    break;
                }
                if (right < arr.length - 1) {
                    right ++;
                } else {
                    break;
                }
            }
            int width = right - left + 1;
            area = Math.max(area, height * width);
        }
        return area;
    }
    public static int largestRec1(int[] arr){  // time O(n)
        int area = 0;
        Deque<Integer> stack = new LinkedList<>();
        for (int i = 0; i <= arr.length; i ++) {
            int cur = i == arr.length ? 0 : arr[i];
            while (! stack.isEmpty() && arr[stack.peekFirst()] >= cur) {
                int height = arr[stack.pollFirst()];
                int left = stack.isEmpty() ? 0 : stack.peekFirst() + 1;
                area = Math.max(area, height * (i - left));
            }
            stack.offerFirst(i);
        }
        return area;
    }
    @Test
    public void largestRectTest(){

    }

    /** max water 2D */
    public static int maxWater1(int[] arr){
        if (arr.length == 0) return 0;
        int result = 0;
        int left = 0, right = arr.length - 1;
        int lmax = arr[left], rmax = arr[right];
        while (left < right) {
            lmax = Math.max(lmax, arr[left]);
            rmax = Math.max(rmax, arr[right]);
            if (lmax < rmax) {
                result += Math.max(0, lmax - arr[left ++]);
            } else {
                result += Math.max(0, rmax - arr[right --]);
            }
        }
        return result;
    }

    /** max water 3D */
    public static int maxWater2(int[][] matrix) {
        int result = 0;
        int rows = matrix.length, cols = matrix[0].length;
        if (rows < 3 || cols < 3) return 0;
        PriorityQueue<Cube> minHeap = new PriorityQueue<>((c1, c2) -> {
            if (c1.val == c2.val) return 0;
            return c1.val < c2.val ? -1 : 1;
        });
        boolean[][] visited = new boolean[rows][cols];
        processBorder(matrix, minHeap, visited, rows, cols);
        while (! minHeap.isEmpty()) {
            Cube cube = minHeap.poll();
            List<Cube> neis = allNeis(cube, matrix, rows, cols);
            for (Cube c : neis) {
                if (visited[c.row][c.col]) continue;
                visited[c.row][c.col] = true;
                minHeap.offer(c);
                result += Math.max(0, cube.val - c.val);
                c.val = Math.max(c.val, cube.val);
            }
        }
        return result;
    }
    private static void processBorder(int[][] matrix, PriorityQueue<Cube> minHeap, boolean[][] visited, int rows, int cols){
        for (int i = 0; i < rows; i ++) {
            minHeap.offer(new Cube(i, 0, matrix[i][0]));
            visited[i][0] = true;
            minHeap.offer(new Cube(i, cols - 1, matrix[i][cols - 1]));
            visited[i][cols - 1] = true;
        }
        for (int i = 0; i < cols; i ++) {
            minHeap.offer(new Cube(0, i, matrix[0][i]));
            visited[0][i] = true;
            minHeap.offer(new Cube(rows - 1, i, matrix[rows - 1][i]));
            visited[rows - 1][i] = true;
        }
    }
    private static List<Cube> allNeis(Cube cube, int[][] matrix, int rows, int cols){
        List<Cube> list = new ArrayList<>();
        int row = cube.row, col = cube.col;
        if (row - 1 >= 0) {
            list.add(new Cube(row - 1, col, matrix[row - 1][col]));
        }
        if (row + 1 < rows) {
            list.add(new Cube(row + 1, col, matrix[row + 1][col]));
        }
        if (col - 1 >= 0) {
            list.add(new Cube(row, col - 1, matrix[row][col - 1]));
        }
        if (col + 1 < cols) {
            list.add(new Cube(row, cols + 1, matrix[row][col + 1]));
        }
        return list;
    }
}
class Pair{
    int left;
    int right;
    public Pair(int left, int right){
        this.left = left;
        this.right = right;
    }
}
class Cube {
    int row;
    int col;
    int val;
    public Cube(int row, int col, int val){
        this.row = row;
        this.col = col;
        this.val = val;
    }
}
