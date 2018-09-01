package utils.algorithm;

import java.util.*;

public class LC601_700 {
    /**
     * LC653 653. Two Sum IV - Input is a BST
     * */
    public boolean findTarget(TreeNode root, int k) {
        List<Integer> list = inorder(root);
        int left = 0, right = list.size() - 1;
        while (left < right) {
            int sum = list.get(left) + list.get(right);
            if (sum == k)
                return true;
            if (sum < k)
                left++;
            else
                right--;
        }
        return false;
    }
    public List<Integer> inorder(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null)
            return result;
        result.addAll(inorder(root.left));
        result.add(root.value);
        result.addAll(inorder(root.right));
        return result;
    }
    /**LC617 Merge two binary tree
     * 两树对应位置相加来合并
     You need to merge them into a new binary tree. The merge rule is that if two nodes overlap, then sum node values up as the new value of the merged node. Otherwise, the NOT null node will be used as the node of new tree.

     Example 1:
     Input:
     Tree 1                     Tree 2
        1                         2
       / \                       / \
      3   2                     1   3
     /                           \   \
     5                             4   7
     Output:
     Merged tree:
         3
        / \
       4   5
      / \   \
     5   4   7

     * */
    public TreeNode mergeTrees(TreeNode t1, TreeNode t2) {
        if (t1 == null)
            return t2;
        if (t2 == null)
            return t1;
        t1.value += t2.value;
        t1.left = mergeTrees(t1.left, t2.left);
        t1.right = mergeTrees(t1.right, t2.right);
        return t1;
    }
    public TreeNode mergeTrees2(TreeNode one, TreeNode two){
        if(one == null) return two;
        Queue<TreeNode[]> queue = new LinkedList<>();
        queue.offer(new TreeNode[]{one, two});
        while (! queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i ++) {
                TreeNode[] cur = queue.poll();
                if(cur[0] == null || cur[1] == null) continue;
                cur[0].value += cur[1].value;
                if(cur[0].left == null) {
                    cur[0].left = cur[1].left;
                } else {
                    queue.offer(new TreeNode[]{cur[0].left, cur[1].left});
                }
                if(cur[0].right == null) {
                    cur[0].right = cur[1].right;
                } else {
                    queue.offer(new TreeNode[]{cur[0].right, cur[1].right});
                }
            }
        }
        return one;
    }
    /**
     * LC682 Baseball Game(栈的表达式运算)
     * You're now a baseball game point recorder.
     Given a list of strings, each string can be one of the 4 following types:

     Integer (one round's score): Directly represents the number of points you get in this round.
     "+" (one round's score): Represents that the points you get in this round are the sum of the last two valid round's points.
     "D" (one round's score): Represents that the points you get in this round are the doubled data of the last valid round's points.
     "C" (an operation, which isn't a round's score): Represents the last valid round's points you get were invalid and should be removed.
     Each round's operation is permanent and could have an impact on the round before and the round after.

     You need to return the sum of the points you could get in all the rounds.

     Example 1:
     Input: ["5","2","C","D","+"]
     Output: 30
     Explanation:
     Round 1: You could get 5 points. The sum is: 5.
     Round 2: You could get 2 points. The sum is: 7.
     Operation 1: The round 2's data was invalid. The sum is: 5.
     Round 3: You could get 10 points (the round 2's data has been removed). The sum is: 15.
     Round 4: You could get 5 + 10 = 15 points. The sum is: 30.
     Example 2:
     Input: ["5","-2","4","C","D","9","+","+"]
     Output: 27
     Explanation:
     Round 1: You could get 5 points. The sum is: 5.
     Round 2: You could get -2 points. The sum is: 3.
     Round 3: You could get 4 points. The sum is: 7.
     Operation 1: The round 3's data is invalid. The sum is: 3.
     Round 4: You could get -4 points (the round 3's data has been removed). The sum is: -1.
     Round 5: You could get 9 points. The sum is: 8.
     Round 6: You could get -4 + 9 = 5 points. The sum is 13.
     Round 7: You could get 9 + 5 = 14 points. The sum is 27.
     Note:
     The size of the input list will be between 1 and 1000.
     Every integer represented in the list will be between -30000 and 30000.
     * */
    public int calPoints(String[] ops) {
        Deque<Integer> stack = new LinkedList<>();
        for(String op : ops) {
            if (op.equals("+")) {
                int top = stack.pollFirst();
                int newtop = top + stack.peekFirst();
                stack.offerFirst(top);
                stack.offerFirst(newtop);
            } else if (op.equals("C")) {
                stack.pollFirst();
            } else if (op.equals("D")) {
                stack.offerFirst(2 * stack.peekFirst());
            } else {
                stack.offerFirst(Integer.valueOf(op));
            }
        }
        int ret = 0;
        for(int score : stack) ret += score;
        return ret;
    }
}
