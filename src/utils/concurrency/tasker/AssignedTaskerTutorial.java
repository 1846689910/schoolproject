package utils.concurrency.tasker;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class AssignedTaskerTutorial {
    public static void main(String[] args) {
        /*
         * test the utils.concurrency.tasker.AssignedTasker
         * */
        List<Integer> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        for (int i = 0; i < 10; i ++) {
            list.add(i);
            list1.add("hello");
        }
        Queue<Integer> queue = new IntegerTasker(list, 12, i -> i).launch();
        Queue<String> queue1 = new StringTasker(list1, Executors.newFixedThreadPool(12), i -> i).launch();
        System.out.println(queue);
        System.out.println(queue1);
    }
}
class IntegerTasker extends AssignedTasker<Integer> {
    public IntegerTasker(List<Integer> tasks, int numOfThread, Function<Integer, Integer> task) {
        super(tasks, numOfThread, task);
    }
}
class StringTasker extends AssignedTasker<String> {
    public StringTasker(List<String> tasks, ExecutorService executor, Function<String, String> task) {
        super(tasks, executor, task);
    }
}

