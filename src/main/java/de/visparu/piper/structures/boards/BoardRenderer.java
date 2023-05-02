package de.visparu.piper.structures.boards;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class BoardRenderer {
    private final Field[][] fields;

    public BoardRenderer(Field[][] fields) {
        this.fields = fields;
    }

    public void render(Graphics2D g2d, boolean paused) {
        if (paused) {
            g2d.setColor(Board.COLOR_PAUSE);
            g2d.setFont(new Font("Times New Roman", Font.BOLD, 11));
            g2d.drawString("Paused... (Press escape to resume)", 20, 20);
            return;
        }
        for (int y = 0; y < this.fields.length; y++) {
            for (int x = 0; x < this.fields[y].length; x++) {
                Field         field   = this.fields[y][x];
                BufferedImage img     = new BufferedImage(Field.SIZE, Field.SIZE, BufferedImage.TYPE_INT_RGB);
                Graphics2D    g2d_img = img.createGraphics();
                field.render(g2d_img);
                g2d_img.dispose();
                g2d.drawImage(img, x * Field.SIZE, y * Field.SIZE, null);
            }
        }
    }
}
