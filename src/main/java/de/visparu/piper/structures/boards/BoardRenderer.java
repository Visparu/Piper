package de.visparu.piper.structures.boards;

import de.visparu.piper.structures.fields.Field;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BoardRenderer {
    private Board board;

    public void initialize(Board board) {
        this.board = board;
    }

    public void render(Graphics2D g2d, boolean paused) {
        if (paused) {
            g2d.setColor(Board.COLOR_PAUSE);
            g2d.setFont(new Font("Times New Roman", Font.BOLD, 11));
            g2d.drawString("Paused... (Press escape to resume)", 20, 20);
            return;
        }
        for (int y = 0; y < this.board.getBoardHeight(); y++) {
            for (int x = 0; x < this.board.getBoardWidth(); x++) {
                Field         field   = this.board.getField(x, y);
                BufferedImage img     = new BufferedImage(Field.SIZE, Field.SIZE, BufferedImage.TYPE_INT_RGB);
                Graphics2D    g2d_img = img.createGraphics();
                field.render(g2d_img, this.board.hasWon());
                g2d_img.dispose();
                g2d.drawImage(img, x * Field.SIZE, y * Field.SIZE, null);
            }
        }
    }
}
