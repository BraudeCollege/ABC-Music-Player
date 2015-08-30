package player.visitor;

/**
 * Collection of math helper functions
 */
class MathHelpers
{
    /**
     * @param a
     * @param b
     * @return greatest common divisor of a and b
     */
    public static int gcd(int a, int b)
    {
        while (b > 0) {
            int temp = b;
            b = a % b; // % is remainder
            a = temp;
        }

        return a;
    }

    /**
     * @param numbers numbers must have at least one element
     * @return the lowest common multiple of a list of numbers
     */
    public static int lcm(int[] numbers)
    {
        int result = numbers[0];
        for (int i = 1; i < numbers.length; i++) result = lcm(result, numbers[i]);
        return result;
    }

    /**
     * @param a
     * @param b
     * @return the lowest common multiple of a and b
     */
    private static int lcm(int a, int b)
    {
        return a * (b / gcd(a, b));
    }
}
