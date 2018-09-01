package utils.algorithm;
import org.junit.Test;

import java.util.*;
public class LC801_900 {
    /**
     * LC819 Most Common Word
     Given a paragraph and a list of banned words, return the most frequent word that is not in the list of banned words.  It is guaranteed there is at least one word that isn't banned, and that the answer is unique.

     Words in the list of banned words are given in lowercase, and free of punctuation.  Words in the paragraph are not case sensitive.  The answer is in lowercase.

     Example:
     Input:
     paragraph = "Bob hit a ball, the hit BALL flew far after it was hit."
     banned = ["hit"]
     Output: "ball"
     Explanation:
     "hit" occurs 3 times, but it is a banned word.
     "ball" occurs twice (and no other word does), so it is the most frequent non-banned word in the paragraph.
     Note that words in the paragraph are not case sensitive,
     that punctuation is ignored (even if adjacent to words, such as "ball,"),
     and that "hit" isn't the answer even though it occurs more because it is banned.
     * */
    public String mostCommon(String paragraph, String[] banned){
        String[] arr = paragraph.split("[\b|\\s|.|,|;|'|!|?]");
        Set<String> set = new HashSet<>();
        for(String s : banned) set.add(s);
        Map<String, Integer> map = new HashMap<>();  // <String, 数量>
        for(String s : arr) {
            String s1 = s.toLowerCase().trim();
            if (! s1.isEmpty()) map.merge(s1, 1, Integer::sum);
        }
        PriorityQueue<Map.Entry<String, Integer>> maxHeap = new PriorityQueue<>(map.size(), (e1, e2) -> {
            if(e1.getValue().equals(e2.getValue())) return 0;
            return e1.getValue() > e2.getValue() ? -1 : 1;
        });
        for (Map.Entry<String, Integer> entry : map.entrySet()) maxHeap.offer(entry);
        while (! maxHeap.isEmpty()) {
            Map.Entry<String, Integer> cur = maxHeap.poll();
            if(! set.contains(cur.getKey())) return cur.getKey();
        }
        return "";
    }
    @Test
    public void test1(){
        mostCommon("Bob hit a ball, the hit BALL flew far after it was hit.", new String[]{"hit"});
    }
    public String mostCommonWord(String paragraph, String[] banned) {
        paragraph += ".";
        Set<String> banset = new HashSet<>();
        for (String word: banned) banset.add(word);
        Map<String, Integer> count = new HashMap<>();
        String ans = "";
        int ansfreq = 0;
        StringBuilder word = new StringBuilder();
        for (char c: paragraph.toCharArray()) {
            if (Character.isLetter(c)) {
                word.append(Character.toLowerCase(c));
            } else if (word.length() > 0) {
                String finalword = word.toString();
                if (!banset.contains(finalword)) {
                    count.put(finalword, count.getOrDefault(finalword, 0) + 1);
                    if (count.get(finalword) > ansfreq) {
                        ans = finalword;
                        ansfreq = count.get(finalword);
                    }
                }
                word = new StringBuilder();
            }
        }
        return ans;
    }
}
