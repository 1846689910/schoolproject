package utils.algorithm;

import org.junit.Test;

import java.util.*;

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
//    private static ListNodeR getAscList(int beg, int size, int step){
//
//    }
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
