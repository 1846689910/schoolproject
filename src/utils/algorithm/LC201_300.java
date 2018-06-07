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
