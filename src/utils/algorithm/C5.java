package utils.algorithm;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

public class C5 {
    /** k smallest elements in an unsorted array */
    public static int[] kSmallest(int[] arr, int k){
        if (arr.length == 0 || k == 0) return new int[0];
        int[] ret = new int[k];
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(k, (i1, i2) -> {
            if (i1.equals(i2)) return 0;
            return i1 > i2 ? -1 : 1;
        });
        for (int i = 0; i < arr.length; i ++) {
            if (i < k) {
                maxHeap.offer(arr[i]);
            } else if (maxHeap.peek() > arr[i]) {
                maxHeap.poll();
                maxHeap.offer(arr[i]);
            }
        }
        int idx = 0;
        while (! maxHeap.isEmpty()) {
            ret[idx ++] = maxHeap.poll();
        }
        return ret;
    }
    public static int[] kSmallest1(int[] arr, int k){
        if (arr.length == 0 || k == 0) return new int[0];
        int[] ret = new int[k];
        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        for (int num : arr) minHeap.offer(num);
        int i = 0;
        while (k > 0) {
            ret[i ++] = minHeap.poll();
            k --;
        }
        return ret;
    }
    @Test
    public void kSmallestTest(){
        int[] arr = new int[]{10, 2, 5, 3, 1, 7, 4};
        System.out.println(Arrays.toString(kSmallest(arr, 3)));
        arr = new int[]{10, 2, 5, 3, 1, 7, 4};
        System.out.println(Arrays.toString(kSmallest1(arr, 3)));
    }

    /** Top K frequent word */
    public static String[] topKFrequentWords (String[] strs, int k) {
        if (strs == null || strs.length == 0) return new String[0];
        Map<String, Integer> map = new HashMap<>();
        for (String s : strs) {
            Integer count = map.get(s);
            if (count != null) {
                map.put(s, count + 1);
            } else {
                map.put(s, 1);
            }
        }
        PriorityQueue<Map.Entry<String, Integer>> minHeap = new PriorityQueue<>(k, (e1, e2) -> {
            if (e1.getValue().equals(e2.getValue())) return 0;
            return e1.getValue() < e2.getValue() ? -1 : 1;
        });
        for (Map.Entry<String, Integer> entry : map.entrySet()) {
            if (minHeap.size() < k) {
                minHeap.offer(entry);
            } else if (minHeap.peek().getValue() < entry.getValue()) {
                minHeap.poll();
                minHeap.offer(entry);
            }
        }
        String[] ret = new String[minHeap.size()];
        int i = 0;
        while (! minHeap.isEmpty()) {
            ret[i ++] = minHeap.poll().getKey();
        }
        return ret;
    }
    @Test
    public void topKFrequentWordsTest(){
        String[] strs = new String[]{"a", "a", "a", "b", "b", "c", "a"};
        System.out.println(Arrays.toString(topKFrequentWords(strs, 2)));
    }

    /** find 2 strings without common letters and their length production should be largest*/
    private static Map<String, Integer> mapMask(String[] arr) {
        Map<String, Integer> map = new HashMap<>();
        for (String s : arr) {
            int i = 0;
            for (char c : s.toCharArray()) {
                i |= 1 << c;
            }
            map.put(s, i);
        }
        return map;
    }
    public static int findLargest (String[] arr) {
        if (arr == null || arr.length <= 2) return 0;
        Map<String, Integer> mapMasks = mapMask(arr);
        int largest = 0;
        Arrays.sort(arr, (s1, s2) -> {
            if (s1.length() == s2.length()) return 0;
            return s1.length() > s2.length() ? -1 : 1;
        });
        for (int i = 1; i < arr.length; i ++) {
            for (int j = 0; j < i; j ++) {
                int prod = arr[i].length() * arr[j].length();
                if (prod <= largest) break;
                int mask1 = mapMasks.get(arr[i]);
                int mask2 = mapMasks.get(arr[j]);
                if ((mask1 & mask2) == 0) {
                    largest = prod;
                }
            }
        }
        return largest;
    }
    @Test
    public void findLargest(){
        String[] arr = new String[]{"apple", "orange", "banana", "grape", "melon", "wiki"};
        System.out.println(findLargest(arr));
    }

    /** find kth smallest number for f(x, y, z) = 3^x * 5^y * 7^z , time O(klogk)*/
    public static long kthSmallest(int k){
        PriorityQueue<Long> minHeap = new PriorityQueue<>(k);
        Set<Long> set = new HashSet<>();
        minHeap.offer(3 * 5 * 7L);
        set.add(3 * 5 * 7L);
        while (k > 1) {
            Long cur = minHeap.poll();
            if (set.add(cur * 3)) {
                minHeap.offer(cur * 3);
            }
            if (set.add(cur * 5)) {
                minHeap.offer(cur * 5);
            }
            if (set.add(cur * 7)) {
                minHeap.offer(cur * 7);
            }
            k --;
        }
        return minHeap.peek();
    }

