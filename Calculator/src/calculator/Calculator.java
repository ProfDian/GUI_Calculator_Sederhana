package calculator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Calculator extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField display;
    private double result = 0;
    private String lastCommand = "=";
    private boolean start = true;

    public Calculator() {
        // Set up the frame
        setTitle("Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create display field - Increased size and font
        display = new JTextField("0");
        display.setPreferredSize(new Dimension(400, 100)); // Lebih besar
        display.setEditable(false);
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setFont(new Font("Arial", Font.BOLD, 36)); // Font lebih besar
        add(display, BorderLayout.NORTH);

        // Create button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(5, 4, 5, 5));

        // Add buttons
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
        
        // Set size and center the application
        setSize(400, 500);
        setLocationRelativeTo(null); // Menempatkan di tengah layar
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
                }
            } else {
                calculate(Double.parseDouble(display.getText()));
                lastCommand = command;
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
                    break;
                case "log":
                    result = Math.log10(x);
                    break;
                case "^":
                    result = Math.pow(result, x);
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