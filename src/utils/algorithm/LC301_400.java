package utils.algorithm;

public class LC301_400 {
    /**
     * LC326 Power of three
     * */
    public boolean isPowerOfThree(int n) {
        if (n == 0) return false;
        while (n % 3 == 0) {
            n /= 3;
        }
        return n == 1;
    }
    /**
     * LC342 Power of four
     * */
    public boolean isPowerOfFour(int num) {
        if (num <= 0) return false;
        while (num % 4 == 0) num /= 4;
        return num == 1;
    }
}
