package utils.concurrency.pc;

import java.util.concurrent.BlockingQueue;

abstract public class Producer<E> implements Runnable {
    private BlockingQueue<E> queue;

    public Producer(final BlockingQueue<E> queue){
        this.queue = queue;
    }
    public abstract void put(E e);
}
