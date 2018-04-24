package utils.concurrency.tasker;

public class TimerTaskerTutorial {
    public static void main(String[] args) {
//        new TimerTaskerTutorial().testSetTimeout();
        new TimerTaskerTutorial().testSetInterval();
    }

    public void testSetTimeout(){
        Thread t = TimerTasker.setTimeout(() -> {
            System.out.println("hello world");
        }, 1000);
        TimerTasker.setTimeout(() -> {
            TimerTasker.clearTimer(t);
        }, 500);
        try {
            System.out.println("I am " + Thread.currentThread().getName());
            Thread.sleep(5000);
            System.out.println("wake up");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void testSetInterval(){
        Thread t = TimerTasker.setInterval(() -> {
            System.out.println("hello");
        }, 10);
        TimerTasker.setTimeout(() -> TimerTasker.clearTimer(t), 50);
        try {
            System.out.println("I am " + Thread.currentThread().getName());
            Thread.sleep(1000);
            System.out.println("wake up");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
