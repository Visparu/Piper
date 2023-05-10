package de.visparu.piper.game.settings;

import de.visparu.piper.game.structures.pipes.Pipe;

import java.util.Map;

public final class Settings {
    private int   boardWidth;
    private int   boardHeight;
    private float startDelaySeconds;
    private float progressIncrement;
    private Map<Class<? extends Pipe>, Integer> boardFixedPieces;
    private int   boardEntries;
    private int   boardExits;

    private Settings() {

    }

    public int getBoardWidth() {
        return boardWidth;
    }

    public int getBoardHeight() {
        return boardHeight;
    }

    public float getStartDelaySeconds() {
        return startDelaySeconds;
    }

    public float getProgressIncrement() {
        return progressIncrement;
    }

    public Map<Class<? extends Pipe>, Integer> getBoardFixedPieces() {
        return boardFixedPieces;
    }

    public int getBoardEntries() {
        return boardEntries;
    }

    public int getBoardExits() {
        return boardExits;
    }

    public static SettingsBuilder builder() {
        return new SettingsBuilder();
    }

    public static Settings forDifficulty(Difficulty difficulty) {
        return Settings.builder()
                       .boardWidth(difficulty.getWidth())
                       .boardHeight(difficulty.getHeight())
                       .startDelaySeconds(difficulty.getDelaySeconds())
                       .progressIncrement(difficulty.getProgressIncrement())
                       .boardFixedPieces(difficulty.getFixedPieces())
                       .boardEntries(difficulty.getEntries())
                       .boardExits(difficulty.getExits())
                       .build();
    }

    public static class SettingsBuilder {
        private final Settings instance;

        private SettingsBuilder() {
            this.instance = new Settings();
        }

        public SettingsBuilder boardWidth(int boardWidth) {
            this.instance.boardWidth = boardWidth;
            return this;
        }

        public SettingsBuilder boardHeight(int boardHeight) {
            this.instance.boardHeight = boardHeight;
            return this;
        }

        public SettingsBuilder startDelaySeconds(float startDelaySeconds) {
            this.instance.startDelaySeconds = startDelaySeconds;
            return this;
        }

        public SettingsBuilder progressIncrement(float progressIncrement) {
            this.instance.progressIncrement = progressIncrement;
            return this;
        }

        public SettingsBuilder boardFixedPieces(Map<Class<? extends Pipe>, Integer> boardFixedPieces) {
            this.instance.boardFixedPieces = boardFixedPieces;
            return this;
        }

        public SettingsBuilder boardEntries(int boardEntries) {
            this.instance.boardEntries = boardEntries;
            return this;
        }

        public SettingsBuilder boardExits(int boardExits) {
            this.instance.boardExits = boardExits;
            return this;
        }

        public Settings build() {
            return this.instance;
        }
    }
}
