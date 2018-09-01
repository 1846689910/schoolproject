package utils.algorithm;

import org.junit.Test;

import java.util.*;
import java.util.function.Function;

public class C3 {
    /** generate list for following demands */
    private static ListNode getAscList(int beg, int size, int step){
        ListNode head = new ListNode(beg);
        ListNode dummy = head;
        int count = 0;
        while (count < size - 1) {
            dummy = dummy.setNext(beg += step);
            count ++;
        }
        return head;
    }
    @Test
    public void getListTest(){
        ListNode head = getAscList(1, 5, 2);
        head.print();
        getAscList(2, 7, 1).print();
    }
    /** reverse LinkedList(Iterative) time O(n), space: O(n)
     * Logic:
     *  prev(begin is null)   head   next
     * let the head.next pointer reversed, and let prev become head and head become next for the next round operation
     * */
    public static ListNode reverseLinkedListIt(ListNode head){
        if (head == null || head.next == null) return head;
        ListNode prev = null;
        while (head != null) {
            ListNode next = head.next;
            head.next = prev;
            prev = head;
            head = next;
        }
        return prev;
    }
    /** reverse LinkedList (Recursive)
     * Logic:
     * use recursive to keep running until reach the last node
     * for each node, let its next node's next pointer point back: head.next.next = head
     * and drop its own .next pointer: head.next = null
     * */
    public static ListNode reverseLinkedListRe(ListNode head) {
        if (head == null || head.next == null) return head;
        ListNode newHead = reverseLinkedListRe(head.next);
        head.next.next = head;
        head.next = null;
        return newHead;
    }
    @Test
    public void reverseLinkedListTest(){
        ListNode head = getAscList(1, 10, 1);
        head.print();
        reverseLinkedListRe(head).print();

        head = getAscList(1, 10, 1);
        reverseLinkedListPair(head).print();

        head = getAscList(1, 10, 1);
        reverseLinkedListTri(head).print();
    }
    /** reverse LinkedList by pair
     * 	N1 → N2 → N4 → N3 → N6 → N5 …
     *  head next nnext
     * */
    public static ListNode reverseLinkedListPair(ListNode head){
        if (head == null || head.next == null)return head;
        ListNode newHead = head.next;
        head.next = reverseLinkedListPair(head.next.next);
        newHead.next = head;
        return newHead;
    }
    /**reverse LinkedList each three elements
     * 	N1 → N2 → N4 → N3 → N6 → N5 …
     *  head next nnext nnnext
     * */
    public static ListNode reverseLinkedListTri(ListNode head) {
        if (head == null || head.next == null)return head;
        ListNode next = head.next;
        ListNode nnext = head.next.next;
        head.next = reverseLinkedListTri(head.next.next.next);
        next.next = head;
        nnext.next = next;
        return nnext;
    }

    /** reverse Tree up down */
    public static TreeNode reverseTree(TreeNode root){
        if (root == null || root.left == null) return root;
        TreeNode newRoot = reverseTree(root.left);
        root.left.right = root.right;
        root.right.right = root;
        root.left = null;
        root.right = null;
        return newRoot;
    }

    /** reverse Tree left right */
    public static void reverseTreeLR(TreeNode root) {
        if(root == null) return;
        TreeNode tmpNode = root.left;
        root.left = root.right;
        root.right = tmpNode;
        reverseTreeLR(root.left);
        reverseTreeLR(root.right);
    }

    /** Deep copy linkedList with Random Pointer */
    public static ListNodeR deepCopyListNodeR(ListNodeR head){
        if (head == null) return null;
        ListNodeR dummy = new ListNodeR(0);
        ListNodeR cur = dummy;
        Map<ListNodeR, ListNodeR> map = new HashMap<>();
        while (head != null) {
            if (! map.containsKey(head)) {
                map.put(head, new ListNodeR(head.value));
            }
            cur.next = map.get(head);
            if (head.random != null) {
                if (! map.containsKey(head.random)) {
                    map.put(head.random, new ListNodeR(head.random.value));
                }
                cur.next.random = map.get(head.random);
            }
            cur = cur.next;
            head = head.next;
        }
        return dummy.next;
    }
    @Test
    public void ListNodeRTest(){
        ListNodeR head = new ListNodeR(1);
        head.next = new ListNodeR(2);
        head.random = new ListNodeR(5);
        ListNodeR copiedHead = deepCopyListNodeR(head);
        System.out.println(copiedHead.value == head.value && copiedHead.hashCode() != head.hashCode());
        System.out.println(copiedHead.next.value == head.next.value && copiedHead.next.hashCode() != head.next.hashCode());
        System.out.println(copiedHead.random.value == head.random.value && copiedHead.random.hashCode() != head.random.hashCode());
    }

