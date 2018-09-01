package utils.algorithm;

import org.junit.Test;

import java.util.*;

import static utils.algorithm.C1.swap;

public class C17 {
    /** perfect shuffle */
    public static void shuffle(int[] arr){
        if (arr == null || arr.length <= 1) return;
        for (int i = 0; i < arr.length; i ++) {
            int j = (int)(Math.random() * (arr.length - i) + i);
            swap(arr, i, j);
        }
    }
    @Test
    public void shuffleTest(){
        int[] arr = new int[]{1, 3, 5, 7, 9};
        shuffle(arr);
        System.out.println(Arrays.toString(arr));
    }
    /** get Random M by using Random N (N > M) */
    public static int randomM_N1(int m, int n) {
        int boundary = (int)(Math.floor(n / m) * m);
        while (true) {
            int num = (int)(Math.random() * n);
            if (num < boundary) return num % m;
        }
    }
    /** get Random M by using Random N (N < M) */
    public static int randomM_N2(int m, int n) {
        int power = (int)(Math.ceil(Math.log(m) / Math.log(n)));
        int boundary = (int)(Math.floor(Math.pow(n, power) / m) * m);
        while (true) {
            int num = 0;
            for (int i = 0; i < power; i ++) {
                num = num * n + (int)(Math.random() * n);
            }
            if (num < boundary) return num % m;
        }
    }
    @Test
    public void MedianTrackerTest(){
        MedianTracker tracker = new MedianTracker();
        tracker.read(2);
        tracker.read(6);
        System.out.println(tracker.median());
        tracker.read(3);
        System.out.println(tracker.median());
    }
    /** 95th percentile */
    public static int percentile95(List<Integer> lengths){
        int[] count = new int[4097];
        lengths.forEach(len -> count[len] ++);
        int sum = 0;
        int len = 4097;
        while (sum <= 0.05 * lengths.size()) {
            sum += count[-- len];
        }
        return len;
    }
}
/** Reservoir sampling */
class Reservoir {
    private int count;
    private Integer sample;
    public Reservoir (int count, Integer sample){
        this.count = 0;
        this.sample = null;
    }
    public void read(int value){
        count ++;
        int p = (int)(Math.random() * (count));
        if (p == 0) sample = value;
    }
    public Integer getSample(){
        return sample;
    }
}
class MedianTracker{
    private PriorityQueue<Integer> leftMaxheap;
    private PriorityQueue<Integer> rightMinheap;
    public MedianTracker(){
        leftMaxheap = new PriorityQueue<>(Comparator.reverseOrder());
        rightMinheap = new PriorityQueue<>();
    }
    public void read(int value) {
        if (leftMaxheap.isEmpty() || leftMaxheap.peek() >= value) {
            leftMaxheap.offer(value);
        } else {
            rightMinheap.offer(value);
        }
        if (leftMaxheap.size() - rightMinheap.size() >= 2) {
            rightMinheap.offer(leftMaxheap.poll());
        } else if (rightMinheap.size() > leftMaxheap.size()) {
            leftMaxheap.offer(rightMinheap.poll());
        }
    }
    public int size(){
        return leftMaxheap.size() + rightMinheap.size();
    }
    public double median(){
        if (size() % 2 == 0) {
            return (double)((leftMaxheap.peek() + rightMinheap.peek()) / 2.0);
        } else {
            return (double)leftMaxheap.peek();
        }
    }
    public void clear(){
        leftMaxheap.clear();
        rightMinheap.clear();
    }
}