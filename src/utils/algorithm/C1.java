package utils.algorithm;

import org.junit.Test;

import java.util.*;


public class C1 {
    /**merge sort
     * time: O(nlogn), space: O(n)
     * */
    public static int[] mergeSort(int[] arr) {
        if (arr == null || arr.length <= 1) return arr;
        int[] helper = new int[arr.length];
        divide(arr, helper, 0, arr.length - 1);
        return arr;
    }
    private static void divide(int[] arr, int[] helper, int left, int right) {
        if (left >= right) return;
        int mid = left + (right - left) / 2;
        divide(arr, helper, left, mid);
        divide(arr, helper, mid + 1, right);
        merge(arr, helper, left, mid, right);
    }
    private static void merge(int[] arr, int[] helper, int left, int mid, int right) {
        for (int i = left; i <= right; i ++) helper[i] = arr[i];
        int lp = left, rp = mid + 1;
        while (lp <= mid && rp <= right) {
            if (helper[lp] <= helper[rp]) {
                arr[left ++] = helper[lp ++];
            } else {
                arr[left ++] = helper[rp ++];
            }
        }
        while (lp <= mid) {
            arr[left ++] = helper[lp ++];
        }
    }
    @Test
    public void mergeSortTest(){
        int[] arr = new int[]{-10, 25, 6, 3, 12, -7, -9, 30, 4, 1};
        System.out.println(Arrays.toString(mergeSort(arr)));
    }

    /** quick sort
     * time O(nlogn) -> O(n^2) 如果pivotPos每次都取到最右边就要递归n层
     * space O(logn) -> O(n)
     * */
    public static int[] quickSort(int[] arr){
        if (arr == null || arr.length <= 1) return arr;
        quick(arr, 0, arr.length - 1);
        return arr;
    }
    private static void quick(int[] arr, int left, int right) {
        if (left >= right) return;
        int pivotPos = partition(arr, left, right);
        quick(arr, left, pivotPos - 1);
        quick(arr, pivotPos + 1, right);
    }
    private static int partition(int[] arr, int left, int right) {
        int pivotPos = (int)(Math.random() * (right - left + 1)) + left;
        int pivot = arr[pivotPos];
        swap(arr, pivotPos, right);
        int lp = left, rp = right - 1;
        while (lp <= rp) {
            if (arr[lp] < pivot) {
                lp ++;
            } else if (arr[rp] >= pivot) {
                rp --;
            } else {
                swap(arr, lp ++, rp --);
            }
        }
        swap(arr, lp, right);
        return lp;
    }
    public static void swap(int[] arr, int i, int j) {
        int tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    @Test
    public void quickSortTest() {
        int[] arr = new int[]{-10, 25, 6, 3, 12, -7, -9, 30, 4, 1};
        System.out.println(Arrays.toString(quickSort(arr)));
    }

    /**
     * Rainbow sort 挡板问题
     * {0} is sorted to {0}
     * {1, 0} is sorted to {0, 1}
     * {1, 0, 1, -1, 0} is sorted to {-1, 0, 0, 1, 1}
     * */
    public static int[] rainbowSort(int[] arr) {
        if (arr == null || arr.length <= 1) return arr;
        int neg = 0, zero = 0, one = arr.length - 1;
        while (zero <= one) {
            if (arr[zero] == -1) {
                swap(arr, zero ++, neg ++);
            } else if (arr[zero] == 0) {
                zero ++;
            } else {
                swap(arr, zero, one --);
            }
        }
        return arr;
    }
    @Test
    public void rainbowSortTest(){
        int[] arr = new int[]{1, 0, -1, 0, -1, 1, 0};
        System.out.println(Arrays.toString(rainbowSort(arr)));
    }

    /**move all 0 to the end (not keep order)*/
    public static int[] moveAll0ToEnd1(int[] arr) {
        if(arr == null || arr.length <= 1) return arr;
        int left = 0, right = arr.length - 1;
        while (left <= right) {
            if (arr[left] != 0) {
                left ++;
            } else if (arr[right] == 0) {
                right --;
            } else {
                swap(arr, left ++, right --);
            }
        }
        return arr;
    }
    /**move all 0 to the end (keep order)*/
    public static int[] moveAll0ToEnd2(int[] arr) {
        if (arr == null || arr.length <= 1) return arr;
        int slow = 0, fast = 0;
        while (fast < arr.length) {
            if (arr[fast] != 0) {arr[slow ++] = arr[fast];}
            fast ++;
        }
        for (int i = slow; i < arr.length; i ++) arr[i] = 0;
        return arr;
    }
    @Test
    public void moveAll0ToEndTest(){
        int[] arr = new int[]{1, 0, -1, 0, -1, 1, 0};
        System.out.println(Arrays.toString(moveAll0ToEnd1(arr)));
        arr = new int[]{1, 0, -1, 0, -1, 1, 0};
        System.out.println(Arrays.toString(moveAll0ToEnd2(arr)));
    }

    /**count sort
     * time O(n) space O(n)
     * */
    public static int[] countSort(int[] arr, int lower, int upper){
        if (arr == null || arr.length <= 1) return arr;
        int[] count = new int[upper - lower + 1];
        for (int x : arr) count[x - lower] ++;
        int idx = 0;
        for (int i = 0; i < count.length; i ++) {
            while (count[i] > 0) {
                arr[idx ++] = i + lower;
                count[i] --;
            }
        }
        return arr;
    }
    @Test
    public void countSortTest(){
        int[] arr = new int[]{25, 9, -5, 10, 7, 30, 1, 5, 4};
        System.out.println(Arrays.toString(countSort(arr, -10, 30)));
    }

    /**k sort: each number is at most off its correct position by k steps
     * time O(nlogk) space O(n)
     * */
    public static int[] kSort(int[] arr, int k){
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int i = 0; i <= Math.min(k, arr.length - 1); i ++) minHeap.offer(arr[i]);
        for (int i = 0; i < arr.length; i ++) {
            if (i + k + 1 < arr.length) {
                minHeap.offer(arr[i]);
            }
            arr[i] = minHeap.poll();
        }
        return arr;
    }
    @Test
    public void kSortTest(){
        int[] arr = new int[]{25, 9, -5, 10, 7, 30, 1, 5, 4};
        System.out.println(Arrays.toString(kSort(arr, 9)));
    }

