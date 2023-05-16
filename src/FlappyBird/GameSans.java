package FlappyBird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static FlappyBird.Window.getWindowHeight;
import static FlappyBird.Window.getWindowWidth;

public class GameSans extends JPanel implements KeyListener {
    private final ResetManager RESET_MANAGER;
    private int skyHeight;
    private static final int BIRD_HEIGHT = 30;
    private static final int BIRD_WIDTH = 40;
    private static int birdY;
    private static int birdJumpHigh = -9;
    private boolean pressed = false;

    private static final int BIRD_X = getWindowWidth() / 2 - BIRD_WIDTH / 2;
    private final int GRAVITY_SPEED = 40;
    private final int GRAUNE_HIGET = 100;
    private final Image IMAGE;
    private final Image IMAGE_BACKGROUNDER;
    private int gravity;
    private final int PIPE_DISTANSE = 223;

    private int bestScore = 0;

    private final Pipes PIPES ;
    private final Pipes PIPES_1;

    public static int getBirdX() {
        return BIRD_X;
    }

    public GameSans() {
        IMAGE = Toolkit.getDefaultToolkit().createImage(".idea\\image1\\MyGif.gif");
        IMAGE_BACKGROUNDER = Toolkit.getDefaultToolkit().getImage(".idea\\image1\\backgraund.jpg");

        birdY = getHeight() / 2;
        gravity = 0;

        RESET_MANAGER = new ResetManager(this);
        int pipeSpeed = 9;

        this.PIPES  = new Pipes(getWindowWidth(), 0, getWidth(), this.skyHeight, pipeSpeed);
        this.PIPES_1 = new Pipes(getWindowWidth() + PIPE_DICTANC, 0, getWidth(), this.skyHeight, pipeSpeed);
        setFocusable(true);
        addKeyListener(this);

        new Thread(() -> {
            while (true) {
                Rectangle lowerPipes = this.PIPES .calculateLowerPipes();
                Rectangle upperPipes = this.PIPES .calculateUpperPipes();
                Rectangle lowerPipes1 = this.PIPES_1.calculateLowerPipes();
                Rectangle upperPipes1 = this.PIPES_1.calculateUpperPipes();

                gravity++;
                birdY += gravity;

                if (birdY > getHeight() - GRAUNE_HIGET - BIRD_HEIGHT)
                    RESET_MANAGER.resetGame();

                if (birdY < 0)
                    RESET_MANAGER.resetGame();

                if (Utils.checkCollision(lowerPipes, upperPipes, calculateBird()) ||
                        Utils.checkCollision(lowerPipes1, upperPipes1, calculateBird())) {
                    RESET_MANAGER.resetGame();
                }

                Utils.sleep(GRAVITY_SPEED);

                repaint();
            }
        }).start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(IMAGE_BACKGROUNDER, 0, 0, getWidth(), getHeight() - GRAUNE_HIGET, this);

        PIPES .paint(g);
        PIPES_1.paint(g);

        g.setColor(Color.orange);
        g.drawImage(IMAGE, BIRD_X, birdY, BIRD_WIDTH, BIRD_HEIGHT, this);

        g.fillRect(0, getHeight() - GRAUNE_HIGET, getWidth(), GRAUNE_HIGET);

        Font font = new Font("Arial", Font.BOLD, 25);
        g.setFont(font);
        g.setColor(Color.white);
        if (bestScore < Pipes.getPassageCounter()) {
            bestScore = Pipes.getPassageCounter();
        }

         String bestScoreText = "Best Score: " + bestScore;
        String scoreText = "Score: " + Pipes.getPassageCounter();
        if (Pipes.getPassageCounter() == 15) {
            String stage2 = "STAGE 2";
            g.drawString(stage2, getWindowWidth() / 2 - 20, getWindowHeight() / 3);
        }
        int PASS_CAUNT = 0;
        g.drawString(scoreText, 10, 30);
        g.drawString(bestScoreText, 10, 50);
    }

    public static Rectangle calculateBird() {
        return new Rectangle(BIRD_X, birdY, BIRD_WIDTH, BIRD_HEIGHT - 1);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!pressed && (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W)) {
            gravity = birdJumpHigh;
            pressed = true;
        }

        if (Pipes.getPassageCounter() > 15) {
            PIPES .setPipeSpeed(9);
            PIPES_1.setPipeSpeed(9);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
            pressed = false;
        }
    }

    void resetVariables() {
        birdY = getHeight() / 2;
        gravity = 0;
        Pipes.setPassageCounter(0);
        birdJumpHigh = -9;
        PIPES .setPipeSpeed(12);
        PIPES_1.setPipeSpeed(12);
    }

    void resetPipes() {
        PIPES .pipeX = getWindowWidth();
        PIPES_1.pipeX = getWindowWidth() + PIPE_DICTANC;
    }

}
