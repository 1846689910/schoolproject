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
    public static List<Integer> preOrderTraverseIt1(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode cur = root;
        while (! stack.isEmpty() || cur != null) {
            if (cur != null) {
                stack.offerFirst(cur);
                result.add(cur.value);
                cur = cur.left;
            } else {
                cur = stack.pollFirst();
                cur = cur.right;
            }
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
    public static List<Integer> postOrderTraverseIt1(TreeNode root) {
        List<Integer> result = new ArrayList<>();
        if (root == null) return result;
        Deque<TreeNode> stack = new LinkedList<>();
        TreeNode cur = root;
        while (! stack.isEmpty() || cur != null) {
            if (cur != null) {
                stack.offerFirst(cur);
                result.add(cur.value);
                cur = cur.right;
            } else {
                cur = stack.pollFirst();
                cur = cur.left;
            }
        }
        Collections.reverse(result);
        return result;
    }

    @Test
    public void postOrderTreaverseTest(){
        System.out.println(postOrderTraverseRe(TreeNode.binaryTree1()));
        System.out.println(postOrderTraverseIt(TreeNode.binaryTree1()));
        System.out.println(postOrderTraverseIt1(TreeNode.binaryTree1()));
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

    /** closest number in BST */
    public static TreeNode closestInBST (TreeNode root, int value){
        TreeNode cur = root;
        while (root != null) {
            if (root.value == value) {
                return root;
            } else {
                if (Math.abs(root.value - value) < Math.abs(cur.value - value)) {
                    cur = root;
                }
                if (value < root.value) {
                    root = root.left;
                } else {
                    root = root.right;
                }
            }
        }
        return cur;
    }
    @Test
    public void closestInBSTTest(){
        TreeNode root = TreeNode.BST();
        System.out.println(levelOrderTraverse(root));
        System.out.println(closestInBST(root, 11).value);
    }

    /** largest smaller in BST */
    public static TreeNode largestSmallerInBST(TreeNode root, int value){
        TreeNode cur = new TreeNode(Integer.MIN_VALUE);
        while (root != null) {
            if (value <= root.value) {
                root = root.left;
            } else {
                cur = root;
                root = root.right;
            }
        }
        return cur;
    }
    @Test
    public void largestSmallerInBSTTest(){
        TreeNode root = TreeNode.BST();
        System.out.println(largestSmallerInBST(root, 11).value);
    }

    /** Maximum sum path */
    /** PLL */
    public static int maxSumPathPLL(TreeNode root){
        int[] max = new int[]{Integer.MIN_VALUE};
        maxSumPathPLLHelper(root, max);
        return max[0];
    }
    private static int maxSumPathPLLHelper(TreeNode root, int[] max) {
        if (root == null) return 0;
        int leftSum = maxSumPathPLLHelper(root.left, max);
        int rightSum = maxSumPathPLLHelper(root.right, max);
        if (root.left != null && root.right != null) {
            max[0] = Math.max(max[0], leftSum + rightSum + root.value);
            return Math.max(leftSum, rightSum) + root.value;
        }
        return root.left == null ? rightSum + root.value : leftSum + root.value;
    }
    /** PAA */
    public static int maxSumPathPAA(TreeNode root){
        int[] max = new int[]{Integer.MIN_VALUE};
        maxSumPathPAAHelper(root, max);
        return max[0];
    }
    private static int maxSumPathPAAHelper(TreeNode root, int[] max) {
        if (root == null) return 0;
        int leftSum = maxSumPathPAAHelper(root.left, max);
        int rightSum = maxSumPathPAAHelper(root.right, max);
        leftSum = Math.max(leftSum, 0);
        rightSum = Math.max(rightSum, 0);
        max[0] = Math.max(max[0], leftSum + rightSum + root.value);
        return Math.max(leftSum, rightSum) + root.value;
    }

    /** SRL */
    public static int maxSumPathSRL(TreeNode root){
        int[] max = new int[]{Integer.MIN_VALUE};
        maxSumPathSRLHelper(root, max);
        return max[0];
    }
    private static int maxSumPathSRLHelper(TreeNode root, int[] max) {
        if (root == null) return 0;
        int leftSum = maxSumPathSRLHelper(root.left, max);
        int rightSum = maxSumPathSRLHelper(root.right, max);
        int temp = Math.max(leftSum, rightSum) + root.value;
        max[0] = temp;
        return temp;
    }

    /** SAA */
    public static int maxSumPathSAA(TreeNode root) {
        int[] max = new int[]{Integer.MIN_VALUE};
        maxSumPathSAAHelper(root, max);
        return max[0];
    }
    private static int maxSumPathSAAHelper(TreeNode root, int[] max) {
        if (root == null) return 0;
        int leftSum = maxSumPathSAAHelper(root.left, max);
        int rightSum = maxSumPathSAAHelper(root.right, max);
        int temp = Math.max(Math.max(leftSum, rightSum), 0) + root.value;
        max[0] = Math.max(max[0], temp);
        return temp;
    }

    /** sumToTarget */
    public static boolean sumToTarget(TreeNode root, int target){
        if (root == null) return false;
        Set<Integer> set = new HashSet<>();
        int sum = 0;
        set.add(0);
        return sumToTargetHelper(root, set, sum, target);
    }
    private static boolean sumToTargetHelper(TreeNode root, Set<Integer> set, int sum, int target) {
        sum += root.value;
        if (set.contains(sum - target)) return true;
        boolean flag = set.add(sum);
        if (root.left != null && sumToTargetHelper(root.left, set, sum, target)) return true;
        if (root.right != null && sumToTargetHelper(root.right, set, sum, target)) return true;
        if (flag) set.remove(sum);
        return false;
    }

    /**Tree deserialization
     * in-pre
     * */
    public static TreeNode inPreBuild(int[] in, int[] pre){
        Map<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < in.length; i ++) {
            inMap.put(in[i], i);
        }
        return inPreBuildHelper(inMap, pre, 0, in.length - 1, 0, pre.length - 1);
    }
    private static TreeNode inPreBuildHelper(Map<Integer, Integer> map, int[] pre, int inLeft, int inRight, int preLeft, int preRight) {
        if (inLeft > inRight) return null;
        TreeNode root = new TreeNode(pre[preLeft]);
        int inMid = map.get(root.value);
        root.left = inPreBuildHelper(map, pre, inLeft, inMid - 1, preLeft + 1, preLeft + inMid - inLeft);
        root.right = inPreBuildHelper(map, pre, inMid + 1, inRight, preRight + inMid - inRight + 1, preRight);
        return root;
    }
    /** in-level */
    public static TreeNode inLevelBuild(int[] in, int[] level){
        Map<Integer, Integer> inMap = new HashMap<>();
        for (int i = 0; i < in.length; i ++) {
            inMap.put(in[i], i);
        }
        List<Integer> levelList = new ArrayList<>();
        for (int i : level) levelList.add(i);
        return inLevelBuildHelper(inMap, levelList);
    }
    private static TreeNode inLevelBuildHelper(Map<Integer, Integer> inMap, List<Integer> levelList){
        if (levelList.isEmpty()) return null;
        TreeNode root = new TreeNode(levelList.remove(0));
        List<Integer> left = new ArrayList<>();
        List<Integer> right = new ArrayList<>();
        for (int num : levelList) {
            if (inMap.get(num) < inMap.get(root.value)) {
                left.add(num);
            } else {
                right.add(num);
            }
        }
        root.left = inLevelBuildHelper(inMap, left);
        root.right = inLevelBuildHelper(inMap, right);
        return root;
    }

    /** post-bst build */
    public static TreeNode postBSTBuild(int[] post){
        int[] idx = new int[]{post.length - 1};
        return postBSTBuildHelper(post, idx, Integer.MIN_VALUE);
    }
    private static TreeNode postBSTBuildHelper(int[] post, int[] idx, int min) {
        if (idx[0] < 0 || post[idx[0]] <= min) return null;
        TreeNode root = new TreeNode(post[idx[0] --]);
        root.right = postBSTBuildHelper(post, idx, root.value);
        root.left = postBSTBuildHelper(post, idx, min);
        return root;
    }

    /** LCA1 */
    public static TreeNode LCA1(TreeNode root, TreeNode one, TreeNode two){
        if (root == null) return null;
        if (root == one || root == two) return root;
        TreeNode left = LCA1(root.left, one, two);
        TreeNode right = LCA1(root.right, one, two);
        if (left != null && right != null) return root;
        return left != null ? left : right;
    }

    /** LCA2 */
    public static TreeNodeP LCA2(TreeNodeP root, TreeNodeP one, TreeNodeP two) {
        int l1 = length(one);
        int l2 = length(two);
        if (l1 < l2) {
            return merge(one, two, l2 - l1);
        } else {
            return merge(two, one, l1 - l2);
        }
    }
    private static int length(TreeNodeP node){
        int len = 0;
        while (node != null) {
            len ++;
            node = node.parent;
        }
        return len;
    }
    private static TreeNodeP merge(TreeNodeP s, TreeNodeP l, int diff) {
        while (diff > 0) {
            l = l.parent;
            diff --;
        }
        while (s != l) {
            l = l.parent;
            s = s.parent;
        }
        return l;
    }

    /** LCA3 */
    public static TreeNode LCA3(TreeNode root, TreeNode one, TreeNode two) {
        Map<TreeNode, TreeNodeX> map = getInfoMap(root);
        int l1 = map.containsKey(one) ? map.get(one).height : 1;
        int l2 = map.containsKey(two) ? map.get(two).height : 1;
        if (l1 < l2) {
            return merge(one, two, l2 - l1, map);
        } else {
            return merge(two, one, l1 - l2, map);
        }
    }
    private static Map<TreeNode, TreeNodeX> getInfoMap(TreeNode root){
        Map<TreeNode, TreeNodeX> map = new HashMap<>();
        Queue<TreeNode> queue = new LinkedList<>();
        int height = 0;
        queue.offer(root);
        map.put(root, new TreeNodeX(null, false, height));
        while (! queue.isEmpty()) {
            int size = queue.size();
            height ++;
            for (int i = 0; i < size; i ++) {
                TreeNode cur = queue.poll();
                if (cur.left != null) map.put(cur.left, new TreeNodeX(cur, true, height));
                if (cur.right != null) map.put(cur.right, new TreeNodeX(cur, false, height));
            }
        }
        return map;
    }
    private static TreeNode merge(TreeNode s, TreeNode l, int diff, Map<TreeNode, TreeNodeX> map) {
        while (diff > 0) {
            l = map.containsKey(l) ? map.get(l).parent : null;
            diff --;
        }
        while (s != l) {
            l = map.containsKey(l) ? map.get(l).parent : null;
            s = map.containsKey(s) ? map.get(s).parent : null;
        }
        return l;
    }

    /** LCA4 */
    public static TreeNode LCA4(TreeNode root, List<TreeNode> nodes) {
        Set<TreeNode> set = new HashSet<>(nodes);
        return LCA4Helper(root, set);
    }
    private static TreeNode LCA4Helper(TreeNode root, Set<TreeNode> set) {
        if (root == null) return null;
        if (set.contains(root)) return root;
        TreeNode left = LCA4Helper(root.left, set);
        TreeNode right = LCA4Helper(root.right, set);
        if (left != null && right != null) return root;
        return left != null ? left : right;
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
class TreeNodeP{
    int value;
    TreeNodeP left;
    TreeNodeP right;
    TreeNodeP parent;
    public TreeNodeP(int value){
        this.value = value;
    }
}
class TreeNodeX{
    TreeNode parent;
    boolean isLeft;
    int height;
    public TreeNodeX(TreeNode parent, boolean isLeft, int height){
        this.parent = parent;
        this.isLeft = isLeft;
        this.height = height;
    }
}

