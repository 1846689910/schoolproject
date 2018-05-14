package utils.algorithm;

import org.junit.Test;

import java.util.Arrays;

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
    @Test
    public void baseTest(){
        System.out.println((int)'a');
    }
}
