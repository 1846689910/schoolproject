package utils.concurrency.pc;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PCTutorial {

    public static void main(String[] args) {
        BlockingQueue<Notebook> queue = new LinkedBlockingQueue<>();

        new Thread(new Consumer<Notebook>(queue) {
            @Override
            public Notebook take() {
                try {
                    return queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                } finally {
                    System.out.println(Thread.currentThread().getName() + " want to take one notebook");
                    System.out.println("There are " + (queue.size()) + " notebook(s) left");
                }
                return null;
            }
            @Override
            public void run() {
                while (true){
                    try {
                        Thread.sleep(500);
                        take();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }, "consumer").start();

        new Thread(new Producer<Notebook>(queue) {
            @Override
            public void put(Notebook n) {
                try {
                    queue.put(n);
                    System.out.println(Thread.currentThread().getName() + " put one notebook");
                    System.out.println("There are " + queue.size() + " notebook left");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }

            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                        put(new Notebook("student", 100));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }, "producer").start();

    }

}

class Notebook{
    String type;
    int pages;
    Notebook(String type, int pages){
        this.type = type;
        this.pages = pages;
    }
}
