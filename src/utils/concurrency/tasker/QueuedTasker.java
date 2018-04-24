package utils.concurrency.tasker;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;


/**
 * 用途和AssignedTasker相似，但是区别是，QueuedTasker只需要将Collection<E>型的任务传入即可
 * 再将任务列表转为ConcurrentLinkedQueue
 * 运行时，不纠正线程数的问题，同开线程，不分配任务区间，线程相继从任务队列中取任务
 * 优势：可以接受任务列表为单例集合Collection的实现类，也即List或Set
 * 劣势：效率不如AssignedTasker
 * */
public class QueuedTasker<E> {

    private Collection<E> tasks;
    private int numOfThread;
    private Function<E, E> task;
    private Queue<E> queue;
    private Queue<E> result;
    private ExecutorService executor;
    private long timeout = Long.MAX_VALUE;  // 当使用executor时, 默认无限等待直到任务结束
    private TimeUnit timeUnit = TimeUnit.NANOSECONDS;
    private boolean verbose = false;
    private int mode = 0;

    public QueuedTasker(Collection<E> tasks, int numOfThread, Function<E, E> task){
        this.tasks = tasks;
        this.numOfThread = numOfThread;
        this.task = task;
        this.queue = new ConcurrentLinkedQueue<>(tasks);
        this.result = new ConcurrentLinkedQueue<>();
        this.mode = 0;
    }

    public QueuedTasker(Collection<E> tasks, ExecutorService executor, Function<E, E> task){
        this.tasks = tasks;
        this.executor = executor;
        this.task = task;
        this.result = new ConcurrentLinkedQueue<>();
        this.mode = 1;
    }

    public QueuedTasker(Collection<E> tasks, ExecutorService executor, Function<E, E> task, long timeout, TimeUnit timeUnit) {
        this(tasks, executor, task);
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    public Queue<E> launch(){
        switch(mode) {
            case 0:
                return launchWithTaskerCore();
            case 1:
                return launchWithExecutor();
            default:
                if (verbose) System.out.println("no such mode");
                return result;
        }
    }

    private Queue<E> launchWithExecutor(){
        tasks.forEach(each -> executor.execute(() -> result.offer(task.apply(each))));

        executor.shutdown();

        try {
            executor.awaitTermination(timeout, timeUnit);
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return result;
    }

    private Queue<E> launchWithTaskerCore(){
        Thread[] threads = new Thread[numOfThread];
        for (int i = 0; i < numOfThread; i ++) {
            threads[i] = new Thread(new TaskerCore<>(queue, result, task), "Thread" + i);
            threads[i].start();
            if (verbose) System.out.println(threads[i].getName() + " started");
        }
        for (Thread t : threads) {
            try {
                t.join();
            } catch(InterruptedException e){
                e.printStackTrace();
                Thread.currentThread().interrupt();
                if (verbose) System.out.println(Thread.currentThread().getName() + " interrupted");
            }
        }
        return result;
    }

    private class TaskerCore<E> implements Runnable {
        Queue<E> queue;
        Function<E, E> task;
        Queue<E> result;
        TaskerCore(Queue<E> queue, Queue<E> result, Function<E, E> task){
            this.queue = queue;
            this.task = task;
            this.result = result;
        }
        @Override
        public void run(){

            while (! queue.isEmpty()) {
                synchronized (QueuedTasker.class) {
                    if (! queue.isEmpty()) result.offer(task.apply(queue.poll()));
                }
            }

            if (verbose) System.out.println(Thread.currentThread().getName() + " ended");
        }
    }

    public QueuedTasker<E> setVerbose(boolean verbose){
        this.verbose = verbose;
        return this;
    }

}
