package utils.algorithm;

import org.junit.Test;

import java.util.*;

public class C4 {
    /** pre-order traverse binary tree */
    public static List<Integer> preOrderTraverseRe(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        result.add(root.value);
        result.addAll(preOrderTraverseRe(root.left));
        result.addAll(preOrderTraverseRe(root.right));
        return result;
    }
    public static List<Integer> preOrderTraverseIt(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Deque<TreeNode> stack = new LinkedList<>();
        stack.offerFirst(root);
        while (! stack.isEmpty()) {
            TreeNode cur = stack.pollFirst();
            if (cur.right != null) stack.offerFirst(cur.right);
            if (cur.left != null) stack.offerFirst(cur.left);
            result.add(cur.value);
        }
        return result;
    }
    @Test
    public void preOrderTraverseTest(){
        System.out.println(preOrderTraverseRe(TreeNode.binaryTree1()));
        System.out.println(preOrderTraverseIt(TreeNode.binaryTree1()));
    }

    /** in order traver */
    public static List<Integer> inOrderTraverseRe(TreeNode root){
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        result.addAll(inOrderTraverseRe(root.left));
        result.add(root.value);
        result.addAll(inOrderTraverseRe(root.right));
        return result;
    }
    public static List<Integer> inOrderTraverseIt(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode cur = root;
        while (cur != null || ! stack.isEmpty()) {
            if (cur != null) {
                stack.offerFirst(cur);
                cur = cur.left;
            } else {
                cur = stack.pollFirst();
                result.add(cur.value);
                cur = cur.right;
            }
        }
        return result;
    }
    @Test
    public void inOrderTraverseTest(){
        System.out.println(inOrderTraverseRe(TreeNode.binaryTree1()));
        System.out.println(inOrderTraverseIt(TreeNode.binaryTree1()));
    }
    /** post order traverse */
    public static List<Integer> postOrderTraverseRe(TreeNode root){
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        result.addAll(postOrderTraverseRe(root.left));
        result.addAll(postOrderTraverseRe(root.right));
        result.add(root.value);
        return result;
    }
    public static List<Integer> postOrderTraverseIt(TreeNode root){
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode prev = null;
        stack.offerFirst(root);
        while (! stack.isEmpty()) {
            TreeNode cur = stack.peekFirst();
            if (prev == null || prev.left == cur || prev.right == cur) {
                if (cur.left != null) {
                    stack.offerFirst(cur.left);
                } else if (cur.right != null) {
                    stack.offerFirst(cur.right);
                } else {
                    stack.pollFirst();
                    result.add(cur.value);
                }
            } else if (prev == cur.right || prev == cur.left && cur.right == null) {
                stack.pollFirst();
                result.add(cur.value);
            } else {
                stack.offerFirst(cur.right);
            }
            prev = cur;
        }
        return result;
    }
    @Test
    public void postOrderTreaverseTest(){
        System.out.println(postOrderTraverseRe(TreeNode.binaryTree1()));
        System.out.println(postOrderTraverseIt(TreeNode.binaryTree1()));
    }
    /** level order traverse */
    public static List<List<Integer>> levelOrderTraverse(TreeNode root){
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (! queue.isEmpty()) {
            int size = queue.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i ++) {
                TreeNode cur = queue.poll();
                if (cur.left != null) queue.offer(cur.left);
                if (cur.right != null) queue.offer(cur.right);
                level.add(cur.value);
            }
            result.add(level);
        }
        return result;
    }
    @Test
    public void levelOrderTraverseTest(){
        System.out.println(levelOrderTraverse(TreeNode.binaryTree1()));
    }
    /** zigzag order traverse */
    public static List<List<Integer>> zigzagOrderTraverse(TreeNode root) {
        List<List<Integer>> result = new ArrayList<>();
        if (root == null) return result;
        Deque<TreeNode> deque = new LinkedList<>();
        deque.offerFirst(root);
        boolean isEvenLevel = true;
        while (! deque.isEmpty()) {
            int size = deque.size();
            List<Integer> level = new ArrayList<>();
            for (int i = 0; i < size; i ++) {
                TreeNode cur;
                if (isEvenLevel) {
                    cur = deque.pollFirst();
                    if (cur.right != null) deque.offerLast(cur.right);
                    if (cur.left != null) deque.offerLast(cur.left);
                } else {
                    cur = deque.pollLast();
                    if (cur.left != null) deque.offerFirst(cur.left);
                    if (cur.right != null) deque.offerFirst(cur.right);
                }
                level.add(cur.value);
            }
            isEvenLevel = ! isEvenLevel;
            result.add(level);
        }
        return result;
    }
    @Test
    public void zigzagOrderTraverseTest(){
        System.out.println(zigzagOrderTraverse(TreeNode.binaryTree1()));
    }

    /** convert a binary tree to Doubly Linked List (DLL) */
    static TreeNode prev = null;
    static TreeNode head = null;
    public static TreeNode binaryTree2DLLRe(TreeNode root){
        if (root == null) return null;
        binaryTree2DLLRe(root.left);
        if (prev == null) {
            head = root;
        } else {
            root.left = prev;
            prev.right = root;
        }
        prev = root;
        binaryTree2DLLRe(root.right);
        return head;
    }
    public static TreeNode binaryTree2DLLIt(TreeNode root) {
        if (root == null) return null;
        TreeNode dummy = new TreeNode(0);
        TreeNode prev = dummy;
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode cur = root;
        while (cur != null || ! stack.isEmpty()) {
            if (cur != null) {
                stack.offerFirst(cur);
                cur = cur.left;
            } else {
                cur = stack.pollFirst();
                cur.left = prev;
                prev.right = cur;
                prev = prev.right;
                cur =  cur.right;
            }
        }
        return dummy.right;
    }



}
class TreeNode{
    TreeNode left;
    TreeNode right;
    int value;
    public TreeNode(int value){
        this.value = value;
        this.left = this.right = null;
    }
    /**
     *          5
     *         / \
     *        7   9
     *       /\   /\
     *     10 11 12 14
     * */
    public static TreeNode binaryTree1(){
        TreeNode root = new TreeNode(5);
        root.left = new TreeNode(7);
        root.right = new TreeNode(9);
        root.left.left = new TreeNode(10);
        root.left.right = new TreeNode(11);
        root.right.left = new TreeNode(12);
        root.right.right = new TreeNode(14);
        return root;
    }
}

