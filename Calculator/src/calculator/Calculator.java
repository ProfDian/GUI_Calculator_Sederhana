package calculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField display;
    private JLabel operationSign;
    private JLabel previousOperand;
    private double result = 0;
    private String lastCommand = "=";
    private boolean start = true;

    public Calculator() {
        setTitle("Calculator dengan GUI Sederhana");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        display = new JTextField("0");
        display.setPreferredSize(new Dimension(400, 100));
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Arial", Font.BOLD, 31));
        
        operationSign = new JLabel("");
        operationSign.setHorizontalAlignment(JLabel.RIGHT);
        operationSign.setFont(new Font("Arial", Font.PLAIN, 16));
        
        previousOperand = new JLabel("");
        previousOperand.setHorizontalAlignment(JLabel.RIGHT);
        previousOperand.setFont(new Font("Arial", Font.PLAIN, 16));
        
        JPanel displayPanel = new JPanel(new BorderLayout());
        displayPanel.add(display, BorderLayout.CENTER);
        
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.add(previousOperand, BorderLayout.WEST);
        infoPanel.add(operationSign, BorderLayout.EAST);
        
        displayPanel.add(infoPanel, BorderLayout.NORTH);
        
        add(displayPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));

        String[] buttonLabels = {
            "7", "8", "9", "/",
            "4", "5", "6", "*",
            "1", "2", "3", "-",
            "0", ".", "=", "+",
            "log", "^", "C", "←"
        };

        for (String label : buttonLabels) {
            JButton button = new JButton(label);
            buttonPanel.add(button);
            if (Character.isDigit(label.charAt(0)) || label.equals(".")) {
                button.addActionListener(new NumberListener());
            } else {
                button.addActionListener(new CommandListener());
            }
        }

        add(buttonPanel, BorderLayout.CENTER);
        
        setSize(400, 500);
        setLocationRelativeTo(null);
    }

    private class NumberListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String input = event.getActionCommand();
            if (start) {
                display.setText(input);
                start = false;
            } else {
                display.setText(display.getText() + input);
            }
        }
    }

    private class CommandListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String command = event.getActionCommand();

            if (command.equals("C")) {
                result = 0;
                display.setText("0");
                operationSign.setText("");
                previousOperand.setText("");
                lastCommand = "=";
                start = true;
                return;
            }

            if (command.equals("←")) {
                String currentText = display.getText();
                if (currentText.length() > 0) {
                    display.setText(currentText.substring(0, currentText.length() - 1));
                    if (display.getText().isEmpty()) {
                        display.setText("0");
                        start = true;
                    }
                }
                return;
            }

            if (start) {
                if (command.equals("-")) {
                    display.setText(command);
                    start = false;
                } else {
                    lastCommand = command;
                    operationSign.setText(command);
                }
            } else {
                double x = Double.parseDouble(display.getText());
                previousOperand.setText(display.getText());
                calculate(x);
                lastCommand = command;
                operationSign.setText(command);
                start = true;
            }
        }
    }

    public void calculate(double x) {
        try {
            switch (lastCommand) {
                case "+":
                    result += x;
                    break;
                case "-":
                    result -= x;
                    break;
                case "*":
                    result *= x;
                    break;
                case "/":
                    if (x == 0) {
                        display.setText("You Can't Do That Buddy");
                        start = true;
                        return;
                    }
                    result /= x;
                    break;
                case "=":
                    result = x;
                    operationSign.setText("");
                    previousOperand.setText("");
                    break;
                case "log":
                    result = Math.log10(x);
                    operationSign.setText("");
                    previousOperand.setText("");
                    break;
                case "^":
                    result = Math.pow(result, x);
                    operationSign.setText("");
                    previousOperand.setText("");
                    break;
            }
            display.setText("" + result);
        } catch (Exception e) {
            display.setText("Error");
            start = true;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Calculator calc = new Calculator();
            calc.setVisible(true);
        });
    }
}