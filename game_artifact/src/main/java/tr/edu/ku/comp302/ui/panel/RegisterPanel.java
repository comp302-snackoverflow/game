package tr.edu.ku.comp302.ui.panel;

import tr.edu.ku.comp302.domain.event.KeyPressHandler;
import tr.edu.ku.comp302.domain.handler.RegisterHandler;
import tr.edu.ku.comp302.ui.frame.MainFrame;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private final JTextField usernameTextField;
    private final JPasswordField pwdField;
    private final JPasswordField confirmPwdField;
    private final JTextArea errorTextArea;
    private BufferedImage backgroundImage;

    public RegisterPanel(MainFrame mainFrame) {
        Font font = new Font("Segoe UI Semibold", Font.PLAIN, 22);
        setLayout(new GridBagLayout());

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        usernameLabel.setFont(font);

        usernameTextField = new JTextField();
        usernameTextField.setFont(font);
        usernameTextField.setColumns(10);
        usernameTextField.setOpaque(true);
        usernameTextField.setBorder(null);

        JLabel pwdLabel = new JLabel("Password:");
        pwdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        pwdLabel.setFont(font);

        pwdField = new JPasswordField();
        pwdField.setFont(font);
        pwdField.setOpaque(true);
        pwdField.setBorder(null);

        JLabel confirmPwdLabel = new JLabel("Confirm Password:");
        confirmPwdLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        confirmPwdLabel.setFont(font);

        confirmPwdField = new JPasswordField();
        confirmPwdField.setFont(font);
        confirmPwdField.setOpaque(true);
        confirmPwdField.setBorder(null);

        errorTextArea = new JTextArea();
        errorTextArea.setWrapStyleWord(true);
        errorTextArea.setLineWrap(true);
        errorTextArea.setForeground(Color.RED);
        errorTextArea.setFont(new Font("Segoe UI", Font.BOLD, 16));
        errorTextArea.setEditable(false);
        errorTextArea.setOpaque(false);
        errorTextArea.setBackground(new Color(0, 0, 0, 0));

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> {
            String username = usernameTextField.getText();
            String password = new String(pwdField.getPassword());
            String passwordRepeat = new String(confirmPwdField.getPassword());
            int response = RegisterHandler.getInstance().register(username, password, passwordRepeat);

            switch (response) {
                case RegisterHandler.SUCCESS -> {
                    JOptionPane.showMessageDialog(null, "Account created", "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearInputs();
                    mainFrame.showLoginPanel();
                }
                case RegisterHandler.USERNAME_TOO_SHORT ->
                        errorTextArea.setText("Number of characters in username must be greater than 2");

                case RegisterHandler.USERNAME_TOO_LONG ->
                        errorTextArea.setText("Number of characters in username must be at most 32");

                case RegisterHandler.USERNAME_NOT_UNIQUE ->
                        errorTextArea.setText("Account already exists with that username");

                case RegisterHandler.PASSWORD_TOO_SHORT ->
                        errorTextArea.setText("Number of characters in password must be greater than 8");

                case RegisterHandler.PASSWORD_TOO_LONG ->
                        errorTextArea.setText("Number of characters in password must be at most 32");

                case RegisterHandler.PASSWORDS_DO_NOT_MATCH -> errorTextArea.setText("Passwords do not match");
                default -> errorTextArea.setText("Womp Womp");
            }
        });

        KeyPressHandler.bindKeyPressAction(this, "ENTER", e -> registerButton.doClick());

        registerButton.setFont(font);
        registerButton.setOpaque(false);
        registerButton.setContentAreaFilled(true);

        JLabel hasAccountLabel = new JLabel("Already have an account?");
        hasAccountLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        hasAccountLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 16));
        loginButton.setOpaque(false);
        loginButton.setContentAreaFilled(true);

        loginButton.addActionListener(e -> {
            clearInputs();
            setVisible(false);
            mainFrame.showLoginPanel();
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
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 10, 5);
        add(confirmPwdLabel, gbc);

        gbc.gridx = 2;
        gbc.gridy = 2;
        gbc.gridwidth = 3;
        gbc.insets = new Insets(0, 5, 10, 0);
        add(confirmPwdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(errorTextArea, gbc);

        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 10, 0);
        add(registerButton, gbc);

        gbc.gridy = 5;
        gbc.gridwidth = 5;
        gbc.insets = new Insets(0, 0, 0, 0);
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.EAST;

        JPanel registrationSection = new JPanel();
        registrationSection.setLayout(new BoxLayout(registrationSection, BoxLayout.X_AXIS));
        registrationSection.setOpaque(false); // Make the panel transparent
        registrationSection.add(hasAccountLabel);
        registrationSection.add(Box.createHorizontalStrut(10));
        registrationSection.add(loginButton);

        add(registrationSection, gbc);

        try {
            backgroundImage = ImageIO.read(getClass().getResource("/assets/light_sat.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void clearInputs() {
        usernameTextField.setText("");
        pwdField.setText("");
        confirmPwdField.setText("");
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