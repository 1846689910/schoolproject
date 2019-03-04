package utils.algorithm;

import org.junit.Test;
import java.util.*;
import utils.algorithm.LC1_100.Interval;

public class LC201_300 {
    /**
     * LC201 Bitwise and of Number range
     * 数字[m, n]之间，他们分别相与的结果
     * */
    public int rangeBitwiseAnd(int m, int n) {
        while (m < n) {
            n &= (n - 1);
        }
        return n;
    }
    /**
     * LC202 Happy Number
     * 将一个数的每个数位拆解，然后求所有数位的平方之和。不停分解下去，最后得到1的就是happy number
     * Input: 19
     Output: true
     Explanation:
     1^2 + 9^2 = 82
     8^2 + 2^2 = 68
     6^2 + 8^2 = 100
     1^2 + 0^2 + 0^2 = 1
     * */
    public boolean isHappy(int n) {
        Set<Integer> set = new HashSet<>();
        while (n > 1) {
            int m = 0;
            while (n > 0) {
                int d = n % 10;
                m += d * d;
                n /= 10;
            }
            if (set.add(m)) {
                n = m;
            } else {
                return false;
            }
        }
        return true;
    }
    @Test
    public void isHappyTest(){
        System.out.println(isHappy(19));
    }
    @Test
    public void test11(){
        String s = "Hello World";

        String s1 = new String(Base64.getEncoder().encode(s.getBytes()));
        StringBuilder sb = new StringBuilder();

        String s2 = new String(Base64.getDecoder().decode(s1));
        System.out.println(s2);
    }

