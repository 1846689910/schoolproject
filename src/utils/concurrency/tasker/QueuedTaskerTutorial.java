package utils.concurrency.tasker;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class QueuedTaskerTutorial {
    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < 30000; i ++) {
            list.add(i);
//            set.add(i);
        }
        long d = new Date().getTime();
        Queue<Integer> queue = new IntegerTasker1(list, 10, i -> i).setVerbose(true).launch();
        long d1 = new Date().getTime();
        Queue<Integer> queue1 = new IntegerTasker(list, 10, i -> i).launch();
        long d2 = new Date().getTime();
        Queue<Integer> queue2 = new IntegerTasker2(list, Executors.newFixedThreadPool(10), i -> i).launch();
        System.out.println("QueuedTasker took " + (d1 - d) + "ms");
        System.out.println("AssignedTasker took " + (d2 - d1) + "ms");
        System.out.println(d1 - d == d2 - d1 ? "QueuedTasker = AssignedTasker" : d1 - d < d2 - d1 ? "QueuedTasker better" : "AssignedTasker better");
    }
}
class IntegerTasker1 extends QueuedTasker<Integer> {
    public IntegerTasker1(Collection<Integer> tasks, int numOfThread, Function<Integer, Integer> task){
        super(tasks, numOfThread, task);
    }
}

class IntegerTasker2 extends QueuedTasker<Integer> {
    public IntegerTasker2(Collection<Integer> tasks, ExecutorService executor, Function<Integer, Integer> task) {
        super(tasks, executor, task);
    }
}

