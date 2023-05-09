package de.visparu.piper.structures;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.stream.IntStream;

import de.visparu.piper.context.GameContext;
import de.visparu.piper.structures.fields.Field;
import de.visparu.piper.structures.pipes.Pipe;
import de.visparu.piper.structures.pipes.standard.CrossPipe;
import de.visparu.piper.structures.pipes.standard.CurvedPipe;
import de.visparu.piper.structures.pipes.standard.DeadEndPipe;
import de.visparu.piper.structures.pipes.standard.SplitPipe;
import de.visparu.piper.structures.pipes.standard.StraightPipe;

public class Toolbox {
    public static final Color COLOR_SELECTED_TILE = Color.GREEN;

    public static final int BORDER_SIZE     = 2;
    public static final int PANEL_SPACING_H = 10;

    public static final int PIPE_PREVIEW_AMOUNT = 5;

    private final Random rand;

    private final List<Field> fields = new ArrayList<>();
    private final Queue<Pipe> pipes  = new ArrayDeque<>();

    private final List<Class<? extends Pipe>> weights = new ArrayList<>();

    public Toolbox(Random rand) {
        this.rand = rand;
        for (int i = 0; i < Toolbox.PIPE_PREVIEW_AMOUNT; i++) {
            this.fields.add(new Field());
        }
        this.populatePercentiles();
        this.reinitializeList();
    }

    private void populatePercentiles() {
        this.populateWeight(StraightPipe.class, 6);
        this.populateWeight(CurvedPipe.class, 5);
        this.populateWeight(SplitPipe.class, 4);
        this.populateWeight(CrossPipe.class, 1);
        this.populateWeight(DeadEndPipe.class, 2);
    }

    private void populateWeight(Class<? extends Pipe> pipeClass,
                                int weight) {
        IntStream.range(0, weight)
                 .forEach(unused -> this.weights.add(pipeClass));
    }

    private void reinitializeList() {
        int sizeBefore = this.pipes.size();
        for (int i = 0; i < Toolbox.PIPE_PREVIEW_AMOUNT - sizeBefore; i++) {
            this.pipes.add(this.createNewPipe());
        }
        int i = 0;
        for (Pipe pipe : this.pipes) {
            Field field = this.fields.get(i++);
            field.setPipe(pipe);
        }
    }

    public void render(Graphics2D g2d) {
        float completeSpacingV = GameContext.get()
                                            .getGameWindow()
                                            .getToolboxCanvasHeight() - this.fields.size() * Field.SIZE;
        float singularSpacingV = completeSpacingV / (this.fields.size() + 1);
        for (int i = 0; i < this.fields.size(); i++) {
            int x = Toolbox.PANEL_SPACING_H;
            int y = (int) (singularSpacingV * (i + 1) + Field.SIZE * i);
            int w = Field.SIZE;
            int h = Field.SIZE;
            if (i == 0) {
                g2d.setColor(Toolbox.COLOR_SELECTED_TILE);
                g2d.fillRect(x - Toolbox.BORDER_SIZE, y - Toolbox.BORDER_SIZE, w + 2 * Toolbox.BORDER_SIZE, h + 2 * Toolbox.BORDER_SIZE);
            }
            BufferedImage img     = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            Graphics2D    g2d_img = img.createGraphics();
            this.fields.get(i)
                       .render(g2d_img, false);
            g2d_img.dispose();
            g2d.drawImage(img, x, y, null);
        }
    }

    public Pipe createNewPipe() {
        int percentage = this.rand.nextInt(this.weights.size());
        try {
            return this.weights.get(percentage)
                               .getConstructor()
                               .newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Pipe pollNextPipe() {
        Pipe pipe = this.pipes.poll();
        this.reinitializeList();
        return pipe;
    }

    public int getCanvasWidth() {
        return Toolbox.PANEL_SPACING_H * 2 + Field.SIZE;
    }
}
