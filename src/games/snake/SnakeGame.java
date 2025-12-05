package games.snake;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * 贪吃蛇游戏主类
 */
public class SnakeGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("贪吃蛇游戏");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            
            GamePanel gamePanel = new GamePanel();
            frame.add(gamePanel);
            
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
    
    /**
     * 游戏面板类
     */
    static class GamePanel extends JPanel implements ActionListener, KeyListener {
        private static final int SCREEN_WIDTH = 600;
        private static final int SCREEN_HEIGHT = 600;
        private static final int UNIT_SIZE = 25;
        private static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
        private static final int DELAY = 100;
        
        private final int[] x = new int[GAME_UNITS];
        private final int[] y = new int[GAME_UNITS];
        private int bodyParts = 6;
        private int applesEaten;
        private int appleX;
        private int appleY;
        private char direction = 'R'; // 初始方向：右
        private boolean running = false;
        private Timer timer;
        private Random random;
        
        public GamePanel() {
            random = new Random();
            this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            this.setBackground(Color.black);
            this.setFocusable(true);
            this.addKeyListener(this);
            startGame();
        }
        
        public void startGame() {
            newApple();
            running = true;
            timer = new Timer(DELAY, this);
            timer.start();
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }
        
        public void draw(Graphics g) {
            if (running) {
                // 绘制网格线（可选）
                for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
                    g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
                    g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
                }
                
                // 绘制苹果
                g.setColor(Color.red);
                g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);
                
                // 绘制蛇身体
                for (int i = 0; i < bodyParts; i++) {
                    if (i == 0) {
                        g.setColor(Color.green); // 蛇头
                    } else {
                        g.setColor(new Color(45, 180, 0)); // 蛇身
                    }
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                
                // 绘制分数
                g.setColor(Color.white);
                g.setFont(new Font("Arial", Font.BOLD, 25));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("分数: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("分数: " + applesEaten)) / 2, g.getFont().getSize());
            } else {
                gameOver(g);
            }
        }
        
        public void newApple() {
            appleX = random.nextInt((int) (SCREEN_WIDTH / UNIT_SIZE)) * UNIT_SIZE;
            appleY = random.nextInt((int) (SCREEN_HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
        }
        
        public void move() {
            // 移动身体
            for (int i = bodyParts; i > 0; i--) {
                x[i] = x[i - 1];
                y[i] = y[i - 1];
            }
            
            // 移动头部
            switch (direction) {
                case 'U':
                    y[0] = y[0] - UNIT_SIZE;
                    break;
                case 'D':
                    y[0] = y[0] + UNIT_SIZE;
                    break;
                case 'L':
                    x[0] = x[0] - UNIT_SIZE;
                    break;
                case 'R':
                    x[0] = x[0] + UNIT_SIZE;
                    break;
            }
        }
        
        public void checkApple() {
            if ((x[0] == appleX) && (y[0] == appleY)) {
                bodyParts++;
                applesEaten++;
                newApple();
            }
        }
        
        public void checkCollisions() {
            // 检查是否撞到自己的身体
            for (int i = bodyParts; i > 0; i--) {
                if ((x[0] == x[i]) && (y[0] == y[i])) {
                    running = false;
                }
            }
            
            // 检查是否撞到左边界
            if (x[0] < 0) {
                running = false;
            }
            
            // 检查是否撞到右边界
            if (x[0] >= SCREEN_WIDTH) {
                running = false;
            }
            
            // 检查是否撞到上边界
            if (y[0] < 0) {
                running = false;
            }
            
            // 检查是否撞到下边界
            if (y[0] >= SCREEN_HEIGHT) {
                running = false;
            }
            
            if (!running) {
                timer.stop();
            }
        }
        
        public void gameOver(Graphics g) {
            // 绘制分数
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            FontMetrics metrics1 = getFontMetrics(g.getFont());
            g.drawString("分数: " + applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("分数: " + applesEaten)) / 2, g.getFont().getSize());
            
            // 绘制游戏结束文字
            g.setColor(Color.red);
            g.setFont(new Font("Arial", Font.BOLD, 75));
            FontMetrics metrics2 = getFontMetrics(g.getFont());
            g.drawString("游戏结束", (SCREEN_WIDTH - metrics2.stringWidth("游戏结束")) / 2, SCREEN_HEIGHT / 2);
            
            // 绘制重新开始提示
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 25));
            FontMetrics metrics3 = getFontMetrics(g.getFont());
            g.drawString("按任意键重新开始", (SCREEN_WIDTH - metrics3.stringWidth("按任意键重新开始")) / 2, SCREEN_HEIGHT / 2 + 100);
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (running) {
                move();
                checkApple();
                checkCollisions();
            }
            repaint();
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            if (!running) {
                running = true;
                bodyParts = 6;
                applesEaten = 0;
                direction = 'R';
                for (int i = 0; i < bodyParts; i++) {
                    x[i] = 250 - (i * UNIT_SIZE);
                    y[i] = 250;
                }
                newApple();
                timer = new Timer(DELAY, this);
                timer.start();
                return;
            }
            
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
        
        @Override
        public void keyReleased(KeyEvent e) {}
        
        @Override
        public void keyTyped(KeyEvent e) {}
    }
}