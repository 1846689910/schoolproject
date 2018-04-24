package utils.concurrency.watcher;

import org.junit.Test;
import utils.concurrency.tasker.TimerTasker;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class WatcherTutorial {
    public static void main(String[] args) {

        Map<String, Integer> map = new HashMap<>();
        map.put("a", 123);
        map.put("b", 456);
        MapWatcher watcher = MapWatcher.getMapWatcher(map, map1 -> {
            map1.put("a", map1.get("a") + 1);
            map1.put("b", map1.get("b") + 1);
            return map1;
        }, 10000);

        // 资源监视器
        new Thread(watcher, "Map Watcher").start();

        // 资源使用：同时开四个线程，代表四个用户来获取数据
        new Thread(() -> {
            useResource(watcher);
        }, "retrieving1").start();
        new Thread(() -> {
            useResource(watcher);
        }, "retrieving2").start();
        new Thread(() -> {
            useResource(watcher);
        }, "retrieving3").start();
        new Thread(() -> {
            useResource(watcher);
        }, "retrieving4").start();

    }

    @Test
    public void delayTest(){
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 123);
        map.put("b", 456);
        MapWatcher watcher = MapWatcher.getMapWatcher(map, map1 -> {
            map1.put("a", map1.get("a") + 1);
            map1.put("b", map1.get("b") + 1);

            try {
                Thread.sleep(5000);  // 更新耗时5s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map1.forEach((k, v) -> System.out.println(k + " : " + v));
            return map1;
        }, 1000 * 1 * 60);  // 每1min更新一次

        // 资源监视器
        new Thread(watcher, "Map Watcher").start();

        Thread setTime = TimerTasker.setInterval(watcher::setTime, 1000 * 5 * 60);  // 每5min重置更新时间

        TimerTasker.setTimeout(setTime::interrupt, 60 * 15 * 1000);  // 15 min后结束重置线程

        try {
            Thread.sleep(1000 * 60 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void updateNowTest(){
        Map<String, Integer> map = new HashMap<>();
        map.put("a", 123);
        map.put("b", 456);
        MapWatcher watcher = MapWatcher.getMapWatcher(map, map1 -> {
            map1.put("a", map1.get("a") + 1);
            map1.put("b", map1.get("b") + 1);

            try {
                Thread.sleep(5000);  // 更新耗时5s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            map1.forEach((k, v) -> System.out.println(k + " : " + v));
            return map1;
        }, 1000 * 1 * 60);  // 每1min更新一次

        // 资源监视器
        new Thread(watcher, "Map Watcher").start();

        TimerTasker.setInterval(() -> {
            watcher.updateNow(true);  // 立即触发更新，并推迟下一次更新
            watcher.getResource().forEach((k, v) -> {
                System.out.println(k + " : " + v);
            });
        }, 1000 * 3 * 60);  // 每3min立即触发更新一次，并打印内容

        try {
            Thread.sleep(1000 * 60 * 60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void useResource(MapWatcher watcher){
        while (true) {
            try {
                Thread.sleep(1000);  // 每隔1秒取一次
                Map<String, Integer> map1 = watcher.getResource(1000);
                System.out.println(Thread.currentThread().getName() + ": " + map1.entrySet());
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            }
        }
    }
}
final class MapWatcher extends Watcher<Map<String, Integer>> {
    private static volatile MapWatcher mapWatcher = null;
    private MapWatcher(Map<String, Integer> map, Function<Map<String, Integer>, Map<String, Integer>> task, long checkCycle){
        // the map is the resource that is under watch of MapWatcher
        super(map, task, checkCycle);
    }
    public static MapWatcher getMapWatcher(Map<String, Integer> map, Function<Map<String, Integer>, Map<String, Integer>> task, long checkCycle){
        if (mapWatcher == null) {
            synchronized (MapWatcher.class) {
                if (mapWatcher == null) {
                    return new MapWatcher(map, task, checkCycle);
                }
            }
        }
        return mapWatcher;
    }
}
