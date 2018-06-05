package utils.algorithm;

import org.junit.Test;

import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

import static utils.algorithm.C3.findMidListNode;

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
     /** 验证一个字符串是否是严格的palindrome，不忽略大小写和其他字符，严格地验证
     * */
    public boolean isPalindrome1(String s){
        if (s.isEmpty()) return true;
        int left = 0, right = s.length() - 1;
        while (left < right) {
            if (s.charAt(left ++) != s.charAt(right --)) return false;
        }
        return true;
    }
    @Test
    public void isPalindromeTest(){
        String s = ",.";
        System.out.println(isPalindrome(s));
    }
    /**
     * LC127 word Ladder
     * 从一个单词变到另一个单词，每次只能变一个字母，每次变得结果都要在WordList中存在，所以要变多少步才能变过去
     * Input:
     beginWord = "hit",
     endWord = "cog",
     wordList = ["hot","dot","dog","lot","log","cog"]

     Output: 5

     Explanation: As one shortest transformation is "hit" -> "hot" -> "dot" -> "dog" -> "cog",
     return its length 5.
     * */
    public int ladderLength(String beginWord, String endWord, List<String> wordList) {
        Queue<String> queue = new LinkedList<>();
        queue.offer(beginWord);
        queue.offer(null);
        Set<String> dict = new HashSet<>(wordList);
        Set<String> visited = new HashSet<>();
        visited.add(beginWord);
        if(!dict.contains(endWord)) return 0;
        int level = 1;
        while (! queue.isEmpty()) {
            String s = queue.poll();
            if(s != null) {
                for(int i = 0; i < s.length(); i++) {
                    char[] chars = s.toCharArray();
                    for(char c = 'a'; c <= 'z'; c ++) {
                        chars[i] = c;
                        String word = new String(chars);
                        if(word.equals(endWord)) return level + 1;
                        if(dict.contains(word) && !visited.contains(word)) {
                            queue.add(word);
                            visited.add(word);
                        }
                    }
                }
            } else {
                level++;
                if(!queue.isEmpty()) {
                    queue.add(null);
                }
            }
        }
        return 0;
    }

    /**
     * LC128 longest consecutive sequence
     * */
    public int longestConsecutive(int[] arr) {
        if (arr == null || arr.length == 0) return 0;

        Arrays.sort(arr);

        int longest = 1;
        int cur = 1;

        for (int i = 1; i < arr.length; i ++) {
            if (arr[i] != arr[i - 1]) {
                if (arr[i] == arr[i - 1] + 1) {
                    cur += 1;
                }
                else {
                    longest = Math.max(longest, cur);
                    cur = 1;
                }
            }
        }

        return Math.max(longest, cur);
    }
    /**
     * LC129 Sum root to leaf numbers
     * 将根到叶的所有可能的通路上的数字组成一个数，将这些数求和
     Input: [1,2,3]
      1
     / \
     2   3
     Output: 25
     Explanation:
     The root-to-leaf path 1->2 represents the number 12.
     The root-to-leaf path 1->3 represents the number 13.
     Therefore, sum = 12 + 13 = 25.
     * */
    public int sumNumbers(TreeNode root) {
        return sum(root, 0);
    }
    public int sum(TreeNode root, int s){
        if (root == null) return 0;
        if (root.right == null && root.left == null) return s * 10 + root.value;
        return sum(root.left, s * 10 + root.value) + sum(root.right, s * 10 + root.value);
    }
    /**
     * LC130 surrounded region
     Example:

     X X X X
     X O O X
     X X O X
     X O X X
     After running your function, the board should be:

     X X X X
     X X X X
     X X X X
     X O X X
     将矩阵中的字母O全部换成X，但是边界上的不行。有点类似于围棋中被吃掉的部分才能去掉，但是连接边界的点或一片不能去掉
     * */
    public void solve(char[][] board){
        if (board == null || board.length == 0) return;
        int rows = board.length, cols = board[0].length;
        for (int i = 0; i < rows; i ++) {
            for (int j = 0; j < cols; j ++) {
                if ((i == 0 || i == rows - 1 || j == 0 || j == cols - 1) && board[i][j] == 'O') {
                    Queue<Point> queue = new LinkedList<>();
                    board[i][j] = 'B';
                    queue.offer(new Point(i, j));
                    while (! queue.isEmpty()) {
                        Point cur = queue.poll();
                        validAndPut(board, queue, cur);
                    }
                }
            }
        }
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++) {
                if (board[i][j] == 'B')
                    board[i][j] = 'O';
                else if (board[i][j] == 'O')
                    board[i][j] = 'X';
            }
        }
    }
    private void validAndPut(char[][] board, Queue<Point> queue, Point cur){
        int rows = board.length, cols = board[0].length;
        int x = cur.x - 1;
        int y = cur.y;
        if (x >= 0 && x < rows && y >= 0 && y < cols && board[x][y] == 'O') {
            board[x][y] = 'B';
            queue.offer(new Point(x, y));
        }
        x = cur.x + 1;
        y = cur.y;
        if (x >= 0 && x < rows && y >= 0 && y < cols && board[x][y] == 'O') {
            board[x][y] = 'B';
            queue.offer(new Point(x, y));
        }
        x = cur.x;
        y = cur.y - 1;
        if (x >= 0 && x < rows && y >= 0 && y < cols && board[x][y] == 'O') {
            board[x][y] = 'B';
            queue.offer(new Point(x, y));
        }
        x = cur.x;
        y = cur.y + 1;
        if (x >= 0 && x < rows && y >= 0 && y < cols && board[x][y] == 'O') {
            board[x][y] = 'B';
            queue.offer(new Point(x, y));
        }
    }
    public static void solve1(char[][] board) {
        if (board == null || board.length == 0)
            return;
        int rows = board.length, columns = board[0].length;
        int[][] direction = { { -1, 0 }, { 1, 0 }, { 0, 1 }, { 0, -1 } };
        for (int i = 0; i < rows; i ++)
            for (int j = 0; j < columns; j ++) {
                if ((i == 0 || i == rows - 1 || j == 0 || j == columns - 1) && board[i][j] == 'O') {
                    Queue<Point> queue = new LinkedList<>();
                    board[i][j] = 'B';
                    queue.offer(new Point(i, j));
                    while (! queue.isEmpty()) {
                        Point point = queue.poll();
                        for (int k = 0; k < 4; k++) {
                            int x = direction[k][0] + point.x;
                            int y = direction[k][1] + point.y;
                            if (x >= 0 && x < rows && y >= 0 && y < columns && board[x][y] == 'O') {
                                board[x][y] = 'B';
                                queue.offer(new Point(x, y));
                            }
                        }
                    }
                }
            }
        for (int i = 0; i < rows; i++){
            for (int j = 0; j < columns; j++) {
                if (board[i][j] == 'B')
                    board[i][j] = 'O';
                else if (board[i][j] == 'O')
                    board[i][j] = 'X';
            }
        }
    }
    /**
     * LC131 palindrome partitioning
     * 给定一个字符串，分隔这个字符串，使得每个部分都是一个palindrome, 列出所有的分法
     Input: "aab"
     Output:
     [
        ["aa","b"],
        ["a","a","b"]
     ]
     * */
    public List<List<String>> partition(String s) {
        List<List<String>> result = new ArrayList<>();
        if(s.length() == 0) return result;
        partitionHelper(result, new ArrayList<>(), s);
        return result;
    }
    private void partitionHelper(List<List<String>> result, List<String> cur, String s){
        if(s.isEmpty()){
            result.add(new ArrayList<>(cur));
            return;
        }
        for(int i = 0; i < s.length(); i ++){
            if(isPalindrome1(s.substring(0, i + 1))){
                cur.add(s.substring(0, i + 1));
                partitionHelper(result, cur, s.substring(i + 1));
                cur.remove(cur.size() - 1);
            }
        }
    }
    /**
     * LC133 clone graph
     * */
    public UndirectedGraphNode cloneGraph(UndirectedGraphNode node) {
        return clone(node, new HashMap<>());
    }
    private UndirectedGraphNode clone(UndirectedGraphNode node, Map<UndirectedGraphNode, UndirectedGraphNode> map) {
        if (node == null) return null;
        if (map.containsKey(node)) return map.get(node);
        UndirectedGraphNode clone = new UndirectedGraphNode(node.label);
        map.put(node, clone);
        for (UndirectedGraphNode neighbor : node.neighbors) {
            clone.neighbors.add(clone(neighbor, map));
        }
        return clone;
    }
    /**
     * LC134 Gas Station
     * N 座加油站位于环行线上，第i个加油站的油量是gas[i]. 有个车油箱无限大, 从加油站i开到加油站i+1, 需要耗费cost[i]的油量
     * 汽车开始无油，从某一个加油站开出，它是否能环绕路线一圈。如果能返回起始加油站的索引，如果不能返回-1
     * 假定：
     *  如果有解，解唯一
     *  gas和cost不空，且长度相同，并且没有负数元素
     * Input:
     gas  = [1,2,3,4,5]
     cost = [3,4,5,1,2]

     Output: 3

     Explanation:
     Start at station 3 (index 3) and fill up with 4 unit of gas. Your tank = 0 + 4 = 4
     Travel to station 4. Your tank = 4 - 1 + 5 = 8
     Travel to station 0. Your tank = 8 - 2 + 1 = 7
     Travel to station 1. Your tank = 7 - 3 + 2 = 6
     Travel to station 2. Your tank = 6 - 4 + 3 = 5
     Travel to station 3. The cost is 5. Your gas is just enough to travel back to station 3.
     Therefore, return 3 as the starting index.
     *
     * */
    public int canCompleteCircuit1(int[] gas, int[] cost) {
        int start = 0, total = 0, tank = 0;
        for (int i = 0; i < gas.length; i ++) {
            tank = tank + gas[i] - cost[i];
            if (tank < 0) {
                start = i + 1;
                total += tank;
                tank = 0;
            }
        }
        return total + tank < 0 ? -1 : start;
    }
    public int canCompleteCircuit(int[] gas, int[] cost){
        int sum = 0, result = 0, total = 0;
        for (int i = 0; i < gas.length; i ++) {
            sum += (gas[i] - cost[i]);
            if (sum < 0) {
                total += sum;
                sum = 0;
                result = i + 1;
            }
        }
        total += sum;
        return total < 0 ? -1 : result;
    }
    /**
     * LC137 Single Number2
     * 一个数组中，每个数都重复3遍，只有一个数是unique的，找到该数
     * */
    public int singleNumber(int[] arr) {
        int ones = 0, twos = 0;
        for(int i = 0; i < arr.length; i++){
            ones = (ones ^ arr[i]) & ~twos;
            twos = (twos ^ arr[i]) & ~ones;
        }
        return ones;
    }
    /**
     * LC147 Insertion sort list 插入法排序list
     * */
    public ListNode insertionSortList(ListNode head) {
        if(head == null || head.next == null) return head;
        ListNode dummy = new ListNode(0); //new starter of the sorted list
        ListNode cur = head; //the node will be inserted
        ListNode pre = dummy; //insert node between pre and pre.next
        ListNode next = null; //the next node will be inserted
        //not the end of input list
        while( cur != null ){
            next = cur.next;
            //find the right place to insert
            while( pre.next != null && pre.next.value < cur.value ){
                pre = pre.next;
            }
            //insert between pre and pre.next
            cur.next = pre.next;
            pre.next = cur;
            cur = next;
            pre = dummy;
        }
        return dummy.next;
    }
    /**
     * LC148 merge sort list
     * */
    public ListNode mergeSort(ListNode head) {
        if (head == null || head.next == null) return head;
        // step 1. cut the list to two halves
        ListNode mid = findMidListNode(head);
        ListNode left = head;
        ListNode right = mid.next;
        mid.next = null;
        // step 2. sort each half
        ListNode l1 = mergeSort(left);
        ListNode l2 = mergeSort(right);
        // step 3. merge l1 and l2
        return merge(l1, l2);
    }
    ListNode merge(ListNode one, ListNode two) {
        ListNode dummy = new ListNode(0), cur = dummy;
        while (one != null && two != null) {
            if (one.value < two.value) {
                cur.next = one;
                one = one.next;
            } else {
                cur.next = two;
                two = two.next;
            }
            cur = cur.next;
        }
        if (one != null) cur.next = one;
        if (two != null) cur.next = two;
        return dummy.next;
    }
    /**
     * LC149 max points on a line
     * */
