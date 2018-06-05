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
}
