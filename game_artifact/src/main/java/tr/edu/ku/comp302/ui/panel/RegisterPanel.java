package tr.edu.ku.comp302.ui.panel;


import tr.edu.ku.comp302.domain.handler.RegisterHandler;
import tr.edu.ku.comp302.ui.frame.LoginRegisterFrame;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JPasswordField;
import java.awt.Component;

import javax.swing.Box;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.UIManager;
import java.awt.Color;

public class RegisterPanel extends JPanel {

    protected JTextField usernameTextField;
    private JLabel confirmPwdLabel;
    protected JPasswordField pwdField;
    protected JPasswordField confirmPwdField;
    private LoginRegisterFrame loginRegisterFrame;
    protected JTextArea errorTextArea;

    public RegisterPanel() {
        setLayout(null);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(232, 68, 137, 38);
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usernameLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        add(usernameLabel);

        usernameTextField = new JTextField();
        usernameTextField.setBounds(374, 68, 216, 38);
        usernameTextField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        add(usernameTextField);
        usernameTextField.setColumns(10);

        Component horizontalStrut = Box.createHorizontalStrut(20);
        horizontalStrut.setBounds(0, 0, 0, 0);
        add(horizontalStrut);

        JLabel pwdLabel = new JLabel("Password:");
        pwdLabel.setBounds(232, 111, 137, 41);
        pwdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        pwdLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        add(pwdLabel);

        pwdField = new JPasswordField();
        pwdField.setBounds(374, 114, 216, 38);
        pwdField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        add(pwdField);

        confirmPwdLabel = new JLabel("Confirm Password:");
        confirmPwdLabel.setBounds(153, 157, 216, 43);
        confirmPwdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        confirmPwdLabel.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        add(confirmPwdLabel);

        confirmPwdField = new JPasswordField();
        confirmPwdField.setBounds(374, 162, 216, 38);
        confirmPwdField.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        add(confirmPwdField);

        errorTextArea = new JTextArea();
        errorTextArea.setWrapStyleWord(true);
        errorTextArea.setLineWrap(true);
        errorTextArea.setForeground(Color.RED);
        errorTextArea.setFont(new Font("Segoe UI", Font.BOLD, 16));
        errorTextArea.setEditable(false);
        errorTextArea.setBackground(UIManager.getColor("Button.background"));
        errorTextArea.setBounds(374, 211, 216, 77);
        add(errorTextArea);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = new String(pwdField.getPassword());
            String passwordRepeat = new String(confirmPwdField.getPassword());
            int response = RegisterHandler.getInstance().register(username, password, passwordRepeat);

            switch(response) {
                case RegisterHandler.SUCCESS:
                    new MainFrame();
                    // TODO: Create user object
                    clearInputs();
                    break;
                case RegisterHandler.USERNAME_TOO_SHORT:
                    errorTextArea.setText("Number of characters in username must be greater than 2");
                    break;
                case RegisterHandler.USERNAME_TOO_LONG:
                    errorTextArea.setText("Number of characters in username must be smaller than 33");
                    break;
                case RegisterHandler.USERNAME_NOT_UNIQUE:
                    errorTextArea.setText("Account already exists with that username");
                    break;
                case RegisterHandler.PASSWORD_TOO_SHORT:
                    errorTextArea.setText("Number of characters in password must be greater than 8");
                    break;
                case RegisterHandler.PASSWORD_TOO_LONG:
                    errorTextArea.setText("Number of characters in password must be greater than 8");
                    break;
                case RegisterHandler.PASSWORDS_DO_NOT_MATCH:
                    errorTextArea.setText("Passwords do not match");
                    break;
                default:
                    errorTextArea.setText("Womp Womp");
                    break;
            }
        });
        registerButton.setBounds(418, 311, 147, 43);
        registerButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 22));
        add(registerButton);

        JLabel hasAccountLabel = new JLabel("Do you have an account?");
        hasAccountLabel.setBounds(253, 361, 227, 35);
        hasAccountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        hasAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        add(hasAccountLabel);

        JButton loginButton = new JButton("Login");

        loginButton.setBounds(490, 362, 108, 35);
        loginButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        add(loginButton);


        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearInputs();
                setVisible(false);
                getLoginRegisterFrame().getLoginPanel().setVisible(true);
            }
        });
    }

    public void setLoginRegisterFrame(LoginRegisterFrame loginRegisterFrame) {
        this.loginRegisterFrame = loginRegisterFrame;
    }

    public LoginRegisterFrame getLoginRegisterFrame() {
        return loginRegisterFrame;
    }

    public JTextField getUsernameTextField() {
        return usernameTextField;
    }

    public void setUsernameTextField(JTextField usernameTextField) {
        this.usernameTextField = usernameTextField;
    }

    public JLabel getConfirmPwdLabel() {
        return confirmPwdLabel;
    }

    public void setConfirmPwdLabel(JLabel confirmPwdLabel) {
        this.confirmPwdLabel = confirmPwdLabel;
    }

    public JPasswordField getPwdField() {
        return pwdField;
    }

    public void setPwdField(JPasswordField pwdField) {
        this.pwdField = pwdField;
    }

    public JPasswordField getConfirmPwdField() {
        return confirmPwdField;
    }

    public void setConfirmPwdField(JPasswordField confirmPwdField) {
        this.confirmPwdField = confirmPwdField;
    }

    private void clearInputs(){
        usernameTextField.setText("");
        pwdField.setText("");
        confirmPwdField.setText("");
        errorTextArea.setText("");
    }
}
