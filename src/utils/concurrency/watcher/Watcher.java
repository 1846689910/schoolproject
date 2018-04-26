package utils.concurrency.watcher;

import org.joda.time.DateTime;
import utils.common.JDate;
import utils.concurrency.tasker.TimerTasker;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * 数据监视器，监视数据结构，并隔一段时间进行更新
 * @apiNote 使用时，继承类要使用单例模式
 * */
public class Watcher<E> implements Runnable {
    final private Lock lock;
    private boolean ready;
    final private Condition updating;
    final private long checkCycle;
    final private Function<E, E> task;
    private E resource;
    private long[] time;

    /**
     * The constructor is used for accepting lock, boolean value and condition outside the Watcher
     * you could define them in AppStatus and use them to create Watcher
     */
    public Watcher(Lock lock, boolean ready, Condition updating, E resource, Function<E, E> task, long checkCycle){
        this.lock = lock;
        this.ready = ready;
        this.updating = updating;
        this.resource = resource;
        this.task = task;
        this.checkCycle = checkCycle;
        this.time = new long[]{new DateTime().getMillis()};
    }

    /** The constructor is used for accepting only task and checkCycle
     *  outside could use Getters to acquire lock, condition and boolean value
     * */
    public Watcher(E resource, Function<E, E> task, long checkCycle){
        this.lock = new ReentrantLock();
        this.ready = true;
        this.updating = this.lock.newCondition();
        this.checkCycle = checkCycle;
        this.resource = resource;
        this.task = task;
        this.time = new long[]{new DateTime().getMillis()};
    }

    private void update(long[] time){
        long curTime = new DateTime().getMillis();
        if (curTime - time[0] >= checkCycle) {
            lock.lock();  // 锁住资源
            System.out.println("check cycle " + checkCycle + "ms reached, begin update resources");
            System.out.println(">>>>>>>>>update start[" + JDate.getStdTime(curTime) + "]>>>>>>>>>");
            try {
                ready = false;  // 将boolean的flag置为false，还没准备好

                resource = task.apply(resource);  // 更新数据

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                time[0] = curTime;
                ready = true;  // 数据更新完毕，重置flag
                updating.signalAll();  // 这个updating必须是使用lock.newCondition()产生的，这样他们才能配合控制阻塞
                System.out.println("<<<<<<<<<update finished[" + JDate.getStdTime() + "]<<<<<<<<<");
                lock.unlock();  // 释放资源
            }
        }
    }

    /** manually trigger update immediately and delay the next update cycle */
    public void updateNow(boolean delayNextAutoUpdate){
        lock.lock();
        try {
            ready = false;
            System.out.println("manually triggered update, " + (delayNextAutoUpdate ? "next check cycle reset" : "next check cycle kept"));
            System.out.println(">>>>>>>>>update start[" + JDate.getStdTime() + "]>>>>>>>>>");

            resource = task.apply(resource);

            System.out.println("<<<<<<<<<update finished[" + JDate.getStdTime() + "]<<<<<<<<<");
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (delayNextAutoUpdate) setTime();
            ready = true;
            updating.signalAll();
            lock.unlock();
        }
    }

    @Override
    public void run(){
        long[] time = getTime();
        TimerTasker.setInterval(() -> {
            update(time);
        }, checkCycle);
    }

    /** set start time no earlier than the passed time
     * basically delay the watcher upgrade cycle
     * */
    public void setTime(){
        long curTime = new DateTime().getMillis();
        this.time[0] = curTime;
        System.out.println("The next watcher update cycle start time was reset to (no earlier than) " + JDate.getStdTime(curTime + checkCycle));
    }

    public void setTime(long time){
        long curTime = new DateTime().getMillis();
        this.time[0] = time < curTime ? curTime : time;  // you can only setup a future time or current time
        System.out.println("The next watcher update cycle start time was reset to (no earlier than) " + JDate.getStdTime(this.time[0] + checkCycle));
    }

    private long[] getTime(){
        return time;
    }

    public Lock getLock() {
        return lock;
    }

    public boolean isReady() {
        return ready;
    }

    public Condition getUpdating() {
        return updating;
    }

    public long getCheckCycle() {
        return checkCycle;
    }

    /** @param resultCheckCycle if the resource is not ready, the current thread should check by the time range checkCycle */
    public E getResource(long resultCheckCycle) {
        lock.lock();
        try {
            while (! ready){
                System.out.println("Resource not ready, " + Thread.currentThread().getName() + " has to wait");
                updating.await();
                Thread.sleep(resultCheckCycle);
            }
            return resource;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }
    /** basically get result immediately when result is ready */
    public E getResource(){
        return getResource(0);
    }

}
