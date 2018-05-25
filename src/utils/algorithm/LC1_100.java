package utils.algorithm;

import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static utils.algorithm.C2.firstOccur;
import static utils.algorithm.C2.lastOccur;

public class LC1_100 {
    /**
     * LC2 Add Two Numbers
     * Input: (2 -> 4 -> 3) + (5 -> 6 -> 4)
     Output: 7 -> 0 -> 8
     Explanation: 342 + 465 = 807.
     */
    public static ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        int carry = 0;
        while (l1 != null && l2 != null) {
            int val = l1.value + l2.value + carry;
            cur.next = new ListNode(val % 10);
            carry = val / 10;
            l1 = l1.next;
            l2 = l2.next;
            cur = cur.next;
        }
        if (l1 != null) cur.next = l1;
        if (l2 != null) cur.next = l2;
        if (carry > 0) {
            while (cur.next != null){
                int value = cur.next.value + carry;
                cur.next.value = value % 10;
                carry = value / 10;
                cur = cur.next;
            }
            if (carry > 0) {
                cur.next = new ListNode(carry);
            }
        }
        return dummy.next;
    }
    /** LC6 ZigZag Conversion
     * Input: s = "PAYPALISHIRING", numRows = 4
     Output: "PINALSIGYAHRPI"
     Explanation:

     P     I    N
     A   L S  I G
     Y A   H R
     P     I
     * */
    public String zigZagConvert (String s, int rows) {
        if(s == null || s.length() <= 1 || rows <= 1) return s;  // 一行就可以
        StringBuilder[] sbs = new StringBuilder[rows];
        for(int i = 0; i < sbs.length; i ++){
            sbs[i] = new StringBuilder();
        }
        int increment = 1;  // 1 表示向下走，也表示步进值，每次走一行, -1表示折返向上走
        int index = 0;
        for(char c : s.toCharArray()){
            sbs[index].append(c);
            if(index == 0) { increment = 1; }
            if(index == rows-1){ increment = -1; }  // 对于到达了最后一行，递归值为-1，表示折返向上
            index += increment;  // 将下一个字符放在数组中不同的StringBuilder里
        }
        StringBuilder ret = new StringBuilder();
        for (StringBuilder sb : sbs) {
            ret.append(sb);
        }

        return ret.toString();
    }

    /** LC7 Reverse Integer
     * 123 -> 321
     * -123 -> -321
     * 120 -> 21
     * 翻转后的结果超出Int范围 -> 0
     * */
    public int reverseInt(int x) {
        long result = 0;
        while (x != 0 ){
            result = result * 10 + x % 10;
            x /= 10;
        }
        if (result < Integer.MIN_VALUE || result > Integer.MAX_VALUE) {
            return 0;
        } else {
            return (int) result;
        }
    }
    /**LC8 String to Integer(atoi)
     * "42" -> 42
     *Input: "4193 with words"
     Output: 4193
     Input: "words and 987"
     Output: 0
     Input: "-91283472332"
     Output: -2147483648 超出Int就返回边界值
     * */
    public int myAtoi(String str) {
        if (str == null || str.length() == 0) return 0;  // 1. empty string
        char[] chars = str.toCharArray();
        int i = 0, sign = 1, num = 0;
        //2. Remove Spaces
        while(i < chars.length && chars[i] == ' ')
            i ++;
        //3. Handle signs
        if(i < chars.length && (chars[i] == '+' || chars[i] == '-')){
            sign = chars[i] == '+' ? 1 : -1;
            i ++;
        }
        //4. Convert number and avoid overflow
        while(i < chars.length){
            int digit = chars[i] - '0';
            if(digit < 0 || digit > 9) break;

            //check if num will be overflow after 10 times and add digit，为了提前停止
            if(num * 10 > Integer.MAX_VALUE || num * 10 == Integer.MAX_VALUE && Integer.MAX_VALUE % 10 < digit) {
                return sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
            }
            num = 10 * num + digit;
            i ++;
        }
        return num * sign;
    }
    /** LC9 Palindrome Number
     * 123 -> false
     * 121 -> true
     * -121 -> false
     * */
    public boolean isPalindrome(int x) {
        String s = String.valueOf(x);
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left ++) != s.charAt(right --)) return false;
        }
        return true;
    }
    /**
     * LC11 Container with most water
     * 实际是找垂直线图中最大矩形的问题 (不是直方图, 垂线自身没有宽度)
     * */
    public int maxArea(int[] height) {
        int maxarea = 0, left = 0, right = height.length - 1;
        while (left < right) {
            maxarea = Math.max(maxarea, Math.min(height[left], height[right]) * (right - left));
            if (height[left] < height[right])
                left ++;
            else
                right --;
        }
        return maxarea;
    }
    /** LC12 Integer to Roman整数转罗马数字
     *Symbol       Value
     I             1
     V             5
     X             10
     L             50
     C             100
     D             500
     M             1000
     * */
    public String int2Roman(int num) {
        int[] values = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] strs = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < values.length; i ++) {
            while(num >= values[i]) {
                num -= values[i];
                sb.append(strs[i]);
            }
        }
        return sb.toString();
    }
    /**
     * LC13 Roman to Integer
     * */
    public int romanToInt(String s) {
        Map<Character, Integer> map = new HashMap<>();
        map.put('I', 1); map.put('V', 5); map.put('X', 10);
        map.put('L', 50); map.put('C', 100); map.put('D', 500); map.put('M', 1000);
        char [] chars = s.toCharArray();
        int num = 0;
        for(int i = 0; i < chars.length; i ++) {
            if(i < chars.length - 1 && map.get(chars[i]) < map.get(chars[i + 1])) {
                num -= map.get(chars[i]);
            } else {
                num += map.get(chars[i]);
            }
        }
        return num;
    }
    /**
     * LC14 longest common prefix
     * time O(S), space: O(1)
     * */
    public String longestCommonPrefix(String[] strs) {
        if (strs.length == 0) return "";
        String prefix = strs[0];
        for (int i = 1; i < strs.length; i++)
            while (strs[i].indexOf(prefix) != 0) {
                prefix = prefix.substring(0, prefix.length() - 1);
                if (prefix.isEmpty()) return "";
            }
        return prefix;
    }
    /**
     * LC16 3 sum closest, 找到3数之和最接近target，如果是target就返回target
     * */
    public int threeSumClosest(int[] arr, int target){
        Arrays.sort(arr);
        int result = arr[0] + arr[1] + arr[2];
        for (int i = 0; i < arr.length - 2; i ++) {
            if (i > 0 && arr[i] == arr[i - 1]) continue;
            int left = i + 1, right = arr.length - 1;
            while (left < right) {
                int cur = arr[left] + arr[right] + arr[i];
                result = Math.abs(result - target) < Math.abs(cur - target) ? result : cur;
                if (cur == target) {
                    return target;
                } else if (cur < target) {
                    left ++;
                } else {
                    right --;
                }
            }
        }
        return result;
    }
    /**
     * LC17 Letter combination of a phone number
     * 手机拨号键盘上每个数字基本关联3个字母，他们的组合，给定两个数字求他们的所有组合
     * */
    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        if (digits == null || digits.length() == 0) return result;
        String[] table = new String[]{"", "", "abc", "def", "ghi", "jkl", "mno", "pqrs", "tuv", "wxyz"};
        char[] chars = digits.toCharArray();
        StringBuilder sb = new StringBuilder();
        letterCombinationsHelper(chars, sb, table, result, 0);
        return result;
    }
    private void letterCombinationsHelper(char[] chars, StringBuilder sb, String[] table, List<String> result, int idx){
        if (sb.length() == chars.length) {
            result.add(sb.toString());
            return;
        }
        String correspondingStr = table[chars[idx] - '0'];
        for (int i = 0; i < correspondingStr.length(); i ++) {
            sb.append(correspondingStr.charAt(i));
            letterCombinationsHelper(chars, sb, table, result, idx + 1);
            sb.deleteCharAt(sb.length() - 1);
        }
    }
    /**
     * LC18 4Sum
     * 不是判断是求和，而且是去重
     * */
    public List<List<Integer>> fourSum(int[] arr, int target) {
        List<List<Integer>> result = new ArrayList<>();
        if (arr == null || arr.length < 4) return result;
        Arrays.sort(arr);
        for (int i = 0; i < arr.length - 3; i ++) {
            if (i > 0 && arr[i] == arr[i - 1]) continue;
            for (int j = i + 1; j < arr.length - 2; j ++) {
                if (j > i + 1 && arr[j] == arr[j - 1]) continue;
                int left = j + 1, right = arr.length - 1;
                while (left < right) {
                    int cur = arr[i] + arr[j] + arr[left] + arr[right];
                    if (cur == target) {
                        result.add(Arrays.asList(arr[i], arr[j], arr[left], arr[right]));
                        left ++;
                        while (left < right && arr[left] == arr[left - 1]) left ++;
                    } else if (cur < target) {
                        left ++;
                    } else {
                        right --;
                    }
                }
            }
        }
        return result;
    }
    /**
     * LC20 valid parentheses
     * 验证给定字符串是否为合法括号表达形式
     * */
    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();
        for (char c : s.toCharArray()) {
            if (c == '(')
                stack.push(')');
            else if (c == '{')
                stack.push('}');
            else if (c == '[')
                stack.push(']');
            else if (stack.isEmpty() || stack.pop() != c)
                return false;
        }
        return stack.isEmpty();
    }
    public boolean isValid1(String s) {
        while ((s.contains("{}")) || (s.contains("()")) || (s.contains("[]"))){
            s = s.replaceAll("\\{}","");
            s = s.replaceAll("\\[]","");
            s = s.replaceAll("\\(\\)","");}
        return s.equals("");
    }
    /** LC29 divide two integers
     * 被除数dividend与除数divisor都可为32bit 有符号整数
     * 除数肯定不为0
     * 当除数overflows时，返回2^31 - 1
     * */
    public int divide(int bcs, int cs) {
        if(bcs==Integer.MIN_VALUE && cs==-1) return Integer.MAX_VALUE;
        if(bcs > 0 && cs > 0) return divideHelper(-bcs, -cs);
        else if(bcs > 0) return -divideHelper(-bcs,cs);
        else if(cs > 0) return -divideHelper(bcs,-cs);
        else return divideHelper(bcs, cs);
    }

    private int divideHelper(int bcs, int cs){
        // base case
        if(cs < bcs) return 0;
        // get highest digit of divisor
        int cur = 0, res = 0;
        while((cs << cur) >= bcs && (cs << cur) < 0 && cur < 31) cur++;
        res = bcs - (cs << (cur - 1));
        if(res > cs) return 1 << (cur - 1);
        return (1 << (cur - 1))+divide(res, cs);
    }
    /**
     * LC31 Next Permutation
     * 在一个数字的各个位数的排列中，找到一个排列是比当前的数字大的数里最小的那个。如果找不到就倒序
     * 123 -> 132
     * 3,2,1 → 1,2,3
     * 1,1,5 → 1,5,1
     * 方法一: 全排列然后找 time： O(n!), space(On)
     * 方法二: 从右向左找到一个降序数字，然后再向右找到一个比刚才的降序数恰好大的数字，交换两数. 并将降序数位置右边的所有数倒序
     * timeO(n), space O(1)
     * 方法2：
     * */
    public void nextPermutation(int[] arr) {
        int i = arr.length - 2;
        while (i >= 0 && arr[i + 1] <= arr[i]) {
            i--;
        }
        if (i >= 0) {
            int j = arr.length - 1;
            while (j >= 0 && arr[j] <= arr[i]) {
                j--;
            }
            swap(arr, i, j);
        }
        reverse(arr, i + 1);
    }
    private void reverse(int[] nums, int start) {
        int i = start, j = nums.length - 1;
        while (i < j) {
            swap(nums, i, j);
            i++;
            j--;
        }
    }
    private void swap(int[] nums, int i, int j) {
        int temp = nums[i];
        nums[i] = nums[j];
        nums[j] = temp;
    }

    /**
     * LC32 Longest Valid Parentheses
     * 空stack放-1，遇到左括号放入idx，遇到右括号，弹出stack并用该数索引减去栈顶数作为当前最大与之前的max 比大小
     * */
    public int longestValidParentheses(String s) {
        int longest = 0;
        Deque<Integer> stack = new LinkedList<>();
        stack.offerFirst(-1);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '(') {
                stack.offerFirst(i);
            } else {
                stack.pollFirst();
                if (stack.isEmpty()) {
                    stack.offerFirst(i);
                } else {
                    longest = Math.max(longest, i - stack.peekFirst());
                }
            }
        }
        return longest;
    }
    /**
     * LC34 search for a Range
     * 给定升序的数组，某数可能会重复，找到该数的起止索引
     * Example 1:

     Input: nums = [5,7,7,8,8,10], target = 8
     Output: [3,4]
     Example 2:

     Input: nums = [5,7,7,8,8,10], target = 6
     Output: [-1,-1]
     二分法: Time O(logn), space O(1)
     * */
    public int[] searchRange(int[] nums, int target) {
        if (nums== null || nums.length == 0) return new int[]{-1, -1};
        return new int[]{firstOccur(nums, target), lastOccur(nums, target)};
    }
    /**
     * LC35 search insert position
     * 找到就返回位置，找不到就返回它应该插入的位置
     * 使用C2的findLargestSmaller(arr, target), 但是后处理要先右后左，确保拿到小于target的最大数
     * */
    public int searchInsert(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;
        int idx = findLargestSmaller(arr, target);
        if (idx == -1 || arr[idx] != target) {
            return idx + 1;
        } else {
            return idx;
        }
    }
    public static int findLargestSmaller(int[] arr, int target) {
        if (arr == null || arr.length == 0) return -1;
        int left = 0, right = arr.length - 1;
        while (left + 1 < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid;
            } else if (arr[mid] < target) {
                left = mid;
            } else {
                right = mid;
            }
        }
        if (arr[right] < target) return right;
        if (arr[left] < target) return left;
        return -1;
    }
    /**
     * LC36 Valid Sudoku
     * 合法数独：每行, 每列, 每个小3x3矩阵数据1-9之间无重复, 没有填写的空格用字符0表示
     * */
    public boolean isValidSudoku(char[][] board) {
        if (board == null || board.length == 0 || board[0].length == 0) return false;
        int rows = board.length, cols = board[0].length;
        Set<Integer> set = new HashSet<>();
        // check rows
        for (int i = 0; i < rows; i ++) {
            set = new HashSet<>();
            for (int j = 0; j < cols; j ++) {
                if (board[i][j] == '.') continue;
                int num = (int)(board[i][j] - '0');
                if (num < 1 || num > 9 || set.contains(num)) return false;
                set.add(num);
            }
        }
        // check cols
        for (int i = 0; i < cols; i ++) {
            set = new HashSet<>();
            for (int j = 0; j < rows; j ++) {
                if (board[j][i] == '.') continue;
                int num = (int)(board[j][i] - '0');
                if (num < 1 || num > 9 || set.contains(num)) return false;
                set.add(num);
            }
        }
        // check small box
        int boxRows = 3, boxCols = 3;
        for (int boxRow = 0; boxRow < boxRows; boxRow ++) {
            for (int boxCol = 0; boxCol < boxCols; boxCol ++) {
                set = new HashSet<>();
                for (int i = 0; i < boxRow * 3 - 1; i ++) {
                    for (int j = 0; j < boxCol * 3 - 1; j ++) {
                        if (board[i][j] == '.')continue;
                        int num = (int) (board[i][j] - '0');
                        if (num < 1 || num > 9 || set.contains(num)) return false;
                        set.add(num);
                    }
                }
            }
        }
        return true;
    }
    public boolean isValidSudoku1(char[][] board) {
        boolean[][] row = new boolean[9][9];
        boolean[][] column = new boolean[9][9];
        boolean[][] block = new boolean[9][9];
        for(int i = 0;i < 9; i ++){
            for(int j = 0; j < 9; j ++){
                if(board[i][j]=='.') continue;
                int c = board[i][j] - '1';
                if(row[i][c] || column[j][c] || block[i - i % 3 + j / 3][c]) return false;
                row[i][c] = column[j][c] = block[i - i % 3 + j / 3][c] = true;
            }
        }
        return true;
    }
    /**
     * LC38 Count and Say
     * 用字符串来描述每个数字从1开始
     * 1 -> 被读作1
     * 2 -> 上一个数字，就是一个1，那么就是11
     * 3 -> 上一个数字，就是二个1，那么就是21
     * 4 -> 上一个数字，就是一个2一个1，那么就是1211
     * ...依次读下去，每次都读出上一次的数字
     * */
    public String countAndSay(int n) {
        if(n <= 1)return "1";
        String str = countAndSay(n - 1);
        StringBuilder sb = new StringBuilder();
        int idx = 0;
        int count = 0;
        for(int i = 0; i < str.length(); i ++){
            if(str.charAt(i) == str.charAt(idx)){  // 数字相同就增加数量
                count ++;
            } else {
                sb.append(count).append(str.charAt(idx));  // 先放入这个数字的数量, 再放入这个数字本身
                count = 1;
                idx = i;
            }
        }
        sb.append(count).append(str.charAt(idx));  // 不要忘了最后的结果
        return sb.toString();
    }
    /**
     * LC39 Combination Sum (99 cent变体)
     * 但是返回值中保存的不是索引对应的数量，而是那些实际数字的集合
     * */
    public List<List<Integer>> combinationSum(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> cur = new ArrayList<>();
        helper(target, candidates, 0, cur, result);

        return result;
    }
    private void helper(int target, int[] candidates, int idx, List<Integer> cur, List<List<Integer>> result) {
        if (idx == candidates.length - 1) {
            if (target % candidates[idx] == 0) {
                cur.add(target / candidates[idx]);
                List<Integer> list = new ArrayList<>();  // ----这里开始不一样
                for (int i = 0; i < cur.size(); i ++){
                    int count = cur.get(i);
                    while (count > 0) {
                        list.add(candidates[i]);
                        count --;
                    }
                }
                result.add(list);  // ----到这里
                cur.remove(cur.size() - 1);
            }
            return;
        }
        int max = target / candidates[idx];
        for (int i = 0; i <= max; i ++) {
            cur.add(i);
            helper(target - i * candidates[idx], candidates, idx + 1, cur, result);
            cur.remove(cur.size() - 1);
        }
    }
    public List<List<Integer>> combinationSum2(int[] candidates, int target) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> cur = new ArrayList<>();
        combinationSum2Helper(target, candidates, 0, cur, result);
        return result;
    }
    private void combinationSum2Helper(int target, int[] candidates, int idx, List<Integer> cur, List<List<Integer>> result) {
        if (idx == candidates.length - 1) {
            if (target % candidates[idx] == 0 && target / candidates[idx] <= 1) {  // ----这里开始不一样
                cur.add(target / candidates[idx]);
                List<Integer> list = new ArrayList<>();
                for (int i = 0; i < cur.size(); i ++){
                    int count = cur.get(i);
                    while (count > 0) {
                        list.add(candidates[i]);
                        count --;
                    }
                }
                boolean shouldAdd = true;
                for (List<Integer> lst : result) {
                    if (twoListEqual(lst, list)) {
                        shouldAdd = false;
                        break;
                    }
                }
                if (shouldAdd) result.add(list);
                cur.remove(cur.size() - 1);
            }
            return;
        }
        int max = target / candidates[idx] > 1 ? 1 : target / candidates[idx];  // ----到这里
        for (int i = 0; i <= max; i ++) {
            cur.add(i);
            combinationSum2Helper(target - i * candidates[idx], candidates, idx + 1, cur, result);
            cur.remove(cur.size() - 1);
        }
    }
    private boolean twoListEqual(List<Integer> list1, List<Integer> list2){
        if (list1.size() != list2.size()) return false;
        Collections.sort(list1);
        Collections.sort(list2);
        for (int i = 0, size = list1.size(); i < size; i ++) {
            if (! list1.get(i).equals(list2.get(i))) return false;
        }
        return true;
    }
    /**
     * LC41 First Missing Positive
     * Example 1:

     Input: [1,2,0]
     Output: 3
     Example 2:

     Input: [3,4,-1,1]
     Output: 2
     Example 3:

     Input: [7,8,9,11,12]
     Output: 1
     * */
    public int firstMissingPositive(int[] nums) {
        Set<Integer> set = new HashSet<>();
        for (int num : nums) {
            set.add(num);
        }
        for (int i = 1; i < Integer.MAX_VALUE; i ++) {
            if (! set.contains(i)) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }
    /**
     * LC43 Multiply Strings: 将两个字符串表示的数字相乘
     * Example 1:

     Input: num1 = "2", num2 = "3"
     Output: "6"
     Example 2:

     Input: num1 = "123", num2 = "456"
     Output: "56088"
     * */
    public String multiply(String num1, String num2) {
        int m = num1.length(), n = num2.length();
        int[] pos = new int[m + n];
        for(int i = m - 1; i >= 0; i--) {
            for(int j = n - 1; j >= 0; j--) {
                int mul = (num1.charAt(i) - '0') * (num2.charAt(j) - '0');
                int p1 = i + j, p2 = i + j + 1;
                int sum = mul + pos[p2];

                pos[p1] += sum / 10;
                pos[p2] = (sum) % 10;
            }
        }

        StringBuilder sb = new StringBuilder();
        for(int p : pos) if(!(sb.length() == 0 && p == 0)) sb.append(p);
        return sb.length() == 0 ? "0" : sb.toString();
    }
    /**
     * LC46 Permutations 全排列问题
     * Input: [1,2,3]
     Output:
     [
     [1,2,3],
     [1,3,2],
     [2,1,3],
     [2,3,1],
     [3,1,2],
     [3,2,1]
     ]
     * */
    public List<List<Integer>> permute(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        permuteHelper(nums, result, 0);
        return result;
    }
    private void permuteHelper(int[] nums, List<List<Integer>> result, int idx) {
        if (idx == nums.length) {
            List<Integer> cur = new ArrayList<>();
            for(int num: nums) cur.add(num);
            result.add(cur);
            return;
        }
        for (int i = idx; i < nums.length; i ++) {
            swap(nums, idx, i);
            permuteHelper(nums, result, idx + 1);
            swap(nums, idx, i);
        }
    }
    /**
     * LC47 Permutation2 去重的全排列
     * */
    public List<List<Integer>> permuteUnique(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        permuteUniqueHelper(nums, result, 0);
        return result;
    }
    private void permuteUniqueHelper(int[] nums, List<List<Integer>> result, int idx) {
        if (idx == nums.length) {
            List<Integer> cur = new ArrayList<>();
            for(int num: nums) cur.add(num);
            result.add(cur);
            return;
        }
        Set<Integer> set = new HashSet<>();
        for (int i = idx; i < nums.length; i ++) {
            if (set.add(nums[i])) {
                swap(nums, idx, i);
                permuteUniqueHelper(nums, result, idx + 1);
                swap(nums, idx, i);
            }
        }
    }
    /**
     * LC49 Group Anagrams
     * */
    public List<List<String>> groupAnagrams(String[] strs) {
        List<List<String>> result = new ArrayList<>();
        if (strs == null || strs.length == 0) return result;
        Map<String, List<String>> map = new HashMap<>();
        for (String s : strs) {
            String mask = getStringMask(s);
            if (! map.containsKey(mask)) {
                map.put(mask, new ArrayList<>());
            }
            map.get(mask).add(s);
        }
        return new ArrayList<>(map.values());
    }
    private String getStringMask(String s){
        char[] chars = s.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }
    /**
     * LC50 pow(x, n)
     * */
    public double myPow(double x, int n) {
        if (n == Integer.MIN_VALUE){
            double temp = myPow(x, n >> 1);
            return temp * temp;
        }
        if (n < 0) return 1 / myPow(x, -n);
        if (n == 0) return 1;
        double half = myPow(x, n / 2);
        if(n % 2 == 0)
            return half * half;
        else
            return half * half * x;
    }
    /**
     * LC56 merge intervals: merge all overlapping intervals
     * Definition for an interval.
     * public class Interval {
     *     int start;
     *     int end;
     *     Interval() { start = 0; end = 0; }
     *     Interval(int s, int e) { start = s; end = e; }
     * }
     * Example 1:
     Input: [[1,3],[2,6],[8,10],[15,18]]
     Output: [[1,6],[8,10],[15,18]]
     Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
     Example 2:

     Input: [[1,4],[4,5]]
     Output: [[1,5]]
     Explanation: Intervals [1,4] and [4,5] are considerred overlapping.
     * */
    public List<Interval> merge(List<Interval> intervals) {
        Collections.sort(intervals, (i1, i2) -> {
            if (i1.start == i2.start) return 0;
            return i1.start < i2.start ? -1 : 1;
        });
        LinkedList<Interval> merged = new LinkedList<Interval>();
        for (Interval interval : intervals) {
            // if the list of merged intervals is empty or if the current
            // interval does not overlap with the previous, simply append it.
            if (merged.isEmpty() || merged.getLast().end < interval.start) {
                merged.add(interval);
            }
            // otherwise, there is overlap, so we merge the current and previous
            // intervals.
            else {
                merged.getLast().end = Math.max(merged.getLast().end, interval.end);
            }
        }
        return merged;
    }
    /**
     * LC58 Length of Last Word in String
     * */
    public int lengthOfLastWord(String s) {
        if(s == null || s.length() == 0) return 0;
        int start = 0, end = s.length() -1;
        for (int len = s.length(), i = len - 1; i >= 0; i --) {
            if (s.charAt(i) != ' ') {
                end = i;
                break;
            }
        }
        for (int i = end; i >= 0; i --) {
            if (s.charAt(i) == ' ') {
                start = i + 1;
                break;
            }
        }
        return end - start + 1;
    }
    /**
     * LC60 Permutation Sequence
     * */
    public String getPermutation(int n, int k) {
        List<Integer> numbers = new ArrayList<>();
        int[] factorial = new int[n + 1];
        StringBuilder sb = new StringBuilder();
        // create an array of factorial lookup
        int sum = 1;
        factorial[0] = 1;
        for(int i = 1; i <= n; i ++){
            sum *= i;
            factorial[i] = sum;
            // factorial[] = {1, 1, 2, 6, 24, ... n!}
            // create a list of numbers to get indices
            numbers.add(i);
        }
        // numbers = {1, 2, 3, 4}
        k--;
        for(int i = 1; i <= n; i++){
            int index = k / factorial[n - i];
            sb.append(String.valueOf(numbers.get(index)));
            numbers.remove(index);
            k -= index * factorial[n - i];
        }
        return sb.toString();
    }
    /**
     * LC61 Rotate List
     * 翻转一个list 的右边k位，如果k很大就不停的翻转直到k小于list长度
     *
     * */
    public ListNode rotateRight(ListNode head, int k) {
        if (head == null || head.next == null) return head;
        int len = 1;
        ListNode curHead = head;
        while (curHead.next != null) {
            curHead = curHead.next;
            len ++;  // list length
        }
        if (k > len) k = k - k / len * len;
        int leftK = len - k;  // 换成左边几位
        if (leftK == 0) return head;
        ListNode left = head, right = head;
        while (leftK > 1) {
            head = head.next;
            leftK --;
        }
        right = head.next;
        head.next = null;
        ListNode leftHead = C3.reverseLinkedListRe(left);
        ListNode rightHead = C3.reverseLinkedListRe(right);
        left.next = rightHead;
        return C3.reverseLinkedListRe(leftHead);
    }
    /**
     * LC62 Unique Path
     * 排列组合问题，总共需要横着走m-1, 竖着走n-1, 那么结果就是一个组合数
     * C(m + n - 2)_(m - 1)
     * */
    public int uniquePaths(int m, int n) {
        long result = 1;
        for(int i = 0; i < Math.min(m - 1, n - 1); i ++) {
            result = result * (m + n - 2 - i) / (i + 1);
        }
        return (int) result;
    }
    /**
     * LC63 Unique Path2, 有障碍物(1)的矩阵中，找出左上角到右下角的通路(0表示)有几条
     * [
     [0,0,0],
     [0,1,0],
     [0,0,0]
     ]
     * */
    public int uniquePathsWithObstacles(int[][] matrix) {
        /**
         * base case: M[0] = 1
         * induction rule: M[j], 从col=0到col=i总共有几条通路
         * 0 -> 行: 从上到下, 若matrix[i][j] = 1, 该路径有路障, 没有通路，为0
         *          否则：路径数 arr[j] = arr[j] + arr[j - 1]
         * */
        int cols = matrix[0].length;
        int[] arr = new int[cols];
        arr[0] = 1;
        for (int i = 0; i < matrix.length; i ++) {
            for (int j = 0; j < matrix[0].length; j ++) {
                if (matrix[i][j] == 1) {
                    arr[j] = 0;
                } else if (j > 0) {
                    arr[j] += arr[j - 1];
                }
            }
        }
        return arr[cols - 1];
    }
    /**
     * LC64 minimum path sum
     * 从一个矩阵的左上角到右下角走，找到一个路径，使得路径经过的数字之和最小
     * 只能向下或向右走
     * */
    public int minPathSum(int[][] matrix) {
        int rows = matrix.length;
        int cols = matrix[0].length;
        for(int i = 1; i < cols; i ++){
            matrix[0][i] += matrix[0][i - 1];
        }
        for(int i = 1; i < rows; i ++){
            matrix[i][0] += matrix[i - 1][0];
        }
        for(int i = 1; i < rows; i ++){
            for(int j = 1; j < cols; j ++){
                matrix[i][j] += Math.min(matrix[i - 1][j], matrix[i][j - 1]);
            }
        }
        return matrix[rows - 1][cols - 1];
    }
    /**
     * LC66 plus One,
     * [1, 2, 3] -> [1, 2, 4]
     * 数组的每位表示一个数字的位，将该数字+1
     * */
    public int[] plusOne(int[] digits) {
        int carry = 0;
        digits[digits.length - 1] += 1;
        for (int i = digits.length - 1; i >= 0; i --) {
            digits[i] += carry;
            if (digits[i] >= 10) {
                digits[i] -= 10;
                carry = 1;
            } else {
                carry = 0;
            }
        }
        if (carry > 0) {
            int[] arr = new int[digits.length + 1];
            arr[0] = carry;
            System.arraycopy(digits, 0, arr, 1, digits.length);
            return arr;
        } else {
            return digits;
        }
    }
    /**
     * LC67 Add Binary
     * 两个数组分别表示两个binary data，相加
     * */
    public String addBinary(String a, String b) {
        if(a == null || a.isEmpty()) {
            return b;
        }
        if(b == null || b.isEmpty()) {
            return a;
        }
        char[] aArray = a.toCharArray();
        char[] bArray = b.toCharArray();
        StringBuilder sb = new StringBuilder();

        int i = aArray.length - 1;
        int j = bArray.length - 1;
        int carry = 0;
        int result = 0;

        while(i >= 0 || j >= 0 || carry == 1) {
            int aByte = (i >= 0) ? Character.getNumericValue(aArray[i --]) : 0;
            int bByte = (j >= 0) ? Character.getNumericValue(bArray[j --]) : 0;
            result = aByte ^ bByte ^ carry;
            carry = ((aByte + bByte + carry) >= 2) ? 1 : 0;
            sb.append(result);
        }
        return sb.reverse().toString();
    }
    /**
     * LC69 sqrt(x)
     * */
    public int mySqrt(int x) {
        int i = 0;
        while(x > 0){
            x -= 2 * (i ++) + 1;
        }
        if(x == 0) return i;
        return i - 1;
    }
    /**
     * LC70 climbing stairs
     * n级台阶，每次上1或2，有几种上法
     * */
    public int climbStairs(int n) {
        if (n == 1) {
            return 1;
        }
        int[] arr = new int[n + 1];
        arr[1] = 1;
        arr[2] = 2;
        for (int i = 3; i <= n; i++) {
            arr[i] = arr[i - 1] + arr[i - 2];
        }
        return arr[n];
    }
    /**
     * LC71 Simplify path
     * */
    public String simplifyPath(String path) {
        Deque<String> stack = new LinkedList<>();
        Set<String> skip = new HashSet<>(Arrays.asList("..",".",""));
        for (String dir : path.split("/")) {
            if (dir.equals("..") && !stack.isEmpty()) stack.pop();
            else if (!skip.contains(dir)) stack.push(dir);
        }
        StringBuilder sb = new StringBuilder();
        for (String dir : stack) sb.insert(0, "/" + dir);
        return (sb.length() == 0) ? "/" : sb.toString();
    }
    /**
     * LC76 Minimum Window SubString
     * 在s中找到最小能够包含所有t中出现字符的子字符串
     * */
    public String minWindow(String s, String t) {
        if (s.length() < t.length() || t.length() == 0) return "";
        Map<Character, Integer> map = new HashMap<>();
        for (char c : t.toCharArray()) {
            map.merge(c, 1, Integer::sum);  // 放每个字符的数量map.merge(c, 1, (a,b) -> a+b)
        }
        int start = 0, end = 0, soluStart = 0, soluEnd = 0, len = t.length(), shortest = Integer.MAX_VALUE;
        while (end < s.length()) {
            char c = s.charAt(end);
            if (map.merge(c, -1, Integer::sum) >= 0) {  // 找到字符c之后，数量-1，此时数量仍然>=0
                len --;
            }
            while (len == 0) {
                char c1 = s.charAt(start);
                if (shortest >= end - start + 1) {
                    soluStart = start;
                    soluEnd = end + 1;
                    shortest = end - start + 1;
                }
                if (map.merge(c1, +1, Integer::sum) > 0) {
                    len ++;
                }
                start ++;
            }
            end ++;
        }
        return s.substring(soluStart, soluEnd);
    }

    @Test
    public void LC76Test(){
        System.out.println(minWindow("ADOBECODEBANC", "ABC"));
        System.out.println(minWindow("a", "aa"));
        System.out.println(minWindow("a", "b"));
    }
    /**
     * LC77 Combinations
     * */
    public List<List<Integer>> combinations(int n, int k) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> cur = new ArrayList<>();
        combinationsHelper(result, cur, 1, n, k);
        return result;
    }
    public void combinationsHelper(List<List<Integer>> result, List<Integer> cur, int idx, int n, int k) {
        if(k == 0) {
            result.add(new ArrayList<>(cur));
            return;
        }
        for(int i = idx; i <= n; i ++) {
            cur.add(i);
            combinationsHelper(result, cur, i + 1, n, k - 1);
            cur.remove(cur.size() - 1);
        }
    }
    /**
     * LC78 subsets
     * */
    public List<List<Integer>> subsets(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null) return result;
        List<Integer> cur = new ArrayList<>();
        subsetsHelper(nums, result, cur, 0);
        return result;
    }
    private void subsetsHelper(int[] nums, List<List<Integer>> result, List<Integer> cur, int idx){
        if (idx == nums.length) {
            result.add(new ArrayList<>(cur));
            return;
        }
        cur.add(nums[idx]);
        subsetsHelper(nums, result, cur, idx + 1);
        cur.remove(cur.size() - 1);
        subsetsHelper(nums, result, cur, idx + 1);
    }
    /**
     * LC90 subsets2
     * */
    public List<List<Integer>> subsets2(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null) return result;
        List<Integer> cur = new ArrayList<>();
        Arrays.sort(nums);
        subsetsHelper2(nums, result, cur, 0);
        return result;
    }
    private void subsetsHelper2(int[] nums, List<List<Integer>> result, List<Integer> cur, int idx){
        if (idx == nums.length) {
            result.add(new ArrayList<>(cur));
            return;
        }
        cur.add(nums[idx]);
        subsetsHelper2(nums, result, cur, idx + 1);
        cur.remove(cur.size() - 1);
        while (idx < nums.length - 1 && nums[idx] == nums[idx + 1]) idx ++;
        subsetsHelper2(nums, result, cur, idx + 1);
    }

    /**
     * LC79 word search
     * 矩阵中找单词，必须是上下左右相邻的字母串起来构成一个Word
     * */
    public boolean exist(char[][] board, String word) {
        boolean[][] visited = new boolean[board.length][board[0].length];
        for(int i = 0; i < board.length; i++){
            for(int j = 0; j < board[i].length; j++){
                if((word.charAt(0) == board[i][j]) && exist1Helper(board, visited, word, i, j, 0)){
                    return true;
                }
            }
        }
        return false;
    }
    private boolean exist1Helper(char[][]board, boolean[][] visited, String word, int i, int j, int idx){
        if(idx == word.length()){
            return true;
        }

        if(i >= board.length || i < 0 || j >= board[i].length || j < 0 || board[i][j] != word.charAt(idx) || visited[i][j]){
            return false;
        }

        visited[i][j] = true;
        if(exist1Helper(board, visited, word, i - 1, j, idx + 1) ||
                exist1Helper(board, visited, word, i + 1, j, idx + 1) ||
                exist1Helper(board, visited, word, i, j - 1, idx + 1) ||
                exist1Helper(board, visited, word, i, j + 1, idx + 1)){
            return true;
        }
        visited[i][j] = false;
        return false;
    }
    /**
     * LC80 remove duplicates from sorted array
     * at most save two same numbers
     * do in-place no new memory
     * */
    public int removeDuplicates2_1(int[] arr) {
        int slow = 0, fast = 0;
        while (fast < arr.length) {
            if (slow < 2 || arr[fast] > arr[slow - 2]) {
                arr[slow ++] = arr[fast];
            }
            fast ++;
        }
        return slow;
    }
    /**
     * remove duplicates from sorted array, no duplicates
     * */
    public int removeDuplicates1_1(int[] arr) {
        int slow = 0, fast = 0;
        while (fast < arr.length) {
            if (slow < 1 || arr[fast] > arr[slow - 1]) {
                arr[slow ++] = arr[fast];
            }
            fast ++;
        }
        return slow;
    }
    /**
     * LC82 remove duplicates from sorted linkedlist
     * 凡是有重复出现的节点都不要，只要unique的节点
     * */
    public ListNode deleteDuplicates2(ListNode head) {
        if(head == null || head.next == null) return head;
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode pre = dummy;
        ListNode cur = head;
        while(cur != null){
            while(cur.next != null && cur.value == cur.next.value){
                cur = cur.next;
            }
            if(pre.next == cur){
                pre = pre.next;
            } else {
                pre.next = cur.next;
            }
            cur = cur.next;
        }
        return dummy.next;
    }
    public ListNode deleteDuplicates2_1(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        ListNode prev = dummy;
        ListNode cur = head;
        while (cur != null) {
            while (cur.next != null && cur.value == cur.next.value) {
                cur = cur.next;
            }
            if (prev.next == cur) {
                prev = prev.next;
            } else {
                prev.next = cur.next;
            }
            cur = cur.next;
        }
        return dummy.next;
    }
    /**
     * LC83 remove duplicates from sorted linkedlist
     * 同样value的节点只出现一次
     * */
    public ListNode deleteDuplicates1(ListNode head) {
        ListNode cur = head;
        while (cur != null && cur.next != null) {
            if (cur.next.value == cur.value) {
                cur.next = cur.next.next;
            } else {
                cur = cur.next;
            }
        }
        return head;
    }
    /**
     * LC88 merge two sorted array
     * 假定A要比AB有元素的长度都长，足够放下排好序的元素
     * */
    public void merge(int A[], int m, int B[], int n) {
        int i = m - 1, j = n - 1, k = m + n - 1;
        while (i >= 0 && j >= 0) {
            if (A[i] > B[j]) {
                A[k --] = A[i --];
            } else {
                A[k --] = B[j --];
            }
        }
        while (j >= 0) A[k --] = B[j --];
    }
    /**
     * LC89 gray code
     * */
    public List<Integer> grayCode(int n) {
        List<Integer> result = new ArrayList<>();
        if (n < 0) return result;
        if (n == 0) {
            result.add(0);
            return result;
        }
        List<Integer> list = grayCode(n - 1);
        result = new ArrayList<>(list);
        int addNumber = 1 << (n - 1);
        for (int i = list.size() - 1; i >= 0; i--) {
            result.add(addNumber + list.get(i));
        }
        return result;
    }
    @Test
    public void baseTest(){
        int[] arr = new int[]{1,1,1,2,2,3};
        System.out.println(removeDuplicates1_1(arr));
        System.out.println(Arrays.toString(arr));
    }
    @Test
    public void jsonGenerateTest(){
        double left = -115.03781035927585, right = -100.05050567177585, up = 45.01138652062278, down = 35.0007753775683;
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode outer = mapper.createObjectNode();
        outer.put("type", "FeatureCollection");
        outer.set("features", mapper.createArrayNode());
        for (int i = 0; i < 20000; i ++) {
            double lng = Math.random() * (right - left + 1) + left;
            double lat = Math.random() * (up - down + 1) + down;
            ObjectNode inner = mapper.createObjectNode();
            ObjectNode geometry = mapper.createObjectNode();
            ObjectNode properties = mapper.createObjectNode();
            geometry.put("type", "Point");
            ArrayNode pos = mapper.createArrayNode();
            pos.add(lng).add(lat);
            geometry.set("coordinates", pos);
            inner.set("geometry", geometry);
            inner.put("type", "Feature");
            properties.put("popupContent", "This is a B-Cycle Station. Come pick up a bike and pay by the hour. What a deal!");
            inner.set("properties", properties);
            inner.put("id", i);

            ((ArrayNode) outer.get("features")).add(inner);
        }
        ObjectWriter writer = mapper.writer(new DefaultPrettyPrinter());
        try {
            writer.writeValue(new File("/Users/eriche/Documents/reddwarf-api/public/resources/temp/lines.json"), outer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
class Interval {
    int start;
    int end;
    Interval() { start = 0; end = 0; }
    Interval(int s, int e) { start = s; end = e; }
}