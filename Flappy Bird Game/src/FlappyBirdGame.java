import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;

public class FlappyBirdGame extends JPanel implements ActionListener {

    // Game constants
    private final int WIDTH = 800;
    private final int HEIGHT = 600;
    private final int BIRD_SIZE = 30;
    private final int GRAVITY = 1;
    private final int JUMP_STRENGTH = 15;
    private final int PIPE_WIDTH = 100;
    private final int PIPE_GAP = 150;

    private Timer timer;
    private ArrayList<Rectangle> pipes;
    private int birdY;
    private int birdVelocity;
    private int score;
    private boolean gameOver;

    public FlappyBirdGame() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.CYAN);
        pipes = new ArrayList<>();
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        score = 0;
        gameOver = false;

        // Timer for the game loop
        timer = new Timer(20, this);
        timer.start();

        // Key listener for bird jump
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                    birdVelocity = -JUMP_STRENGTH;
                } else if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
                    resetGame();
                }
            }
        });
        setFocusable(true);

        // Create initial pipes
        createPipe();
    }

    // Game loop
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            birdVelocity += GRAVITY;
            birdY += birdVelocity;

            // Move pipes
            for (Rectangle pipe : pipes) {
                pipe.x -= 5;
            }

            // Check for collision
            checkCollision();

            // Remove off-screen pipes and increase score
            if (!pipes.isEmpty() && pipes.get(0).x < -PIPE_WIDTH) {
                pipes.remove(0);
                createPipe();
                score++;
            }

            repaint();
        }
    }

    // Create pipes
    private void createPipe() {
        int pipeHeight = new Random().nextInt(200) + 100; // Random height
        pipes.add(new Rectangle(WIDTH, 0, PIPE_WIDTH, pipeHeight)); // Upper pipe
        pipes.add(new Rectangle(WIDTH, pipeHeight + PIPE_GAP, PIPE_WIDTH, HEIGHT - pipeHeight - PIPE_GAP)); // Lower
                                                                                                            // pipe
    }

    // Check for collision
    private void checkCollision() {
        if (birdY > HEIGHT || birdY < 0) {
            gameOver = true;
        }

        for (Rectangle pipe : pipes) {
            if (pipe.intersects(new Rectangle(100, birdY, BIRD_SIZE, BIRD_SIZE))) {
                gameOver = true;
            }
        }
    }

    // Reset the game
    private void resetGame() {
        pipes.clear();
        birdY = HEIGHT / 2;
        birdVelocity = 0;
        score = 0;
        gameOver = false;
        createPipe();
    }

    // Paint the game
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.YELLOW);
        g.fillRect(100, birdY, BIRD_SIZE, BIRD_SIZE); // Draw bird

        g.setColor(Color.GREEN);
        for (Rectangle pipe : pipes) {
            g.fillRect(pipe.x, pipe.y, pipe.width, pipe.height); // Draw pipes
        }

        // Draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Score: " + score, 10, 30);

        if (gameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 50));
            g.drawString("Game Over", WIDTH / 2 - 150, HEIGHT / 2);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Press 'R' to Restart", WIDTH / 2 - 150, HEIGHT / 2 + 50);
        }
    }

    // Main method to run the game
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird");
        FlappyBirdGame game = new FlappyBirdGame();
        frame.add(game);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
