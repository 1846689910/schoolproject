package utils.algorithm;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

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

}
