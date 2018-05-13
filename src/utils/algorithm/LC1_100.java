package utils.algorithm;

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
        while (cur.next != null && carry > 0){
            int value = cur.next.value + carry;
            cur.next.value = value % 10;
            carry = value / 10;
            cur = cur.next;
        }
        if (cur.next == null && carry > 0) cur.next = new ListNode(carry);
        return dummy.next;
    }

}
