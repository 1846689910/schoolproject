package utils.concurrency;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class ObjectPool<E> {

    private Queue<E> pool;
    /**
        ScheduledExecutorService starts a special task in a separate thread and observes
        the minimum and maximum number of objects in the pool periodically in a specified
        time (with parameter validationInterval).
        When the number of objects is less than the minimum, missing instances will be created.
        When the number of objects is greater than the maximum, too many instances will be removed.
        This is sometimes useful for the balance of memory consuming objects in the pool.
    */
    private ScheduledExecutorService service;

    public ObjectPool(final int min) {
        initialize(min);
    }
    /**
      Creates the pool.
      @param min:   minimum number of objects residing in the pool.
      @param max:   maximum number of objects residing in the pool.
      @param checkCycle: time in seconds for periodical checking of
         minObjects / maxObjects conditions in a separate thread.
      When the number of objects is less than minObjects, missing instances will be created.
      When the number of objects is greater than maxObjects, too many instances will be removed.
    */
    public ObjectPool(final int min, final int max, final long checkCycle) {
        initialize(min);
        // check ObjectPool status in a separate thread
        service = Executors.newSingleThreadScheduledExecutor();

        service.scheduleWithFixedDelay(() -> {  // implements Runnable and @Override public void run()
            int size = pool.size();

            if (size < min) {

                int added = min + size;

                for (int i = 0; i < added; i ++) pool.offer(createObject());

            } else if (size > max) {

                int removed = size - max;

                for (int i = 0; i < removed; i ++) pool.poll();
            }
        }, checkCycle, checkCycle, TimeUnit.MILLISECONDS);
    }

    private void initialize(final int minObjects){
        pool = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < minObjects; i ++) {
            pool.offer(createObject());
        }
    }

    protected abstract E createObject();

    /** take object from the pool */
    public E take(){
        E object = pool.poll();
        return object == null ? createObject() : object;
    }

    /** put back the object to the pool after use */
    public void put(E object){
        if (object == null) return;
        pool.offer(object);
    }

    /** turn off the ObjectPool */
    public void shutdown(){
        service.shutdown();
    }

    /** return the current size of the pool */
    public int size(){
        return pool.size();
    }

    /** print all the elements in the pool */
    public String toString(){
        return pool.toString();
    }
}
