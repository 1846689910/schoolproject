package utils.algorithm;

import org.junit.Test;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.*;

public class LC400_500 {
    /**
     * LC415 Add Strings
     * Given two non-negative integers num1 and num2 represented as string, return the sum of num1 and num2.

     Note:

     The length of both num1 and num2 is < 5100.
     Both num1 and num2 contains only digits 0-9.
     Both num1 and num2 does not contain any leading zero.
     You must not use any built-in BigInteger library or convert the inputs to integer directly.
     * */
    public String addStrings(String num1, String num2) {
        int i = num1.length() - 1;
        int j = num2.length() - 1;
        int carry = 0;
        StringBuilder sb = new StringBuilder();
        while (i >= 0 || j >= 0){
            int sum = carry;
            if (i >= 0){
                sum += (num1.charAt(i --) - '0');
            }
            if (j >= 0){
                sum += (num2.charAt(j --) - '0');
            }
            carry = sum / 10;
            sb.append(sum % 10);
        }
        if (carry == 1){
            sb.append(1);
        }
        return sb.reverse().toString();
    }
    /**
     * LC445 add two numbers 2
     * */
    public ListNode addTwoNumbers(ListNode l1, ListNode l2) {
        Stack<Integer> s1 = new Stack<>();
        Stack<Integer> s2 = new Stack<>();

        while(l1 != null) {
            s1.push(l1.value);
            l1 = l1.next;
        }
        while(l2 != null) {
            s2.push(l2.value);
            l2 = l2.next;
        }

        int sum = 0;
        ListNode list = new ListNode(0);
        while (!s1.empty() || !s2.empty()) {
            if (!s1.empty()) sum += s1.pop();
            if (!s2.empty()) sum += s2.pop();
            list.value = sum % 10;
            ListNode next = new ListNode(sum /= 10);
            next.next = list;
            list = next;
        }

        return list.value == 0 ? list.next : list;
    }
    public ListNode addTwoNumbers1(ListNode one, ListNode two) {
        Deque<Integer> s1 = new LinkedList<>();
        Deque<Integer> s2 = new LinkedList<>();
        while(one != null) {
            s1.offerFirst(one.value);
            one = one.next;
        }
        while(two != null) {
            s2.offerFirst(two.value);
            two = two.next;
        }
        int sum = 0;
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (! s1.isEmpty() && ! s2.isEmpty()){
            if (!s1.isEmpty()) sum += s1.pollFirst();
            if (!s2.isEmpty()) sum += s2.pollFirst();
            cur.next = new ListNode(sum % 10);
            sum /= 10;
            cur = cur.next;
        }
        cur.next = new ListNode(sum);
        return dummy.next;
    }
    @Test
    public void addTest(){

    }
    /**
     * LC457 circular array loop
     * */
    public boolean circularArrayLoop(int[] arr) {
        if(arr == null || arr.length == 0) return false;
        for(int i = 0, len = arr.length; i < len; i ++){
            if(arr[i] == 0) continue;
            int slow = i, fast = i, count = 0;
            boolean forward = arr[slow] > 0;
            while (slow != fast || count == 0) {
                int tempSlow = slow;
                slow = (slow + arr[slow] + len) % len;

                if(forward && arr[fast] < 0 || !forward && arr[fast] > 0) return false;
                fast = (fast + arr[fast] + len) % len;

                if(forward && arr[fast] < 0 || !forward && arr[fast] > 0) return false;
                fast = (fast + arr[fast] + len) % len;

                arr[tempSlow] = 0;
                count ++;

            }
            if(count > 1) return true;
        }
        return false;
    }
    /*
public:
    bool circularArrayLoop(vector<int>& nums) {
        unordered_map<int, int> m;
        int n = nums.size();
        vector<bool> visited(n, false);
        for (int i = 0; i < n; ++i) {
            if (visited[i]) continue;
            int cur = i;
            while (true) {
                visited[cur] = true;
                int next = (cur + nums[cur]) % n;
                if (next < 0) next += n;
                if (next == cur || nums[next] * nums[cur] < 0) break;
                if (m.count(next)) return true;
                m[cur] = next;
                cur = next;
            }
        }
        return false;
    }
    * */
}


/**
 * LC460
 * */
class LFUCache {
    Map<Integer, Integer> vals;
    Map<Integer, Integer> counts;
    HashMap<Integer, LinkedHashSet<Integer>> lists;
    int size;
    int min = -1;
    public LFUCache(int capacity) {
        size = capacity;
        vals = new HashMap<>();
        counts = new HashMap<>();
        lists = new HashMap<>();
        lists.put(1, new LinkedHashSet<>());
    }
    public int get(int key) {
        if(!vals.containsKey(key)) return -1;
        int count = counts.get(key);
        counts.put(key, count+1);
        lists.get(count).remove(key);
        if(count==min && lists.get(count).size()==0) min++;
        if(!lists.containsKey(count+1)) lists.put(count+1, new LinkedHashSet<>());
        lists.get(count+1).add(key);
        return vals.get(key);
    }
    public void set(int key, int value) {
        if(size <=0) return;
        if(vals.containsKey(key)) {
            vals.put(key, value);
            get(key);
            return;
        }
        if(vals.size() >= size) {
            int evit = lists.get(min).iterator().next();
            lists.get(min).remove(evit);
            vals.remove(evit);
        }
        vals.put(key, value);
        counts.put(key, 1);
        min = 1;
        lists.get(1).add(key);
    }
}
