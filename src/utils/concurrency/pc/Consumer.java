package utils.concurrency.pc;

import java.util.concurrent.BlockingQueue;

abstract public class Consumer<E> implements Runnable {
    private BlockingQueue<E> queue;
    public Consumer(final BlockingQueue<E> queue){
        this.queue = queue;
    }
    public abstract E take();
}
