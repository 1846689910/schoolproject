package utils.algorithm;

import java.util.ArrayList;
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

}
