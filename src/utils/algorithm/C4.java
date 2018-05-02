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
    /** binary tree is balanced */
    public static boolean isBalanced(TreeNode root){
        return getHeight(root) != -1;
    }
    public static int getHeight(TreeNode root){
        if (root == null) return 0;
        int leftHeight = getHeight(root.left);
        int rightHeight = getHeight(root.right);
        if (leftHeight == -1 || rightHeight == -1 || Math.abs(leftHeight - rightHeight) > 1) return -1;
        return Math.max(leftHeight, rightHeight) + 1;
    }
    @Test
    public void isBalancedTest(){
        TreeNode root = TreeNode.binaryTree1();
        System.out.println(isBalanced(root));
    }
    /** binary Tree is symmetric */
    public static boolean isSymmetric(TreeNode root){
        if (root == null) return true;
        return isSymmetricHelper(root.left, root.right);
    }
    private static boolean isSymmetricHelper(TreeNode left, TreeNode right){
        if (left == null && right == null) {
            return true;
        } else if (left == null || right == null) {
            return false;
        } else if (left.value != right.value) {
            return false;
        }
        return isSymmetricHelper(left.left, right.right) && isSymmetricHelper(left.right, right.left);
    }
    @Test
    public void isSymmetricTest(){

    }
    /** binary tree is tweaked identical */
    public static boolean isTweakedIdentical(TreeNode one, TreeNode two){
        if (one == null && two == null) {
            return true;
        } else if (one == null || two == null) {
            return false;
        } else if (one.value != two.value) {
            return false;
        }
        return isTweakedIdentical(one.left, two.right) && isTweakedIdentical(one.right, two.left) ||
                isTweakedIdentical(one.left, two.left) && isTweakedIdentical(one.right, two.right);
    }
    @Test
    public void isTweakedIdenticalTest(){

    }
    /** is binary search tree */
    public static boolean isBST(TreeNode root){
        return isBSTHelper(root, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    private static boolean isBSTHelper(TreeNode root, int min, int max){
        if (root == null) {
            return true;
        } else if (root.value < min || root.value > max) {
            return false;
        }
        return isBSTHelper(root.left, min, root.value - 1) && isBSTHelper(root.right, root.value + 1, max);
    }
    @Test
    public void isBSTTest(){
        TreeNode root = TreeNode.BST();
        System.out.println(isBST(root));
    }
    /** can find two nodes is BST whose sum is equal to target */
    public static boolean canFindSumToTarget(TreeNode root, int target){
        if (root == null) return false;
        boolean done1 = false, done2 = false;
        int val1 = 0, val2 = 0;
        TreeNode cur1 = root, cur2 = root;
        Deque<TreeNode> stack1 = new LinkedList<>(), stack2 = new LinkedList<>();
        while (true) {
            while (! done1 && (cur1 != null || ! stack1.isEmpty())) {  // 左数负责向左到底拿到最小再回溯
                if (cur1 != null) {
                    stack1.offerFirst(cur1);
                    cur1 = cur1.left;
                } else {
                    cur1 = stack1.pollFirst();
                    val1 = cur1.value;
                    cur1 = cur1.right;
                    done1 = true;
                }
            }
            while (! done2 && (cur2 != null || ! stack2.isEmpty())) {  // 右数负责向右到底拿到最大再回溯
                if (cur2 != null) {
                    stack2.offerFirst(cur2);
                    cur2 = cur2.right;
                } else {
                    cur2 = stack2.pollFirst();
                    val2 = cur2.value;
                    cur2 = cur2.left;
                    done2 = true;
                }
            }
            if (val1 != val2 && (val1 + val2 == target)) {
                return true;
            } else if (val1 + val2 < target) {
                done1 = false;
            } else if (val1 + val2 > target) {
                done2 = false;
            }
            if (val1 >= val2) return false;
        }
    }
    @Test
    public void canFindSumToTargetTest(){
        TreeNode root = TreeNode.BST();
        System.out.println(canFindSumToTarget(root, 29));
        System.out.println(canFindSumToTarget(root, 56));
    }

    /** Binary tree is complete */
    public static boolean isComplete(TreeNode root) {
        if (root == null) return true;
        Queue<TreeNode> queue = new LinkedList<>();
        boolean isNull = false;
        queue.offer(root);
        while (! queue.isEmpty()) {
            TreeNode cur = queue.poll();
            if (cur.left == null) {
                isNull = true;
            } else if (isNull) {
                return false;
            } else {
                queue.offer(cur.left);
            }
            if (cur.right == null) {
                isNull = true;
            } else if (isNull) {
                return false;
            } else {
                queue.offer(cur.right);
            }
        }
        return true;
    }
    @Test
    public void isCompleteTest(){
        TreeNode root = TreeNode.BST();
        System.out.println(isComplete(root));
    }

    /** two nodes in binary tree are cousins */
    public static boolean areCousins(TreeNode root, TreeNode one, TreeNode two){
        if (root == null) return false;
        Queue<TreeNode> queue = new LinkedList<>();
        queue.offer(root);
        while (! queue.isEmpty()) {
            int count = 0;
            int size = queue.size();
            for (int i = 0; i < size; i ++) {
                TreeNode cur = queue.poll();
                if (cur == one || cur == two) count ++;
                if (count == 2) return true;
                if (cur.left != null && cur.right != null && ((cur.left == one && cur.right == two) || (cur.left == two && cur.right == one))) return false;
                if (cur.left != null) queue.offer(cur.left);
                if (cur.right != null) queue.offer(cur.right);
            }
            if (count == 1) return false;
        }
        return false;
    }
    @Test
    public void areCousinsTest(){
        TreeNode root = TreeNode.BST();
        System.out.println(areCousins(root, root.left.right, root.right.right));
    }

    /** get values in BST */
    public static List<Integer> getValuesInRange(TreeNode root, int min, int max){
        List<Integer> result = new ArrayList<>();
        getValuesInRangeHelper(root, min, max, result);
        return result;
    }
    private static void getValuesInRangeHelper(TreeNode root, int min, int max, List<Integer> result) {
        if (root == null) return;
        if (root.value > min) {
            getValuesInRangeHelper(root.left, min, max, result);
        }
        if (root.value >= min && root.value <= max) {
            result.add(root.value);
        }
        if (root.value < max){
            getValuesInRangeHelper(root.right, min, max, result);
        }
    }
    @Test
    public void getValuesInRangeTest(){
        TreeNode root = TreeNode.BST();
        System.out.println(getValuesInRange(root, 12, 16));
    }

    /** insert in BST */
    public static TreeNode insertInBSTRe(TreeNode root, int value){
        if (root == null) return new TreeNode(value);
        if (value < root.value) {
            root.left = insertInBSTRe(root.left, value);
        } else if (root.value < value) {
            root.right = insertInBSTRe(root.right, value);
        }
        return root;
    }
    public static TreeNode insertInBSTIt(TreeNode root, int value) {
        TreeNode node = new TreeNode(value);
        if (root == null) return node;
        TreeNode cur = root;
        while (cur.value != value) {
            if (value < cur.value) {
                if (cur.left != null) {
                    cur = cur.left;
                } else {
                    cur.left = node;
                    break;
                }
            } else {
                if (cur.right != null) {
                    cur = cur.right;
                } else {
                    cur.right = node;
                    break;
                }
            }
        }
        return root;
    }
    @Test
    public void insertInBSTTest(){
        TreeNode root = TreeNode.BST();
        TreeNode root1 = insertInBSTRe(root, 13);
        System.out.println(levelOrderTraverse(root1));
        System.out.println(root1.left.right.left.value);

        root = TreeNode.BST();
        root1 = insertInBSTIt(root, 13);
        System.out.println(levelOrderTraverse(root1));
        System.out.println(root1.left.right.left.value);
    }
    /** search in BST */
    public static TreeNode searchInBSTRe(TreeNode root, int value){
        if (root == null || root.value == value) return root;
        if (value < root.value) {
            return searchInBSTRe(root.left, value);
        } else {
            return searchInBSTRe(root.right, value);
        }
    }
    public static TreeNode searchInBSTIt(TreeNode root, int value) {
        TreeNode cur = root;
        while (cur != null && cur.value != value) {
            if (value < cur.value) {
                cur = cur.left;
            } else {
                cur = cur.right;
            }
        }
        return cur;
    }
    @Test
    public void searchInBSTTest(){
        TreeNode root = TreeNode.BST();
        System.out.println(searchInBSTIt(root, 20).value);
        System.out.println(searchInBSTRe(root, 10).value);
    }

    /** delete in BST */
    public static TreeNode removeInBST(TreeNode root, int value){
        if (root == null) return null;
        if (value < root.value) {
            root.left = removeInBST(root.left, value);
        } else if (root.value < value) {
            root.right = removeInBST(root.right, value);
        } else {
            if (root.left == null) {
                return root.right;
            } else if (root.right == null) {
                return root.left;
            } else if (root.right.left == null) {
                root.right.left = root.left;
                return root.right;
            } else {
                TreeNode node = deleteSmallest(root.right);
                node.left = root.left;
                node.right = root.right;
                return node;
            }
        }
        return root;
    }
    public static TreeNode deleteSmallest(TreeNode root) {
        while (root.left.left != null) {
            root = root.left;
        }
        TreeNode smallest = root.left;
        root.left = root.left.right;
        return smallest;
    }
    @Test
    public void removeInBSTTest(){
        TreeNode root = TreeNode.BST();
        System.out.println(levelOrderTraverse(root));
        TreeNode root1 = removeInBST(root, 12);
        System.out.println(levelOrderTraverse(root1));
    }

    /** remove nodes in BST which are not within [min, max]
     * or remove nodes in BST until only elements within [min, max] are left
     * */
    public static TreeNode removeInBSTOutRange(TreeNode root, int min, int max) {
        if (root == null) return null;
        root.left = removeInBSTOutRange(root.left, min, max);
        root.right = removeInBSTOutRange(root.right, min, max);
        if (root.value < min) {
            return root.right;
        } else if (root.value > max) {
            return root.left;
        }
        return root;
    }
    @Test
    public void removeInBSTOutRangeTest(){
        TreeNode root = TreeNode.BST();
        System.out.println(levelOrderTraverse(root));
        TreeNode root1 = removeInBSTOutRange(root, 12, 18);
        System.out.println(levelOrderTraverse(root1));
    }
    /** remove nodes in BST which has only one child node */
    public static TreeNode removeInBSTSingleChildNodes(TreeNode root){
        if (root == null) return null;
        root.left = removeInBSTSingleChildNodes(root.left);
        root.right = removeInBSTSingleChildNodes(root.right);
        if (root.left == null && root.right == null) {
            return root;
        } else if (root.left == null) {
            return root.right;
        } else {
            return root.left;
        }
    }
    @Test
    public void removeInBSTSingleChildNodesTest(){
        TreeNode root = TreeNode.BST();
        root.right.left = null;
        System.out.println(levelOrderTraverse(root));
        removeInBSTSingleChildNodes(root);
        System.out.println(levelOrderTraverse(root));
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
    /**
     *          15
     *         /  \
     *       12   19
     *       /\   / \
     *     10 14 16 20
     * */
    public static TreeNode BST(){
        TreeNode root = new TreeNode(15);
        root.left = new TreeNode(12);
        root.right = new TreeNode(19);
        root.left.left = new TreeNode(10);
        root.left.right = new TreeNode(14);
        root.right.left = new TreeNode(16);
        root.right.right = new TreeNode(20);
        return root;
    }


}

