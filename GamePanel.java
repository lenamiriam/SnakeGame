import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int screenWidth = 500;
    static final int screenHeight = 500;
    static final int unit = 20; // how big objects
    static final int gameUnits = (screenWidth * screenHeight)/unit; // how many objects
    static final int timerDelay = 75; // the higher, the slower

    final int[] x = new int[gameUnits]; // array holding all the x coordinates of the body parts including head
    final int[] y = new int[gameUnits]; // array holding all the y coordinates

    int bodyParts = 6; // beginning
    int scoredPoints = 0;
    int pointX;
    int pointY;

    char direction = 'R'; // snake starts with going right; R - right, L - left, U - up, D - down

    boolean running = false;

    Timer timer;

    Random random;

    GamePanel() {
        random = new Random();
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame() {
        newPoint();
        running = true;
        timer = new Timer(timerDelay, this);
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        if (running) {
            g.setColor(Color.pink);
            g.fillRect(pointX, pointY, unit, unit);
            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.green);
                } else {
                    g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
                }
                g.fillRect(x[i], y[i], unit, unit);
            }
            // iterating through all the body parts of the snake
            g.setColor(Color.pink);
            g.setFont(new Font("Ink Free", Font.BOLD, 35));
            FontMetrics metrics = getFontMetrics(g.getFont());
            g.drawString("Score " + scoredPoints, (screenWidth - metrics.stringWidth("Score " + scoredPoints))/2, g.getFont().getSize());
        } else {
            gameOver(g);
        }
    }

    public void newPoint() {
        pointX = random.nextInt(screenWidth/unit) * unit;
        pointY = random.nextInt(screenHeight/unit) * unit;
        // the location of the point is random
    }

    public void move() {
        for (int i = bodyParts; i > 0; i--) {
            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }
        // shifting around body parts of the snake

        switch (direction) {
            case 'U' -> y[0] = y[0] - unit;
            case 'D' -> y[0] = y[0] + unit;
            case 'R' -> x[0] = x[0] + unit;
            case 'L' -> x[0] = x[0] - unit;
        }
    }

    public void checkScoredPoint() {
        if ((x[0] == pointX) && (y[0] == pointY)) {
            bodyParts++;
            scoredPoints++;
            newPoint();
        }
    }

    public void checkCollisions() {
        // checks if the head has collided with the body; x[0] -> head of snake
        for (int i = bodyParts; i > 0; i--) {
            if((x[0] ==i) && (y[0] == y[i])) {
                running = false;
            }
        }
        // checks if the head has touched left border
        if (x[0] < 0) {
            running = false;
        }
        // checks if the head has touched right border
        if (x[0] > screenWidth) {
            running = false;
        }
        // checks if the head has touched top border
        if (y[0] < 0) {
            running = false;
        }
        // checks if the head has touched bottom border
        if (y[0] > screenHeight) {
            running = false;
        }
        if (!running) {
            timer.stop();
        }
    }

    public void gameOver(Graphics g) {
        // Game over
        g.setColor(Color.pink);
        g.setFont(new Font("Ink Free", Font.BOLD, 75));
        FontMetrics metricsGameOver = getFontMetrics(g.getFont());
        g.drawString("Game over", (screenWidth - metricsGameOver.stringWidth("Game over"))/2, screenHeight/2);
        // Score
        g.setColor(Color.pink);
        g.setFont(new Font("Ink Free", Font.BOLD, 35));
        FontMetrics metricsScore = getFontMetrics(g.getFont());
        g.drawString("Score " + scoredPoints, (screenWidth - metricsScore.stringWidth("Score " + scoredPoints))/2, g.getFont().getSize()*8);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (running) {
            move();
            checkScoredPoint();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (direction != 'R') {
                        direction = 'L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (direction != 'L') {
                        direction = 'R';
                    }
                    break;
                case KeyEvent.VK_UP:
                    if (direction != 'D') {
                        direction = 'U';
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if (direction != 'U') {
                        direction = 'D';
                    }
                    break;
            }
        }
    }
}