//    public int maxPoints(Point[] points) {
//        // assume: points is not null, and points.length >= 2
//        // record the maximum number of points on the same line
//        int result = 0;
//        // we use each pair of points to form a line
//        for (int i = 0; i < points.length; i ++) {
//            // any line can be represented by a point and a slope
//            // we take the point as seed and try to find all possible slopes
//            Point seed = points[i];
//            // record the points with same <x, y>
//            int same = 1;
//            // record the points with same x, for the special case of infinite slope
//            // infinite slope
//            int sameX = 0;
//            // record the maximum number of points on the same line
//            // crossing the seed point
//            int most = 0;
//            // a map with all possible slopes
//            Map<Double, Integer> cnt = new HashMap<>();  // 使用的是点斜式的直线表示法
//            // 第一个for循环确定一个点，以这个点出发连线别的点，如果发现斜率相等
//            // 那么就是同一条直线
//            for (int j = 0; j < points.length; j ++) {
//                if (i == j) {
//                    continue;
//                }
//                Point tmp = points[j];
//                if (tmp.x == seed.x && tmp.y == seed.y) {
//                    // handle the points with same <x, y>
//                    same ++;
//                } else if (tmp.x == seed.x) {
//                    // handle the points with same x
//                    sameX ++;
//                } else {
//                    // otherwise, just calculate the slope and increment the counter
//                    // for the calculated slope
//                    double slope = ((tmp.y - seed.y) + 0.0) / (tmp.x - seed.x);
//                    if (! cnt.containsKey(slope)) {
//                        cnt.put(slope, 1);
//                    } else {
//                        cnt.put(slope, cnt.get(slope) + 1);
//                    }
//                    most = Math.max(most, cnt.get(slope));
//                }
//            }
//            most = Math.max(most, sameX) + same;  // 总共点斜式的直线就两种情况，斜率为0和不为0
//            System.out.println("here: " + most);
//            result = Math.max(result, most);
//        }
//        return result;
//
//    }
//    @Test
//    public void maxPointsTest(){
//        Point[] points = new Point[3];
//        points[0] = new Point(0, 0);
//        points[1] = new Point(94911151,94911150);
//        points[2] = new Point(94911152,94911151);
//        System.out.println(maxPoints(points));
//        System.out.println((double)(points[1].y / points[1].x) == (double)(points[2].y / points[2].x));
////        System.out.println((double)(points[2].y / points[2].x));
//    }
    /**
     * LC150 evaluate reverse polish notation
     * */
    public int evalRPN(String[] arr) {
        Deque<Integer> stack = new LinkedList<>();
        for (String s : arr) {
            switch (s) {
                case "+":
                    stack.offerFirst(stack.pollFirst() + stack.pollFirst());
                    break;

                case "-":
                    stack.offerFirst(-stack.pollFirst() + stack.pollFirst());
                    break;

                case "*":
                    stack.offerFirst(stack.pollFirst() * stack.pollFirst());
                    break;

                case "/":
                    int n1 = stack.pollFirst(), n2 = stack.pollFirst();
                    stack.offerFirst(n2 / n1);
                    break;
                default:
                    stack.offerFirst(Integer.parseInt(s));
            }
        }
        return stack.pollFirst();
    }
    /**
     * LC152 Maximum product subarray
     * Example 1:

     Input: [2,3,-2,4]
     Output: 6
     Explanation: [2,3] has the largest product 6.
     Example 2:

     Input: [-2,0,-1]
     Output: 0
     Explanation: The result cannot be 2, because [-2,-1] is not a subarray.
     * */
    public int maxProduct(int[] arr) {
        if (arr == null || arr.length == 0) return 0;
        int result = arr[0], min = arr[0], max = arr[0];
        for (int i = 1; i < arr.length; i ++) {
            if (arr[i] >= 0) {
                max = Math.max(arr[i], max * arr[i]);
                min = Math.min(arr[i], min * arr[i]);
            } else {
                int tmp = max;
                max = Math.max(arr[i], min * arr[i]);
                min = Math.min(arr[i], tmp * arr[i]);
            }
            result = Math.max(result, max);
        }
        return result;
    }
    @Test
    public void maxProductTest(){
//        System.out.println(maxProduct(new int[]{2, 3, -2, 4}));
        System.out.println(maxProduct(new int[]{-1, -2, 1, 1, 4, 5, -3}));
//        System.out.println(maxProduct(new int[]{-2, 0, -1}));
//        System.out.println(maxProduct(new int[]{-4, -3}));
//        System.out.println(maxProduct(new int[]{0, 2}));
    }
    /**
     * LC153 find minimum in rotated sorted array
     * */
    public int findMin (int[] arr) {
        if (arr == null || arr.length == 0) return -1;
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            if (arr[left] < arr[right]) {
                return arr[left];
            }
            int mid = left + (right - left) / 2;
            if (arr[mid] >= arr[right]) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }
        return arr[left];
    }
    /**
     * LC154 find minimum in rotated sorted array2
     * 有可能有重复的数字
     * */
    public int findMin2(int[] arr){
        if (arr == null || arr.length == 0) return -1;
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] > arr[right]) {
                left = mid + 1;
            } else if (arr[mid] < arr[right]) {
                right = mid;
            } else {
                right --;
            }
        }
        return arr[left];
    }
    @Test
    public void findMinTest(){
        System.out.println(findMin2(new int[]{3, 4, 5, 1, 2}));
        System.out.println(findMin2(new int[]{3, 1, 1}));
    }
    /**
     * LC162 find peak element
     * Example 1:
     Input: nums = [1,2,3,1]
     Output: 2
     Explanation: 3 is a peak element and your function should return the index number 2.

     Example 2:
     Input: nums = [1,2,1,3,5,6,4]
     Output: 1 or 5
     Explanation: Your function can return either index number 1 where the peak element is 2,
     or index number 5 where the peak element is 6.
     * */
    public int findPeakElement(int[] arr) {
        int left = 0, right = arr.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] > arr[mid + 1])
                right = mid;
            else
                left = mid + 1;
        }
        return left;
    }
    /**
     * LC165 compare version numbers
     * Example 1:

     Input: version1 = "0.1", version2 = "1.1"
     Output: -1
     Example 2:

     Input: version1 = "1.0.1", version2 = "1"
     Output: 1
     Example 3:

     Input: version1 = "7.5.2.4", version2 = "7.5.3"
     Output: -1
     * */
    public int compareVersion(String version1, String version2) {
        int num1 = 0,num2 = 0;
        int len1 = version1.length(), len2 = version2.length();
        int i = 0, j = 0;
        while(i < len1 || j < len2) {
            num1 = 0;
            num2 = 0;
            while(i < len1 && version1.charAt(i) != '.') {
                num1 = num1 * 10 + version1.charAt(i ++) - '0';

            }
            while(j < len2 && version2.charAt(j) != '.') {
                num2 = num2 * 10 + version2.charAt(j ++) - '0';

            }
            if(num1 > num2) {
                return 1;
            } else if(num1<num2){
                return -1;
            } else {
                i ++;
                j ++;
            }
        }
        return 0;
    }
    /**
     * LC166 Fraction to recurring decimal
     * */
    public String fractionToDecimal(int numerator, int denominator) {
        boolean isNegative = numerator < 0 && denominator > 0 || numerator > 0 && denominator < 0;
        long numeratorL = Math.abs((long) numerator);
        long denominatorL = Math.abs((long) denominator);
        Map<Long, Integer> remains = new HashMap<>();
        StringBuilder sb = new StringBuilder();
        long quotient = numeratorL / denominatorL;
        sb.append(quotient);
        numeratorL %= denominatorL;
        if (numeratorL != 0) {
            sb.append(".");
        }
        int quotientIndex = 0;
        while (numeratorL != 0) {
            numeratorL *= 10;
            quotient = Math.abs(numeratorL / denominatorL);
            if (!remains.containsKey(numeratorL)) {
                sb.append(quotient);
                remains.put(numeratorL, quotientIndex ++);
            } else {
                int firstIndex = 1 + remains.get(numeratorL) + sb.indexOf(".");
                sb.insert(firstIndex, '(');
                sb.append(")");
                break;
            }
            numeratorL %= denominatorL;
        }
        if (isNegative) {
            sb.insert(0, "-");
        }
        return sb.toString();
    }
    /**
     * LC168 Excel Sheet column number to letter
     * */
    public String convertToTitle(int n) {
        StringBuilder sb = new StringBuilder();
        while(n > 0){
            n --;
            sb.insert(0, (char)('A' + n % 26));
            n /= 26;
        }
        return sb.toString();
    }
    /**
     * LC171 Excel Sheet column letter to num
     * */
    public int titleToNumber(String s) {
        int result = 0;
        for (int i = 0; i < s.length(); i++){
            result = result * 26 + (s.charAt(i) - 'A' + 1);
        }
        return result;
    }
    /**
     * LC172 Factorial trailing zeroes
     * 计算一个数得阶乘的结果有多少0，0基本都是由5*2产生的，所以我们需要数里面总共有多少个5
     * */
    public int trailingZeroes(int n) {
        return n == 0 ? 0 : n / 5 + trailingZeroes(n / 5);
    }

    /**
     *          15
     *         /  \
     *       12   19
     *       /\   / \
     *     10 14 16 20
     * */
    public TreeNode BST(){
        TreeNode root = new TreeNode(15);
        root.left = new TreeNode(12);
        root.right = new TreeNode(19);
        root.left.left = new TreeNode(10);
        root.left.right = new TreeNode(14);
        root.right.left = new TreeNode(16);
        root.right.right = new TreeNode(20);
        return root;
    }
    @Test
    public void BSTIteratorTest(){
        TreeNode root = BST();
        BSTIterator it = new BSTIterator(root);
        while (it.hasNext()) {
            System.out.println(it.next());
        }
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
class UndirectedGraphNode {
    int label;
    List<UndirectedGraphNode> neighbors;
    public UndirectedGraphNode(int x) {
        label = x;
        neighbors = new ArrayList<UndirectedGraphNode>();
    }
}
/**
 * LC173 BST Iterator
 * should run in O(1) time and O(h) memory, h is the height of the tree
 * inOrder will work
 * */
class BSTIterator {
    private Deque<TreeNode> stack;
    public BSTIterator(TreeNode root) {
        stack = new LinkedList<>();
        TreeNode cur = root;
        while(cur != null){
            stack.offerFirst(cur);
            if(cur.left != null) {
                cur = cur.left;
            } else {
                break;
            }
        }
    }

    /** @return whether we have a next smallest number */
    public boolean hasNext() {
        return ! stack.isEmpty();
    }

    /** @return the next smallest number */
    public int next() {
        TreeNode node = stack.pollFirst();
        TreeNode cur = node;
        // traversal right branch
        if(cur.right != null){
            cur = cur.right;
            while(cur != null){
                stack.offerFirst(cur);
                if(cur.left != null)
                    cur = cur.left;
                else
                    break;
            }
        }
        return node.value;
    }
}