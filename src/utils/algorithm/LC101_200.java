package utils.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class LC101_200 {
    /**
     * LC103 Binary Tree Zigzag Level Order Traversal
     * */
    public List<List<Integer>> zigzagLevelOrder(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Deque<TreeNode> deque = new LinkedList<>();
        deque.offerFirst(root);
        boolean eventLayer = false;
        while (! deque.isEmpty()) {
            int size = deque.size();
            List<Integer> curLayer = new ArrayList<>();
            if (eventLayer) {
                for (int i = 0; i < size; i ++) {
                    TreeNode cur = deque.pollFirst();
                    if (cur.right != null) deque.offerLast(cur.right);
                    if (cur.left != null) deque.offerLast(cur.left);
                    curLayer.add(cur.value);
                }
            } else {
                for (int i = 0; i < size; i ++) {
                    TreeNode cur = deque.pollLast();
                    if (cur.left != null) deque.offerFirst(cur.left);
                    if (cur.right != null) deque.offerFirst(cur.right);
                    curLayer.add(cur.value);
                }
            }
            eventLayer = ! eventLayer;
            result.add(curLayer);
        }
        return result;
    }
    /**
     * LC104 Maximum Depth of Binary Tree
     * */
    public static int maxDepth (TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 先考察看看当前节点左右子树是不是都已经不balance了，如果是这样，直接返回-1就好了
        int leftHeight = maxDepth(root.left);
        int rightHeight = maxDepth(root.right);
        return Math.max(leftHeight, rightHeight) + 1;  // 根节点也算一层，也要加上
    }
    /**
     * LC 106 Construct Binary Tree from Inorder and Postorder Traversal 中后重构
     * */
    public TreeNode inPostBuildTree (int[] in, int[] post) {
        return inPostBuildTreeHelper(in, post, 0, 0, in.length);
    }
    private TreeNode inPostBuildTreeHelper(int[] in,int[] post,int idx1,int idx2,int inLen) {
        if (inLen == 0) {
            return null;
        }
        TreeNode root = new TreeNode(post[idx2 + inLen - 1]);
        int leftLen = 0;
        while (in[idx1 + leftLen] != post[idx2 + inLen - 1]) {
            leftLen ++;
        }
        root.left = inPostBuildTreeHelper(in, post, idx1, idx2, leftLen);
        root.right = inPostBuildTreeHelper(in, post,idx1 + leftLen + 1,idx2 + leftLen,inLen - leftLen - 1);
        return root;
    }
    /**
     * LC107 level order traverse 2 倒着排
     * For example:
     Given binary tree [3,9,20,null,null,15,7],
     3
     / \
     9  20
     /  \
     15   7
     return its bottom-up level order traversal as:
     [
     [15,7],
     [9,20],
     [3]
     ]
     * */
    public List<List<Integer>> levelOrderBottom(TreeNode root) {
        Deque<List<Integer>> result = new LinkedList<>();
        if (root == null) return new ArrayList<>(result);
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (! queue.isEmpty()) {
            int size = queue.size();
            List<Integer> curLayer = new ArrayList<>();
            for (int i = 0; i < size; i ++) {
                TreeNode cur = queue.poll();
                if (cur.left != null) queue.offer(cur.left);
                if (cur.right != null) queue.offer(cur.right);
                curLayer.add(cur.value);
            }
            result.offerFirst(curLayer);
        }
        return new ArrayList<>(result);
    }
    /**
     * LC108 Convert sorted array to BST
     * */
    public TreeNode sortedArrToBST(int[] arr) {
        if (arr == null || arr.length == 0) return null;
        TreeNode root = sortedArrToBSTHelper(arr, 0, arr.length - 1);
        return root;
    }
    private TreeNode sortedArrToBSTHelper(int[] arr, int start, int end) {
        if (start > end) return null;
        int mid = start + (end - start) / 2;
        TreeNode node = new TreeNode(arr[mid]);
        node.left = sortedArrToBSTHelper(arr, start, mid - 1);
        node.right = sortedArrToBSTHelper(arr, mid + 1, end);
        return node;
    }
    /**
     * LC109 Convert sorted list to BST
     * */
    public TreeNode sortedListToBST(ListNode head) {
        if(head == null) return null;
        return sortedListToBSTHelper(head,null);
    }
    private TreeNode sortedListToBSTHelper(ListNode head, ListNode tail){
        ListNode slow = head;
        ListNode fast = head;
        if(head == tail) return null;
        while(fast != tail && fast.next != tail){
            slow = slow.next;
            fast = fast.next.next;
        }
        TreeNode root = new TreeNode(slow.value);
        root.left = sortedListToBSTHelper(head, slow);
        root.right = sortedListToBSTHelper(slow.next, tail);
        return root;
    }
    /**
     * LC111 minimum depth of Binary tree
     * */
    public int minDepth1(TreeNode root) {
        if (root == null) return 0;
        int leftHeight = minDepth1(root.left);
        int rightHeight = minDepth1(root.right);
        if (leftHeight == 0) {
            return rightHeight + 1;
        } else if (rightHeight == 0) {
            return leftHeight + 1;
        }
        return Math.min(leftHeight, rightHeight) + 1;
    }
    /**
     * LC112 Path sum
     * 直通根到叶，检查是否有一条通路，节点数值之和为sum
     * */
    public boolean hasPathSum(TreeNode root, int sum) {
        if(root == null) return false;
        if(root.left == null && root.right == null && sum == root.value) return true;
        return hasPathSum(root.left, sum - root.value) || hasPathSum(root.right, sum - root.value);
    }
    /**
     * LC113 Path sum 2
     * 直通根到叶，获取所有的通路
     * */
    public List<List<Integer>> pathSum2(TreeNode root, int sum) {
        List<List<Integer>> result = new ArrayList<>();
        List<Integer> cur = new ArrayList<>();
        pathSum2Helper(root, sum, result, cur);
        return result;
    }

    private void pathSum2Helper(TreeNode root, int sum, List<List<Integer>> result, List<Integer> cur){
        if(root == null) return;
        cur.add(root.value);

        if(root.left != null) {
            pathSum2Helper(root.left,sum - root.value, result, cur);
            cur.remove(cur.size() - 1);
        }
        if(root.right != null) {
            pathSum2Helper(root.right,sum - root.value, result, cur);
            cur.remove(cur.size() - 1);
        }
        if(root.left == null && root.right == null && root.value == sum){
            result.add(new ArrayList<>(cur));
        }
    }
    /**
     * LC437 Path sum3
     * 直通any到any. 有多少路径可以让和为sum
     * */
    public int pathSum3(TreeNode root, int sum) {
        if (root == null) return 0;
        return pathSum3Helper(root, sum) + pathSum3(root.left, sum) + pathSum3(root.right, sum);
    }

    private int pathSum3Helper (TreeNode root, int sum) {
        if (root == null) return 0;
        return (root.value == sum ? 1 : 0)
                + pathSum3Helper(root.left, sum - root.value) + pathSum3Helper(root.right, sum - root.value);
    }
    /**
     * LC114 Flatten a binary tree to LinkedList
     *1
     / \
     2   5
     / \   \
     3   4   6
     The flattened tree should look like:
    1-2-3-4-5-6
     * */
    public void flatten(TreeNode root){
        flattenHelper(root, new TreeNode[]{null});
    }
    private void flattenHelper(TreeNode root, TreeNode[] prev) {
        if (root == null)return;
        flattenHelper(root.right, prev);
        flattenHelper(root.left, prev);
        root.right = prev[0];
        root.left = null;
        prev[0] = root;
    }
    /**
     * LC116 Populating Next Right Pointers in Each Node
     class TreeLinkNode{
        TreeLinkNode left;
        TreeLinkNode right;
        TreeLinkNode next;
        int val;
        public TreeLinkNode(int val){
            this.val = val;
        }
     }
     设置每个节点的next指针指向相邻的下一节点
     Given the following perfect binary tree,

        1
      /  \
     2    3
     / \  / \
     4  5  6  7
     After calling your function, the tree should look like:

     1 -> NULL
     /  \
     2 -> 3 -> NULL
     / \  / \
     4->5->6->7 -> NULL
     假定是完美的二叉树，所有父节点都有两个子节点
     You may assume that it is a perfect binary tree (ie, all leaves are at the same level, and every parent has two children).
     * */
    public void connect(TreeLinkNode root) {
        TreeLinkNode start = root;
        while(start != null){
            TreeLinkNode cur = start;
            while(cur != null){
                if(cur.left != null) cur.left.next = cur.right;
                if(cur.right != null && cur.next != null) cur.right.next = cur.next.left;
                cur = cur.next;
            }
            start = start.left;
        }
    }
    /**
     * LC117 同LC116，但没有完美节点的假设
     * */
    public void connect1(TreeLinkNode root) {
        while(root != null){
            TreeLinkNode dummy = new TreeLinkNode(0);
            TreeLinkNode cur = dummy;
            while(root != null){
                if(root.left != null) {
                    cur.next = root.left;
                    cur = cur.next;
                }
                if(root.right != null) {
                    cur.next = root.right;
                    cur = cur.next;
                }
                root = root.next;
            }
            root = dummy.next;
        }
    }
    /**
     * LC118 Pascal's triangle
     * 三角形外围都是1，内部每个数都是肩上两数之和
     Input: 5
     Output:
     [
     [1],
     [1,1],
     [1,2,1],
     [1,3,3,1],
     [1,4,6,4,1]
     ]
     * */
    public List<List<Integer>> pascalTriangle(int numRows) {
        List<List<Integer>> result = new ArrayList<>();

        // First base case; if user requests zero rows, they get zero rows.
        if (numRows == 0) {
            return result;
        }

        // Second base case; first row is always [1].
        result.add(Arrays.asList(1));

        for (int i = 1; i < numRows; i ++) {
            List<Integer> cur = new ArrayList<>();
            List<Integer> prev = result.get(i - 1);

            // The first row element is always 1.
            cur.add(1);

            // Each triangle element (other than the first and last of each row)
            // is equal to the sum of the elements above-and-to-the-left and
            // above-and-to-the-right.
            for (int j = 1; j < i; j ++) {
                cur.add(prev.get(j - 1) + prev.get(j));
            }

            // The last row element is always 1.
            cur.add(1);

            result.add(cur);
        }
        return result;
    }
    /**
     * LC119 pascal triangle 2，同上要求
     * 给定一个行号，返回该行的三角形数
     * */
    public List<Integer> pascalTriangle2(int idx) {
        List<Integer> result = new ArrayList<>();
        if (idx < 0) return result;
        for (int i = 0; i < idx + 1; i ++) {
            result.add(0, 1);
            for (int j = 1; j < result.size() - 1; j ++) {
                result.set(j, result.get(j) + result.get(j + 1));
            }
        }
        return result;
    }
    /**
     * LC120 triangle
     * 给定一个List[List[Int]]的三角形, 找到顶到底的通路使得和最小
     * */
    public int minTotal(List<List<Integer>> triangle) {
        int[] arr = new int[triangle.size() + 1];
        for(int i = triangle.size() - 1; i >= 0; i --){
            for(int j = 0; j < triangle.get(i).size(); j ++){
                arr[j] = Math.min(arr[j], arr[j + 1]) + triangle.get(i).get(j);
            }
        }
        return arr[0];
    }
    /**
     * LC121 Best Time to Buy and Sell Stock
     * 给定一个array，arr[0]表示第一天的股票价格...以此类推
     * 在这个array长度的天数中，买股票，之后再卖出去，要求收益最大，求收益
     * Input: [7,1,5,3,6,4]
     Output: 5
     Explanation: Buy on day 2 (price = 1) and sell on day 5 (price = 6), profit = 6-1 = 5.
     Not 7-1 = 6, as selling price needs to be larger than buying price.

     Input: [7,6,4,3,1]
     Output: 0
     Explanation: In this case, no transaction is done, i.e. max profit = 0.
     time O(n), space O(1)
     * */
    public int maxProfit(int prices[]) {
        int minprice = Integer.MAX_VALUE;
        int maxprofit = 0;
        for (int i = 0; i < prices.length; i++) {
            if (prices[i] < minprice)
                minprice = prices[i];
            else if (prices[i] - minprice > maxprofit)
                maxprofit = prices[i] - minprice;
        }
        return maxprofit;
    }
    /**
     * LC122 Best time to buy and sell stock 2
     * 基本同上。但是这次你可以买卖多次。但是你只能卖掉再买，不可以同时持有很多股
     * Input: [7,1,5,3,6,4]
     Output: 7
     Explanation: Buy on day 2 (price = 1) and sell on day 3 (price = 5), profit = 5-1 = 4.
     Then buy on day 4 (price = 3) and sell on day 5 (price = 6), profit = 6-3 = 3.

     Input: [1,2,3,4,5]
     Output: 4
     Explanation: Buy on day 1 (price = 1) and sell on day 5 (price = 5), profit = 5-1 = 4.
     Note that you cannot buy on day 1, buy on day 2 and sell them later, as you are
     engaging multiple transactions at the same time. You must sell before buying again.

     Input: [7,6,4,3,1]
     Output: 0
     Explanation: In this case, no transaction is done, i.e. max profit = 0.
     time O(n), space O(1)
     * */
    public int maxProfit2(int[] prices) {
        if(prices == null || prices.length == 0) return 0;
        int i = 0;
        int valley = prices[0];
        int peak = prices[0];
        int maxprofit = 0;
        while (i < prices.length - 1) {
            while (i < prices.length - 1 && prices[i] >= prices[i + 1]) i++;
            valley = prices[i];
            while (i < prices.length - 1 && prices[i] <= prices[i + 1]) i++;
            peak = prices[i];
            maxprofit += peak - valley;
        }
        return maxprofit;
    }
    /**
     * LC123 Best time to buy and sell stock3
     * 基本要求同上，但是最多只能做两次买卖
     * */
    public int maxProfit3(int[] prices) {
        int sell1 = 0, sell2 = 0, buy1 = Integer.MIN_VALUE, buy2 = Integer.MIN_VALUE;
        for (int i = 0; i < prices.length; i ++) {
            buy1 = Math.max(buy1, -prices[i]);  // 刚开始假设没钱，需要借钱来买，所以是支出，也就是负值
            sell1 = Math.max(sell1, buy1 + prices[i]);  // 第一次卖出时，我们所剩的就是 price[i]-buy1绝对值，也即两者相加
            buy2 = Math.max(buy2, sell1 - prices[i]);  // 第二次买，就从上一次卖掉后的所得里减去当日价格
            sell2 = Math.max(sell2, buy2 + prices[i]);  // 第二次卖就是，上一次买入后剩的钱buy2, 和当日卖掉所得的price[i]之和
        }
        return sell2;
    }
    /**
     * LC188 Best time to buy and sell stock4
     * 基本要求同上，但是要求最多只能做k次买卖
     * */
    //buy[i][k]  ith day k transaction buy stock and maximum profit
    //sell[i][k] ith day k transaction sell stock at hand and maximum profit
    public int maxProfit4(int k, int[] prices) {
        if(k > prices.length / 2) return maxP(prices);
        int[][] buy = new int[prices.length][k + 1];
        int[][] sell = new int[prices.length][k + 1];
        buy[0][0] = - prices[0];
        for(int i = 1; i < prices.length; i ++) buy[i][0] = Math.max(buy[i - 1][0], - prices[i]);
        for(int j = 1; j <= k; j ++) buy[0][j] = - prices[0];
        for(int i = 1; i < prices.length; i ++){
            for(int j = 1; j <= k; j ++){
                buy[i][j] = Math.max(sell[i - 1][j] - prices[i], buy[i - 1][j]);
                sell[i][j] = Math.max(buy[i - 1][j - 1] + prices[i], sell[i - 1][j]);
            }
        }
        return Math.max(buy[prices.length - 1][k], sell[prices.length - 1][k]);
    }
    public int maxP(int[] prices){
        int res = 0;
        for(int i = 0; i < prices.length; i ++){
            if(i > 0 && prices[i] > prices[i - 1]){
                res += prices[i] - prices[i - 1];
            }
        }
        return res;
    }
    /**
     * LC125 valid palindrome
     * 验证一个字符串是否是palindrome，忽略大小写，并且只验证字母的部分
     * */
    public boolean isPalindrome(String s) {
        if (s.isEmpty()) return true;
        int left = 0, right = s.length() - 1;
        while(left < right) {
            if (!Character.isLetterOrDigit(s.charAt(left))) {
                left++;
            } else if(!Character.isLetterOrDigit(s.charAt(right))) {
                right--;
            } else if (Character.toLowerCase(s.charAt(left ++)) != Character.toLowerCase(s.charAt(right --))){
                return false;
            }
        }
        return true;
    }
    @Test
    public void isPalindromeTest(){
        String s = ",.";
        System.out.println(isPalindrome(s));
    }
}
class TreeLinkNode{
    TreeLinkNode left;
    TreeLinkNode right;
    TreeLinkNode next;
    int val;
    public TreeLinkNode(int val){
        this.val = val;
    }
}