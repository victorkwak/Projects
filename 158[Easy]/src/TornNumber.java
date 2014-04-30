import java.util.*;

/**
 * I had the other day in my possession a label bearing the number 3 0 2 5 in large figures.
 * This got accidentally torn in half, so that 3 0 was on one piece and 2 5 on the other. On looking
 * at these pieces I began to make a calculation, when I discovered this little peculiarity. If we
 * add the 3 0 and the 2 5 together and square the sum we get as the result, the complete original number
 * on the label! Thus, 30 added to 25 is 55, and 55 multiplied by 55 is 3025. Curious, is it not?
 * <p>
 * Now, the challenge is to find another number, composed of four figures, all different,
 * which may be divided in the middle and produce the same result.
 */
public class TornNumber {
    private static ArrayList<Long> tornArray = new ArrayList<>();

    public static long divisor(long numberLength) {
        StringJoiner stringJoiner = new StringJoiner("");
        for (int i = 0; i < numberLength / 2; i++) {
            stringJoiner.add("0");
        }
        String modifier = "1" + stringJoiner;
        return Long.parseLong(modifier);
    }

    public static void tornNumber(long num, long divisor) {
        long n1 = num / divisor;
        long n2 = num % divisor;
        long add = n1 + n2;
        long squared = add * add;
        if (squared == num) {
            tornArray.add(num);
        }
    }

    public static void checkDuplicate() {
        for (long e : tornArray) {
            Set<String> longSet = new HashSet<>();
            boolean duplicate = false;
            String[] split = String.valueOf(e).split("");
            for (String i : split) {
                if (longSet.contains(i)) {
                    duplicate = true;
                }
                longSet.add(i);
            }
            if (!duplicate) {
                System.out.println(e);
            }
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Length of number (even numbers only): ");

        int numberLength = scanner.nextInt();
        if (numberLength % 2 != 0) {
            System.out.println("Number not even. Exiting...");
            System.exit(0);
        }
        long divisor = divisor(numberLength);
        long max = (long) (divisor * Math.pow(10, numberLength / 2));
        long min = max / 10;

        for (long i = min; i < max; i++) {
            tornNumber(i, divisor);
        }
        checkDuplicate();
        //comments!!!!!!
    }
}