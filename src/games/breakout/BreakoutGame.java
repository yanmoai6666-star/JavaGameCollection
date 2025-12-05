package games.breakout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * 打砖块游戏主类
 */
public class BreakoutGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("打砖块游戏");
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
        private static final int PADDLE_WIDTH = 100;
        private static final int PADDLE_HEIGHT = 10;
        private static final int BALL_DIAMETER = 15;
        private static final int BRICK_WIDTH = 60;
        private static final int BRICK_HEIGHT = 20;
        private static final int BRICK_GAP = 5;
        private static final int DELAY = 8;
        
        private int paddleX;
        private int ballX;
        private int ballY;
        private int ballDX;
        private int ballDY;
        private int score;
        private int lives;
        private boolean gameOver;
        private Timer timer;
        private ArrayList<Brick> bricks;
        
        public GamePanel() {
            this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            this.setBackground(Color.black);
            this.setFocusable(true);
            this.addKeyListener(this);
            
            initializeGame();
        }
        
        private void initializeGame() {
            // 初始化挡板位置
            paddleX = (SCREEN_WIDTH - PADDLE_WIDTH) / 2;
            
            // 初始化球的位置和速度
            ballX = SCREEN_WIDTH / 2 - BALL_DIAMETER / 2;
            ballY = SCREEN_HEIGHT - 50 - BALL_DIAMETER;
            ballDX = 2;
            ballDY = -2;
            
            // 初始化分数和生命值
            score = 0;
            lives = 3;
            gameOver = false;
            
            // 初始化砖块
            bricks = new ArrayList<>();
            createBricks();
            
            // 启动游戏循环
            timer = new Timer(DELAY, this);
            timer.start();
        }
        
        private void createBricks() {
            // 创建多行多列的砖块
            int rows = 8;
            int cols = (SCREEN_WIDTH - 40) / (BRICK_WIDTH + BRICK_GAP);
            
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    int brickX = 20 + col * (BRICK_WIDTH + BRICK_GAP);
                    int brickY = 50 + row * (BRICK_HEIGHT + BRICK_GAP);
                    
                    // 根据行数设置不同颜色
                    Color color = getBrickColor(row);
                    
                    bricks.add(new Brick(brickX, brickY, color));
                }
            }
        }
        
        private Color getBrickColor(int row) {
            switch (row) {
                case 0: return Color.RED;
                case 1: return Color.ORANGE;
                case 2: return Color.YELLOW;
                case 3: return Color.GREEN;
                case 4: return Color.BLUE;
                case 5: return Color.INDIGO;
                case 6: return Color.VIOLET;
                default: return Color.CYAN;
            }
        }
        
        private void movePaddle() {
            // 挡板跟随鼠标移动
            MouseInfo.getPointerInfo();
            Point mousePoint = MouseInfo.getPointerInfo().getLocation();
            SwingUtilities.convertPointFromScreen(mousePoint, this);
            
            int newX = mousePoint.x - PADDLE_WIDTH / 2;
            
            // 确保挡板不会超出屏幕边界
            if (newX < 0) {
                newX = 0;
            } else if (newX > SCREEN_WIDTH - PADDLE_WIDTH) {
                newX = SCREEN_WIDTH - PADDLE_WIDTH;
            }
            
            paddleX = newX;
        }
        
        private void moveBall() {
            ballX += ballDX;
            ballY += ballDY;
            
            // 检查球是否碰到左右边界
            if (ballX <= 0 || ballX >= SCREEN_WIDTH - BALL_DIAMETER) {
                ballDX = -ballDX;
            }
            
            // 检查球是否碰到上边界
            if (ballY <= 0) {
                ballDY = -ballDY;
            }
            
            // 检查球是否碰到下边界（失去一条生命）
            if (ballY >= SCREEN_HEIGHT) {
                lives--;
                if (lives <= 0) {
                    gameOver = true;
                    timer.stop();
                } else {
                    resetBall();
                }
            }
            
            // 检查球是否碰到挡板
            if (ballY + BALL_DIAMETER >= SCREEN_HEIGHT - 50 - PADDLE_HEIGHT &&
                ballX + BALL_DIAMETER >= paddleX &&
                ballX <= paddleX + PADDLE_WIDTH) {
                ballDY = -ballDY;
                // 球的水平速度根据击中挡板的位置调整
                int hitPos = (int) (ballX - paddleX);
                ballDX = (hitPos - PADDLE_WIDTH / 2) / 25;
            }
            
            // 检查球是否碰到砖块
            for (int i = 0; i < bricks.size(); i++) {
                Brick brick = bricks.get(i);
                if (brick.isVisible()) {
                    if (ballX + BALL_DIAMETER >= brick.x &&
                        ballX <= brick.x + BRICK_WIDTH &&
                        ballY + BALL_DIAMETER >= brick.y &&
                        ballY <= brick.y + BRICK_HEIGHT) {
                        ballDY = -ballDY;
                        brick.setVisible(false);
                        score += 10;
                        
                        // 检查是否所有砖块都被打掉
                        if (allBricksDestroyed()) {
                            gameOver = true;
                            timer.stop();
                        }
                        break;
                    }
                }
            }
        }
        
        private void resetBall() {
            ballX = SCREEN_WIDTH / 2 - BALL_DIAMETER / 2;
            ballY = SCREEN_HEIGHT - 50 - BALL_DIAMETER;
            ballDX = 2;
            ballDY = -2;
        }
        
        private boolean allBricksDestroyed() {
            for (Brick brick : bricks) {
                if (brick.isVisible()) {
                    return false;
                }
            }
            return true;
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }
        
        private void draw(Graphics g) {
            // 绘制挡板
            g.setColor(Color.WHITE);
            g.fillRect(paddleX, SCREEN_HEIGHT - 50, PADDLE_WIDTH, PADDLE_HEIGHT);
            
            // 绘制球
            g.setColor(Color.RED);
            g.fillOval(ballX, ballY, BALL_DIAMETER, BALL_DIAMETER);
            
            // 绘制砖块
            for (Brick brick : bricks) {
                if (brick.isVisible()) {
                    g.setColor(brick.color);
                    g.fillRect(brick.x, brick.y, BRICK_WIDTH, BRICK_HEIGHT);
                    g.setColor(Color.BLACK);
                    g.drawRect(brick.x, brick.y, BRICK_WIDTH, BRICK_HEIGHT);
                }
            }
            
            // 绘制分数和生命值
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("分数: " + score, 10, 25);
            g.drawString("生命值: " + lives, SCREEN_WIDTH - 120, 25);
            
            // 绘制游戏结束信息
            if (gameOver) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                FontMetrics metrics = getFontMetrics(g.getFont());
                if (allBricksDestroyed()) {
                    g.drawString("恭喜！你赢了！", (SCREEN_WIDTH - metrics.stringWidth("恭喜！你赢了！")) / 2, SCREEN_HEIGHT / 2);
                } else {
                    g.drawString("游戏结束", (SCREEN_WIDTH - metrics.stringWidth("游戏结束")) / 2, SCREEN_HEIGHT / 2);
                }
                
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                metrics = getFontMetrics(g.getFont());
                g.drawString("按任意键重新开始", (SCREEN_WIDTH - metrics.stringWidth("按任意键重新开始")) / 2, SCREEN_HEIGHT / 2 + 50);
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameOver) {
                movePaddle();
                moveBall();
            }
            repaint();
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            if (gameOver) {
                // 重新开始游戏
                initializeGame();
                return;
            }
            
            // 左右方向键控制挡板
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                paddleX -= 20;
                if (paddleX < 0) {
                    paddleX = 0;
                }
            } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                paddleX += 20;
                if (paddleX > SCREEN_WIDTH - PADDLE_WIDTH) {
                    paddleX = SCREEN_WIDTH - PADDLE_WIDTH;
                }
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) {}
        
        @Override
        public void keyTyped(KeyEvent e) {}
        
        /**
         * 砖块类
         */
        class Brick {
            private int x;
            private int y;
            private Color color;
            private boolean visible;
            
            public Brick(int x, int y, Color color) {
                this.x = x;
                this.y = y;
                this.color = color;
                this.visible = true;
            }
            
            public boolean isVisible() {
                return visible;
            }
            
            public void setVisible(boolean visible) {
                this.visible = visible;
            }
        }
    }
}