    /**bucket sort
     * time O(n) -> O(n^2) space O(n)
     * */
    public static double[] bucketSort(double[] arr, double lower, double upper){
        if (arr == null || arr.length <= 1) return arr;
        List<List<Double>> list = new ArrayList<>();
        for (double i : arr) list.add(new ArrayList<>());
        for (double i : arr) list.get((int)((i - lower) / (upper - lower + 1) * arr.length)).add(i);
        for (List<Double> lst: list) Collections.sort(lst);
        int idx = 0;
        for (List<Double> lst: list) {
            for (double d : lst) arr[idx ++] = d;
        }
        return arr;
    }
    @Test
    public void bucketSortTest(){
        double[] arr = new double[]{25.1, 9.5, -5.6, 10.01, 7.3, 30.2, 1.3, 5.4, 4.9};
        System.out.println(Arrays.toString(bucketSort(arr, -6, 31)));
    }

    /**Largest and Smallest*/
    public static int[] largestAndSmallest(int[] arr){
        int n = arr.length;
        for (int i = 0; i < n / 2; i ++) {
            if (arr[i] < arr[n - 1 - i]) {
                swap(arr, i, n - i - 1);
            }
        }
        int max = arr[0];
        for (int i = 1; i < (n - 1) / 2; i ++) max = Math.max(max, arr[i]);
        int min = arr[n - 1];
        for (int i = n / 2; i < n - 2; i ++) min = Math.min(min, arr[i]);
        return new int[]{max, min};
    }
    @Test
    public void largestAndSmallestTest(){
        int[] arr = new int[]{25, 9, -5, 10, 7, 30, 1, 5, 4};
        System.out.println(Arrays.toString(largestAndSmallest(arr)));
    }

