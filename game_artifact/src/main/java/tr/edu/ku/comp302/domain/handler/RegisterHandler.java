package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private static final Logger logger = LogManager.getLogger();
    private RegisterHandler() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public int register(String username, String password, String passwordRepeat) {
        int input_valid = validateInput(username, password, passwordRepeat);
        if (input_valid != SUCCESS) {
            return input_valid;
        }

        if (!dbHandler.isUsernameUnique(username)) {
            logger.debug(String.format("Username %s is not unique. Registration failed because the username is already taken.", username));
            return USERNAME_NOT_UNIQUE;
        }

        String salt = Hash.generateSalt();
        String hash = Hash.hash(password, salt);

        if (dbHandler.createUser(username, hash, salt)) {
            logger.info(String.format("User %s registered successfully", username));
            return SUCCESS;
        }
        logger.fatal("Womp Womp Fatal Error");
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
            logger.debug("Passwords do not match");
            return PASSWORDS_DO_NOT_MATCH;
        }
        if (uname.length() < 3) {
            logger.debug(String.format("Username %s must be longer than 2 letters for registration", uname));
            return USERNAME_TOO_SHORT;
        }
        if (uname.length() > 32) {
            logger.debug(String.format("Username %s must be shorter than 33 letters for registration", uname));
            return USERNAME_TOO_LONG;
        }
        if (password.length() < 8) {
            logger.debug("Password length is too short for registration. Minimum length requirement is 8");
            return PASSWORD_TOO_SHORT;
        }
        if (password.length() > 32) {
            logger.debug("Password length is too long for registration. Maximum length requirement is 32");
            return PASSWORD_TOO_LONG;
        }
        return SUCCESS;
    }
}
