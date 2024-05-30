package tr.edu.ku.comp302.domain.lanceofdestiny.state;

public enum GameState {
    MENU, PLAYING, PAUSE;

    public boolean isMenu() {
        return this == MENU;
    }

    public boolean isPlaying() {
        return this == PLAYING;
    }

    public boolean isPaused() {
        return this == PAUSE;
    }


}
