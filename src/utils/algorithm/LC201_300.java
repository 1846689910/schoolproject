package utils.algorithm;

import java.util.*;

public class LC201_300 {
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
