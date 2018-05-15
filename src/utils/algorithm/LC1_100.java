package utils.algorithm;

import org.junit.Test;

import java.util.*;

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
    @Test
    public void baseTest(){
        int[] arr = new int[]{2, 1, 3, 3, 4};
        System.out.println(maxArea(arr));
    }
}