    /**
     * LC204 Count Primes
     * 计算小于某个非负数n得所有质数的数量
     * Input: 10
     Output: 4
     Explanation: There are 4 prime numbers less than 10, they are 2, 3, 5, 7.
     * */
    public int countPrimes(int n) {
        boolean[] notPrime = new boolean[n];
        int count = 0;
        for (int i = 2; i < n; i ++) {
            if (! notPrime[i]) {
                count ++;
                for (int j = 2; i * j < n; j ++) {
                    notPrime[i * j] = true;
                }
            }
        }
        return count;
    }
    /**
     * LC205 isomorphic strings
     * 如果一个字符串中的相同字符被替换后可以变成另一个字符串。他们两是isomorphic
     * Input: s = "egg", t = "add"
     Output: true
     * */
    public boolean isIsomorphic11(String s, String t) {
        if (s == null && t == null) return true;
        if (s == null || t == null || s.length() != t.length()) return false;
        Map<Character, Character> map = new HashMap<>();
        for (int i = 0, len = s.length(); i < len; i ++) {
            char si = s.charAt(i);
            char ti = t.charAt(i);
            if (map.containsKey(ti)){
                if (map.get(ti) != si) return false;
            } else {
                if (map.containsValue(si)) return false;
                map.put(ti, si);
            }
        }
        return true;
    }
    public boolean isIsomorphic(String s, String t) {
        if (s == null || t == null || s.length() != t.length()) return false;
        int[] arr1 = new int[256];
        int[] arr2 = new int[256];
        int len = s.length();
        for (int i = 0; i < len; i ++) {
            if (arr1[s.charAt(i)] != arr2[t.charAt(i)]) return false;
            arr1[s.charAt(i)] = i + 1;
            arr2[t.charAt(i)] = i + 1;
        }
        return true;
    }
    public boolean isIsomorphic1(String s, String t) {
        if (s == null && t == null) {
            return true;
        }
        if (s == null || t == null || s.length() != t.length()) {
            return false;
        }
        char[] arr = new char[256];
//        System.out.println(Arrays.toString(arr));
        boolean[] ocp = new boolean[256];
        for (int i = 0, len = s.length(); i < len; i ++) {
            char si = s.charAt(i);
            char ti = t.charAt(i);
            if (arr[si] == 0) {
                if (ocp[ti]) return false;
                arr[si] = ti;
                ocp[ti] = true;
            }
            if (arr[si] != ti) return false;
        }
        return true;
    }
    @Test
    public void isIsomorphicTest(){
        System.out.println(isIsomorphic1("ab", "aa"));
    }
    /**
     * LC207 course schedule
     * [0, 1]每个课程表示为数组，表示要想上课程0,必须先修课程1
     *Input: 2, [[1,0]]
     Output: true
     Explanation: There are a total of 2 courses to take.
     To take course 1 you should have finished course 0. So it is possible.

     Input: 2, [[1,0],[0,1]]
     Output: false
     Explanation: There are a total of 2 courses to take.
     To take course 1 you should have finished course 0, and to take course 0 you should
     also have finished course 1. So it is impossible.
     * */
    public boolean canFinish(int numCourses, int[][] prerequisites) {
        List<List<Integer>> graph = new ArrayList<>();
        for(int i = 0; i < numCourses; i ++) graph.add(new ArrayList<>());
        for(int[] arr : prerequisites) graph.get(arr[1]).add(arr[0]);
        int[] visited = new int[numCourses];
        for(int i = 0; i < numCourses; i ++){
            if(! valid(graph, visited, i))return false;
        }
        return true;
    }
    private boolean valid(List<List<Integer>> graph, int[] visited, int i){
        if(visited[i] == 1) return false;
        if(visited[i] == 2) return true;
        visited[i] = 1;
        for(int idx : graph.get(i)){
            if(! valid(graph, visited, idx)) return false;
        }
        visited[i] = 2;
        return true;
    }
    /**
     * LC209 Minimum size subarray sum
     * 给定num和arr，从arr中找到最小的片段使得和>=num
     * */
    public int minSubArrayLen(int num, int[] arr){
        int result = Integer.MAX_VALUE;
        int left = 0;
        int sum = 0;
        for (int i = 0; i < arr.length; i ++) {
            sum += arr[i];
            while (sum >= num) {
                result = Math.min(result, i + 1 - left);  // it1 + 1 - left is the current size of subarray
                sum -= arr[left ++];
            }
        }
        System.out.println(left - 1);  // left - 1是开始，left + result - 1是结束
        return result != Integer.MAX_VALUE ? result : 0;
    }
    @Test
    public void minSubArrayLenTest(){
        System.out.println(minSubArrayLen(7, new int[]{2, 3, 1, 2, 4, 3}));
//        System.out.println(minSubArrayLen(2, new int[]{0, 0, 0, 1, 3, 1, 0, 0}));
    }
    /**
     * LC210 course schedule 2
     * 给定要选课的数量和[[课号, 先修课号], ...], 如果能修完，那么顺序是什么. 可能有多重顺序，只要得出一种即可。如果不能，返回空数组
     * */
    public int[] findOrder(int numCourses, int[][] prerequisites) {
        int[] edges = new int[numCourses];  // 索引是课，值为先修课数
        List<List<Integer>> graph = new ArrayList<>(numCourses);  // List<先修课--> List<课号>>
        for(int i = 0; i < numCourses; i ++) graph.add(new ArrayList<>());
        for(int[] edge : prerequisites) {
            edges[edge[0]] ++;  // 每个节点的边数，有边表示有先修课
            graph.get(edge[1]).add(edge[0]);
        }
        return BFS(edges, graph);
    }
    private int[] BFS(int[] edges, List<List<Integer>> graph){
        int[] order = new int[edges.length];
        Queue<Integer> queue = new LinkedList<>();  // 需要修的课程
        for (int i = 0; i < edges.length; i ++) {  // 先把没有先修课的课程放入queue, 他们是根节点
            if (edges[i] == 0) queue.offer(i);
        }
        int idx = 0;
        while (! queue.isEmpty()) {
            int cur = queue.poll();
            order[idx ++] = cur;
            for (int j : graph.get(cur)) {
                edges[j] --;
                if (edges[j] == 0) queue.offer(j);
            }
        }
        return idx == edges.length ? order : new int[0];
    }
    /**
     * LC287
     * find duplicate number
     * 在一个数组中，有一个数重复多次，找出这个数
     * */
    public int findDuplicate(int[] nums) {  // sorting, time O(nlgn), space O(1)
        Arrays.sort(nums);
        for (int i = 1; i < nums.length; i++) {
            if (nums[i] == nums[i-1]) {
                return nums[i];
            }
        }
        return -1;
    }
    public int findDuplicate1(int[] nums) {  // use set, time O(n), space O(n)
        Set<Integer> seen = new HashSet<Integer>();
        for (int num : nums) {
            if (seen.contains(num)) {
                return num;
            }
            seen.add(num);
        }

        return -1;
    }
    /**
     * LC213 House robbers 2
     * 数组成环形，也就是说第一个元素代表的房子和最后一个房子是相邻的
     * */
    public int rob(int[] arr) {

        if (arr.length == 1)
            return arr[0];

        int a = 0, b = 0;

        for (int i = 0; i < arr.length - 1; i ++) {
            if (i % 2 == 0) {
                a = Math.max(b, a + arr[i]);
            } else {
                b = Math.max(a, b + arr[i]);
            }
        }

        int max = Math.max(a, b);
        a = 0; b = 0;

        for (int i = 1; i < arr.length; i ++) {
            if (i % 2 == 0) {
                a = Math.max(b, a + arr[i]);
            } else {
                b = Math.max(a, b + arr[i]);
            }
        }

        max = Math.max(max, Math.max(a,b));

        return max;
    }
    /**
     * LC214 shortest Palindrome
     * */
    public String shortestPalindrome(String s){
        int j = 0;
        for (int i = s.length() - 1; i >= 0; i--) {
            if (s.charAt(i) == s.charAt(j)) j ++;
        }
        if (j == s.length()) return s;
        String suffix = s.substring(j);
        return new StringBuilder(suffix).reverse().toString() + shortestPalindrome(s.substring(0, j)) + suffix;
    }
    /**
     * LC215 kth largest element in array
     * */
    public int findKthLargest(int[] nums, int k) {
        Arrays.sort(nums);
        return nums[nums.length - k];
    }
    public int findKthLargest2(int[] arr, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
        for(int i = 0; i < arr.length; i ++) {
            minHeap.offer(arr[i]);
            if(i >= k) minHeap.poll();
        }
        return minHeap.peek();
    }
    private int findKthSmallest(int[] arr, int k) {
        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(k, (i1, i2) -> {
            if(i1.equals(i2)) return 0;
            return i1 > i2 ? -1 : 1;
        });
        for(int i = 0; i < arr.length; i ++) {
            maxHeap.offer(arr[i]);
            if(i >= k) maxHeap.poll();
        }
        return maxHeap.peek();
    }
    @Test
    public void kthTest(){
        int[] arr = new int[]{3, 2, 1, 5, 6, 4};
        System.out.println(findKthSmallest(arr, 2));
//        System.out.println(findKthLargest2(arr, 2));
    }
    /**
     * LC216 Combination Sum 3
     * */
    public List<List<Integer>> combinationSum3(int k, int n) {
        List<List<Integer>> result = new ArrayList<>();
        combinationSum3Helper(result, new ArrayList<>(), k, n, 1);
        return result;
    }
    private void combinationSum3Helper(List<List<Integer>> result, List<Integer> cur, int k, int n, int start) {
        if (cur.size() == k && n == 0) {
            result.add(new ArrayList<>(cur));
            return;
        }
        for (int i = start; i <= 9; i ++) {
            cur.add(i);
            combinationSum3Helper(result, cur, k, n - i, i + 1);
            cur.remove(cur.size() - 1);
        }
    }
    /**
     * LC217 contains duplicate
     * */
    public boolean containsDuplicate(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            if (! set.add(num)) return true;
        }
        return false;
    }
    /**
     * LC219 contains duplicate2
     * arr中是否存在两个数，他们数值相同，但是索引最多相差k
     * */
    public boolean containsNearbyDuplicate(int[] nums, int k) {
        Set<Integer> set = new HashSet<>();
        for(int i = 0; i < nums.length; i++){
            if(i > k) set.remove(nums[i - k - 1]);
            if(! set.add(nums[i])) return true;
        }
        return false;
    }
    /**
     * LC220 contains duplicate3
     * arr中是否存在arr[it1]和arr[it2],他们数值最多相差t，索引最多相差k
     * */
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
        if (k < 1 || t < 0) return false;
        Map<Long, Long> map = new HashMap<>();
        for (int i = 0; i < nums.length; i ++) {
            long remappedNum = (long) nums[i] - Integer.MIN_VALUE;
            long bucket = remappedNum / ((long) t + 1);
            if (map.containsKey(bucket)
                    || (map.containsKey(bucket - 1) && remappedNum - map.get(bucket - 1) <= t)
                    || (map.containsKey(bucket + 1) && map.get(bucket + 1) - remappedNum <= t))
                return true;
            if (map.size() >= k) {
                long lastBucket = ((long) nums[i - k] - Integer.MIN_VALUE) / ((long) t + 1);
                map.remove(lastBucket);
            }
            map.put(bucket, remappedNum);
        }
        return false;
    }
    public boolean containsNearbyAlmostDuplicate1(int[] nums, int k, int t) {
        TreeSet<Integer> set = new TreeSet<>();
        for (int i = 0; i < nums.length; ++i) {
            // Find the successor of current element
            Integer s = set.ceiling(nums[i]);
            if (s != null && s <= nums[i] + t) return true;
            // Find the predecessor of current element
            Integer g = set.floor(nums[i]);
            if (g != null && nums[i] <= g + t) return true;

            set.add(nums[i]);
            if (set.size() > k) {
                set.remove(nums[i - k]);
            }
        }
        return false;
    }
    // Get the ID of the bucket from element value x and bucket width w
    // In Java, `-3 / 5 = 0` and but we need `-3 / 5 = -1`.
    private long getID(long x, long w) {
        return x < 0 ? (x + 1) / w - 1 : x / w;
    }

    public boolean containsNearbyAlmostDuplicate2(int[] nums, int k, int t) {
        if (t < 0) return false;
        Map<Long, Long> d = new HashMap<>();
        long w = (long)t + 1;
        for (int i = 0; i < nums.length; ++i) {
            long m = getID(nums[i], w);
            // check if bucket m is empty, each bucket may contain at most one element
            if (d.containsKey(m))
                return true;
            // check the neighbor buckets for almost duplicate
            if (d.containsKey(m - 1) && Math.abs(nums[i] - d.get(m - 1)) < w)
                return true;
            if (d.containsKey(m + 1) && Math.abs(nums[i] - d.get(m + 1)) < w)
                return true;
            // now bucket m is empty and no almost duplicate in neighbor buckets
            d.put(m, (long)nums[i]);
            if (i >= k) d.remove(getID(nums[i - k], w));
        }
        return false;
    }
    /**
     * LC211 maximal square
     * */
    public int maximalSquare(char[][] matrix) {
        int rows = matrix.length, cols = rows > 0 ? matrix[0].length : 0;
        int[][] largest = new int[rows + 1][cols + 1];
        int maxsqlen = 0;
        for (int i = 1; i <= rows; i++) {
            for (int j = 1; j <= cols; j++) {
                if (matrix[i - 1][j - 1] == '1'){
                    largest[i][j] = Math.min(Math.min(largest[i][j - 1], largest[i - 1][j]), largest[i - 1][j - 1]) + 1;
                    maxsqlen = Math.max(maxsqlen, largest[i][j]);
                }
            }
        }
        return maxsqlen * maxsqlen;
    }
    @Test
    public void largestSquareTest(){
        System.out.println(maximalSquare(new char[][]{
                {'0', '0', '0'},
                {'0', '0', '0'},
                {'0', '0', '0'},
                {'0', '0', '0'}
        }));

    }
    /**
     * LC222 count complete tree node
     * given a complete binary tree, 计算共有多少个节点
     * */
    public int countNodes(TreeNode root) {
        int leftDepth = getDepth(root, true);
        int rightDepth = getDepth(root, false);
        if (leftDepth == rightDepth) {
            return (1 << leftDepth) - 1;  // 正好对称
        } else {
            return 1 + countNodes(root.left) + countNodes(root.right);
        }
    }
    private int getDepth(TreeNode root, boolean goLeft) {
        int dep = 0;
        while (root != null) {
            if (goLeft) {
                root = root.left;
            } else {
                root = root.right;
            }
            dep ++;
        }
        return dep;
    }
    /**
     * LC223 Rectangle area
     * 给定两个矩形，每个矩形对角线的坐标，求两个矩形的覆盖的总面积
     * (A,B), (C,D), (E,F), (G,H)
     * */
    public int computeArea(int A, int B, int C, int D, int E, int F, int G, int H) {
        int areaOfSqrA = (C-A) * (D-B);
        int areaOfSqrB = (G-E) * (H-F);
        int left = Math.max(A, E);
        int right = Math.min(G, C);
        int bottom = Math.max(F, B);
        int top = Math.min(D, H);
        //If overlap
        int overlap = 0;
        if(right > left && top > bottom)
            overlap = (right - left) * (top - bottom);
        return areaOfSqrA + areaOfSqrB - overlap;
    }
    /**
     * LC227 Basic Calculator2
     * Example 1:

     Input: "3+2*2"
     Output: 7
     Example 2:

     Input: " 3/2 "
     Output: 1
     Example 3:

     Input: " 3+5 / 2 "
     Output: 5
     * */
    public int calculate(String s) {
        if(s == null || s.length() == 0) return 0;
        Stack<Integer> stack = new Stack<>();
        int num = 0;
        char sign = '+';
        char[] chars = s.toCharArray();
        for(int i = 0; i < chars.length; i ++){
            if(Character.isDigit(chars[i])){
                num = num * 10 + chars[i] - '0';
            }
            if((!Character.isDigit(chars[i]) && ' ' != chars[i]) || i == chars.length - 1){
                if(sign=='-'){
                    stack.push(-num);
                }
                if(sign=='+'){
                    stack.push(num);
                }
                if(sign=='*'){
                    stack.push(stack.pop()*num);
                }
                if(sign=='/'){
                    stack.push(stack.pop()/num);
                }
                sign = chars[i];
                num = 0;
            }
        }
        int result = 0;
        for(int i : stack){
            result += i;
        }
        return result;
    }
    /**
     * LC228 Summary Ranges
     * 给定一个无dup的数组，找出它的元素的范围
     * Example 1:

     Input:  [0,1,2,4,5,7]
     Output: ["0->2","4->5","7"]
     Explanation: 0,1,2 form a continuous range; 4,5 form a continuous range.
     Example 2:

     Input:  [0,2,3,4,6,8,9]
     Output: ["0","2->4","6","8->9"]
     Explanation: 2,3,4 form a continuous range; 8,9 form a continuous range.

     * */
    public List<String> summaryRanges(int[] arr) {
        List<String> result = new ArrayList<>();
        if(arr.length == 1){
            result.add(arr[0] + "");
            return result;
        }
        for(int i = 0; i < arr.length; i ++){
            int a = arr[i];
            while(i + 1 < arr.length && (arr[i + 1] - arr[i]) == 1){
                i ++;
            }
            if(a != arr[i]){
                result.add(a + "->" + arr[i]);
            } else {
                result.add(a + "");
            }
        }
        return result;
    }
    /**
     * LC230 kth smallest element in BST, 1<=k<=元素数
     * */
    public int kthSmallest(TreeNode root, int k) {
        Stack<TreeNode> stack = new Stack<>();
        TreeNode cur = root;
        int count = 0;
        while(!stack.isEmpty() || cur != null) {
            if(cur != null) {
                stack.push(cur);  // Just like recursion
                cur = cur.left;
            } else {
                TreeNode node = stack.pop();
                if(++ count == k) return node.value;
                cur = node.right;
            }
        }
        return Integer.MIN_VALUE;
    }
    /**
     * LC237 Delete Node in LinkedList
     * 给定一个 未知LinkedList的节点，删除这个节点,
     * 交换数值法，将自己的值改为下一个节点的值，然后将next连接下下节点
     * */
    public void deleteNode(ListNode node) {
        node.value = node.next.value;
        node.next = node.next.next;
    }
    /**
     * LC238 product of array except itself
     * */
    public int[] productExceptSelf(int[] arr) {
        int n = arr.length;
        int[] ret = new int[n];
        ret[0] = 1;
        for (int i = 1; i < n; i ++) {
            ret[i] = ret[i - 1] * arr[i - 1];
        }
        int right = 1;
        for (int i = n - 1; i >= 0; i --) {
            ret[i] *= right;
            right *= arr[i];
        }
        return ret;
    }
    /**
     * LC241 different ways to add parentheses
     * 给定字符串的算数式，决定有多少种加括号的方式产生不同的结果
     * Example 1:

     Input: "2-1-1"
     Output: [0, 2]
     Explanation:
     ((2-1)-1) = 0
     (2-(1-1)) = 2
     Example 2:

     Input: "2*3-4*5"
     Output: [-34, -14, -10, -10, 10]
     Explanation:
     (2*(3-(4*5))) = -34
     ((2*3)-(4*5)) = -14
     ((2*(3-4))*5) = -10
     (2*((3-4)*5)) = -10
     (((2*3)-4)*5) = 10
     * */
    public List<Integer> diffWaysToCompute(String s) {
        List<Integer> result = new ArrayList<>();
        for (int i = 0; i < s.length(); i ++) {
            if (s.charAt(i) == '-' ||
                    s.charAt(i) == '*' ||
                    s.charAt(i) == '+' ) {
                String part1 = s.substring(0, i);
                String part2 = s.substring(i + 1);
                List<Integer> part1Ret = diffWaysToCompute(part1);
                List<Integer> part2Ret = diffWaysToCompute(part2);
                for (Integer p1 : part1Ret) {
                    for (Integer p2 : part2Ret) {
                        int c = 0;
                        switch (s.charAt(i)) {
                            case '+':
                                c = p1 + p2;
                                break;
                            case '-':
                                c = p1 - p2;
                                break;
                            case '*':
                                c = p1 * p2;
                                break;
                        }
                        result.add(c);
                    }
                }
            }
        }
        if (result.size() == 0) {
            result.add(Integer.valueOf(s));
        }
        return result;
    }
    /**
     * LC243 Shortest Word distance
     * Assume that words = ["practice", "makes", "perfect", "coding", "makes"].

     Input: word1 = “coding”, word2 = “practice”
     Output: 3
     Input: word1 = "makes", word2 = "coding"
     Output: 1
     Note:
     You may assume that word1 does not equal to word2, and word1 and word2 are both in the list.
     * */
    public int shortestDistance(String[] words, String word1, String word2) {
        int i1 = -1, i2 = -1;
        int dist = words.length;
        for (int i = 0; i < words.length; i++) {
            if (words[i].equals(word1)) {
                i1 = i;
            } else if (words[i].equals(word2)) {
                i2 = i;
            }
            if (i1 != -1 && i2 != -1) {
                dist = Math.min(dist, Math.abs(i1 - i2));
            }
        }
        return dist;
    }
    /**
     * LC245 Shortest Word distance 3
     * Assume that words = ["practice", "makes", "perfect", "coding", "makes"].
     Input: word1 = “makes”, word2 = “coding”
     Output: 1
     Input: word1 = "makes", word2 = "makes"
     Output: 3
     Note:
     You may assume word1 and word2 are both in the list.
     * */
    public int shortestWordDistance(String[] words, String word1, String word2) {
        long dist = Integer.MAX_VALUE, i1 = dist, i2 = -dist;
        boolean same = word1.equals(word2);
        for (int i=0; i<words.length; i++) {
            if (words[i].equals(word1)) {
                if (same) {
                    i1 = i2;
                    i2 = i;
                } else {
                    i1 = i;
                }
            } else if (words[i].equals(word2)) {
                i2 = i;
            }
            dist = Math.min(dist, Math.abs(i1 - i2));
        }
        return (int) dist;
    }
    /**
     * LC246 Strobogrammatic Number
     * 一个数旋转180度之后还和原来相等(上下颠倒之后还相等)
     * Example 1:

     Input:  "69"
     Output: true
     Example 2:

     Input:  "88"
     Output: true
     Example 3:

     Input:  "962"
     Output: false
     * */
    public boolean isStrobogrammatic(String s) {
        int[] arr = new int[10];
        arr[6] = 9;
        arr[9] = 6;
        arr[1] = 1;
        arr[0] = 0;
        arr[8] = 8;

        char[] chars = s.toCharArray();
        int left = 0, right = chars.length - 1;
        while(left <= right) {
            int begin = chars[left] - '0';
            int end = chars[right] - '0';
            if((begin != 0 && arr[begin] == 0) || arr[begin] != end) {
                return false;
            }
            left++;
            right--;
        }
        return true;
    }
    /**LC247 Strobogrammatic Number 2
     * 找到有n位的strobogrammatic数
     * Example:
     Input:  n = 2
     Output: ["11","69","88","96"]
     * */
    public List<String> findStrobogrammatic1(int n) {
        List<String> list;
        if (n % 2 == 1) {
            list = Arrays.asList("0", "1", "8");
        } else {
            list = Arrays.asList("");
        }
        for(int i = (n % 2) + 2; i <= n; i += 2){
            List<String> newList = new ArrayList<>();
            for(String str : list){
                if(i != n)
                    newList.add("0" + str + "0");
                newList.add("1" + str + "1");
                newList.add("6" + str + "9");
                newList.add("8" + str + "8");
                newList.add("9" + str + "6");
            }
            list = newList;
        }
        return list;
    }
    /**
     * LC249 Group Shifted Strings
     * Given a string, we can "shift" each of its letter to its successive letter, for example: "abc" -> "bcd". We can keep "shifting" which forms the sequence:

     "abc" -> "bcd" -> ... -> "xyz"
     Given a list of strings which contains only lowercase alphabets, group all strings that belong to the same shifting sequence.

     Example:

     Input: ["abc", "bcd", "acef", "xyz", "az", "ba", "a", "z"],
     Output:
     [
     ["abc","bcd","xyz"],
     ["az","ba"],
     ["acef"],
     ["a","z"]
     ]
     * */
    public List<List<String>> groupStrings(String[] strs) {
        //Create a hashmap. key is a number (the offset compared to its first char),
        //value is a list of strings which have the same offset.
        //For each string, convert it to char array
        //Then subtract its first character for every character
        //eg. "abc" -> "0,1,2,"        "am"->"0,12,"
        Map<String,List<String>> map = new HashMap<>();
        for(String str : strs) {
            String key = "";
            char first = str.charAt(0);
            for(char c:str.toCharArray())
                key+=(c-first<0?c-first+26:c-first)+",";
            if(!map.containsKey(key))
                map.put(key,new ArrayList<String>());
            map.get(key).add(str);
        }
        for(String key:map.keySet())
            Collections.sort(map.get(key));
        return new ArrayList<List<String>>(map.values());
    }
    public List<List<String>> groupStrings1(String[] strs) {
        //Create a hashmap. key is a number (the offset compared to its first char),
        //value is a list of strings which have the same offset.
        //For each string, convert it to char array
        //Then subtract its first character for every character
        //eg. "abc" -> "0,1,2,"        "am"->"0,12,"
        Map<String,List<String>> map = new HashMap<>();
        for(String str : strs) {
            StringBuilder sb = new StringBuilder();
            char first = str.charAt(0);
            for(char c:str.toCharArray())
                sb.append(c-first<0?c-first+26:c-first).append(",");
            String key = sb.toString();
            if(!map.containsKey(key))
                map.put(key, new ArrayList<>());
            map.get(key).add(str);
        }
        for(String key:map.keySet())
            Collections.sort(map.get(key));
        return new ArrayList<>(map.values());
    }
    /**
     * LC250 count univalue subtrees
     * Given a binary tree, count the number of uni-value subtrees.
     A Uni-value subtree means all nodes of the subtree have the same value.
     Example :
     Input:  root = [5,1,5,5,5,null,5]

         5
        / \
       1   5
      / \   \
     5   5   5
     Output: 4
     * */
    public int countUnivalSubtrees(TreeNode root) {
        int[] count = new int[1];
        countUnivalSubtreesHelper(root, count);
        return count[0];
    }
    private boolean countUnivalSubtreesHelper(TreeNode node, int[] count) {
        if (node == null) {
            return true;
        }
        boolean left = countUnivalSubtreesHelper(node.left, count);
        boolean right = countUnivalSubtreesHelper(node.right, count);
        if (left && right) {
            if (node.left != null && node.value != node.left.value) {
                return false;
            }
            if (node.right != null && node.value != node.right.value) {
                return false;
            }
            count[0]++;
            return true;
        }
        return false;
    }
    /**
     * LC252 Meeting Rooms
     * Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei), determine if a person could attend all meetings.
     Example 1:
     Input: [[0,30],[5,10],[15,20]]
     Output: false
     Example 2:
     Input: [[7,10],[2,4]]
     Output: true
     * */
    public boolean canAttendMeetings(Interval[] intervals) {
        Arrays.sort(intervals, (i1, i2) -> {
            return i1.start - i2.start;
        });
        for (int i = 0; i < intervals.length - 1; i++) {
            if (intervals[i].end > intervals[i + 1].start) return false;
        }
        return true;
    }
    /**
     * LC253 Meeting Rooms 2
     * Given an array of meeting time intervals consisting of start and end times [[s1,e1],[s2,e2],...] (si < ei), find the minimum number of conference rooms required.
     Example 1:
     Input: [[0, 30],[5, 10],[15, 20]]
     Output: 2
     Example 2:
     Input: [[7,10],[2,4]]
     Output: 1
     * */
    public int minMeetingRooms(Interval[] intervals) {
        if (intervals == null || intervals.length == 0) return 0;
        // Sort the intervals by start time
        Arrays.sort(intervals, (a, b) -> { return a.start - b.start; });
        // Use a min heap to track the minimum end time of merged intervals
        PriorityQueue<Interval> minHeap = new PriorityQueue<>(intervals.length, (a, b) -> { return a.end - b.end; });
        // start with the first meeting, put it to a meeting room
        minHeap.offer(intervals[0]);
        for (int i = 1; i < intervals.length; i++) {
            // get the meeting room that finishes earliest
            Interval interval = minHeap.poll();
            if (intervals[i].start >= interval.end) {
                // if the current meeting starts right after
                // there's no need for a new room, merge the interval
                interval.end = intervals[i].end;
            } else {
                // otherwise, this meeting needs a new room
                minHeap.offer(intervals[i]);
            }
            // don't forget to put the meeting room back
            minHeap.offer(interval);
        }
        return minHeap.size();
    }
    /**
     * LC254 Factor Combinations
     * Numbers can be regarded as product of its factors. For example,
     8 = 2 x 2 x 2;
     = 2 x 4.
     Write a function that takes an integer n and return all possible combinations of its factors.

     Note:

     You may assume that n is always positive.
     Factors should be greater than 1 and less than n.
     Example 1:

     Input: 1
     Output: []
     Example 2:

     Input: 37
     Output:[]
     Example 3:

     Input: 12
     Output:
     [
     [2, 6],
     [2, 2, 3],
     [3, 4]
     ]
     Example 4:

     Input: 32
     Output:
     [
     [2, 16],
     [2, 2, 8],
     [2, 2, 2, 4],
     [2, 2, 2, 2, 2],
     [2, 4, 4],
     [4, 8]
     ]
     * */
    public List<List<Integer>> getFactors(int n) {
        List<List<Integer>> result = new ArrayList<>();
        factorHelper(result, new ArrayList<>(), n, 2);
        return result;
    }

    public void factorHelper(List<List<Integer>> result, List<Integer> item, int n, int start){
        if (n <= 1) {
            if (item.size() > 1) {
                result.add(new ArrayList<>(item));
            }
            return;
        }

        for (int i = start; i <= n; ++i) {
            if (n % i == 0) {
                item.add(i);
                factorHelper(result, item, n/i, i);
                item.remove(item.size()-1);
            }
        }
    }
    public List<List<Integer>> getFactors1(int n) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> cur = new ArrayList<>();
        factor1Helper(n, result, cur, 2);
        return result;
    }

    private void factor1Helper(int n, List<List<Integer>> result, List<Integer> cur, int idx) {
        if (!cur.isEmpty()) {
            List<Integer> nsol = new ArrayList<>(cur);
            nsol.add(n);
            result.add(nsol);
        }

        for (int i = idx; i * i <= n; ++i) {
            if (n % i == 0) {
                cur.add(i);
                factor1Helper(n / i, result, cur, i);
                cur.remove(cur.size() - 1);
            }
        }
    }
    /**
     * LC255 verify preorder sequence in BST
     5
     / \
     2   6
     / \
     1   3
     Example 1:

     Input: [5,2,6,1,3]
     Output: false
     Example 2:

     Input: [5,2,1,3,6]
     Output: true
     * */
    public boolean verifyPreorder(int[] preorder) {
        int low = Integer.MIN_VALUE, i = -1;
        for (int p : preorder) {
            if (p < low) return false;
            while (i >= 0 && p > preorder[i]) low = preorder[i--];
            preorder[++i] = p;
        }
        return true;
    }
    public boolean verifyPreorder1(int[] preorder) {
        int low = Integer.MIN_VALUE;
        Stack<Integer> path = new Stack<>();
        for (int p : preorder) {
            if (p < low)
                return false;
            while (!path.empty() && p > path.peek())
                low = path.pop();
            path.push(p);
        }
        return true;
    }
    public boolean verifyInorder(int arr[]) {
        // Array has one or no element
        if (arr.length == 0 || arr.length == 1) return true;
        for (int i = 1; i < arr.length; i ++) {
            // Unsorted pair found
            if (arr[i - 1] > arr[i]) return false;
        }
        // No unsorted pair found
        return true;
    }

    public boolean verifyPreorder2(int[] preorder) {
        return verifyHelper(preorder, Integer.MIN_VALUE, Integer.MAX_VALUE, new int[]{0});
    }
    private boolean verifyHelper(int[] preorder,int min, int max, int[] idx) {
        if (idx[0] == preorder.length || preorder[idx[0]] > max) return true;
        if (preorder[idx[0]] < min) return false;
        int root = preorder[idx[0] ++];
        return verifyHelper(preorder,min, root, idx) && verifyHelper(preorder, root, max, idx);
    }
    @Test
    public void testVerifyPreorder(){
        int[] arr = new int[]{10,6,5,8,7,9};
        System.out.println(verifyPreorder(arr));
        System.out.println(verifyInorder(arr));
        System.out.println(verifyInorder(new int[]{1, 2, 3, 5, 6}));
        System.out.println(verifyInorder(new int[]{5, 6, 7, 8, 9, 10}));
    }

    /**LC256 Paint house 相邻不同色问题
     There are a row of n houses, each house can be painted with one of the three colors: red, blue or green. The cost of painting each house with a certain color is different. You have to paint all the houses such that no two adjacent houses have the same color.
     The cost of painting each house with a certain color is represented by a n x 3 cost matrix. For example, costs[0][0] is the cost of painting house 0 with color red; costs[1][2] is the cost of painting house 1 with color green, and so on... Find the minimum cost to paint all houses.
     Note:
     All costs are positive integers.
     Example:
     Input: [[17,2,17],[16,16,5],[14,3,19]]
     Output: 10
     Explanation: Paint house 0 into blue, paint house 1 into green, paint house 2 into blue.
     Minimum cost: 2 + 5 + 3 = 10.
     * */
    public int minCost(int[][] costs) {
        if(costs == null || costs.length == 0){
            return 0;
        }
        for(int i=1; i<costs.length; i++){
            costs[i][0] += Math.min(costs[i-1][1],costs[i-1][2]);
            costs[i][1] += Math.min(costs[i-1][0],costs[i-1][2]);
            costs[i][2] += Math.min(costs[i-1][1],costs[i-1][0]);
        }
        int n = costs.length-1;
        return Math.min(Math.min(costs[n][0], costs[n][1]), costs[n][2]);
    }
    /**
     * LC257 binary tree paths
     * 找到一个树从root到leaf的所有path
     * */
    public List<String> binaryTreePaths(TreeNode root) {
        List<String> result = new ArrayList<>();
        if (root == null) return result;
        searchBT(root, "", result);
        return result;
    }
    private void searchBT(TreeNode root, String path, List<String> result) {
        if (root.left == null && root.right == null) result.add(path + root.value);
        if (root.left != null) searchBT(root.left, path + root.value + "->", result);
        if (root.right != null) searchBT(root.right, path + root.value + "->", result);
    }
    @Test
    public void binaryTeePathsTest(){
        TreeNode root = new TreeNode(1);
        root.left = new TreeNode(2);
        root.right = new TreeNode(3);
        root.left.right = new TreeNode(5);
        System.out.println(binaryTreePaths(root));
    }
    /**
     * LC258 add digits
     * 将一个数的所有数位相加，直到最后只剩一位数
     * */
    public int addDigits(int num) {
        while (num >= 10) {
            num = num / 10 + num % 10;
        }
        return num;
    }
    /**
     * LC259 3Sum Smaller
     Given an array of n integers nums and a target, find the number of index triplets i, j, k with 0 <= i < j < k < n that satisfy the condition nums[i] + nums[j] + nums[k] < target.
     Example:
     Input: nums = [-2,0,1,3], and target = 2
     Output: 2
     Explanation: Because there are two triplets which sums are less than 2:
     [-2,0,1]
     [-2,0,3]
     Follow up: Could you solve it in O(n2) runtime?
     * */
    public int threeSumSmaller(int[] arr, int target) {
        Arrays.sort(arr);
        int ret = 0;
        for (int i = 0; i < arr.length - 2; i ++) {
            //if(i > 0 && arr[i] == arr[i - 1])continue;
            int left = i + 1, right = arr.length - 1;
            while (left < right) {
                int cur = arr[left] + arr[right] + arr[i];
                if (cur < target) {
                    System.out.println(cur);
                    ret += right - left;
                    left ++;
                } else {
                    right --;
                }
            }
        }
        return ret;
    }
    private int twoSumSmaller(int[] arr, int target) {
        int ret = 0;
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int cur = arr[left] + arr[right];
            if (cur < target) {
                ret += right - left;
                left++;
            } else {
                right--;
            }
        }
        return ret;
    }
    /**
     * LC260 single number 3
     * 一个数组里，有两个数各自只出现一次，其他数都是重复两次的
     * Example:
     Input:  [1,2,1,3,2,5]
     Output: [3,5] 结果顺序无所谓 [5,3]也行
     要求: time O(n), space O(1)
     * */
    public int[] singleNumber(int[] nums) {
        // Pass 1 :
        // Get the XOR of the two numbers we need to find
        int diff = 0;
        for (int num : nums) {
            diff ^= num;
        }
        // Get its last set bit
        diff &= -diff;
        // Pass 2 :
        int[] rets = {0, 0}; // this array stores the two numbers we will return
        for (int num : nums) {
            if ((num & diff) == 0) { // the bit is not set
                rets[0] ^= num;
            } else {  // the bit is set
                rets[1] ^= num;
            }
        }
        return rets;
    }
    /**
     * LC261 Graph Valid Tree
     * 给一个数组的数组(矩阵)每一组数都表示一条边，看看能不能组成一个树
     * Example 1:
     Input: n = 5, and edges = [[0,1], [0,2], [0,3], [1,4]]
     Output: true
     Example 2:
     Input: n = 5, and edges = [[0,1], [1,2], [2,3], [1,3], [1,4]]
     Output: false
     Note: you can assume that no duplicate edges will appear in edges. Since all edges are undirected, [0,1] is the same as [1,0] and thus will not appear together in edges.
     * */
    public boolean validTree(int n, int[][] edges) {
        // initialize n isolated islands
        int[] arr = new int[n];
        Arrays.fill(arr, -1);
        // perform union find
        for (int i = 0; i < edges.length; i++) {
            int x = find(arr, edges[i][0]);
            int y = find(arr, edges[i][1]);
            // if two vertices happen to be in the same set
            // then there's a cycle
            if (x == y) return false;
            // union
            arr[x] = y;
        }

        return edges.length == n - 1;
    }
    private int find(int[] arr, int i) {
        if (arr[i] == -1) return i;
        return find(arr, arr[i]);
    }
    /**
     * LC263 Ugly Number
     * */
    public boolean isUgly(int num) {
        if(num == 1) return true;
        if(num == 0) return false;
        while(num % 2 == 0) num = num / 2;
        while(num % 3 == 0) num = num / 3;
        while(num % 5 == 0) num = num / 5;
        return num == 1;
    }
    /**
     * LC264 ugly number 2
     * 找到nth ugly number
     * */
    public int nthUglyNumber(int n) {
        int[] arr = new int[n];
        arr[0] = 1;
        int idx2 = 0, idx3 = 0, idx5 = 0;
        int factor2 = 2, factor3 = 3, factor5 = 5;
        for(int i = 1; i < n; i ++){
            int min = Math.min(Math.min(factor2, factor3), factor5);
            arr[i] = min;
            if(factor2 == min)
                factor2 = 2 * arr[++ idx2];
            if(factor3 == min)
                factor3 = 3 * arr[++ idx3];
            if(factor5 == min)
                factor5 = 5 * arr[++ idx5];
        }
        return arr[n - 1];
    }
    /**LC266 Palindrome Permutation
     * 给一个string，它的排列能形成palindrome吗
     Example 1:
     Input: "code"
     Output: false
     Example 2:
     Input: "aab"
     Output: true
     Example 3:
     Input: "carerac"
     Output: true
     * */
    public boolean canPermutePalindrome1(String s) {  // time O(n), space O(n)
        Set < Character > set = new HashSet < > ();
        for (int i = 0; i < s.length(); i++) {
            if (!set.add(s.charAt(i)))
                set.remove(s.charAt(i));
        }
        return set.size() <= 1;
    }
    public boolean canPermutePalindrome2(String s) {  // time O(n), space O(1)
        int[] arr = new int[128];
        for (int i = 0; i < s.length(); i++) {
            arr[s.charAt(i)]++;
        }
        int count = 0;
        for (int num : arr) {
            count += num % 2;
            if (count > 1) break;
        }
        return count <= 1;
    }
    /**
     * LC267 Palindrome permutation2
     * Given a string s, return all the palindromic permutations (without duplicates) of it. Return an empty list if no palindromic permutation could be form.
     Example 1:
     Input: "aabb"
     Output: ["abba", "baab"]
     Example 2:
     Input: "abc"
     Output: []
     * */
    public List<String> generatePalindromes1(String s) {
        int odd = 0;
        String mid = "";
        List<String> res = new ArrayList<>();
        List<Character> list = new ArrayList<>();
        Map<Character, Integer> map = new HashMap<>();
        // step 1. build character count map and count odds
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            map.put(c, map.containsKey(c) ? map.get(c) + 1 : 1);
            odd += map.get(c) % 2 != 0 ? 1 : -1;
        }
        // cannot form any palindromic string
        if (odd > 1) return res;
        // step 2. add half count of each character to list
        for (Map.Entry<Character, Integer> entry : map.entrySet()) {
            char key = entry.getKey();
            int val = entry.getValue();
            if (val % 2 != 0) mid += key;
            for (int i = 0; i < val / 2; i++) list.add(key);
        }
        // step 3. generate all the permutations
        getPerm(list, mid, new boolean[list.size()], new StringBuilder(), res);
        return res;
    }
    // generate all unique permutation from list
    private void getPerm(List<Character> list, String mid, boolean[] used, StringBuilder sb, List<String> res) {
        if (sb.length() == list.size()) {
            // form the palindromic string
            res.add(sb.toString() + mid + sb.reverse().toString());
            sb.reverse();
            return;
        }
        for (int i = 0; i < list.size(); i++) {
            // avoid duplication
            if (i > 0 && list.get(i) == list.get(i - 1) && !used[i - 1]) continue;
            if (!used[i]) {
                used[i] = true; sb.append(list.get(i));
                // recursion
                getPerm(list, mid, used, sb, res);
                // backtracking
                used[i] = false; sb.deleteCharAt(sb.length() - 1);
            }
        }
    }

    public List < String > generatePalindromes(String s) {
        Set < String > set = new HashSet <>();
        int[] arr = new int[128];
        char[] chars = new char[s.length() / 2];
        if (!canPermutePalindrome(s, arr)) return new ArrayList <>();
        char c = 0;
        int k = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] % 2 == 1) c = (char) i;
            for (int j = 0; j < arr[i] / 2; j++) {
                chars[k ++] = (char) i;
            }
        }
        permute(set, chars, c, 0);
        return new ArrayList <> (set);
    }
    private boolean canPermutePalindrome(String s, int[] arr) {
        int count = 0;
        for(char c : s.toCharArray()){
            arr[c] ++;
            if(arr[c] % 2 == 0) {
                count --;
            } else {
                count ++;
            }
        }
        return count <= 1;
    }
    private void permute(Set<String> set, char[] chars, char c, int idx) {
        if (idx == chars.length) {
            set.add(new String(chars) + (c == 0 ? "" : c) + new StringBuffer(new String(chars)).reverse());
        } else {
            for (int i = idx; i < chars.length; i++) {
                if (chars[idx] != chars[i] || idx == i) {
                    swap(chars, idx, i);
                    permute(set, chars, c, idx + 1);
                    swap(chars, idx, i);
                }
            }
        }
    }
    public void swap(char[] s, int i, int j) {
        char temp = s[i];
        s[i] = s[j];
        s[j] = temp;
    }
    @Test
    public void test1(){
        char c = 0;
        System.out.println(c == ' ');
    }
    /**
     * LC268 missing number
     * */
    public int missingNumber(int[] nums) {
        int xor = 0;
        for (int n : nums) xor ^= n;
        for (int i = 0; i <= nums.length; i ++) {
            xor ^= i;
        }
        return xor;
    }
    /** LC274 H Index
     * A scientist has index h if h of his/her N papers have at least h citations each, and the other N − h papers have no more than h citations each.
     * 一名科研人员的h指数是他的N个paper中, 有h个paper至少被引用h次，剩下N - h个paper被引用次数不超过h
     * Input: citations = [3,0,6,1,5]
     Output: 3
     Explanation: [3,0,6,1,5] means the researcher has 5 papers in total and each of them had
     received 3, 0, 6, 1, 5 citations respectively.
     Since the researcher has 3 papers with at least 3 citations each and the remaining
     two with no more than 3 citations each, her h-index is 3.
     * */
    public int hIndex(int[] citations) {
        int len = citations.length;
        int[] arr = new int[len + 1];
        for(int i = 0; i < len; i ++) {
            int val = citations[i];
            arr[Math.min(val, len)] ++;
        }
        int sum = 0;
        for(int i = len; i >= 0; i --) {
            sum += arr[i];
            if(sum >= i) return i;
        }
        return 0;
    }
    /**
     * LC275 h index2
     * Given an array of citations sorted in ascending order (each citation is a non-negative integer) of a researcher,
     * write a function to compute the researcher's h-index.
     * Input: citations = [0,1,3,5,6]
     Output: 3
     Explanation: [0,1,3,5,6] means the researcher has 5 papers in total and each of them had
     received 0, 1, 3, 5, 6 citations respectively.
     Since the researcher has 3 papers with at least 3 citations each and the remaining
     two with no more than 3 citations each, her h-index is 3.
     * */
    public int hIndex2(int[] arr) {
        int len = arr.length;
        int left = 0, right = len - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == len - mid) {
                return len - mid;
            } else if (arr[mid] < len - mid) {
                left = mid + 1;
            } else {
                //(citations[med] > len-med), med qualified as a hIndex,
                // but we have to continue to search for a higher one.
                right = mid - 1;
            }
        }
        return len - left;
    }
    /**
     * LC276 Paint Fence
     * 有一个篱笆有n个桩，可以被涂上k种任意的颜色，但是相邻两个桩不同色
     There is a fence with n posts, each post can be painted with one of the k colors.
     You have to paint all the posts such that no more than two adjacent fence posts have the same color.
     Return the total number of ways you can paint the fence.
     Note:
     n and k are non-negative integers.
     Example:
     Input: n = 3, k = 2
     Output: 6
     Explanation: Take c1 as color 1, c2 as color 2. All possible ways are:

     post1  post2  post3
     -----      -----  -----  -----
     1         c1     c1     c2
     2         c1     c2     c1
     3         c1     c2     c2
     4         c2     c1     c1
     5         c2     c1     c2
     6         c2     c2     c1
     * */
    public int numWays(int n, int k) {
        if(n == 0) {
            return 0;
        } else if(n == 1) {
            return k;
        }
        int diffColorCounts = k * (k - 1);  // 相邻不同色的涂法有几种
        int sameColorCounts = k;  // 同色的涂法
        for(int i=2; i<n; i++) {
            int temp = diffColorCounts;
            diffColorCounts = (diffColorCounts + sameColorCounts) * (k-1);
            sameColorCounts = temp;
        }
        return diffColorCounts + sameColorCounts;
    }
    /**
     * LC277 Find the Celebrity
     * n个人的聚会，里面有个名人(他不认识另外n-1个人，但是剩下所有人都认识他，找出他来)
     * Suppose you are at a party with n people (labeled from 0 to n - 1) and among them, there may exist one celebrity. The definition of a celebrity is that all the other n - 1 people know him/her but he/she does not know any of them.
     Now you want to find out who the celebrity is or verify that there is not one. The only thing you are allowed to do is to ask questions like: "Hi, A. Do you know B?" to get information of whether A knows B. You need to find out the celebrity (or verify there is not one) by asking as few questions as possible (in the asymptotic sense).
     You are given a helper function bool knows(a, b) which tells you whether A knows B. Implement a function int findCelebrity(n), your function should minimize the number of calls to knows.
     Note: There will be exactly one celebrity if he/she is in the party. Return the celebrity's label if there is a celebrity in the party. If there is no celebrity, return -1.
     *
     * 提供一个工具API knows方法，可以知道a是否认识b
     * */
    boolean knows(int a, int b) {return false;}
    public int findCelebrity(int n) {
        int candidate = 0;
        for(int i = 1; i < n; i++){  // 名人不认识剩下的人, 所以先找出一个可能是名人的，因为剩余所有人都认识名人
            if(knows(candidate, i)) candidate = i;
        }
        for(int i = 0; i < n; i++){
            if(i != candidate && (knows(candidate, i) || !knows(i, candidate))) return -1;
        }
        return candidate;
    }
    /**
     * LC278 First bad version
     * 一批产品，一个坏了，后边都坏，找到最开始坏的那个，会给一个判断是否坏的API

     Given n = 5, and version = 4 is the first bad version.

     call isBadVersion(3) -> false
     call isBadVersion(5) -> true
     call isBadVersion(4) -> true

     Then 4 is the first bad version.

     * */
    public int firstBadVersion(int n) {
        int left = 1;
        int right = n;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (isBadVersion(mid)) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }
        return left;
    }
    private boolean isBadVersion(int version){
        /** system will give the API, will return good or bad */
        return false;
    }
    /**
     * LC279 perfect square
     * 用最少的平方数来加和得到该数
     * Given a positive integer n, find the least number of perfect square numbers (for example, 1, 4, 9, 16, ...) which sum to n.

     Example 1:

     Input: n = 12
     Output: 3
     Explanation: 12 = 4 + 4 + 4.
     Example 2:

     Input: n = 13
     Output: 2
     Explanation: 13 = 4 + 9.
     * */
    // Reference to DP: 切数字
    /**
     * LC280 wiggle sort
     * Given an unsorted array nums, reorder it in-place such that nums[0] <= nums[1] >= nums[2] <= nums[3]....
     Example:
     Input: nums = [3,5,2,1,6,4]
     Output: One possible answer is [3,5,1,6,2,4]
     * */
    public void wiggleSort(int[] nums) {
        Arrays.sort(nums);
        for (int i = 1; i < nums.length - 1; i += 2) {
            swap(nums, i, i + 1);
        }
    }
    private void swap(int[] arr, int i, int j){
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
    public void wiggleSort1(int[] nums) {
        boolean less = true;
        for (int i = 0; i < nums.length - 1; i++) {
            if (less) {
                if (nums[i] > nums[i + 1]) {
                    swap(nums, i, i + 1);
                }
            } else {
                if (nums[i] < nums[i + 1]) {
                    swap(nums, i, i + 1);
                }
            }
            less = !less;
        }
    }
    public void wiggleSort2(int[] nums) {
        for (int i = 0; i < nums.length - 1; i++) {
            if (((i % 2 == 0) && nums[i] > nums[i + 1])
                    || ((i % 2 == 1) && nums[i] < nums[i + 1])) {
                swap(nums, i, i + 1);
            }
        }
    }
    /**
     * LC285 Inorder successor in BST
     * Given a binary search tree and a node in it, find the in-order successor of that node in the BST.

     Note: If the given node has no in-order successor in the tree, return null.

     Example 1:

     Input: root = [2,1,3], p = 1
     2
     / \
     1   3
     Output: 2
     Example 2:
     Input: root = [5,3,6,2,4,null,null,1], p = 6

     5
     / \
     3   6
     / \
     2   4
     /
     1
     Output: null
     * */
    public TreeNode successor(TreeNode root, TreeNode p) {
        if (root == null)
            return null;
        if (root.value <= p.value) {
            return successor(root.right, p);
        } else {
            TreeNode left = successor(root.left, p);
            return (left != null) ? left : root;
        }
    }
    public TreeNode predecessor(TreeNode root, TreeNode p) {
        if (root == null)
            return null;
        if (root.value >= p.value) {
            return predecessor(root.left, p);
        } else {
            TreeNode right = predecessor(root.right, p);
            return (right != null) ? right : root;
        }
    }
    /**
     * LC286 Walls and Gates
     You are given a m x n 2D grid initialized with these three possible values.
     -1 - A wall or an obstacle.
     0 - A gate.
     INF - Infinity means an empty room. We use the value 231 - 1 = 2147483647 to represent INF as you may assume that the distance to a gate is less than 2147483647.
     Fill each empty room with the distance to its nearest gate. If it is impossible to reach a gate, it should be filled with INF.
     Example:

     Given the 2D grid:
     INF  -1  0  INF
     INF INF INF  -1
     INF  -1 INF  -1
     0  -1 INF INF
     After running your function, the 2D grid should be:
     3  -1   0   1
     2   2   1  -1
     1  -1   2  -1
     0  -1   3   4
     * */
    public void wallsAndGates(int[][] rooms) {
        int EMPTY = Integer.MAX_VALUE;
        int GATE = 0;
        int[][] DIRECTIONS = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };
        int m = rooms.length;
        if (m == 0) return;
        int n = rooms[0].length;
        Queue<int[]> q = new LinkedList<>();
        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {
                if (rooms[row][col] == GATE) {
                    q.add(new int[] { row, col });
                }
            }
        }
        while (!q.isEmpty()) {
            int[] point = q.poll();
            int row = point[0];
            int col = point[1];
            for (int[] direction : DIRECTIONS) {
                int r = row + direction[0];
                int c = col + direction[1];
                if (r < 0 || c < 0 || r >= m || c >= n || rooms[r][c] != EMPTY) {
                    continue;
                }
                rooms[r][c] = rooms[row][col] + 1;
                q.add(new int[] { r, c });
            }
        }
    }

    /**
     * LC289 Game of Life
     * m x n 矩阵的棋盘, 里面数字1代表活，0代表死, 每个点周围有8个元素
     * 规则: 一个点周围活子少于2则死，活子为2或3则生, 活子多于3则死因为人口太多，如果一个子已死但是周围有三个活子则他自己死而复生，
     * 计算这个棋盘的下一个状态
     * Any live cell with fewer than two live neighbors dies, as if caused by under-population.
     Any live cell with two or three live neighbors lives on to the next generation.
     Any live cell with more than three live neighbors dies, as if by over-population..
     Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
     Input:
     [
     [0,1,0],
     [0,0,1],
     [1,1,1],
     [0,0,0]
     ]
     Output:
     [
     [0,0,0],
     [1,0,1],
     [0,1,1],
     [0,1,0]
     ]
     https://leetcode.com/problems/game-of-life/discuss/73223/Easiest-JAVA-solution-with-explanation
     * */
    public void gameOfLife(int[][] board) {
        /*
状态: 前一位表示下一代的状态,后一位表示当前的状态
00: 死->死
10: 死->活
01: 活->死
11: 活->活
        * */
        if (board == null || board.length == 0) return;
        int m = board.length, n = board[0].length;
        for (int i = 0; i < m; i ++) {
            for (int j = 0; j < n; j ++) {
                int lives = liveNeighbors(board, m, n, i, j);
                // In the beginning, every 2nd bit is 0;
                // So we only need to care about when will the 2nd bit become 1.
                if (board[i][j] == 1 && lives >= 2 && lives <= 3) {
                    board[i][j] = 3; // Make the 2nd bit 1: 01 ---> 11
                }
                if (board[i][j] == 0 && lives == 3) {
                    board[i][j] = 2; // Make the 2nd bit 1: 00 ---> 10
                }
            }
        }
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                board[i][j] >>= 1;  // Get the 2nd state.
            }
        }
    }
    private int liveNeighbors(int[][] board, int m, int n, int i, int j) {
        int lives = 0;
        for (int x = Math.max(i - 1, 0); x <= Math.min(i + 1, m - 1); x ++) {
            for (int y = Math.max(j - 1, 0); y <= Math.min(j + 1, n - 1); y ++) {
                lives += board[x][y] & 1;
            }
        }
        lives -= board[i][j] & 1;
        return lives;
    }
    /**
     * LC290 word pattern,
     * 给定字符串和一个pattern，看看单词是否遵照这个pattern
     * 假定全小写，且单词间只有一个空格
     * Input: pattern = "abba", str = "dog cat cat dog"
     Output: true
     Example 2:

     Input:pattern = "abba", str = "dog cat cat fish"
     Output: false
     Example 3:

     Input: pattern = "aaaa", str = "dog cat cat dog"
     Output: false
     Example 4:

     Input: pattern = "abba", str = "dog dog dog dog"
     Output: false

     * */
    public boolean wordPattern(String pattern, String str) {
        String[] arr= str.split(" ");
        Map<Character, String> map = new HashMap<>();
        if (arr.length != pattern.length()) return false;
        for(int i = 0; i < arr.length; i ++){
            char c = pattern.charAt(i);
            if(map.containsKey(c)){
                if(!map.get(c).equals(arr[i]))
                    return false;
            } else {
                if(map.containsValue(arr[i])) return false;
                map.put(c, arr[i]);
            }
        }
        return true;
    }
    /**
     * LC292 Nim game
     * 你和朋友玩一堆石头，你先取石头,每次1~3个，拿走最后一个石头的是赢家，问你能赢吗
     * Input: 4
     Output: false
     Explanation: If there are 4 stones in the heap, then you will never win the game;
     No matter 1, 2, or 3 stones you remove, the last stone will always be
     removed by your friend.
     * */
    public boolean canWinNim(int n) {
        return n % 4 != 0;
    }
    /**
     * LC293 Flip Game
     * You are playing the following Flip Game with your friend: Given a string that contains only these two characters: + and -, you and your friend take turns to flip two consecutive "++" into "--". The game ends when a person can no longer make a move and therefore the other person will be the winner.
     Write a function to compute all possible states of the string after one valid move.
     Example:

     Input: s = "++++"
     Output:
     [
     "--++",
     "+--+",
     "++--"
     ]
     Note: If there is no valid move, return an empty list [].
     * */
    public List<String> generatePossibleNextMoves(String s) {
        List<String> list = new ArrayList<>();
        for (int i = -1; (i = s.indexOf("++", i + 1)) >= 0; )
            list.add(s.substring(0, i) + "--" + s.substring(i + 2));
        return list;
    }
    /**
     * LC294 Flip Game2
     * You are playing the following Flip Game with your friend: Given a string that contains only these two characters: + and -, you and your friend take turns to flip two consecutive "++" into "--". The game ends when a person can no longer make a move and therefore the other person will be the winner.
     Write a function to determine if the starting player can guarantee a win.
     Example:

     Input: s = "++++"
     Output: true
     Explanation: The starting player can guarantee a win by flipping the middle "++" to become "+--+".
     Follow up:
     Derive your algorithm's runtime complexity.
     * */
    public boolean canWin(String s) {
        if (s == null || s.length() < 2) {
            return false;
        }
        for (int i = 0; i < s.length() - 1; i++) {
            if (s.startsWith("++", i)) {
                String t = s.substring(0, i) + "--" + s.substring(i + 2);
                if (!canWin(t)) {
                    return true;
                }
            }
        }
        return false;
    }
    /**
     * LC298 Binary Tree Longest Consecutive Sequence
     * Given a binary tree, find the length of the longest consecutive sequence path.

     The path refers to any sequence of nodes from some starting node to any node in the tree along the parent-child connections. The longest consecutive path need to be from parent to child (cannot be the reverse).

     Example 1:

     Input:

     1
     \
      3
     / \
     2   4
         \
         5

     Output: 3

     Explanation: Longest consecutive sequence path is 3-4-5, so return 3.
     Example 2:

     Input:

     2
     \
      3
     /
    2
   /
  1

     Output: 2

     Explanation: Longest consecutive sequence path is 2-3, not 3-2-1, so return 2.
     * */
    public int longestConsecutive(TreeNode root) {
        int[] maxLen = new int[1];
        longestConsecutiveHelper(root, null, 0, maxLen);
        return maxLen[0];
    }
    private void longestConsecutiveHelper(TreeNode p, TreeNode parent, int length, int[] maxLen) {
        if (p == null) return;
        length = (parent != null && p.value == parent.value + 1) ? length + 1 : 1;
        maxLen[0] = Math.max(maxLen[0], length);
        longestConsecutiveHelper(p.left, p, length, maxLen);
        longestConsecutiveHelper(p.right, p, length, maxLen);
    }
    /**
     * LC299 Bulls and Cows
     * 猜数字游戏，实际数字和猜想数字.
     * 如果所猜数字中有些位数量和位置都对叫Bulls，用A表示
     * 如果所猜数字中有些位数量对但位置不对叫cows，用B表示
     * Example 1:

     Input: secret = "1807", guess = "7810"

     Output: "1A3B"

     Explanation: 1 bull and 3 cows. The bull is 8, the cows are 0, 1 and 7.
     Example 2:

     Input: secret = "1123", guess = "0111"

     Output: "1A1B"

     Explanation: The 1st 1 in friend's guess is a bull, the 2nd or 3rd 1 is a cow.
     * */
    public String getHint(String s, String guess) {
        int bulls = 0;
        int cows = 0;
        int[] arr = new int[10];
        for (int i = 0; i < s.length(); i ++) {
            if (s.charAt(i) == guess.charAt(i)) {
                bulls++;
            } else {
                if (arr[s.charAt(i)-'0']++ < 0) cows++;
                if (arr[guess.charAt(i)-'0']-- > 0) cows++;
            }
        }
        return bulls + "A" + cows + "B";
    }
    /**
     * LC300 longest increasing subsequence
     * */
    // reference to DP longest ascending subsequence

}
/** LC208 Implement Trie (Prefix Tree)
 *  Implement a trie with insert, search, and startsWith methods.
 * */
