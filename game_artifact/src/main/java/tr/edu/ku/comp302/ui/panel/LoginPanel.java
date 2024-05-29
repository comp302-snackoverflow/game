package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.handler.LoginHandler;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginPanel extends JPanel {
    private final MainFrame mainFrame;
    private final JPasswordField pwdField;
    private final JTextField usernameTextField;
    private final JTextArea errorTextArea;
    private final JButton loginButton;

    public LoginPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);

        errorTextArea = new JTextArea();
        errorTextArea.setWrapStyleWord(true);
        errorTextArea.setForeground(Color.RED);
        errorTextArea.setLineWrap(true);
        errorTextArea.setEditable(false);
        errorTextArea.setFont(new Font("Segoe UI", Font.BOLD, 16));
        errorTextArea.setBounds(381, 217, 216, 77);
        errorTextArea.setBackground(new Color(240, 240, 240));
        add(errorTextArea);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            clearInputs();
            mainFrame.showRegisterPanel();

        });
        registerButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        registerButton.setBounds(479, 356, 118, 35);
        add(registerButton);

        JLabel dontHaveAnAccountLabel = new JLabel("Don't have an account?");
        dontHaveAnAccountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dontHaveAnAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dontHaveAnAccountLabel.setBounds(263, 355, 206, 35);
        add(dontHaveAnAccountLabel);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        loginButton.setBounds(417, 305, 147, 43);
        add(loginButton);

        pwdField = new JPasswordField();
        pwdField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        pwdField.setBounds(381, 168, 216, 38);
        add(pwdField);

        usernameTextField = new JTextField();
        usernameTextField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        usernameTextField.setColumns(10);
        usernameTextField.setBounds(381, 122, 216, 38);
        add(usernameTextField);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usernameLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        usernameLabel.setBounds(251, 122, 125, 38);
        add(usernameLabel);

        JLabel pwdLabel = new JLabel("Password:");
        pwdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        pwdLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        pwdLabel.setBounds(251, 165, 125, 41);
        add(pwdLabel);

        loginButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = new String(pwdField.getPassword());
            int response = LoginHandler.getInstance().login(username, password);
            switch (response) {
                case LoginHandler.SUCCESS:
                    JOptionPane.showMessageDialog(null, "Successfully logged in.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearInputs();
                    mainFrame.showMainMenuPanel();
                    // TODO: Create user instance and open his game frame.
                    break;
                case LoginHandler.USERNAME_EMPTY:
                    errorTextArea.setText("You must enter a username!");
                    break;
                case LoginHandler.PASSWORD_EMPTY:
                    errorTextArea.setText("You must enter a password!");
                    break;
                case LoginHandler.USER_NOT_FOUND:
                    errorTextArea.setText("Username or password is not valid!");
                    break;
                default:
                    errorTextArea.setText("Womp womp");
                    break;
            }
        });
    }

    private void clearInputs() {
        usernameTextField.setText("");
        pwdField.setText("");
        errorTextArea.setText("");
    }

    public void registerEnterClick() {
        SwingUtilities.getRootPane(this).setDefaultButton(loginButton);
    }
}
