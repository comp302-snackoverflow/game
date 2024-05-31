package tr.edu.ku.comp302.domain.lanceofdestiny.state;

public enum GameState {
    MENU, PLAYING, PAUSE, MP_PLAYING, MP_PAUSE;

    public boolean isMenu() {
        return this == MENU;
    }

    public boolean isPlaying() {
        return this == PLAYING;
    }

    public boolean isPaused() {
        return this == PAUSE;
    }

    public boolean isMultiplayerPaused() {
        return this == MP_PAUSE;
    }

    public boolean isMultiplayerPlaying() {
        return this == MP_PLAYING;
    }

}
