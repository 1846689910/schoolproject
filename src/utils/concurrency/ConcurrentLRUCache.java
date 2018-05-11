package utils.concurrency;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentLRUCache<K, V> {
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
    private ConcurrentMap<K, Node<K, V>> map;
    private ReadWriteLock lock;
    private Lock readLock;
    private Lock writeLock;
    public ConcurrentLRUCache(int size){
        this.size = size;
        this.map = new ConcurrentHashMap<>();
        this.lock = new ReentrantReadWriteLock();
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
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
        writeLock.lock();
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            writeLock.unlock();
        }
    }
    public V get(K key) {
        readLock.lock();
        try {
            Node<K, V> node = map.get(key);
            if (node != null) {
                remove(node);
                append(node);
                return node.val;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            readLock.unlock();
        }
    }
}
