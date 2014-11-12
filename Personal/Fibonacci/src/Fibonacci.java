import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * Fibonacci number generator. GUI takes n input and outputs nth Fibonacci number. Uses closed form Binet's formula
 * using an approximation of phi.
 */
public class Fibonacci implements ActionListener {

    private static String output;
    private static JTextArea outputField = new JTextArea();
    private static JTextField inputField = new JTextField(10);

    Fibonacci() {
        // Frame
        JFrame frame = new JFrame("Fibonacci");
        frame.setLayout(new FlowLayout());
        frame.setSize(400, 250);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        //Buttons
        JButton calculate = new JButton("Calculate");
        calculate.addActionListener(this);
        JButton clear = new JButton("Clear");
        clear.addActionListener(this);

        //Label
        JLabel n = new JLabel("n:");

        //Configure output box
        outputField.setLineWrap(true);
        outputField.setColumns(30);
        outputField.setRows(10);
        outputField.setEditable(false);
        outputField.setWrapStyleWord(true);
        JScrollPane sPane = new JScrollPane(outputField);
        frame.add(sPane);

        // "other" contains everything besides the outputField. If buttons are just added to the frame, scaling
        // becomes kind of weird.
        JPanel other = new JPanel();
        other.add(n);
        other.add(inputField);
        other.add(calculate);
        other.add(clear);
        frame.add(other);

        frame.setVisible(true);

        // Default Button
        JRootPane jRootPane = frame.getRootPane();
        jRootPane.setDefaultButton(calculate);
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Calculate")) { // Press "Calculate" button
            fib(inputField.getText());
            outputField.setText(output);

        } else { // Press "Clear" button
            outputField.setText("");
            inputField.setText("");
        }
    }

    public static void fib(String in) {
        BigDecimal phi;

        if (in.equals("0") || in.equals("1")) {
            output = in;
        } else {
            // tiered phi values
            if (Integer.parseInt(in) <= 200) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576");
            } else if (Integer.parseInt(in) <= 400) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374");
            } else if (Integer.parseInt(in) < 600) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374" +
                        "84754088075386891752126633862223536931793180060766");
            } else if (Integer.parseInt(in) < 900) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374" +
                        "84754088075386891752126633862223536931793180060766" +
                        "72635443338908659593958290563832266131992829026788");
            } else if (Integer.parseInt(in) < 1100) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374" +
                        "84754088075386891752126633862223536931793180060766" +
                        "72635443338908659593958290563832266131992829026788" +
                        "06752087668925017116962070322210432162695486262963");
            } else if (Integer.parseInt(in) < 1500) {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374" +
                        "84754088075386891752126633862223536931793180060766" +
                        "72635443338908659593958290563832266131992829026788" +
                        "06752087668925017116962070322210432162695486262963" +
                        "13614438149758701220340805887954454749246185695364" +
                        "86444924104432077134494704956584678850987433944221");

            } else {
                phi = new BigDecimal("1.61803398874989484820458683436563811772030917980576" +
                        "28621354486227052604628189024497072072041893911374" +
                        "84754088075386891752126633862223536931793180060766" +
                        "72635443338908659593958290563832266131992829026788" +
                        "06752087668925017116962070322210432162695486262963" +
                        "13614438149758701220340805887954454749246185695364" +
                        "86444924104432077134494704956584678850987433944221" +
                        "25448770664780915884607499887124007652170575179788" +
                        "34166256249407589069704000281210427621771117778053" +
                        "15317141011704666599146697987317613560067087480710" +
                        "131795236894275219484353056783002287856997829");
            }
            BigDecimal psi = BigDecimal.ONE.subtract(phi); //Recipricol of phi
            BigDecimal value = //Binets Formula
                    (phi.pow(Integer.parseInt(in)).subtract(psi.pow(Integer.parseInt(in)))).divide(phi.subtract(psi));

            DecimalFormat decimalFormat = new DecimalFormat();
            decimalFormat.setMaximumFractionDigits(0); // No decimals
            decimalFormat.setGroupingUsed(false);
            output = decimalFormat.format(value);
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