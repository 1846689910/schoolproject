package utils.algorithm;

import org.junit.Test;

import java.util.*;

public class C0 {
    /**
     * 两字符串从i开始相等
     * equals(长短i): 遍短, 若长j+i不短i就错，最后就true
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
     * list, i0, 只要i<=长-短: 若equals(长短i),list放i+短-1, i+短 否则i++, 就list
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
     * 同上list放i
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
     * 把串内容放入chars的i位置：遍短, 长i+j字为短j字
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
     * 0 -> listSize, idx为i+1,若i为list末尾则idx为0。若i串末字不idx串0字就错，最后就true
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
    /**Arrays.sort(), Collections.sort(), PriorityQueue的比较器，从小到大:小问-1 1, 从大到小:大问-1 1*/
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
     * byte: 8 bit; short/char: 16 bit, int/float: 32 bit, long/double: 64 bit
     * 获取：右移；设置：左移
     * */
    @Test
    public void getBinaryBitTest(){
        byte big = 115;
        System.out.println(1 & (big >>> 3));  /** 获取右边第n位: 1与data无符右移n*/
        System.out.println(1 & (big >>> (8 - 3 - 1)));  /** 获取左边第n位：1与data无符右移(data位数-n-1)位*/
        /** 获取data所有位:0 -> data位数,sb缀右边第i位,最后就sb.翻转.toString*/
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
        data |= (1 << 3);  /**data右边第n位置1: data或等1左移n*/
        data |= (1 << (32 - 3 - 1));  /**data左边第n位置1：data或等1左移(data位数-n-1)*/
        /**data右边第n位置0:
         * mask为0, data位数-1 ->=0:若i不n则mask或等(1左移i) 最外data与等mask
         * */
        int mask1 = 0;
        for (int i = 32 - 1; i >= 0; i --) {
            if (i != 3) mask1 |= (1 << i);
        }
        data &= mask1;
        /**data左边第n位置0: 同上换为 若i不data位数-n-1*/
        int mask2 = 0;
        for (int i = 32 - 1; i >= 0; i --) {
            if (i != (32 - 3 - 1)) mask2 |= (1 << i);
        }
        data &= mask2;
        /**翻转data所有位，上述mask，不要若, data^=mask*/
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
     * 两矩阵行列r1, c1, r2, c2. 若c1不r2就null. 建ret[r1][c2]. 0->r1: 0->c2: num0, 0->c1: num+= (a[i][k] * b[k][j]). 外ret[i][j] = num, 就ret*/
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
     * 空list作result, 数组null 0就result. carry进位为0. arrLen-1->=0: num为n * arr[i] + carry  result放num%10.
     * carry为num / 10. 外若carry>0, result放carry. 翻转result。就result
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
     * 空list作result. 两个list或null就null, 都0就result, 谁0就对方.
     * carry进位为0, 若lst1>lst2则temp法交换两lst. 得size1 2. size2-1->=0: num0,
     * 若i>=size2-size1: num为lst2[i] + lst1[i - size2 + size1] + carry,
     * 否则num为lst2[i] + carry.外result放num%10, carry为num/10. 外若carry>0: result放carry. 翻转result. 就result
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

