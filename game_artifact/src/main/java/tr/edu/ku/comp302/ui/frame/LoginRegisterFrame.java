package tr.edu.ku.comp302.ui.frame;

import tr.edu.ku.comp302.ui.panel.LoginPanel;
import tr.edu.ku.comp302.ui.panel.RegisterPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class LoginRegisterFrame extends JFrame {

    private JPanel contentPane;
    private RegisterPanel registerPanel;
    private LoginPanel loginPanel;

    public LoginRegisterFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 828, 828);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        contentPane.setLayout(null);
        setVisible(true);
        setContentPane(contentPane);

        createLoginPanel();
        createRegisterPanel();
        registerPanel.setVisible(false);
        loginPanel.setVisible(true);

    }

    public LoginPanel getLoginPanel() {
        return loginPanel;
    }

    public RegisterPanel getRegisterPanel() {
        return registerPanel;
    }
    private void createLoginPanel() {
        loginPanel = new LoginPanel();
        loginPanel.setBounds(0, 11, 804, 781);
        loginPanel.setLayout(null);
        loginPanel.setLoginRegisterFrame(this);
        contentPane.add(loginPanel);
    }

    private void createRegisterPanel() {
        registerPanel = new RegisterPanel();
        registerPanel.setBounds(0, 11, 804, 781);
        registerPanel.setLayout(null);
        registerPanel.setLoginRegisterFrame(this);
        contentPane.add(registerPanel);
    }

}
