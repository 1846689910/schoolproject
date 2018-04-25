package utils.algorithm;

import org.junit.Test;

import java.util.*;

public class C0 {
    /**
     * 两字符串从i开始相等
     * */
    public static boolean equals(String longS, String shortS, int i){
        for (int j = 0, len = shortS.length(); j < len ; j ++) {
            if (longS.charAt(j + i) != shortS.charAt(j)) {
                return false;
            }
        }
        return true;
    }
    @Test
    public void equalsTest(){
        System.out.println(equals("hello world", "world", 6));
    }

    /**
     * 长串里找短串末尾位置
     * */
    public static List<Integer> getAllEnds(String longS, String shortS) {
        int longLen = longS.length();
        int shortLen = shortS.length();
        List<Integer> allEnds = new ArrayList<>();
        int idx = 0;
        while (idx <= longLen - shortLen) {
            if (equals(longS, shortS, idx)) {
                allEnds.add(idx + shortLen - 1);
                idx += shortLen;
            } else {
                idx ++;
            }
        }
        return allEnds;
    }
    /**
     * 长串里找短串开始位置,
     * */
    public static List<Integer> getAllStarts(String longS, String shortS) {
        int longLen = longS.length();
        int shortLen = shortS.length();
        List<Integer> allStarts = new ArrayList<>();
        int idx = 0;
        while (idx <= longLen - shortLen) {
            if (equals(longS, shortS, idx)) {
                allStarts.add(idx);
                idx += shortLen;
            } else {
                idx ++;
            }
        }
        return allStarts;
    }
    @Test
    public void getAllStartsEndsTest(){
        String longS = "hello, my name is eric. hello world";
        String shortS = "hello";
        System.out.println(getAllEnds(longS, shortS));
        System.out.println(getAllStarts(longS, shortS));
    }
    /**
     * 把串内容放入chars的i位置
     * */
    public static void copy(char[] charsLong, char[] charsShort, int i) {
        for (int j = 0; j < charsShort.length; j ++) {
            charsLong[i + j] = charsShort[j];
        }
        // 或者调用System方法
        // System.arraycopy(copyFromArr, start, copyToArr, start, copyLength)
        System.arraycopy(charsShort, 0, charsLong, i, charsShort.length);
    }
    @Test
    public void copyTest(){
        char[] charsLong = "hello world".toCharArray();
        char[] charsShort = "aaa".toCharArray();
        copy(charsLong, charsShort, 3);
        System.out.println(Arrays.toString(charsLong));
        System.arraycopy(charsShort, 0, charsLong, 3, charsShort.length);
        System.out.println(Arrays.toString(charsLong));
    }
    /**验证list能否成环
     * */
    public static boolean checkCycle(List<String> list){
        for (int i = 0, size = list.size(); i < size; i ++) {
            int idx = i + 1;
            if (i == size - 1) idx = 0;
            if (list.get(i).charAt(list.get(i).length() - 1) != list.get(idx).charAt(0)) {
                return false;
            }
        }
        return true;
    }
    @Test
    public void checkCycleTest(){
        List<String> list = Arrays.asList("aaa", "abb", "bcc", "caa");
        System.out.println(checkCycle(list));
    }
    /**Arrays.sort(), Collections.sort(), PriorityQueue的比较器*/
    @Test
    public void compared(){
        // Arrays.sort
        User[] users = new User[3];
        Arrays.sort(users, (u1, u2) -> {
            if (u1.age == u2.age) return 0;
            return u1.age < u2.age ? -1 : 1;
        });
        // Collections.sort
        List<User> list = new ArrayList<>();
        Collections.sort(list, (u1, u2) -> {
            if (u1.age == u2.age) return 0;
            return u1.age < u2.age ? -1 : 1;
        });
        // Collections.sort could be replaced by list.sort(Comparator<T>)
        list.sort((u1, u2) -> {
            if (u1.age == u2.age) return 0;
            return u1.age < u2.age ? -1 : 1;
        });
        // PriorityQueue
        PriorityQueue<User> pq = new PriorityQueue<>(10, (u1, u2) -> {
            if (u1.age == u2.age) return 0;
            return u1.age < u2.age ? -1 : 1;
        });
    }

