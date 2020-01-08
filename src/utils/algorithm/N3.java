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
    /**
     * stack 保存升序bar的索引
     * 数组从左向右，如果值为升序则将索引存入stack，(stack内，索引对应值 左低右高)
     * 当遇到数小于stack的peek值也就是高点时，那么stack弹出直到栈顶的值小于该数使得stack能再次保持左低右高的顺序，将弹出的这些bar来计算面积
     * */
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

    /** find kth smallest element from 2 sorted array
     * 1 merge K sorted and then get k - 1 th element (because the kth is possibly starts from 1, better confirm k )
     * 2 use binary search
     * */
    public static int findKthSmallest(int[] a, int[] b, int k){
        return findKthSmallestHelper(a, 0, b, 0, k);
    }
    private static int findKthSmallestHelper(int[] a, int aLeft, int[] b, int bLeft, int k) {
        if (aLeft >= a.length) {
            return b[bLeft + k - 1];
        } else if (bLeft >= b.length) {
            return a[aLeft + k - 1];
        }
        if (k == 1) {
            return Math.min(a[aLeft], b[bLeft]);
        }
        int aMid = aLeft + k / 2 - 1;
        int bMid = bLeft + k / 2 - 1;
        int aVal = aMid >= a.length ? Integer.MAX_VALUE : a[aMid];
        int bVal = bMid >= b.length ? Integer.MAX_VALUE : b[bMid];
        if (aVal < bVal) {
            return findKthSmallestHelper(a, aMid + 1, b, bLeft, k - k / 2);
        } else {
            return findKthSmallestHelper(a, aLeft, b, bMid + 1, k - k / 2);
        }
    }
    /* a js version pointer way
function findKth (a, b, k) {
    if (a.length === 0) return b[k - 1];
    if (b.length === 0) return a[k - 1];
    let i = 0, j = 0, ret = 0;
    while (i < a.length && j < b.length && k > 0) {
        if (a[i] < b[j]) {
            
            ret = a[i ++];
            
            k --;
        } else if (b[j] < a[i]) {
            
            ret = b[j ++];
            k --;
        } else {
            ret = a[i];
            i ++;
            j ++;
            k -= 2;
        }
    }
    if (k <= 0) return ret;
    if (i >= a.length) {
        return b[j + k - 1];
    } else {
        return a[i + k - 1];
    }
}
    */
    @Test
    public void findKthSmallest(){
        int[] a = new int[]{1, 3, 5, 7, 9};
        int[] b = new int[]{2, 4, 6, 8, 10};
        System.out.println(findKthSmallest(a, b, 3));
    }
    /** find median from 2 sorted array */
    public static double findMedian(int[] a, int[] b){
        int len = a.length + b.length;
        if (len % 2 == 0) {
            int left = findKthSmallest(a, b, len / 2);
            int right = findKthSmallest(a, b, (len + 1) / 2);
            return (double)((left + right) / 2);
        } else {
            return (double)findKthSmallest(a, b, (len + 1) / 2);
        }
    }
    @Test
    public void findMedianTest(){
        int[] a = new int[]{1, 3, 5, 7, 9};
        int[] b = new int[]{2, 4, 6, 8, 10};
        System.out.println(findMedian(a, b));
    }
    /** sliding window of size K, always return the max value in the window
     * deque 保存降序bar的索引
     * time O(n)
     * */
    public static List<Integer> maxValues(int[] arr, int k){
        List<Integer> result = new ArrayList<>();
        Deque<Integer> deque = new LinkedList<>();
        for (int i = 0; i < arr.length; i ++) {
            while (! deque.isEmpty() && arr[deque.peekLast()] <= arr[i]) {
                deque.pollLast();
            }
            if (! deque.isEmpty() && deque.peekFirst() <= i - k) {
                deque.pollFirst();
            }
            deque.offerLast(i);
            if (i >= k - 1) {
                result.add(arr[deque.peekFirst()]);
            }
        }
        return result;
    }
    @Test
    public void maxValuesTest(){
        System.out.println(maxValues(new int[]{-10, 3, -2, 7, 5, 4, 6, 1, 9, 3}, 3));
    }

    /** Voting
     * find an integer whose amount took up over 50% of the array length
     * */
    public static int findInt(int[] arr){
        Arrays.sort(arr);
        return arr[arr.length / 2];
    }
    public static int findInt1(int[] arr){
        Map<Integer, Integer> map = new HashMap<>();
        for (int num : arr) map.merge(num, 1, (v1, v2) -> v1 + v2);
        for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
            if (entry.getValue() >= arr.length / 2) return entry.getKey();
        }
        return -1;
    }
    public static int findInt2(int[] arr){
        Int i = new Int(arr[0], 1);
        for (int j = 1; j < arr.length; j ++) {
            if (i.count == 0) {
                i.num = arr[j];
                i.count ++;
            } else if (arr[j] == i.num) {
                i.count ++;
            } else {
                i.count --;
            }
        }
        return i.num;
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
class Int{
    int num;
    int count;
    public Int(int num, int count){
        this.num = num;
        this.count = count;
    }
}
