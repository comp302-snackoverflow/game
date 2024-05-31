package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.event.KeyPressHandler;
import tr.edu.ku.comp302.domain.handler.LoginHandler;
import tr.edu.ku.comp302.ui.frame.MainFrame;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    private final JPasswordField pwdField;
    private final JTextField usernameTextField;
    private final JTextArea errorTextArea;
    private final JButton loginButton;
    private BufferedImage backgroundImage;

    public LoginPanel(MainFrame mainFrame) {
        Font buttonFont = new Font("Segoe UI Semibold", Font.PLAIN, 22);

        setLayout(new GridBagLayout());
        try {
            backgroundImage = ImageIO.read(getClass().getResource("/assets/light_sat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        errorTextArea = new JTextArea();
        errorTextArea.setFocusable(false);
        errorTextArea.setWrapStyleWord(true);
        errorTextArea.setForeground(Color.RED);
        errorTextArea.setLineWrap(true);
        errorTextArea.setEditable(false);
        errorTextArea.setFont(new Font("Segoe UI", Font.BOLD, 16));
        errorTextArea.setOpaque(false); // Make the error text area transparent

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            clearInputs();
            mainFrame.showRegisterPanel();
        });

        registerButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));

        JLabel dontHaveAnAccountLabel = new JLabel("Don't have an account?");
        dontHaveAnAccountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        dontHaveAnAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        dontHaveAnAccountLabel.setOpaque(false); // Ensure label background is transparent

        loginButton = new JButton("Login");
        loginButton.setFont(buttonFont);

        KeyPressHandler.bindKeyPressAction(this, "ENTER", e -> loginButton.doClick());

        pwdField = new JPasswordField();
        pwdField.setFont(buttonFont);

        usernameTextField = new JTextField();
        usernameTextField.setFont(buttonFont);
        usernameTextField.setColumns(10);

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usernameLabel.setFont(buttonFont);

        JLabel pwdLabel = new JLabel("Password:");
        pwdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        pwdLabel.setFont(buttonFont);

        loginButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = new String(pwdField.getPassword());
            int response = LoginHandler.getInstance().login(username, password);
            switch (response) {
                case LoginHandler.SUCCESS -> {
                    clearInputs();
                    mainFrame.showMainMenuPanel();
                }
                case LoginHandler.USERNAME_EMPTY -> errorTextArea.setText("You must enter a username!");
                case LoginHandler.PASSWORD_EMPTY -> errorTextArea.setText("You must enter a password!");
                case LoginHandler.USER_NOT_FOUND -> errorTextArea.setText("Username or password is not valid!");
                default -> errorTextArea.setText("Womp womp");
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 10, 5);
        add(usernameLabel, gbc);

        gbc.gridx = 2;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 5, 10, 0);
        add(usernameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 5);
        add(pwdLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 5, 10, 0);
        add(pwdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(errorTextArea, gbc);

        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(loginButton, gbc);

        gbc.gridy = 4;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;

        JPanel registrationSection = new JPanel();
        registrationSection.setLayout(new BoxLayout(registrationSection, BoxLayout.X_AXIS));
        registrationSection.setOpaque(false); // Make the panel transparent
        registrationSection.add(dontHaveAnAccountLabel);
        registrationSection.add(Box.createHorizontalStrut(10));
        registrationSection.add(registerButton);

        add(registrationSection, gbc);
    }

    private void clearInputs() {
        usernameTextField.setText("");
        pwdField.setText("");
        errorTextArea.setText("");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}