    /**
     * 位操作
     * */
    @Test
    public void getBinaryBitTest(){
        byte big = 115;
        System.out.println(1 & (big >>> 3));  /** 获取右边第n位*/
        System.out.println(1 & (big >>> (8 - 3 - 1)));  /** 获取左边第n位*/
        /** 获取data所有位*/
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i ++) {
            sb.append(1 & (big >>> i));
        }
        System.out.println(sb.reverse().toString());
        System.out.println(Integer.toBinaryString(big));  // 几种类型都可以.toBinaryString
    }
    @Test
    public void setBinaryBitTest(){
        int data = 1500;
        data |= (1 << 3);  /**data右边第n位置1*/
        data |= (1 << (32 - 3 - 1));  /**data左边第n位置1*/
        /**data右边第n位置0:
         * */
        int mask1 = 0;
        for (int i = 32 - 1; i >= 0; i --) {
            if (i != 3) mask1 |= (1 << i);
        }
        data &= mask1;
        /**data左边第n位置0*/
        int mask2 = 0;
        for (int i = 32 - 1; i >= 0; i --) {
            if (i != (32 - 3 - 1)) mask2 |= (1 << i);
        }
        data &= mask2;
        /**翻转data所有位*/
        int mask3 = 0;
        for (int i = 32 - 1; i >= 0; i --) mask3 |= (1 << i);
        data ^= mask3;
        System.out.println(data);
    }

    @Test
    public void numTest(){
        /**任意类型数据转字符串*/
        String s = String.valueOf(123);
        /** 字符串可以转为一些primitive类型
         * 每种基本类型都有MIN_VALUE和MAX_VALUE
         * */
        int i = Integer.parseInt("111");
        double d = Double.parseDouble("112.2");
        System.out.println(d);
    }

    public static boolean isEven(int i){return i % 2 == 0;}

    public static boolean isNumber(char c) {
        return Character.isDigit(c);
        // 或return c >= '0' && c <= '9'
    }
    /**判断一个double是否是自然数*/
    public static boolean isNatural(double d) {
        return String.valueOf(d).matches("\\d+.0");
    }
    @Test
    public void isNaturalTest(){
        System.out.println(isNatural(1.2));
        System.out.println(isNatural(2.0));
    }
    /**[a, b] 间随机数*/
    public static int getRandInRange(int a, int b) {
        return (int)(Math.random() * (b - a + 1) + a);
    }
    /**[a, b] 间完全平方数的个数 (int)floor(sqrt(b)) - (int)ceil(sqrt(a)) + 1*/
    public static int getNumOfSquareNumber(int a, int b) {
        return (int)Math.floor(Math.sqrt(b)) - (int)Math.ceil(Math.sqrt(a)) + 1;
    }
    @Test
    public void getNumOfSquareNumberTest(){
        System.out.println(getNumOfSquareNumber(0, 10));
    }

    /**拿出一个数的所有digit:
     *  1 String.valueOf().toCharArray(), 再取char数组的每一位；
     *  2 只要数>0: list放数%10, 数/=10
     */
    public static List<Integer> takeAllDigits(int num){
        List<Integer> list = new ArrayList<>();
        char[] chars = String.valueOf(num).toCharArray();
        for (int i = 0; i < chars.length; i ++) {
            list.add(chars[i] - '0');
        }
        return list;
    }

    public static List<Integer> takeAllDigits1(int num) {
        List<Integer> list = new ArrayList<>();
        while (num > 0) {
            list.add(num % 10);
            num /= 10;
        }
        Collections.reverse(list);
        return list;
    }
    @Test
    public void takeAllDigitsTest(){
        int i = 773;
        System.out.println(takeAllDigits(i));
        System.out.println(takeAllDigits1(i));
    }

    /** 矩阵乘法
     * */
    public static int[][] matrixTimes(int[][] m1, int[][] m2){
        int r1 = m1.length, c1 = m1[0].length, r2 = m2.length, c2 = m2[0].length;
        if (c1 != r2) return null;
        int[][] ret = new int[r1][c2];
        for (int i = 0; i < r1; i ++) {
            for (int j = 0; j < c2; j ++) {
                int num = 0;
                for (int k = 0; k < c1; k ++) {
                    num += m1[i][k] * m2[k][j];
                }
                ret[i][j] = num;
            }
        }
        return ret;
    }
    @Test
    public void matrixTimesTest(){
        int[][] m1 = {{1, 2}, {4, 5}, {7, 8}};
        int[][] m2 = {{1, 1, 2, 2}, {3, 3, 4, 4}};
        int[][] ret = matrixTimes(m1, m2);
        if (ret != null)
        for (int[] arr: ret) {
            System.out.println(Arrays.toString(arr));
        }
    }

    /**
     *List of digits乘数 {1, 3, 6}*2={2, 7, 2}：
     * */
    public static List<Integer> listTimesNum(List<Integer> digits, int n){
        List<Integer> list = new ArrayList<>();
        if (digits == null || digits.size() == 0) return list;
        int carry = 0;
        for (int i = digits.size() - 1; i >= 0; i --) {
            int num = digits.get(i) * n + carry;
            list.add(num % 10);
            carry = num / 10;
        }
        if (carry > 0) list.add(carry);
        Collections.reverse(list);
        return list;
    }
    @Test
    public void listTimesNumTest(){
        System.out.println(listTimesNum(Arrays.asList(1, 2, 5), 3));
        System.out.println(listTimesNum(Arrays.asList(3, 7, 5), 7));
    }

    /**
     * 两list相加/减
     * */
    public static List<Integer> listPlusList(List<Integer> list1, List<Integer> list2) {
        List<Integer> result = new ArrayList<>();
        if (list1 == null || list2 == null) return null;
        int size1 = list1.size(), size2 = list2.size();
        if (size1 == 0 && size2 == 0) return result;
        if (size1 == 0) return list2;
        if (size2 == 0) return list1;
        int carry = 0;
        if (size1 > size2) {
            List<Integer> tmp = list1; list1 = list2; list2 = tmp;
            size1 = list1.size();
            size2 = list2.size();
        }
        for (int i = size2 - 1; i >= 0; i --) {
            int num = 0;
            if (i >= size2 - size1) {
                num = list2.get(i) + list1.get(i - size2 + size1) + carry;
            } else {
                num = list2.get(i) + carry;
            }
            result.add(num % 10);
            carry = num / 10;
        }
        if (carry > 0) result.add(carry);
        Collections.reverse(result);
        return result;
    }

    @Test
    public void listPlusListTest(){
        System.out.println(listPlusList(Arrays.asList(7, 3, 5), Arrays.asList(8, 9, 9)));
    }

}
class User{
    String name;
    int age;
    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }
}