class Trie {
    TrieNode root;
    /** Initialize your data structure here. */
    public Trie() {
        root = new TrieNode();
    }

    /** Inserts a word into the trie. */
    public void insert(String word) {
        TrieNode node = root;
        for(char c : word.toCharArray()){
            if(node.arr[c - 'a'] == null){
                node.arr[c - 'a'] = new TrieNode();
            }
            node = node.arr[c - 'a'];
        }
        node.isEnd = true;
    }
    private TrieNode searchPrefix(String s){
        TrieNode node = root;
        for(char c : s.toCharArray()){
            if(node.arr[c - 'a'] == null){
                return null;
            } else {
                node = node.arr[c - 'a'];
            }
        }
        return node;
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEnd;
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        TrieNode node = searchPrefix(prefix);
        return node != null;
    }
}
class TrieNode {
    TrieNode[] arr;
    boolean isEnd;
    public TrieNode() {
        isEnd = false;
        arr = new TrieNode[26];
    }
/*

    public boolean containsKey(char ch) {
        return arr[ch -'a'] != null;
    }
    public TrieNode get(char ch) {
        return arr[ch -'a'];
    }
    public void put(char ch, TrieNode node) {
        arr[ch -'a'] = node;
    }
    public void setEnd() {
        isEnd = true;
    }
    public boolean isEnd() {
        return isEnd;
    }
*/
}

