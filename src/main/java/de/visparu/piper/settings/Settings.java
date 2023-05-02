package de.visparu.piper.settings;

public final class Settings {
    private static final int   DEFAULT_BOARD_WIDTH         = Difficulty.EASY.getWidth();
    private static final int   DEFAULT_BOARD_HEIGHT        = Difficulty.EASY.getHeight();
    private static final float DEFAULT_START_DELAY_SECONDS = Difficulty.EASY.getDelaySeconds();
    private static final float DEFAULT_PROGRESS_INCREMENT  = Difficulty.EASY.getProgressIncrement();
    private static final int[] DEFAULT_BOARD_FIXED_PIECES  = Difficulty.EASY.getFixedPieces();
    private static final int   DEFAULT_BOARD_ENTRIES       = Difficulty.EASY.getEntries();
    private static final int   DEFAULT_BOARD_EXITS         = Difficulty.EASY.getExits();

    public static final float ACCELERATED_PROGRESS_INCREMENT = Settings.DEFAULT_PROGRESS_INCREMENT * 20;
    public static final int   ID_PIPE_STRAIGHT               = 0;
    public static final int   ID_PIPE_CURVED                 = 1;
    public static final int   ID_PIPE_SPLIT                  = 2;
    public static final int   ID_PIPE_CROSS                  = 3;
    public static final int   ID_PIPE_DEADEND                = 4;

    public static int   BOARD_WIDTH         = Settings.DEFAULT_BOARD_WIDTH;
    public static int   BOARD_HEIGHT        = Settings.DEFAULT_BOARD_HEIGHT;
    public static float START_DELAY_SECONDS = Settings.DEFAULT_START_DELAY_SECONDS;
    public static float PROGRESS_INCREMENT  = Settings.DEFAULT_PROGRESS_INCREMENT;
    public static int   BOARD_ENTRIES       = Settings.DEFAULT_BOARD_ENTRIES;
    public static int   BOARD_EXITS         = Settings.DEFAULT_BOARD_EXITS;
    public static int[] BOARD_FIXED_PIECES  = Settings.DEFAULT_BOARD_FIXED_PIECES;

    private Settings() {
    }

    public static void setDifficulty(Difficulty difficulty) {
        Settings.changeSettings(difficulty.getWidth(), difficulty.getHeight(), difficulty.getDelaySeconds(), difficulty.getProgressIncrement(), difficulty.getFixedPieces(), difficulty.getEntries(), difficulty.getExits());
    }

    public static void changeSettings(int board_width,
                                      int board_height,
                                      float start_delay_seconds,
                                      float progress_increment,
                                      int[] board_fixed_pieces,
                                      int board_entries,
                                      int board_exits) {
        Settings.BOARD_WIDTH         = board_width;
        Settings.BOARD_HEIGHT        = board_height;
        Settings.START_DELAY_SECONDS = start_delay_seconds;
        Settings.PROGRESS_INCREMENT  = progress_increment;
        Settings.BOARD_FIXED_PIECES  = board_fixed_pieces;
        Settings.BOARD_ENTRIES       = board_entries;
        Settings.BOARD_EXITS         = board_exits;
    }
}
