package tr.edu.ku.comp302.domain.lanceofdestiny;

import tr.edu.ku.comp302.domain.listeners.MPDataListener;

public interface MPObserver {
    void notifyListeners(String message);

    void addListener(MPDataListener listener);

    void removeListener(MPDataListener listener);

    void resetListeners();
}