/**
 * LC211 Add and Search Word - Data structure design
 * 设计一个word dictionary支持search和add
 */
class WordDictionary {
    TrieNode root;
    /** Initialize your data structure here. */
    public WordDictionary() {
        root = new TrieNode();
    }

    /** Adds a word into the data structure. */
    public void addWord(String word) {
        TrieNode node = root;
        for(char c : word.toCharArray()){
            if(node.arr[c - 'a'] == null) {
                node.arr[c - 'a'] = new TrieNode();
            }
            node = node.arr[c - 'a'];
        }
        node.isEnd = true;
    }

    /** Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter. */
    public boolean search(String word) {
        return helper(word, root, 0);
    }
    private boolean helper(String s, TrieNode root, int idx){
        if(idx == s.length()) return root.isEnd;
        if(s.charAt(idx) == '.'){
            for(TrieNode node : root.arr){
                if(node != null && helper(s, node, idx + 1)) return true;
            }
            return false;
        } else {
            TrieNode node = root.arr[s.charAt(idx) - 'a'];
            return node != null && helper(s, node, idx + 1);
        }
    }
}
/**
 * LC225 implement stack using queues
 * */
class MyStack {
    Queue<Integer> queue;
    public MyStack() {
        this.queue=new LinkedList<>();
    }
    // Push element x onto stack.
    public void push(int x) {
        queue.add(x);
        reverse();
    }
    private void reverse(){
        int size = queue.size();
        for (int i = 0; i < size - 1; i ++) {
            queue.add(queue.poll());
        }
    }
    // Removes the element on top of the stack.
    public int pop() {
        return queue.poll();
    }
    // Get the top element.
    public int top() {
        return queue.peek();
    }
    // Return whether the stack is empty.
    public boolean empty() {
        return queue.isEmpty();
    }
}
/**
 * LC284 peeking iterator
 * */
