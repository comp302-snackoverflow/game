package tr.edu.ku.comp302.domain.services;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static final Object lock = new Object();
    private static SessionManager instance;
    private final Map<String, Object> sessionData;

    private SessionManager() {
        sessionData = new HashMap<>();
    }

    public static SessionManager getSession() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setSessionData(String key, Object value) {
        synchronized (sessionData) {
            sessionData.put(key, value);
        }
    }

    public Object getSessionData(String key) {
        return sessionData.get(key);
    }

    public void deleteSessionData(String key) {
        sessionData.remove(key);
    }

    public void clear() {
        sessionData.clear();
    }
}
