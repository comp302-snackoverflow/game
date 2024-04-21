package tr.edu.ku.comp302.domain.lanceofdestiny;

public enum GameState {
    PLAYING,
    PAUSE_MENU;

    public boolean isPlaying(){
        return this == PLAYING;
    }
    public boolean isPaused(){
        return this == PAUSE_MENU;
    }
}
