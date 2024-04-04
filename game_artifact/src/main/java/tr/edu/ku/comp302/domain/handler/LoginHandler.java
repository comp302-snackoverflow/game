package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.services.Hash;

public class LoginHandler {
    private LoginHandler instance;
    public static final int SUCCESS = 0;
    public static final int USERNAME_EMPTY = 1;
    public static final int PASSWORD_EMPTY = 2;
    public static final int USER_NOT_FOUND = 3;

    private LoginHandler() {

    }

    public int login(String username, String password) {
        int input_valid = validateInput(username, password);
        if (input_valid != SUCCESS) {
            return input_valid;
        }
        String salt = DatabaseHandler.getSaltByUsername(username);

        if (salt == null) {
            return USER_NOT_FOUND;
        }
        String hash = Hash.hash(password, salt);
        if (DatabaseHandler.validateLogin(username, hash)) {
            return SUCCESS;
        }

        return USER_NOT_FOUND;
    }

    public LoginHandler getInstance() {
        if (instance == null) {
            instance = new LoginHandler();
        }
        return instance;
    }

    private int validateInput(String uname, String password) {
        if (uname.isEmpty()) {
            return USERNAME_EMPTY;
        }
        if (password.isEmpty()) {
            return PASSWORD_EMPTY;
        }

        return SUCCESS;
    }
}