class PeekingIterator implements Iterator<Integer> {
    private Integer next = null;
    private Iterator<Integer> it;

    public PeekingIterator(Iterator<Integer> iterator) {
        // initialize any member here.
        it = iterator;
        if (it.hasNext()) next = it.next();
    }

    // Returns the next element in the iteration without advancing the iterator.
    public Integer peek() {
        return next;
    }

    // hasNext() and next() should behave the same as in the Iterator interface.
    // Override them if needed.
    @Override
    public Integer next() {
        Integer res = next;
        next = it.hasNext() ? it.next() : null;
        return res;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }
}
/**
 * LC244 Shortest Word Distance 2
 * */
class WordDistance {
    private Map<String, List<Integer>> map;
    public WordDistance(String[] words) {
        map = new HashMap<>();
        for(int i = 0; i < words.length; i++) {
            String w = words[i];
            if(map.containsKey(w)) {
                map.get(w).add(i);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(i);
                map.put(w, list);
            }
        }
    }
    public int shortest(String word1, String word2) {
        List<Integer> list1 = map.get(word1);
        List<Integer> list2 = map.get(word2);
        int ret = Integer.MAX_VALUE;
        for(int i = 0, j = 0; i < list1.size() && j < list2.size(); ) {
            int index1 = list1.get(i), index2 = list2.get(j);
            if(index1 < index2) {
                ret = Math.min(ret, index2 - index1);
                i++;
            } else {
                ret = Math.min(ret, index1 - index2);
                j++;
            }
        }
        return ret;
    }
}
/**
 * LC251 Flatten 2D vector
 * Implement an iterator to flatten a 2d vector.
 * Example:
 Input: 2d vector =
 [
 [1,2],
 [3],
 [4,5,6]
 ]
 Output: [1,2,3,4,5,6]
 Explanation: By calling next repeatedly until hasNext returns false,
 the order of elements returned by next should be: [1,2,3,4,5,6].
 * */
