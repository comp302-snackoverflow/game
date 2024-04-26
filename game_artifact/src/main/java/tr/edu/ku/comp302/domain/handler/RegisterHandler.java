package tr.edu.ku.comp302.domain.handler;

import tr.edu.ku.comp302.domain.services.Hash;

public class RegisterHandler {
    private static RegisterHandler instance;
    private final DatabaseHandler dbHandler;
    public static final int SUCCESS = 0;
    public static final int USERNAME_TOO_SHORT = 1;
    public static final int USERNAME_TOO_LONG = 2;
    public static final int USERNAME_NOT_UNIQUE = 3;
    public static final int PASSWORD_TOO_SHORT = 4;
    public static final int PASSWORD_TOO_LONG = 5;
    public static final int PASSWORDS_DO_NOT_MATCH = 6;
    public static final int WOMP_WOMP = -1;

    private RegisterHandler() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public int register(String username, String password, String passwordRepeat) {
        int input_valid = validateInput(username, password, passwordRepeat);
        if (input_valid != SUCCESS) {
            return input_valid;
        }

        if (!dbHandler.isUsernameUnique(username)) {
            return USERNAME_NOT_UNIQUE;
        }

        String salt = Hash.generateSalt();
        String hash = Hash.hash(password, salt);

        if (dbHandler.createUser(username, hash, salt)) {
            return SUCCESS;
        }

        return WOMP_WOMP;
    }

    public static RegisterHandler getInstance() {
        if (instance == null) {
            instance = new RegisterHandler();
        }
        return instance;
    }

    private int validateInput(String uname, String password, String repeat) {
        if (!password.equals(repeat)) {
            return PASSWORDS_DO_NOT_MATCH;
        }
        if (uname.length() < 3) {
            return USERNAME_TOO_SHORT;
        }
        if (uname.length() > 32) {
            return USERNAME_TOO_LONG;
        }
        if (password.length() < 8) {
            return PASSWORD_TOO_SHORT;
        }
        if (password.length() > 32) {
            return PASSWORD_TOO_LONG;
        }
        return SUCCESS;
    }
}
