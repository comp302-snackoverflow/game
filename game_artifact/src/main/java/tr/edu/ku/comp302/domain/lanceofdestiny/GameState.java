package tr.edu.ku.comp302.domain.lanceofdestiny;

public enum GameState {
    MAIN_MENU,
    PLAYING,
    PAUSE_MENU,
    NEW_GAME,
    LOAD_GAME,
    SAVE_GAME,
    CREATE_CUSTOM_MAP,
    HELP,
    OPTIONS;

    public static GameState state = MAIN_MENU;

    public boolean isMainMenu(){
        return this == MAIN_MENU;
    }
    public boolean isPlaying(){
        return this == PLAYING;
    }
    public boolean isPaused(){
        return this == PAUSE_MENU;
    }
    public boolean isNewGame(){
        return this == NEW_GAME;
    }
    public boolean isLoadGame(){
        return this == LOAD_GAME;
    }
    public boolean isSaveGame(){
        return this == SAVE_GAME;
    }
    public boolean isCreateCustomMap(){
        return this == CREATE_CUSTOM_MAP;
    }
    public boolean isHelp(){
        return this == HELP;
    }
    public boolean isOptions(){
        return this == OPTIONS;
    }

}
