import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;
import java.text.DecimalFormat;

/**
 * Fibonacci number generator. GUI takes n input and outputs nth Fibonacci number. Uses fast doubling method.
 * Previously used Binet's formula using constant values for phi. Turns out, that's really slow compared to this.
 */
public class Fibonacci implements ActionListener {

    private String output; // Calculated value
    private JTextArea outputField = new JTextArea(); // Numbers get big. Big Field to output text to.
    private JTextField inputField = new JTextField(10); // Where you input n
    private JTextField timeOutput = new JTextField(6); // Shows time taken to calculate

    Fibonacci() {
        // Frame
        JFrame frame = new JFrame("Fibonacci");
        frame.setLayout(new FlowLayout());
        frame.setSize(470, 250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Buttons
        JButton calculate = new JButton("Calculate");
        calculate.addActionListener(this);
        JButton clear = new JButton("Clear");
        clear.addActionListener(this);

        //Label
        JLabel n = new JLabel("n:");
        JLabel timeTaken = new JLabel("Time Taken:");


        // Calculation output
        outputField.setLineWrap(true);
        outputField.setColumns(40);
        outputField.setRows(10);
        outputField.setEditable(false);
        outputField.setWrapStyleWord(true);
        timeOutput.setEditable(false);
        JScrollPane sPane = new JScrollPane(outputField);
        frame.add(sPane);

        // "input" contains input fields.
        JPanel input = new JPanel();
        input.add(n);
        input.add(inputField);
        input.add(calculate);
        input.add(clear);
        input.add(timeTaken);
        input.add(timeOutput);

        frame.add(input);
        frame.setVisible(true);

        // Default Button
        JRootPane jRootPane = frame.getRootPane();
        jRootPane.setDefaultButton(calculate);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Calculate")) { // Press "Calculate" button
            double startTime = System.nanoTime();
            fib(inputField.getText());
            double calcTime = (System.nanoTime() - startTime) / 1000000000;
            DecimalFormat decimalFormat = new DecimalFormat("##0.00000");
            String timeTaken = decimalFormat.format(calcTime) + "s";
            timeOutput.setText(timeTaken);
            outputField.setText(output);

        } else { // Press "Clear" button
            outputField.setText("");
            inputField.setText("");
            timeOutput.setText("");
        }
    }

    public void fib(String in) {
        if (in.matches("\\d+")) {
            if (in.equals("0") || in.equals("1")) {
                output = in;
            } else {
                BigInteger[] tuple = fib(Integer.parseInt(in));
                output = tuple[0].toString();
            }
        } else {
            output = "Invalid input. Please enter an integer.";
        }
    }

    /**
     * Given f(k) and f(k + 1)
     * f(2k) = f(k)[2f(k + 1) - f(k)]
     * f(2k + 1) = f(k + 1)^2 + f(k)^2
     * @param n fibonacci to generate
     * @return tuple with f(x) and f(x + 1)
     */
    private BigInteger[] fib(int n) {
        BigInteger[] tuple = new BigInteger[2];
        if (n <= 0) {
            tuple[0] = BigInteger.ZERO;
            tuple[1] = BigInteger.ONE;
            return tuple;
        }
        if (n == 1) {
            tuple[0] = BigInteger.ONE;
            tuple[1] = BigInteger.ONE;
            return tuple;
        }
        BigInteger[] ab = fib(n / 2);
        BigInteger a = ab[0];
        BigInteger b = ab[1];
        BigInteger c = a.multiply((BigInteger.valueOf(2).multiply(b)).subtract(a));
        BigInteger d = (a.multiply(a)).add((b.multiply(b)));
        if (n % 2 == 0) {
            tuple[0] = c;
            tuple[1] = d;
            return tuple;
        } else {
            tuple[0] = d;
            tuple[1] = c.add(d);
            return tuple;
        }
    }

    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Nimbus not available");
        }
        SwingUtilities.invokeLater(Fibonacci::new);
    }
}