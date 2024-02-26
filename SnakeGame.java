package snakegames;
import java.awt.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.List;
public class SnakeGame extends JFrame {
    public SnakeGame() {
        initUI();
    }

    private void initUI() {
    	
        setResizable(false);
        add(new GameBoard());
        setResizable(false);
        pack();   
        setTitle("Snake Game");
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            JFrame ex = new SnakeGame();
            ex.setVisible(true);
        });
    }
}
class GameBoard extends JPanel implements KeyListener, ActionListener {

    private static final int BOARD_WIDTH = 300;
    private static final int BOARD_HEIGHT = 300;
    private static final int UNIT_SIZE = 10;
    private int score=0;
    private JLabel scoreLabel;
    private Food food;
    private Snake snake;
    private Timer timer;

    public GameBoard() {

        initBoard();
        setFocusable(true);  // Enable keyboard focus
        addKeyListener(this);//addKeyListener(new TAdapter());  // Add key event listener
    }

    private void initBoard() {
    	scoreLabel=new JLabel("Score:0");
    	scoreLabel.setForeground(Color.WHITE);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 20));
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
        snake = new Snake();
        food = new Food();
        timer = new Timer(100, this);  // Adjust timer delay as needed
        timer.start();
        add(scoreLabel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    private void draw(Graphics g) {
        food.draw(g);
        snake.draw(g);
    }

 

    @Override
    public void actionPerformed(ActionEvent e) {
        snake.move();
        if (isCollisionWithBoard() || snake.isSelfCollision()) {
            gameOver();
            return; // Exit the method
        }
        // Check if snake has eaten food
        if (snake.intersects(food)) {
            snake.grow();
            food.generateNewPosition();
            updateScore();
        }

        repaint();
    }
    private void updateScore() {
        score += 10; // Increase score by 10 when snake eats food
        scoreLabel.setText("Score: " + score); // Update score label text
    }
    private boolean isCollisionWithBoard() {
        Point head = snake.getHead();
        return head.x < 0 || head.x >= BOARD_WIDTH || head.y < 0 || head.y >= BOARD_HEIGHT;
    }

    private void gameOver() {
        timer.stop(); // Stop the game timer
        JOptionPane.showMessageDialog(this, "Game Over!", "Game Over", JOptionPane.INFORMATION_MESSAGE);
        //((SnakeGame) SwingUtilities.getWindowAncestor(this)).restartGame();
    }
 
    public void restartGame() {
        score = 0;
        snake.reset();
        food.generateNewPosition();
        timer.start();
        //((SnakeGame) SwingUtilities.getWindowAncestor(this)).scorePanel.updateScore(score);
        repaint();
    }
   @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_UP:
                snake.setDirection(Snake.UP);
                break;
            case KeyEvent.VK_DOWN:
                snake.setDirection(Snake.DOWN);
                break;
            case KeyEvent.VK_LEFT:
                snake.setDirection(Snake.LEFT);
                break;
            case KeyEvent.VK_RIGHT:
                snake.setDirection(Snake.RIGHT);
                break;
            case KeyEvent.VK_R:
            	resetGame();
            	break;
        }
   }
    private void resetGame() {
        score = 0; // Reset the score
        snake = new Snake(); // Reset the snake
        food = new Food(); // Reset the food
        timer.start(); // Restart the game timer
        scoreLabel.setText("Score: " + score); // Update the score label
        repaint(); // Repaint the game board
    }
//@override
public void KeyTyped(KeyEvent e)
{
	//Handle the Key typed events
}
    @Override
    public void keyReleased(KeyEvent e) {
        // Not needed for this example
    }

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
class Snake {
	private List<Point> body;
    private int direction;
    public static final int UP = 0;
    public static final int DOWN = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    private static final int UNIT_SIZE = 10;
    private int x = 50;
    private int y = 50;
    private int size = 10;

    public Snake() {
        body = new ArrayList<>();
        body.add(new Point(50, 50));
        direction = RIGHT;
    }

	public void reset() {
		// TODO Auto-generated method stub
		
	}

	public void draw(Graphics g) {
       g.setColor(Color.GREEN);
        for (Point segment : body) {
            g.fillRect(segment.x, segment.y, UNIT_SIZE, UNIT_SIZE);
        }
    }
    public boolean intersects(Food food) {
        return body.get(0).equals(food.getPosition());
    }
    public boolean isSelfCollision() {
        Point head = body.get(0);
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true; // Snake collided with itself
            }
        }
        return false; // Snake did not collide with itself
    }

    public void move() {
        // Movement logic
    	  Point head = body.get(0);
          Point newHead = new Point(head.x, head.y);

          // Update the position of the head based on the direction
          switch (direction) {
              case UP:
                  newHead.y -= UNIT_SIZE;
                  break;
              case DOWN:
                  newHead.y += UNIT_SIZE;
                  break;
              case LEFT:
                  newHead.x -= UNIT_SIZE;
                  break;
              case RIGHT:
                  newHead.x += UNIT_SIZE;
                  break;
          }
          // Add the new head to the beginning of the body
          body.add(0, newHead);

          // Remove the tail to simulate movement
          body.remove(body.size() - 1);
    }

    public void grow() {
        // Increase size logic
    	 // Retrieve the position of the last segment (tail)
        Point tail = body.get(body.size() - 1);

        // Create a new segment with the same position as the tail
        Point newTail = new Point(tail.x, tail.y);

        // Add the new segment to the end of the body
        body.add(newTail);
  
    }
    public void setDirection(int direction)
    {
    	this.direction=direction;
    }
    public Point getHead() {
        return body.get(0);
    }

}

class Food {
    private int x;
    private int y;
    private int size = 10;
    private static final int UNIT_SIZE = 10;
    public Food() {
        Random rand = new Random();
        x = rand.nextInt(30) * 10;
        y = rand.nextInt(30) * 10;
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, UNIT_SIZE, UNIT_SIZE);
    }
    
    public void generateNewPosition() {
        Random rand = new Random();
        x = rand.nextInt(30) * UNIT_SIZE;
        y = rand.nextInt(30) * UNIT_SIZE;
    }

    public Point getPosition() {
        return new Point(x, y);
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
