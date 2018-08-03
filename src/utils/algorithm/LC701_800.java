package utils.algorithm;
import java.util.*;
public class LC701_800 {
    /**
     * LC746 min cost climbing stairs
     On a staircase, the i-th step has some non-negative cost cost[i] assigned (0 indexed).

     Once you pay the cost, you can either climb one or two steps. You need to find minimum cost to reach the top of the floor, and you can either start from the step with index 0, or the step with index 1.

     Example 1:
     Input: cost = [10, 15, 20]
     Output: 15
     Explanation: Cheapest is start on cost[1], pay that cost and go to the top.
     Example 2:
     Input: cost = [1, 100, 1, 1, 1, 100, 1, 1, 100, 1]
     Output: 6
     Explanation: Cheapest is start on cost[0], and only step on 1s, skipping cost[3].
     Note:
     cost will have a length in the range [2, 1000].
     Every cost[i] will be an integer in the range [0, 999].
     * */
    public int minCostClimbingStairs(int[] cost) {  //  the final cost f[i] to climb the staircase from some step i is f[i] = cost[i] + min(f[i+1], f[i+2])
        int f1 = 0, f2 = 0;  // 从后向前看，f1和f2为前两步的cost
        for (int i = cost.length - 1; i >= 0; i--) {
            int f0 = cost[i] + Math.min(f1, f2);
            f2 = f1;
            f1 = f0;
        }
        return Math.min(f1, f2);
    }
    /**
     * LC771 Jewels and Stones
     * 两字符, J里的字符都表示珠宝, s里的字符都表示石头，但是如果有J的字符出现在s中则表示这堆石头中有珠宝
     * 问s中有多少石头其实是珠宝
     * You're given strings J representing the types of stones that are jewels, and S representing the stones you have.  Each character in S is a type of stone you have.  You want to know how many of the stones you have are also jewels.

     The letters in J are guaranteed distinct, and all characters in J and S are letters. Letters are case sensitive, so "a" is considered a different type of stone from "A".

     Example 1:

     Input: J = "aA", S = "aAAbbbb"
     Output: 3
     Example 2:

     Input: J = "z", S = "ZZ"
     Output: 0
     Note:

     S and J will consist of letters and have length at most 50.
     The characters in J are distinct.
     * */
    public int numJewelsInStones(String J, String S) {
        Set<Character> set = new HashSet<>();
        for(char c : J.toCharArray()) set.add(c);
        int count = 0;
        for(char c : S.toCharArray()) if(set.contains(c)) count ++;
        return count;
    }
}
