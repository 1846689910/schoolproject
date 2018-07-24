package utils.algorithm;

import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

public class LC400_500 {
}
/**
 * LC460
 * */
class LFUCache {
    Map<Integer, Integer> vals;
    Map<Integer, Integer> counts;
    HashMap<Integer, LinkedHashSet<Integer>> lists;
    int size;
    int min = -1;
    public LFUCache(int capacity) {
        size = capacity;
        vals = new HashMap<>();
        counts = new HashMap<>();
        lists = new HashMap<>();
        lists.put(1, new LinkedHashSet<>());
    }
    public int get(int key) {
        if(!vals.containsKey(key)) return -1;
        int count = counts.get(key);
        counts.put(key, count+1);
        lists.get(count).remove(key);
        if(count==min && lists.get(count).size()==0) min++;
        if(!lists.containsKey(count+1)) lists.put(count+1, new LinkedHashSet<>());
        lists.get(count+1).add(key);
        return vals.get(key);
    }
    public void set(int key, int value) {
        if(size <=0) return;
        if(vals.containsKey(key)) {
            vals.put(key, value);
            get(key);
            return;
        }
        if(vals.size() >= size) {
            int evit = lists.get(min).iterator().next();
            lists.get(min).remove(evit);
            vals.remove(evit);
        }
        vals.put(key, value);
        counts.put(key, 1);
        min = 1;
        lists.get(1).add(key);
    }
}