    /**largest and second*/
    public static int[] largestAndSecond(int[] arr){
        ElementWithList[] elements = new ElementWithList[arr.length];
        for (int i = 0; i < arr.length; i ++) elements[i] = new ElementWithList(arr[i]);
        int n = arr.length;
        while (n > 1) {
            for (int i = 0; i < n / 2; i ++) {
                if (elements[i].num < elements[n - 1 - i].num) {
                    swap(elements, i, n - 1 - i);
                }
                elements[i].compared.add(elements[n - 1 - i].num);
            }
            n = (n + 1) / 2;
        }
        return new int[]{elements[0].num, Collections.max(elements[0].compared)};
    }
    public static <T>void swap(T[] arr, int i, int j) {
        T t = arr[i]; arr[i] = arr[j]; arr[j] = t;
    }
    @Test
    public void largestAndSecondTest(){
        int[] arr = new int[]{25, 9, -5, 10, 7, 30, 1, 5, 4};
        System.out.println(Arrays.toString(largestAndSecond(arr)));
    }
    /**
     * Matrix rotation (N X N) 90 degree
     * */
    public static void rotateMatrix90(int[][] matrix, boolean clockwise) {
        if (matrix.length <= 1 || matrix.length != matrix[0].length) return;
        if (clockwise) {
            mirrorY(matrix);
            mirrorY_X(matrix);
        } else {
            mirrorY_X(matrix);
            mirrorY(matrix);
        }
    }
    /**按Y镜像*/
    public static void mirrorY(int[][] matrix){
        int n = matrix.length;
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < n / 2; j ++) {
                swap(matrix, i, j, i, n - 1 - j);
            }
        }
    }
    /**按Y=X镜像*/
    public static void mirrorY_X(int[][] matrix){
        int n = matrix.length;
        for (int i = 0; i < n; i ++) {
            for (int j = 0; i + j < n - 1; j ++) {
                swap(matrix, i, j, n - 1 - j, n - 1 - i);
            }
        }
    }
    public static void swap(int[][] matrix, int r1, int c1, int r2, int c2) {
        int t = matrix[r1][c1]; matrix[r1][c1] = matrix[r2][c2]; matrix[r2][c2] = t;
    }
    @Test
    public void rotateMatrix90Test(){
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        rotateMatrix90(matrix, true);
        for (int[] arr : matrix) {
            System.out.println(Arrays.toString(arr));
        }
        rotateMatrix90(matrix, false);
        for (int[] arr : matrix) {
            System.out.println(Arrays.toString(arr));
        }
    }

    /**matrix rotation (M X N) 90 degree */
    public static int[][] rotateMatrix90MN(int[][] matrix, boolean clockwise) {
        int rows = matrix.length, cols = matrix[0].length;
        int[][] after = new int[cols][rows];
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j ++) {
                if (clockwise) {
                    after[j][rows - i - 1] = matrix[i][j];
                } else {
                    after[cols - j - 1][i] = matrix[i][j];
                }
            }
        }
        return after;
    }
    @Test
    public void rotateMatrix90MNTest(){
        int[][] matrix = {
                {1, 2},
                {4, 5},
                {7, 8}
        };
        int[][] after1 = rotateMatrix90MN(matrix, true);
        for (int[] arr : after1) System.out.println(Arrays.toString(arr));
        int[][] after2 = rotateMatrix90MN(matrix, false);
        for (int[] arr : after2) System.out.println(Arrays.toString(arr));
    }

    /**sort in specified order
     * A1 = {2, 1, 2, 5, 7, 1, 9, 3}, A2 = {2, 1, 3},
     * A1 will be sorted according to A2
     * A1 is sorted to {2, 2, 1, 1, 3, 5, 7, 9}
     * */
    public static int[] sortSpecified(int[] arr1, int[] arr2){
        if (arr1 == null || arr1.length <= 1) return arr1;
        Integer[] arr11 = new Integer[arr1.length];
        for (int i = 0 ; i < arr1.length; i ++) arr11[i] = arr1[i];
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < arr2.length; i ++) map.put(arr2[i], i);
        Arrays.sort(arr11, (v1, v2) -> {
            Integer k1 = map.get(v1);
            Integer k2 = map.get(v2);
            if (k1 != null && k2 != null) {
                return k1.compareTo(k2);
            } else if (k1 == null && k2 == null) {
                return v1.compareTo(v2);
            } else {
                return k1 != null ? -1 : 1;
            }
        });
        int[] arr3 = new int[arr11.length];
        for (int i = 0; i < arr11.length; i ++) arr3[i] = arr11[i];
        return arr3;
    }
    @Test
    public void sortSpecifiedTest(){
        int[] A1 = {2, 1, 2, 5, 7, 1, 9, 3};
        int[] A2 = {2, 1, 3};
        System.out.println(Arrays.toString(sortSpecified(A1, A2)));
    }

    /**merge K sorted array
     * Time O(knlogk)
     * */
    public static int[] mergeKSortedArray(int[][] matrix) {
        PriorityQueue<Entry> minHeap = new PriorityQueue<>((e1, e2) -> {
            if (e1.value == e2.value) return 0;
            return e1.value < e2.value ? -1 : 1;
        });
        int len = 0;
        for (int i = 0; i < matrix.length; i ++) {
            if (matrix[i].length > 0) {
                minHeap.offer(new Entry(i, 0, matrix[i][0]));
                len += matrix[i].length;
            }
        }
        int[] ret = new int[len];
        int idx = 0;
        while (! minHeap.isEmpty()) {
            Entry cur = minHeap.poll();
            ret[idx ++] = cur.value;
            if (cur.y + 1 < matrix[cur.x].length) {
                minHeap.offer(new Entry(cur.x, cur.y + 1, matrix[cur.x][cur.y + 1]));
            }
        }
        return ret;
    }
    @Test
    public void mergeKSortedArrayTest(){
        int[][] matrix = {
                {12, 15, 20},
                {14, 15, 17},
                {5, 13, 16}
        };
        System.out.println(Arrays.toString(mergeKSortedArray(matrix)));
    }

    /**merge K sorted list*/
    public static ListNode mergeKSortedList(List<ListNode> lists){
        PriorityQueue<ListNode> minHeap = new PriorityQueue<>((n1, n2) -> {
            if (n1.value == n2.value) return 0;
            return n1.value < n2.value ? -1 : 1;
        });
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        for (ListNode node: lists) {
            if (node != null) minHeap.offer(node);
        }
        while (! minHeap.isEmpty()) {
            cur.next = minHeap.poll();
            if (cur.next.next != null) {
                minHeap.offer(cur.next.next);
            }
            cur = cur.next;
        }
        return dummy.next;
    }

    @Test
    public void mergeKSortedList(){
        ListNode h1 = new ListNode(2);
        h1.setNext(4).setNext(6);
        h1.print();

        ListNode h2 = new ListNode(1);
        h2.setNext(3).setNext(5);
        h2.print();

        List<ListNode> list = Arrays.asList(h1, h2);
        mergeKSortedList(list).print();
    }
    public static void setZeroes (int[][] matrix) {
        if (matrix == null) return;
        int rows = matrix.length;
        int cols = matrix[0].length;
        boolean firstRowHas0 = false;
        boolean firstColHas0 = false;
        // 先看看第0行和第0列有没有为0的项，因为之后我们凡是在内部找到0值，都要映射到0行和0列，那么原来有没有0就不好说了
        for (int i = 0; i < cols; i ++) {
            if (matrix[0][i] == 0) {
                firstRowHas0 = true;
                break;
            }
        }
        for (int i = 0; i < rows; i ++) {
            if (matrix[i][0] == 0) {
                firstColHas0 = true;
                break;
            }
        }
        for (int i = 1; i < rows; i ++) {
            for (int j = 1; j < cols; j ++) {
                if (matrix[i][j] == 0) {
                    matrix[i][0] = 0;
                    matrix[0][j] = 0;
                }
            }
        }
        // 先处理内部有0的情况
        for (int i = 1; i < cols; i ++) {
            if (matrix[0][i] == 0) {
                for (int j = 1; j < rows; j ++) {
                    matrix[j][i] = 0;
                }
            }
        }
        for (int i = 1; i < rows; i ++) {
            if (matrix[i][0] == 0) {
                for (int j = 1; j < cols; j ++) {
                    matrix[i][j] = 0;
                }
            }
        }
        // 再处理0行0列如果本来就有0，那么整行整列都为0
        if (firstRowHas0) {
            for (int i = 0; i < cols; i ++) {
                matrix[0][i] = 0;
            }
        }
        if (firstColHas0) {
            for (int i = 0; i < rows; i ++) {
                matrix[i][0] = 0;
            }
        }
    }
}
class ElementWithList{
    public List<Integer> compared;
    public int num;
    ElementWithList(int num){
        this.num = num;
        this.compared = new ArrayList<>();
    }
}
class Entry{
    int x;
    int y;
    int value;
    public Entry (int x, int y, int value){
        this.x = x;
        this.y = y;
        this.value = value;
    }
}
class ListNode {
    int value;
    ListNode next;
    private int size = 0;
    public ListNode(int value){
        this.value = value;
    }
    public ListNode setNext(int value){
        this.next = new ListNode(value);
        size ++;
        return this.next;
    }
    /** print out all the nodes behinds itself (including itself) */
    public void print(){
        ListNode node = this;
        while (node != null) {
            System.out.print(node.value);
            if (node.next != null) System.out.print(" -> ");
            node = node.next;
        }
        System.out.println();
    }
    public int size(){
        return size;
    }
    public boolean isEmpty(){
        return size == 0;
    }
    public static ListNode getAscList(int beg, int size, int step){
        ListNode head = new ListNode(beg);
        ListNode dummy = head;
        int count = 0;
        while (count < size - 1) {
            dummy = dummy.setNext(beg += step);
            count ++;
        }
        return head;
    }
    public static ListNode getList(List<Integer> list){
        if (list == null || list.size() == 0) return null;
        ListNode head = new ListNode(list.get(0));
        ListNode dummy = head;
        int size = list.size();
        for (int i = 1; i < size; i ++) {
            dummy = dummy.setNext(list.get(i));
        }
        return head;
    }
}