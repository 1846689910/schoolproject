package utils.common;

import java.util.HashMap;
import java.util.Map;

public class LRUCache<K, V> {
    private class Node<K, V> {
        K key;
        V val;
        Node<K, V> prev;
        Node<K, V> next;
        Node(K key, V val){
            this.key = key;
            this.val = val;
        }
    }
    private final int size;
    private Node<K, V> head;
    private Node<K, V> tail;
    private Map<K, Node<K, V>> map;
    public LRUCache(int size){
        this.size = size;
        this.map = new HashMap<>();
    }
    private Node<K, V> append(Node<K, V> node){
        map.put(node.key, node);
        if (head == null) {
            head = tail = node;
        } else {
            node.next = head;
            head.prev = node;
            head = node;
        }
        return node;
    }
    private Node<K, V> remove(Node<K, V> node){
        map.remove(node.key);
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        if (node == head) {
            head = head.next;
        }
        if (node == tail) {
            tail = tail.prev;
        }
        node.next = null;
        node.prev = null;
        return node;
    }
    public void set(K key, V val){
        Node<K, V> node;
        if (map.containsKey(key)) {
            node = map.get(key);
            remove(node);
        } else if (map.size() < size) {
            node = new Node(key, val);
        } else {
            node = tail;
            remove(node);
            node.key = key;
            node.val = val;
        }
        append(node);
    }
    public V get(K key) {
        Node<K, V> node = map.get(key);
        if (node != null) {
            remove(node);
            append(node);
            return node.val;
        } else {
            return null;
        }
    }

}
