package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.handler.LoginHandler;
import tr.edu.ku.comp302.ui.frame.LoginRegisterFrame;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class LoginPanel extends JPanel {

    private LoginRegisterFrame loginRegisterFrame;
    protected JPasswordField pwdField;
    protected JTextField usernameTextField;
    protected JTextArea errorTextArea;

    public LoginPanel() {
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
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                clearInputs();
                setVisible(false);
                loginRegisterFrame.getRegisterPanel().setVisible(true);

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
        loginButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = new String(pwdField.getPassword());
            int response = LoginHandler.getInstance().login(username, password);
            switch(response) {
                case LoginHandler.SUCCESS:
                    // new MainFrame();
                    // TODO: Create user instance and open his game frame.
                    JOptionPane.showMessageDialog(null, "Success", "Successfully logged in.", JOptionPane.INFORMATION_MESSAGE);
                    clearInputs();
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

    private void clearInputs(){
        usernameTextField.setText("");
        pwdField.setText("");
        errorTextArea.setText("");
    }
}