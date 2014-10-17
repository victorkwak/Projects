import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

/**
 * Victor Kwak
 * CS240
 * Project 3 - Stack Application
 * This program takes an input from the user (infix expression) and converts it into either a postfix or prefix
 * expression. The program features a GUI and the user converts the expression by pressing either the
 * "Infix to Postfix" or the "Infix to Prefix" buttons.
 */
public class Project3 extends JFrame implements ActionListener {
    JButton postfix;
    JButton prefix;
    JTextField converted;
    JTextField infixField;

    /**
     * Constructor (for GUI)
     */
    public Project3() {
        setTitle("Infix Converter");
        setLayout(new FlowLayout());
        setSize(300, 140);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        infixField = new JTextField(23);
        postfix = new JButton("Infix to Postfix");
        postfix.addActionListener(this);
        prefix = new JButton("Infix to Prefix");
        prefix.addActionListener(this);
        converted = new JTextField(23);
        converted.setEditable(false);
        add(infixField);
        add(postfix);
        add(prefix);
        add(converted);
        setVisible(true);
    }

    /**
     * @param input user's input
     * @return true if input is valid, false otherwise
     */
    public boolean inputValidation(String input) {
        int pCounter = 0;
        boolean operand = true;
        boolean operator = true;
        String current;
        for (int i = 0; i < input.length(); i++) {
            current = input.substring(i, i + 1);
            if (current.equals("(")) {
                pCounter++;
                if (operand && operator && i > 0) {
                    converted.setForeground(Color.RED);
                    converted.setText("Invalid input");
                    return false;
                } else if (!operand && operator) {
                    converted.setForeground(Color.RED);
                    converted.setText("Parenthesis next to operator ");
                    return false;
                }
                if (!operand) {
                    operand = true;
                    operator = true;
                }
            } else if (current.equals(")")) {
                pCounter--;
            } else if (current.matches("[+*/%-]")) {
                if (!operand) {
                    operator = false;
                } else if (i > 0) {
                    operator = false;
                    operand = false;
                } else {
                    converted.setForeground(Color.RED);
                    converted.setText("Misplaced operator");
                    return false;
                }
            } else if (current.matches("[a-zA-Z]")) {
                if (!operand && operator) {
                    converted.setForeground(Color.RED);
                    converted.setText("Misplaced operand");
                    return false;
                }
                operand = !operand;
                if (!operator) {
                    operator = true;
                }
            } else if (!current.equals(" ")) {
                converted.setForeground(Color.RED);
                converted.setText("Invalid Character");
                return false;
            }
        }
        boolean parentheses = pCounter == 0;
        if (!parentheses) {
            converted.setForeground(Color.RED);
            converted.setText("Mismatched parentheses");
            return false;
        }
        return operand && operator;
    }

    /**
     * @param operator operator being read
     * @return a integer reflecting the precedence of the operators
     */
    public int operatorOrder(char operator) {
        if (operator == '(') {
            return 0;
        } else if (operator == '+' || operator == '-') {
            return 1;
        } else if (operator == '*' || operator == '/' || operator == '%') {
            return 2;
        } else {
            return 3;
        }
    }

    /**
     *
     * @param infix user's input
     * @return a string converted to a postfix expression
     */
    public String infixToPostfix(String infix) {
        infix = infix.replaceAll("\\s", "");
        Stack<Character> stack = new Stack<Character>();
        String postFix = "";
        char temp;
        for (int i = 0; i < infix.length(); i++) {
            temp = infix.charAt(i);
            if (Character.isLetter(temp)) {
                postFix += temp;
            } else if (temp == '(') {
                stack.push(temp);
            } else if (temp == ')') {
                while ((temp = stack.pop()) != '(') {
                    postFix += temp;
                }
            } else if (stack.empty()) {
                stack.push(temp);
            } else if (operatorOrder(temp) > operatorOrder(stack.peek())) {
                stack.push(temp);
            } else if (operatorOrder(temp) <= operatorOrder(stack.peek())) {
                do {
                    postFix += stack.pop();
                    if (stack.empty()) {
                        break;
                    }
                } while (temp <= operatorOrder(stack.peek()));
                stack.push(temp);
            }
        }
        while (!stack.empty()) {
            postFix += stack.pop();
        }
        return postFix;
    }

    /**
     *
     * @param infix user's input
     * @return a string converted to a prefix expression
     */
    public String infixToPrefix(String infix) {
        infix = infix.replaceAll("\\s", "");
        Stack<Character> operators = new Stack<Character>();
        Stack<String> operands = new Stack<String>();
        String operator;
        String rightOperand;
        String leftOperand;
        char temp;
        for (int i = 0; i < infix.length(); i++) {
            temp = infix.charAt(i);
            if (Character.isLetter(temp)) {
                operands.push(String.valueOf(temp));
            } else if (temp == '(') {
                operators.push(temp);
            } else if (temp == ')') {
                do {
                    operator = operators.pop().toString();
                    rightOperand = operands.pop();
                    leftOperand = operands.pop();
                    operands.push(operator + leftOperand + rightOperand);
                } while (operators.peek() != '(');
                operators.pop();
            } else if (operators.empty()) {
                operators.push(temp);
            } else if (operatorOrder(temp) > operatorOrder(operators.peek())) {
                operators.push(temp);
            } else if (operatorOrder(temp) <= operatorOrder(operators.peek())) {
                do {
                    operator = operators.pop().toString();
                    rightOperand = operands.pop();
                    leftOperand = operands.pop();
                    operands.push(operator + leftOperand + rightOperand);
                    if (operators.empty()) {
                        break;
                    }
                } while (operatorOrder(temp) <= operatorOrder(operators.peek()));
                operators.push(temp);
            }
        }
        while (!operators.empty()) {
            operator = operators.pop().toString();
            rightOperand = operands.pop();
            leftOperand = operands.pop();
            operands.push(operator + leftOperand + rightOperand);
        }
        return operands.pop();
    }

    /**
     *
     * @param e events of prefix and postfix buttons
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("Infix to Postfix")) {
            if (inputValidation(infixField.getText())) {
                converted.setForeground(Color.BLACK);
                converted.setText(infixToPostfix(infixField.getText()));
            }
        } else if (e.getActionCommand().equals("Infix to Prefix")) {
            if (inputValidation(infixField.getText())) {
                converted.setForeground(Color.BLACK);
                converted.setText(infixToPrefix(infixField.getText()));
            }
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
            System.out.println("Nimbus not available!? I guess the ugly Metal look will suffice...");
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new Project3();
            }
        });
    }
}
