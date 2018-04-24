package utils.concurrency;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BlockingQueue<E> {
    private Queue<E> queue;
    private final int size;
    private Lock lock;
    private Condition full;
    private Condition empty;

    public BlockingQueue(int size){
        this.queue = new LinkedList<>();
        this.size = size;
        this.lock = new ReentrantLock();
        this.full = this.lock.newCondition();
        this.empty = this.lock.newCondition();
    }
    public boolean put(E e){
        lock.lock();
        try {
            while (queue.size() == size) full.await();
            if (queue.isEmpty()) empty.signalAll();
            return queue.offer(e);
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            lock.unlock();
        }
    }
    public E take(){
        lock.lock();
        try {
            while (queue.isEmpty()) empty.await();
            if (queue.size() == size) full.signalAll();
            return queue.peek();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }
    public E peek(){
        return queue.peek();
    }
    public int size(){
        return queue.size();
    }
}
