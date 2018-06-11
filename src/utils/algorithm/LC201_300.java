package utils.algorithm;

import org.junit.Test;

import java.util.*;

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

