package utils.concurrency.tasker;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 后端，一个类要执行一个很大的数组或任务操作要用多线程分配执行任务。可以本类中分配任务区间，
 * 设置子类实现Runnable接口，外类创建多个线程让这些子类分别执行任务
 * 优势：效率高，要比QueuedTasker效率更高
 * 劣势：因为要分配任务区间，所以接收任务类型只能是List
 * */
public class AssignedTasker<E> {
    private List<E> tasks;
    private List<List<Long>> tasksInterval;  // 任务区间List<[start1, end1], [start2, end2], ...>
    private int numOfThread;
    private Function<E, E> task;
    private Queue<E> queue;  // 用于存放最终的结果
    private ExecutorService executor;
    private long timeout = Long.MAX_VALUE;  // 当使用executor时, 默认无限等待直到任务结束
    private TimeUnit timeUnit = TimeUnit.NANOSECONDS;
    private boolean verbose = false;  // 开启内部打印信息，使用setVerbose()
    private int mode = 0;

    /**
     * @param tasks 需要处理的任务列表，E表示任务类，可能任务很多，需要多线程处理;
     * @param numOfThread 需要开几个线程来处理这个任务列表
     * @param task 对于每个任务需要如何处理，Task是接口，需要实现一个doTask方法
     * */
    public AssignedTasker(List<E> tasks, int numOfThread, Function<E, E> task) {
        this.tasks = tasks;
        this.numOfThread = tasks.size() <= 1 ? 1 : tasks.size() <= numOfThread ? tasks.size() / 2 : numOfThread;
        this.tasksInterval = assignTasks(tasks.size(), this.numOfThread);
        this.task = task;
        this.queue = new ConcurrentLinkedQueue<>();
        this.mode = 0;
    }

    /**
     * @param executor 使用传入的线程池来执行任务，这个可以传入多种自定义的线程池(
     *                 Executors.newCachedThreadPool()
     *                 Executors.newFixedThreadPool()
     *                 Executors.newSingleThreadExecutor()
     *                 )
     * 默认会等待任务完全停止
     * */
    public AssignedTasker(List<E> tasks, ExecutorService executor, Function<E, E> task){
        this.tasks = tasks;
        this.executor = executor;
        this.task = task;
        this.queue = new ConcurrentLinkedQueue<>();
        this.mode = 1;
    }

    /**
     * @param timeout 任务结束或者timeout时长到达就停止
     * @param timeUnit 时间单位
     * */
    public AssignedTasker(List<E> tasks, ExecutorService executor, long timeout, TimeUnit timeUnit, Function<E, E> task) {
        this(tasks, executor, task);
        this.timeout = timeout;
        this.timeUnit = timeUnit;
    }

    private class TaskerCore<E> implements Runnable {  // 每一个线程负责一个任务区间
        List<E> tasks;  // 任务列表
        long start;
        long end;
        Queue<E> queue;  // 处理之后的结果可以放到自定义的数据结构或者Map中
        Function<E, E> task;
        TaskerCore(List<E> tasks, long start, long end, Queue<E> queue, Function<E, E> task){
            this.tasks = tasks;
            this.start = start;
            this.end = end;
            this.queue = queue;
            this.task = task;
        }
        @Override
        public void run(){
            for (long i = start; i <= end; i ++) {
                queue.offer(task.apply(tasks.get((int) i)));
                if (i == end && verbose){
                    System.out.println(Thread.currentThread().getName() + " reaches end index " + i);
                }
            }
        }
    }

    public Queue<E> launch () {
        switch (mode) {
            case 0:
                return launchWithTaskerCore();
            case 1:
                return launchWithExecutor();
            default:
                if (verbose) System.out.println("no such mode");
                return queue;
        }
    }

    private Queue<E> launchWithTaskerCore(){
        int size = tasksInterval.size();
        Thread[] threads = new Thread[size];
        if (verbose) System.out.println("dataList interval: " + tasksInterval);
        for (int i = 0; i < size; i ++) {
            long start = tasksInterval.get(i).get(0);
            long end = tasksInterval.get(i).get(1);
            threads[i] = new Thread(new TaskerCore<E>(tasks, start, end, queue, task), "Thread" + i);
            threads[i].start();
            if (verbose) System.out.println(threads[i].getName() + " started");
        }
        for (Thread t : threads) {
            try {
                t.join();  // 等待所有人都完工后，我们再停止
            } catch(InterruptedException e) {
                e.printStackTrace();
                if (verbose) System.out.println(Thread.currentThread().getName() + " interrupted");
                Thread.currentThread().interrupt();
            }
        }
        return queue;
    }

    private Queue<E> launchWithExecutor(){
        tasks.forEach(each -> executor.execute(() -> queue.offer(task.apply(each))));

        executor.shutdown();

        try {

            executor.awaitTermination(timeout, timeUnit);

        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

        return queue;
    }

    /**
     * @param tasksSize 任务列表的总长度
     * @param numOfThread 要开处理线程的个数
     * */
    public static List<List<Long>> assignTasks(int tasksSize, int numOfThread) {
        // 负责将整个tasks列表，分成numOfThread份，让这些线程拿到自己的任务区间
        List<List<Long>> tasksInterval = new ArrayList<>();
        long start = 0;
        for (int i = 0; i < numOfThread; i ++) {
            long end = start + tasksSize / numOfThread;
            end = end >= tasksSize ? tasksSize - 1 : end;
            if (start <= end) tasksInterval.add(Arrays.asList(start, end));
            start = end + 1;
        }
        return tasksInterval;
    }

    public AssignedTasker<E> setVerbose(boolean verbose){
        this.verbose = verbose;
        return this;
    }

}