    /** kth closest to <0, 0, 0> */
    private static long getDist(List<Integer> list, int[] a, int[] b, int[] c){
        return a[list.get(0)] * a[list.get(0)] + b[list.get(1)] * b[list.get(1)] + c[list.get(2)] * c[list.get(2)];
    }
    public static List<Integer> kthClosestTo000(int k, final int[] a, final int[] b, final int[] c){
        PriorityQueue<List<Integer>> minHeap = new PriorityQueue<>(k, (list1, list2) -> {
            long d1 = getDist(list1, a, b, c);
            long d2 = getDist(list2, a, b, c);
            if (d1 == d2) return 0;
            return d1 < d2 ? -1 : 1;
        });
        Set<List<Integer>> set = new HashSet<>();
        List<Integer> start = Arrays.asList(0, 0, 0);
        minHeap.offer(start);
        set.add(start);
        while (k > 1) {
            List<Integer> cur = minHeap.poll();
            List<Integer> next = Arrays.asList(cur.get(0) + 1, cur.get(1), cur.get(2));
            if (next.get(0) < a.length && set.add(next)) minHeap.offer(next);
            next = Arrays.asList(cur.get(0), cur.get(1) + 1, cur.get(2));
            if (next.get(1) < b.length && set.add(next)) minHeap.offer(next);
            next = Arrays.asList(cur.get(0), cur.get(1), cur.get(2) + 1);
            if (next.get(2) < c.length && set.add(next)) minHeap.offer(next);
            k --;
        }
        List<Integer> ret = minHeap.peek();
        return Arrays.asList(a[ret.get(0)], b[ret.get(1)], c[ret.get(2)]);
    }
    @Test
    public void kthClosestTo000Test(){
        int[] a = new int[]{1, 3, 5};
        int[] b = new int[]{2, 4};
        int[] c = new int[]{3, 6};
        System.out.println(kthClosestTo000(2, a, b, c));
    }
    /** Bipartite */
    public static boolean isBipartite(List<GraphNode> list) {
        Map<GraphNode, Integer> map = new HashMap<>();
        for (GraphNode node : list) {
            if (! BFS1(node, map)) return false;
        }
        return true;
    }
    private static boolean BFS1(GraphNode node, Map<GraphNode, Integer> map){
        if (map.containsKey(node)) return true;
        Queue<GraphNode> queue = new LinkedList<>();
        queue.offer(node);
        int group = 0;
        map.put(node, group);
        while (! queue.isEmpty()) {
            GraphNode cur = queue.poll();
            int selfGroup = map.get(cur);
            int otherGroup = selfGroup == 1 ? 0 : 1;
            for (GraphNode nei : node.neighbors) {
                if (! map.containsKey(nei)) {
                    map.put(nei, otherGroup);
                    queue.offer(nei);
                } else if (map.get(nei).equals(selfGroup)) {
                    return false;
                }
            }
        }
        return true;
    }
    /** kth smallest number in sorted matrix */
    public static int[] kthClosestInMatrix(int[][] matrix, int k){
        PriorityQueue<Cell> minHeap = new PriorityQueue<>(k, (c1, c2) -> {
            if (c1.val == c2.val) return 0;
            return c1.val < c2.val ? -1 : 1;
        });
        boolean[][] visited = new boolean[matrix.length][matrix[0].length];
        minHeap.offer(new Cell(0, 0, matrix[0][0]));
        visited[0][0] = true;
        while (k > 1) {
            Cell cur = minHeap.poll();
            if (cur.row + 1 < matrix.length && ! visited[cur.row + 1][cur.col]) {
                minHeap.offer(new Cell(cur.row + 1, cur.col, matrix[cur.row + 1][cur.col]));
                visited[cur.row + 1][cur.col] = true;
            }
            if (cur.col + 1 < matrix[0].length && ! visited[cur.row][cur.col + 1]) {
                minHeap.offer(new Cell(cur.row, cur.col + 1, matrix[cur.row][cur.col + 1]));
                visited[cur.row][cur.col + 1] = true;
            }
            k --;
        }
        Cell ret = minHeap.peek();
        return new int[]{ret.row, ret.col};
    }
}
class Cell {
    int row;
    int col;
    int val;
    public Cell(int row, int col, int val){
        this.row = row;
        this.col = col;
        this.val = val;
    }
}
