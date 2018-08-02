package utils.algorithm;
import java.util.*;
public class LC301_400 {
    /**
     * LC324 wiggle sort 2
     * 与wiggle sort1的区别在于，这次需要nums[0] < nums[1] > nums[2] < nums[3]....
     * 并且数组可能有重复元素
     * */
    public void wiggleSort(int[] nums) {
        Arrays.sort(nums);
        int lo = (nums.length - 1) / 2, hi = nums.length - 1;
        int[] ans = new int[nums.length];
        for (int i = 0; i < nums.length; i++) ans[i] = (i % 2 == 0) ? nums[lo--] : nums[hi--];
        System.arraycopy(ans, 0, nums, 0, nums.length);
    }
    /**
     * LC326 Power of three
     * */
    public boolean isPowerOfThree(int n) {
        if (n == 0) return false;
        while (n % 3 == 0) {
            n /= 3;
        }
        return n == 1;
    }
    /**
     * LC342 Power of four
     * */
    public boolean isPowerOfFour(int num) {
        if (num <= 0) return false;
        while (num % 4 == 0) num /= 4;
        return num == 1;
    }
    /**
     * LC387 first unique char in a string
     * */
    public int firstUniqChar(String s) {
        int[] arr = new int[256];
        char[] chars = s.toCharArray();
        for (char c : chars){
            arr[c] ++;
        }
        int found = -1;
        for(int i = 0; i < chars.length; i ++) {
            if(arr[chars[i]] == 1) {
                found = i;
                break;
            }
        }
        return found;
    }
}
/**
 * LC380 Insert Delete GetRandom O(1)
 * Design a data structure that supports all following operations in average O(1) time.

 insert(val): Inserts an item val to the set if not already present.
 remove(val): Removes an item val from the set if present.
 getRandom: Returns a random element from current set of elements. Each element must have the same probability of being returned.
 Example:

 // Init an empty set.
 RandomizedSet randomSet = new RandomizedSet();

 // Inserts 1 to the set. Returns true as 1 was inserted successfully.
 randomSet.insert(1);

 // Returns false as 2 does not exist in the set.
 randomSet.remove(2);

 // Inserts 2 to the set, returns true. Set now contains [1,2].
 randomSet.insert(2);

 // getRandom should return either 1 or 2 randomly.
 randomSet.getRandom();

 // Removes 1 from the set, returns true. Set now contains [2].
 randomSet.remove(1);

 // 2 was already in the set, so return false.
 randomSet.insert(2);

 // Since 2 is the only number in the set, getRandom always return 2.
 randomSet.getRandom();
 * */
class RandomizedSet {
    private List<Integer> list;
    private Map<Integer, Integer> map;  // <值,序号>
    private java.util.Random rand = new java.util.Random();
    /** Initialize your data structure here. */
    public RandomizedSet() {
        list = new ArrayList<>();
        map = new HashMap<>();
    }

    /** Inserts a value to the set. Returns true if the set did not already contain the specified element. */
    public boolean insert(int val) {
        if (map.containsKey(val)) return false;
        map.put(val, list.size());
        list.add(val);
        return true;
    }

    /** Removes a value from the set. Returns true if the set contained the specified element. */
    public boolean remove(int val) {
        if (! map.containsKey(val)) return false;
        int idx = map.get(val);
        if (idx < list.size() - 1 ) { // not the last one than swap the last one with this val
            int lastone = list.get(list.size() - 1 );
            list.set(idx , lastone);
            map.put(lastone, idx);
        }
        map.remove(val);
        list.remove(list.size() - 1);
        return true;
    }

    /** Get a random element from the set. */
    public int getRandom() {
        return list.get(rand.nextInt(list.size()));
    }
}