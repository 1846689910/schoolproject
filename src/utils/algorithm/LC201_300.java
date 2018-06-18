package utils.algorithm;

import org.junit.Test;

import java.util.*;

import static utils.algorithm.C8.reverse;

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
     12 + 92 = 82
     82 + 22 = 68
     62 + 82 = 100
     12 + 02 + 02 = 1
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
    @Test
    public void isIsomorphicTest(){
        System.out.println(isIsomorphic("att", "egg"));
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
        List<Integer>[] graph = new List[numCourses];  // [List<Integer>, List<Integer>, ...] 数组序号表示的是先修课程的号, 该序号对应的list里保存的是所有以该课程为先修课的课号
        for (int i = 0; i < numCourses; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] p: prerequisites) {
            graph[p[1]].add(p[0]);
        }
        int[] visited = new int[numCourses];  // 该课程被检索过的次数
        for (int i = 0; i < numCourses; i ++) {
            if (! validCourse(graph, i, visited)) {
                return false;
            }
        }
        return true;
    }
    private boolean validCourse(List<Integer>[] graph, int idx, int[] visited) {
        if (visited[idx] == 1) {  // loop
            return false;
        }
        if (visited[idx] == 2) {  // point is already clear
            return true;
        }
        visited[idx] = 1;  // start exploring
        for (int after: graph[idx]) {
            if (! validCourse(graph, after, visited)) {
                return false;
            }
        }
        visited[idx] = 2;  // point clear
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
                result = Math.min(result, i + 1 - left);  // i + 1 - left is the current size of subarray
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
        int[] edges = new int[numCourses];
        List<List<Integer>> graph = new ArrayList<>(numCourses);
        buildGraph(edges, graph, prerequisites);
        return solveByBFS(edges, graph);
    }
    private void buildGraph(int[] edges, List<List<Integer>> graph, int[][] prerequisites){
        int n = edges.length;
        while (n-- > 0) graph.add(new ArrayList<>());
        for (int[] edge : prerequisites) {
            edges[edge[0]] ++;  // 每个节点的边数，有边表示有先修课
            graph.get(edge[1]).add(edge[0]);
        }
    }
    private int[] solveByBFS(int[] edges, List<List<Integer>> graph){
        int[] order = new int[edges.length];
        Queue<Integer> queue = new LinkedList<>();  // 需要修的课程
        for (int i = 0; i < edges.length; i ++) {  // 先把没有先修课的课程放入queue, 他们是根节点
            if (edges[i] == 0) queue.offer(i);
        }
        int idx = 0;
        while (! queue.isEmpty()) {
            int cur = queue.poll();
            order[idx ++] = cur;
            for (int to : graph.get(cur)) {
                edges[to] --;
                if (edges[to] == 0) queue.offer(to);
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
    public int findKthLargest1(int[] arr, int k) {
        PriorityQueue<Integer> minHeap = new PriorityQueue<>(k);
        for(int val : arr) {
            minHeap.offer(val);
            if(minHeap.size() > k) {
                minHeap.poll();
            }
        }
        return minHeap.peek();
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
     * arr中是否存在arr[i]和arr[j],他们数值最多相差t，索引最多相差k
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
            if (map.entrySet().size() >= k) {
                long lastBucket = ((long) nums[i - k] - Integer.MIN_VALUE) / ((long) t + 1);
                map.remove(lastBucket);
            }
            map.put(bucket, remappedNum);
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
     * LC 241 different ways to add parentheses
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

}
/** LC208 Implement Trie (Prefix Tree)
 *  Implement a trie with insert, search, and startsWith methods.
 * */
class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Inserts a word into the trie. time O(m) m is the key length, space O(m) worst case no prefix of the the word exists, need to add new nodes
    public void insert(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char currentChar = word.charAt(i);
            if (!node.containsKey(currentChar)) {
                node.put(currentChar, new TrieNode());
            }
            node = node.get(currentChar);
        }
        node.setEnd();
    }

    // search a prefix or whole key in trie and
    // returns the node where search ends, time O(m), space O(1)
    private TrieNode searchPrefix(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i++) {
            char curLetter = word.charAt(i);
            if (node.containsKey(curLetter)) {
                node = node.get(curLetter);
            } else {
                return null;
            }
        }
        return node;
    }

    // Returns if the word is in the trie.
    public boolean search(String word) {
        TrieNode node = searchPrefix(word);
        return node != null && node.isEnd();
    }

    // Returns if there is any word in the trie
    // that starts with the given prefix.
    public boolean startsWith(String prefix) {  // time O(m), space O(1)
        TrieNode node = searchPrefix(prefix);
        return node != null;
    }
}
class TrieNode {

    // R links to node children
    private TrieNode[] links;

    private boolean isEnd;

    public TrieNode() {
        links = new TrieNode[26];
    }

    public boolean containsKey(char ch) {
        return links[ch -'a'] != null;
    }
    public TrieNode get(char ch) {
        return links[ch -'a'];
    }
    public void put(char ch, TrieNode node) {
        links[ch -'a'] = node;
    }
    public void setEnd() {
        isEnd = true;
    }
    public boolean isEnd() {
        return isEnd;
    }
}/**
 * LC211 Add and Search Word - Data structure design
 * 设计一个word dictionary支持search和add
 */
class WordDictionary {

    private static class TrieNode{
        boolean isWord;
        TrieNode[] children;
        TrieNode(){
            isWord = false;
            children = new TrieNode[26];
        }
    }
    TrieNode root;
    /** Initialize your data structure here. */
    public WordDictionary() {
        root = new TrieNode();
    }

    /** Adds a word into the data structure. */
    public void addWord(String word) {
        TrieNode node = root;
        for (int i = 0; i < word.length(); i ++){
            int j = word.charAt(i) - 'a';
            if (node.children[j] == null){
                node.children[j] = new TrieNode();
            }
            node = node.children[j];
        }
        node.isWord = true;
    }

    /** Returns if the word is in the data structure. A word could contain the dot character '.' to represent any one letter. */
    public boolean search(String word) {
        return dfs(word, root, 0);
    }

    private boolean dfs(String word, TrieNode node, int index){
        if (index == word.length()){
            return node.isWord;
        }
        if (word.charAt(index) == '.'){
            for (TrieNode temp : node.children){
                if (temp != null && dfs(word, temp, index + 1)) return true;
            }
            return false;
        } else {
            int i = word.charAt(index) - 'a';
            TrieNode temp = node.children[i];
            return temp != null && dfs(word, temp, index + 1);
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
