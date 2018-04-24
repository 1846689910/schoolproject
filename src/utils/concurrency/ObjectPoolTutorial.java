package utils.concurrency;

import utils.concurrency.tasker.TimerTasker;

public class ObjectPoolTutorial {
    public static void main(String[] args) {
        // in the 100s of main thread, you will see the timerTasker keep retrieving elements from pool
        // and pool will check every 5 s to see if the elements number in the pool within the range of [min, max]
        // out of this range, it will supplement or remove elements from it
        StringPool stringPool = new StringPool(10, 100, 5000);
        Thread t = TimerTasker.setInterval(() -> {
            System.out.println(stringPool.take());
            System.out.println(stringPool.size());
            System.out.println(stringPool.toString());
        }, 1000);

        TimerTasker.setTimeout(t::interrupt, 50000);  // after 50s, it will be interrupted

        // main thread hold for 100s
        try {
            Thread.sleep(100000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
class StringPool extends ObjectPool<String> {

    public StringPool(int min){
        super(min);
    }

    public StringPool(int min, int max, long checkCycle) {
        super(min, max, checkCycle);
    }

    @Override
    public String createObject(){
        int rnd = (int) (Math.random() * (10000) + 1);
        return "" + rnd;
    }
}
