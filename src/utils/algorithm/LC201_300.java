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
        int left = 0, right = arr.length - 1;
        int sum = 0;
        for (int i = 0; i < arr.length; i ++) {
            sum += arr[i];
            while (sum >= num) {
                result = Math.min(result, i + 1 - left);
                sum -= arr[left ++];
            }
        }
        return result != Integer.MAX_VALUE ? result : 0;
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

    private final int R = 26;

    private boolean isEnd;

    public TrieNode() {
        links = new TrieNode[R];
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
}