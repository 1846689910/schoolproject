package utils.concurrency.tasker;

import org.joda.time.DateTime;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

public class TimerTasker {

    /** similar to setTimeout(fn, time) in JavaScript */
    public static Thread setTimeout(Runnable task, long timeout){
        Thread thread = new Thread(() -> {
            sleep(timeout, task, e -> {
                System.out.println(Thread.currentThread().getName() + " has been interrupted");
                e.printStackTrace();
                Thread.currentThread().interrupt();
            });
        }, "TimeoutTimer_" + new DateTime().getMillis());
        thread.start();
        return thread;
    }

    /** similar to setInterval(fn, time) in JavaScript */
    public static Thread setInterval(Runnable task, long timeInterval) {
        AtomicBoolean keepGoing = new AtomicBoolean(true);
        Thread thread = new Thread(() -> {
            while(keepGoing.get()) {
                sleep(timeInterval, task, e -> {
                    System.out.println(Thread.currentThread().getName() + " has been interrupted");
                    keepGoing.set(false);
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                });
            }
        }, "IntervalTimer_" + new Date().getTime());
        thread.start();
        return thread;
    }

    public static Thread setInterval(Runnable task, long timeInterval, boolean asDaemon) {
        Thread thread = new Thread (() -> {
            try {
                while(true) {
                    Thread.sleep(timeInterval);
                    task.run();
                }
            } catch (InterruptedException e) {
                System.out.println(Thread.currentThread().getName() + " has been interrupted");
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }, "IntervalTimer_" + new DateTime().getMillis());
        thread.setDaemon(asDaemon);
        thread.start();
        return thread;
    }

        /** similar to clearTimeout or clearInterval, basically interrupt thread */
    public static void clearTimer(Thread t){
        t.interrupt();
    }

    public static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void sleep(long time, Consumer<Throwable> error) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            error.accept(e);
        }
    }

    public static void sleep(long time, Runnable success, Consumer<Throwable> error) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            error.accept(e);
            return;
        }
        success.run();
    }

}