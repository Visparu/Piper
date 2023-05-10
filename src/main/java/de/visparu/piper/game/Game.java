package de.visparu.piper.game;

import de.visparu.piper.game.structures.Toolbox;
import de.visparu.piper.game.structures.boards.Board;

import java.awt.Dimension;
import java.awt.Graphics2D;

public interface Game {
    void initialize();
    void render(Graphics2D g2d);
    void update(float delta);
    Board getBoard();
    Toolbox getToolbox();
    Dimension getDimension();
    void dispose();
}
