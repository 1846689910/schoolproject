package utils.algorithm;

import org.junit.Test;

import java.util.*;

public class C6 {
    /** all subsets */
    public static List<String> allSubsets(String str){
        List<String> result = new ArrayList<>();
        if (str == null || str.length() == 0) return result;
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        allSubsetsHelper(chars, result, sb, 0);
        return result;
    }
    private static void allSubsetsHelper(char[] chars, List<String> result, StringBuilder sb, int idx) {
        if (idx == chars.length) {
            result.add(sb.toString());
            return;
        }
        sb.append(chars[idx]);
        allSubsetsHelper(chars, result, sb, idx + 1);
        sb.deleteCharAt(sb.length() - 1);
        allSubsetsHelper(chars, result, sb, idx + 1);
    }
    @Test
    public void allSubsetsTest(){
        System.out.println(allSubsets("abc"));
    }
    /** all subsets2 */
    public static List<String> allSubsets2(String str){
        List<String> result = new ArrayList<>();
        if (str == null || str.length() == 0) return result;
        StringBuilder sb = new StringBuilder();
        char[] chars = str.toCharArray();
        Arrays.sort(chars);
        allSubsets2Helper(chars, sb, result, 0);
        return result;
    }
    private static void allSubsets2Helper(char[] chars, StringBuilder sb, List<String> result, int idx) {
        if (idx == chars.length) {
            result.add(sb.toString());
            return;
        }
        sb.append(chars[idx]);
        allSubsets2Helper(chars, sb, result, idx + 1);
        sb.deleteCharAt(sb.length() - 1);
        while (idx + 1 < chars.length && chars[idx] == chars[idx + 1]) idx ++;
        allSubsets2Helper(chars, sb, result, idx + 1);
    }
    @Test
    public void allSubsets2Test(){
        System.out.println(allSubsets2("aaaabbcccb"));
    }

    /** NQueens */
    public static List<List<Integer>> nQueens(int n){
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> cur = new ArrayList<>();
        nQueensHelper(n, cur, result);
        return result;
    }
    private static void nQueensHelper(int n, List<Integer> cur, List<List<Integer>> result) {
        if (cur.size() == n) {
            result.add(new ArrayList<>(cur));
        }
        for (int i = 0; i < n; i ++) {
            if (valid(cur, i)) {
                cur.add(i);
                nQueensHelper(n, cur, result);
                cur.remove(i);
            }
        }
    }
    private static boolean valid(List<Integer> cur, int col){
        int row = cur.size();
        for (int j = 0; j < row; j ++) {
            if (cur.get(j) == col || Math.abs(cur.get(j) - col) == row - j) {
                return false;
            }
        }
        return true;
    }

