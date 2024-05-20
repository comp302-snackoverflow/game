package tr.edu.ku.comp302.domain.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tr.edu.ku.comp302.domain.services.Hash;
import tr.edu.ku.comp302.domain.services.SessionManager;

public class LoginHandler {
    public static final int SUCCESS = 0;
    public static final int USERNAME_EMPTY = 1;
    public static final int PASSWORD_EMPTY = 2;
    public static final int USER_NOT_FOUND = 3;
    private static final Logger logger = LogManager.getLogger(LoginHandler.class);
    private static LoginHandler instance;
    private final DatabaseHandler dbHandler;

    private LoginHandler() {
        dbHandler = DatabaseHandler.getInstance();
    }

    public static LoginHandler getInstance() {
        if (instance == null) {
            instance = new LoginHandler();
        }
        return instance;
    }

    public int login(String username, String password) {
        int input_valid = validateInput(username, password);
        if (input_valid != SUCCESS) {
            return input_valid;
        }
        String salt = dbHandler.getSaltByUsername(username);

        if (salt == null) {
            logger.debug("User not found");
            return USER_NOT_FOUND;
        }
        String hash = Hash.hash(password, salt);
        Integer uid = dbHandler.validateLogin(username, hash);
        if (uid != null) {
            logger.info(String.format("Player %s successfully logged in", username));
            SessionManager.getSession().setSessionData("userID", uid);
            return SUCCESS;
        }
        logger.debug(String.format("User with username %s not found", username));
        return USER_NOT_FOUND;
    }

    private int validateInput(String uname, String password) {
        if (uname.isEmpty()) {
            logger.debug("Username is empty");
            return USERNAME_EMPTY;
        }
        if (password.isEmpty()) {
            logger.debug("Password is empty");
            return PASSWORD_EMPTY;
        }
        return SUCCESS;
    }
}

