package tr.edu.ku.comp302.ui.frame;

import tr.edu.ku.comp302.ui.panel.LoginPanel;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

public class LoginRegisterFrame extends JFrame {

    private JPanel contentPane;
    // TODO: Add RegisterPanel field.
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
        // TODO: use createRegisterPanel() method in here.
        // TODO: Add registerPanel setVisible with input false.
        loginPanel.setVisible(true);

    }


    public LoginPanel getLoginPanel() {
        return loginPanel;
    }


    private void createLoginPanel() {
        loginPanel = new LoginPanel();
        loginPanel.setBounds(0, 11, 804, 781);
        loginPanel.setLayout(null);
        loginPanel.setLoginRegisterFrame(this);
        contentPane.add(loginPanel);
    }

    // TODO: getRegisterPanel

    // TODO: Implement createRegisterPanel() method in here.


}