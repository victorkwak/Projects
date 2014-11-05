import java.util.Scanner;

/**
 * Victor Kwak
 * October 21, 2014
 * MicroEcon class assignment had me calculating a lot of trivial formulas for online hw.
 */
public class Elasticity {
    public static void main(String[] args) {
        double q1, q2, p1, p2, answer;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Q1: ");
            q1 = scanner.nextDouble();

            System.out.print("Q2: ");
            q2 = scanner.nextDouble();

            System.out.print("P1: ");
            p1 = scanner.nextDouble();

            System.out.print("P2: ");
            p2 = scanner.nextDouble();

            answer = ((2 * (q2 - q1)) / (q2 + q1)) / ((2 * (p2 - p1)) / (p2 + p1));
            System.out.println(answer);
            System.out.println();
        }

    }
}