    /** coins combination
     * time: O(target ^ coins.length)
     * */
    public static List<List<Integer>> coinsCombination(int target, int[] coins) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> cur = new ArrayList<>();
        coinsCombinationHelper(target, coins, 0, cur, result);
        return result;
    }
    private static void coinsCombinationHelper(int target, int[] coins, int idx, List<Integer> cur, List<List<Integer>> result) {
        if (idx == coins.length - 1) {
            if (target % coins[idx] == 0) {
                cur.add(target / coins[idx]);
                result.add(new ArrayList<>(cur));
                cur.remove(cur.size() - 1);
            }
            return;
        }
        int max = target / coins[idx];
        for (int i = 0; i <= max; i ++) {
            cur.add(i);
            coinsCombinationHelper(target - i * coins[idx], coins, idx + 1, cur, result);
            cur.remove(cur.size() - 1);
        }
    }
    @Test
    public void coinsCombinationTest(){
        System.out.println(coinsCombination(99, new int[]{1, 5, 10, 25}));
    }
    /** all parentheses */
    public static List<String> allParentheses(int k){
        List<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        allParenthesesHelper(sb, result, k, k, 0);
        return result;
    }
    private static void allParenthesesHelper(StringBuilder sb, List<String> result, int left, int right, int idx) {
        if (left == 0 && right == 0) {
            result.add(sb.toString());
            return;
        }
        if (left > 0) {
            sb.append('(');
            allParenthesesHelper(sb, result, left - 1, right, idx + 1);
            sb.deleteCharAt(sb.length() - 1);
        }
        if (right > left) {
            sb.append(')');
            allParenthesesHelper(sb, result, left, right - 1, idx + 1);
            sb.deleteCharAt(sb.length() - 1);
        }
    }
    @Test
    public void allParenthesesTest(){
        System.out.println(allParentheses(3));
    }

    /** all parentheses 2 */
    public static List<String> allParentheses2(char[] p, int i, int j, int k) {
        StringBuilder sb = new StringBuilder();
        Deque<Character> stack = new LinkedList<>();
        int[] remain = new int[]{i, i, j, j, k, k};
        int len = 2 * i + 2 * j + 2 * k;
        List<String> result = new ArrayList<>();
        allParentheses2Helper(p, sb, stack, remain, len, result);
        return result;
    }
    private static void allParentheses2Helper(char[] p, StringBuilder sb, Deque<Character> stack, int[] remain, int len, List<String> result) {
        if (sb.length() == len) {
            result.add(sb.toString());
            return;
        }
        for (int i = 0; i < remain.length; i ++) {
            if (i % 2 == 0) {
                if (remain[i] > 0) {
                    sb.append(p[i]);
                    stack.offerFirst(p[i]);
                    remain[i] --;
                    allParentheses2Helper(p, sb, stack, remain, len, result);
                    sb.deleteCharAt(sb.length() - 1);
                    stack.pollFirst();
                    remain[i] ++;
                }
            } else {
                if (! stack.isEmpty() && stack.peekFirst().equals(p[i - 1])) {
                    sb.append(p[i]);
                    stack.pollFirst();
                    remain[i] --;
                    allParentheses2Helper(p, sb, stack, remain, len, result);
                    sb.deleteCharAt(sb.length() - 1);
                    stack.offerFirst(p[i]);
                    remain[i] ++;
                }
            }
        }
    }
    @Test
    public void allParentheses2Helper(){
        System.out.println(allParentheses2(new char[]{'(', ')', '[', ']', '{', '}'}, 1, 2, 3));
    }
    /** all permutation */
    public static List<String> allPermutation(String s){
        List<String> result = new ArrayList<>();
        if (s == null) return result;
        char[] chars = s.toCharArray();
        allPermutationHelper(chars, result, 0);
        return result;
    }
    private static void allPermutationHelper(char[] chars, List<String> result, int idx){
        if (idx == chars.length) {
            result.add(new String(chars));
            return;
        }
        for (int i = idx; i < chars.length; i ++) {
            swap(chars, i, idx);
            allPermutationHelper(chars, result, idx + 1);
            swap(chars, i, idx);
        }
    }
    public static void swap (char[] chars, int i, int j){
        char c = chars[i]; chars[i] = chars[j]; chars[j] = c;
    }
    @Test
    public void allPermutationTest(){
        System.out.println(allPermutation("abc"));
    }
    /** all Permutation 2 */
    public static List<String> allPermutation2(String s){
        List<String> result = new ArrayList<>();
        if (s == null) return result;
        char[] chars = s.toCharArray();
        allPermutation2Helper(chars, result, 0);
        return result;
    }
    private static void allPermutation2Helper(char[] chars, List<String> result, int idx) {
        if (idx == chars.length) {
            result.add(new String(chars));
            return;
        }
        Set<Character> set = new HashSet<>();
        for (int i = idx; i < chars.length; i ++) {
            if (set.add(chars[i])) {
                swap (chars, i, idx);
                allPermutation2Helper(chars, result, idx + 1);
                swap (chars, i, idx);
            }
        }
    }
    @Test
    public void allPermutation2Test(){
        System.out.println(allPermutation2("aab"));
    }
    /** check cycle */
    public static boolean checkCycle(String[] strs) {
        return checkCycleHelper(strs, 0);
    }
    private static boolean checkCycleHelper(String[] strs, int idx) {
        if (idx == strs.length) {
            return strs[strs.length - 1].charAt(strs[strs.length - 1].length() - 1) == strs[0].charAt(0);
        }
        for (int i = idx; i < strs.length; i ++) {
            if (idx == 0 || strs[idx - 1].charAt(strs[idx - 1].length() - 1) == strs[i].charAt(0)) {
                swap(strs, i, idx);
                if (checkCycleHelper(strs, idx + 1)) return true;
                swap(strs, i, idx);
            }
        }
        return false;
    }
    public static void swap(String[] strs, int i, int j){
        String s = strs[i]; strs[i] = strs[j]; strs[j] = s;
    }
    @Test
    public void checkCycleTest(){
        String[] strs = new String[]{"aaa", "bbc", "aab", "ddc", "ced", "cca"};
        System.out.println(checkCycle(strs));
    }
}