class Vector2D {
    private Iterator<List<Integer>> it1;
    private Iterator<Integer> it2;
    public Vector2D(List<List<Integer>> vec2d) {
        it1 = vec2d.iterator();
    }
    public int next() {
        move();
        return it2.next();
    }
    private void move(){
        while ((it2 == null || !it2.hasNext()) && it1.hasNext())
            it2 = it1.next().iterator();
    }
    public boolean hasNext() {
        move();
        return it2 != null && it2.hasNext();
    }
}
/**
 * LC271 Encode and Decode a string
 * */
class Codec {
    // Encodes a list of strings to a single string.
    public String encode(List<String> strs) {
        StringBuilder sb = new StringBuilder();
        for(String s : strs) {
            sb.append(s.length()).append('/').append(s);
        }
        return sb.toString();
    }

    // Decodes a single string to a list of strings.
    public List<String> decode(String s) {
        List<String> ret = new ArrayList<String>();
        int i = 0;
        while(i < s.length()) {
            int slash = s.indexOf('/', i);
            int size = Integer.valueOf(s.substring(i, slash));
            ret.add(s.substring(slash + 1, slash + size + 1));
            i = slash + size + 1;
        }
        return ret;
    }
}
/**
 * LC281 zigzag iterator
 * Given two 1d vectors, implement an iterator to return their elements alternately.

 Example:

 Input:
 v1 = [1,2]
 v2 = [3,4,5,6]

 Output: [1,3,2,4,5,6]

 Explanation: By calling next repeatedly until hasNext returns false,
 the order of elements returned by next should be: [1,3,2,4,5,6].
 Follow up: What if you are given k 1d vectors? How well can your code be extended to such cases?

 Clarification for the follow up question:
 The "Zigzag" order is not clearly defined and is ambiguous for k > 2 cases. If "Zigzag" does not look right to you, replace "Zigzag" with "Cyclic". For example:

 Input:
 [1,2,3]
 [4,5,6,7]
 [8,9]

 Output: [1,4,8,2,5,9,3,6,7].
 * */
class ZigzagIterator {
    private Queue<Iterator<Integer>> queue;
    public ZigzagIterator(List<Integer> v1, List<Integer> v2) {
        queue = new LinkedList<>();
        if(!v1.isEmpty()) queue.offer(v1.iterator());
        if(!v2.isEmpty()) queue.offer(v2.iterator());
    }
    public int next() {
        Iterator<Integer> it = queue.poll();
        int result = it.next();
        if(it.hasNext()) queue.offer(it);
        return result;
    }
    public boolean hasNext() {
        return !queue.isEmpty();
    }
}
