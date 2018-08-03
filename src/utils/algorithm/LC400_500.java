package utils.algorithm;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

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
