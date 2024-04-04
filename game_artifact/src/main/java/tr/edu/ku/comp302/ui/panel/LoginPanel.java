package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.ui.frame.LoginRegisterFrame;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {

    private LoginRegisterFrame loginRegisterFrame;
    private JPasswordField pwdField;
    private JTextField usernameTextField;

    public LoginPanel() {
        setLayout(null);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
                // TODO: Add setVisible for register panel with true input, after creating registerPanel.
            }
        });
        registerButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        registerButton.setBounds(479, 356, 118, 35);
        add(registerButton);

        JLabel dontHaveAnAccountLabel = new JLabel("Don't have an account?");
        dontHaveAnAccountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dontHaveAnAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dontHaveAnAccountLabel.setBounds(263, 355, 206, 35);
        add(dontHaveAnAccountLabel);

        JButton loginButton = new JButton("Login");
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

    }

    public void setLoginRegisterFrame(LoginRegisterFrame loginRegisterFrame) {
        this.loginRegisterFrame = loginRegisterFrame;
    }

    public LoginRegisterFrame getLoginRegisterFrame() {
        return loginRegisterFrame;
    }

    public JPasswordField getPwdField() {
        return pwdField;
    }

    public void setPwdField(JPasswordField pwdField) {
        this.pwdField = pwdField;
    }

    public JTextField getUsernameTextField() {
        return usernameTextField;
    }

    public void setUsernameTextField(JTextField usernameTextField) {
        this.usernameTextField = usernameTextField;
    }


}