    /** Deep copy of Graph */
    public static List<GraphNode> deepCopyGraphNode(List<GraphNode> graph){
        if (graph == null) return null;
        Map<GraphNode, GraphNode> map = new HashMap<>();
        for (GraphNode node: graph) {
            if (! map.containsKey(node)) {
                map.put(node, new GraphNode(node.value));
                DFS(node, map);
            }
        }
        return new ArrayList<>(map.values());
    }
    private static void DFS(GraphNode node, Map<GraphNode, GraphNode> map) {
        for (GraphNode n : map.get(node).neighbors) {
            if (! map.containsKey(n)) {
                map.put(n, new GraphNode(n.value));
                DFS(n, map);
            }
            map.get(node).neighbors.add(map.get(n));
        }
    }

    /** find the mid node of a linked list */
    public static ListNode findMidListNode(ListNode head){
        if (head == null) return null;
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
        }
        return slow;
    }
    @Test
    public void findMidListNodeTest(){
        ListNode head = getAscList(1, 5, 1);
        head.print();
        System.out.println(findMidListNode(head).value);
    }

    /** check if the list node make a cycle */
    public static boolean hasCycle(ListNode head){
        if (head == null || head.next == null) return false;
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) return true;
        }
        return false;
    }
    @Test
    public void hasCycleTest(){
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = head;
        System.out.println(hasCycle(head));
    }

    /** find the cycle start node if the linked list has cycle */
    public static ListNode findCycleStartListNode(ListNode head){
        if (head == null || head.next == null) return null;
        ListNode slow = head, fast = head;
        while (fast.next != null && fast.next.next != null) {
            slow = slow.next;
            fast = fast.next.next;
            if (slow == fast) {
                slow = head;
                while (slow != fast) {
                    slow = slow.next;
                    fast = fast.next;
                }
                return slow;
            }
        }
        return null;
    }
    @Test
    public void findCycleStartListNodeTest(){
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = head;
        System.out.println(findCycleStartListNode(head).value);
    }
    /** is the LinkedList a palindrome list (which its left half the same as the reverse of its right half) */
    public static boolean isPalindromeList(ListNode head){
        if (head == null || head.next == null) return true;
        ListNode mid = findMidListNode(head);
        ListNode left = head;
        ListNode right = mid.next;
        mid.next = null;
        right = reverseLinkedListIt(right);
        while (left != null && right != null) {
            if (left.value != right.value) return false;
            left = left.next;
            right = right.next;
        }
        return true;
    }
    @Test
    public void isPalindromeListTest(){
        ListNode head = ListNode.getList(Arrays.asList(1, 2, 3, 3, 2, 1));
        head.print();
        System.out.println(isPalindromeList(head));

        head = ListNode.getList(Arrays.asList(1, 2, 3, 2, 1));
        head.print();
        System.out.println(isPalindromeList(head));

        head = ListNode.getAscList(1, 10, 1);
        head.print();
        System.out.println(isPalindromeList(head));
    }

    /** Are two LinkedList intersected */
    public static boolean isIntersected(ListNode one, ListNode two){
        if (one == null || two == null) return false;
        while (one.next != null) one = one.next;
        while (two.next != null) two = two.next;
        return one.value == two.value;
    }
    @Test
    public void isIntersectedTest(){
        ListNode one = ListNode.getList(Arrays.asList(1, 2, 3, 4));
        ListNode two = ListNode.getList(Arrays.asList(2, 3, 4));
        System.out.println(isIntersected(one, two));
    }
    /** find the intersected ListNode of two LinkedList if they are intersected (method: DIFF) */
    public static ListNode findIntersectedListNode(ListNode one, ListNode two){
        if (one == null || two == null) return null;
        ListNode a = one, b = two;
        int len1 = 1, len2 = 1;
        while (a.next != null) {
            len1 ++;
            a = a.next;
        }
        while (b.next != null) {
            len2 ++;
            b = b.next;
        }
        if (len1 > len2) {
            ListNode tmp = one; one = two; two = tmp;
        }
        int diff = Math.abs(len1 - len2);
        while (diff > 0) {
            two = two.next;
            diff --;
        }
        while (one != two) {
            one = one.next;
            two = two.next;
        }
        return one;
    }
    /** find intersected ListNode (method: Length) */
    public static ListNode findIntersectedListNode1(ListNode one, ListNode two) {
        if (one == null || two == null) return null;
        ListNode a = one, b = two;
        while (a != b) {
            a = a.next == null ? two : a.next;
            b = b.next == null ? one : b.next;
        }
        return a;
    }
    @Test
    public void findIntersectedListNodeTest(){
        ListNode one = ListNode.getList(Arrays.asList(1, 2, 3, 4));
        ListNode two = new ListNode(-1);
        two.next = one;
        System.out.println(findIntersectedListNode(one, two).value);
        System.out.println(findIntersectedListNode1(one, two).value);
    }
    /** insert in an ascending LinkedList */
    public static ListNode insertInAscList(ListNode head, int value){
        ListNode node = new ListNode(value);
        if (head == null || value < head.value) {
            node.next = head;
            return node;
        }
        ListNode prev = head;
        while (prev.next != null && prev.next.value < value) {
            prev = prev.next;
        }
        node.next = prev.next;
        prev.next = node;
        return head;
    }
    @Test
    public void insertInAscListTest(){
        ListNode head = ListNode.getAscList(1, 10, 2);
        head.print();
        ListNode newHead = insertInAscList(head, 12);
        newHead.print();
    }
    /** remove one or multiple node in a list  */
    /** the following will remove all the node matches the node.value > value */
    public static ListNode removeInList(ListNode head, int value){
        if (head == null) return null;
        while (head.value > value) {
            head = head.next;
            if (head == null) return null;
        }
        ListNode prev = head;
        while (prev.next != null) {
            if (prev.next.value > value) {
                prev.next = prev.next.next;
            } else {
                prev = prev.next;
            }
        }
        return head;
    }
    /** could stringify the condition as a Function<Integer, Boolean> */
    public static ListNode removeInList(ListNode head, Function<Integer, Boolean> fn) {
        if (head == null) return null;
        while (fn.apply(head.value)) {
            head = head.next;
            if (head == null) return null;
        }
        ListNode prev = head;
        while (prev.next != null) {
            if (fn.apply(prev.next.value)) {
                prev.next = prev.next.next;
            } else {
                prev = prev.next;
            }
        }
        return head;
    }
    @Test
    public void removeInListTest(){
        ListNode head = ListNode.getAscList(1, 10, 1);
        head.print();
        removeInList(head, 8).print();

        head = ListNode.getAscList(1, 10, 1);
        head.print();
        removeInList(head, (i) -> i >= 7).print();
    }

    /** remove first nth node in LinkedList */
    public static ListNode removeFirstNthListNode(ListNode head, int n){
        if (head == null) return null;
        ListNode dummy = new ListNode(0);
        ListNode prev = dummy;
        prev.next = head;
        while (head.next != null && n > 1) {  // n starts from 1 or n > 0 if n starts from 0
            prev = prev.next;
            head = head.next;
            n --;
        }
        if (n > 1) return dummy.next;  // n > list size
        prev.next = head.next;
        head.next = null;
        return dummy.next;
    }
    @Test
    public void removeFirstNthListNodeTest(){
        ListNode head = ListNode.getAscList(1, 10, 2);
        head.print();
        removeFirstNthListNode(head, 6).print();
    }
    /** remove last nth ListNode */
    public static ListNode removeLastNthListNode(ListNode head, int n){
        if (head == null) return null;
        ListNode dummy = new ListNode(0);
        ListNode prev = dummy;
        prev.next = head;
        ListNode slow = head, fast = head;
        while (fast.next != null && n > 1) {  // n starts from 1 or n > 0 if n starts from 0
            fast = fast.next;
            n --;
        }
        if (n > 1) {  // n > list size
            return dummy.next;
        }
        while (fast.next != null) {
            slow = slow.next;
            prev = prev.next;
            fast = fast.next;
        }
        prev.next = slow.next;
        slow.next = null;
        return dummy.next;
    }
    @Test
    public void removeLastNthListNodeTest(){
        ListNode head = ListNode.getAscList(1, 10, 1);
        head.print();
        removeLastNthListNode(head, 3).print();
    }
    /** merge two sorted LinkedList */
    public static ListNode mergeList(ListNode one, ListNode two){
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (one != null && two != null) {
            if (one.value <= two.value) {
                cur.next = one;
                one = one.next;
            } else {
                cur.next = two;
                two = two.next;
            }
            cur = cur.next;
        }
        if (one != null) {
            cur.next = one;
        } else if (two != null) {
            cur.next = two;
        }
        return dummy.next;
    }
    @Test
    public void mergeListTest() {
        ListNode one = ListNode.getAscList(1, 10, 2);
        one.print();
        ListNode two = ListNode.getAscList(1, 10, 3);
        two.print();
        mergeList(one, two).print();
    }
    /** Reorder LinkedList */
    public static ListNode reorderList(ListNode head){
        if (head == null || head.next == null) return null;
        ListNode mid = findMidListNode(head);
        ListNode left = head;
        ListNode right = reverseLinkedListIt(mid.next);
        mid.next = null;
        return mergeList1(left, right);
    }
    private static ListNode mergeList1(ListNode left, ListNode right){
        ListNode dummy = new ListNode(0);
        ListNode cur = dummy;
        while (left != null && right != null) {
            cur.next = left;
            left = left.next;
            cur.next.next = right;
            right = right.next;
            cur = cur.next.next;
        }
        if (left != null) {
            cur.next = left;
        } else {
            cur.next = right;
        }
        return dummy.next;
    }
    @Test
    public void reorderListTest(){
        ListNode head = ListNode.getAscList(1, 10, 1);
        head.print();
        reorderList(head).print();
    }
    /** partition LinkedList */
    public static ListNode partitionList(ListNode head, int value){
        if (head == null || head.next == null) return null;
        ListNode large = new ListNode(0), curLarge = large, small = new ListNode(0), curSmall = small;
        while (head != null) {
            if (head.value <= value) {
                curSmall.next = head;
                curSmall = curSmall.next;
            } else {
                curLarge.next = head;
                curLarge = curLarge.next;
            }
            head = head.next;
        }
        curSmall.next = large.next;
        curLarge.next = null;
        return small.next;
    }
    @Test
    public void partitionListTest(){
        ListNode head = ListNode.getList(Arrays.asList(10, 8, 1, 4, 7, 5, 3));
        head.print();
        partitionList(head, 6).print();
    }
    /** flatten a LinkedList */
    public static List<Integer> flattenList(ListNodeC node){
        if (node == null) return null;
        Queue<ListNodeC> queue = new LinkedList<>();
        List<Integer> list = new ArrayList<>();
        while (node != null) {
            queue.offer(node);
            node = node.next;
        }
        while (! queue.isEmpty()) {
            int size = queue.size();
            for (int i = 0; i < size; i ++) {
                ListNodeC cur = queue.poll();
                if (cur.child != null) {
                    queue.offer(cur.child);
                }
                while (cur.child != null && cur.child.next != null) {
                    queue.offer(cur.child.next);
                    cur.child.next = cur.child.next.next;
                }
                list.add(cur.value);
            }
        }
        return list;
    }
}
/** Queue by two stacks */
class QueueBy2Stacks<E> {
    Deque<E> in;
    Deque<E> out;
    public QueueBy2Stacks(){
        this.in = new LinkedList<>();
        this.out = new LinkedList<>();
    }
    private void move(){
        if (out.isEmpty()) {
            while (! in.isEmpty()) {
                out.offerFirst(in.pollFirst());
            }
        }
    }
    public E poll(){
        move();
        return out.pollFirst();
    }
    public E peek(){
        move();
        return out.peekFirst();
    }
    public boolean offer(E e){
        return in.offerFirst(e);
    }
    public int size(){return in.size() + out.size();}
    public boolean isEmpty(){return in.isEmpty() && out.isEmpty();}
}
/** stack with min */
class StackWithMin {
    Deque<Integer> stack;
    Deque<Integer> minStack;
    public StackWithMin(){
        this.stack = new LinkedList<>();
        this.minStack = new LinkedList<>();
    }
    public void push(int num){
        stack.offerFirst(num);
        if (minStack.isEmpty() || minStack.peekFirst() >= num) minStack.offerFirst(num);
    }
    public Integer pop(){
        int ret = stack.pollFirst();  // return null if deque is empty
        if (minStack.peekFirst().equals(ret)) minStack.pollFirst();
        return ret;
    }
    public Integer min(){
        return minStack.peekFirst();
    }
    public Integer peek(){
        return stack.peekFirst();
    }
}
class ListNodeR extends ListNode {
    ListNodeR next;
    ListNodeR random;
    int value;
    public ListNodeR(int value){
        super(value);
    }
}
class GraphNode{
    int value;
    List<GraphNode> neighbors;
    public GraphNode(int value){
        this.value = value;
        this.neighbors = new ArrayList<>();
    }
}
class ListNodeC{
    int value;
    ListNodeC next;
    ListNodeC child;
    public ListNodeC(int value){
        this.value = value;
    }
}
