package utils.algorithm;

import org.junit.Test;

public class C11 {
    /** determine if a given integer is power of 2 */
    public static boolean isPowerOf2(int num){
        while ((num & 1) == 0) {
            num >>>= 1;
        }
        return num == 1;
    }
    @Test
    public void isPowerOf2Test(){
        System.out.println(isPowerOf2(8));
        System.out.println(isPowerOf2(9));
    }

    /** number of different bits of two int */
    public static int differentBits(int a, int b){
        int num = a ^ b;
        int count = 0;
        while (num > 0) {
            count += num & 1;
            num >>>= 1;
        }
        return count;
    }
    @Test
    public void differentBitsTest(){
        System.out.println(differentBits(8, 1));
    }

    /** determine if the chars of a string are all unique */
    public static boolean allUnique(String str){
        char[] chars = str.toCharArray();
        int[] arr = new int[8];
        for (char c : chars) {
            if ((1 & (arr[c / 32] >>> (c % 32))) != 0) return false;
            arr[c / 32] |= (1 << c % 32);
        }
        return true;
    }
    @Test
    public void allUniqueTest(){
        System.out.println(allUnique("abca"));
        System.out.println(allUnique("abc"));
    }

    /** Hexadecimal Representation */
    public static String toHex(int num){
        String prefix = "0x";
        if (num == 0) return "0x0";
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            int rest = num % 16;
            if (rest < 10) {
                sb.append((char)(rest + '0'));
            } else {
                sb.append((char)(rest - 10 + 'A'));
            }
            num >>>= 4;
        }
        return prefix + sb.reverse().toString();
    }
    @Test
    public void toHexTest(){
        System.out.println(toHex(16));
        System.out.println(toHex(20));
    }
    /** reverse bits */
    public static int reverseBits(int num){
        for (int i = 0; i < 16; i ++) {
            int left = 1 & (num >>> (32 - i - 1));
            int right = 1 & (num >>> i);
            if (left != right) {
                num ^= (1 << i | 1 << (32 - i - 1));
            }
        }
        return num;
    }
    @Test
    public void reverseBitsTest(){
        System.out.println(reverseBits(8));
    }
}
