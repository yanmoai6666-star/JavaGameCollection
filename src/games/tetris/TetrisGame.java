package games.tetris;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

/**
 * 俄罗斯方块游戏主类
 */
public class TetrisGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("俄罗斯方块游戏");
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
        private static final int SCREEN_WIDTH = 400;
        private static final int SCREEN_HEIGHT = 600;
        private static final int UNIT_SIZE = 25;
        private static final int DELAY = 150;
        
        private final int[][] grid;
        private final int ROWS;
        private final int COLS;
        
        private int x;
        private int y;
        private int currentPiece;
        private int currentRotation;
        private int score;
        private int level;
        private boolean gameOver;
        private Timer timer;
        private Random random;
        
        // 俄罗斯方块的形状定义
        private final int[][][] TETROMINOS = {
            // I型方块
            {
                {0, 0, 0, 0},
                {1, 1, 1, 1},
                {0, 0, 0, 0},
                {0, 0, 0, 0}
            },
            // J型方块
            {
                {1, 0, 0},
                {1, 1, 1},
                {0, 0, 0}
            },
            // L型方块
            {
                {0, 0, 1},
                {1, 1, 1},
                {0, 0, 0}
            },
            // O型方块
            {
                {1, 1},
                {1, 1}
            },
            // S型方块
            {
                {0, 1, 1},
                {1, 1, 0},
                {0, 0, 0}
            },
            // T型方块
            {
                {0, 1, 0},
                {1, 1, 1},
                {0, 0, 0}
            },
            // Z型方块
            {
                {1, 1, 0},
                {0, 1, 1},
                {0, 0, 0}
            }
        };
        
        public GamePanel() {
            COLS = SCREEN_WIDTH / UNIT_SIZE;
            ROWS = SCREEN_HEIGHT / UNIT_SIZE;
            grid = new int[ROWS][COLS];
            
            random = new Random();
            this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            this.setBackground(Color.black);
            this.setFocusable(true);
            this.addKeyListener(this);
            
            initializeGrid();
            spawnNewPiece();
            startGame();
        }
        
        private void initializeGrid() {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    grid[row][col] = 0;
                }
            }
        }
        
        private void startGame() {
            score = 0;
            level = 1;
            gameOver = false;
            timer = new Timer(DELAY, this);
            timer.start();
        }
        
        private void spawnNewPiece() {
            currentPiece = random.nextInt(TETROMINOS.length);
            currentRotation = 0;
            x = COLS / 2 - TETROMINOS[currentPiece][currentRotation].length / 2;
            y = 0;
            
            // 检查游戏是否结束
            if (!isValidMove(0, 0, 0)) {
                gameOver = true;
                timer.stop();
            }
        }
        
        private boolean isValidMove(int rowOffset, int colOffset, int rotationOffset) {
            int[][][] piece = TETROMINOS;
            int newRotation = (currentRotation + rotationOffset) % piece[currentPiece].length;
            
            for (int row = 0; row < piece[currentPiece][newRotation].length; row++) {
                for (int col = 0; col < piece[currentPiece][newRotation][row].length; col++) {
                    if (piece[currentPiece][newRotation][row][col] != 0) {
                        int newRow = y + row + rowOffset;
                        int newCol = x + col + colOffset;
                        
                        if (newRow < 0 || newRow >= ROWS || newCol < 0 || newCol >= COLS || grid[newRow][newCol] != 0) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        
        private void placePiece() {
            int[][][] piece = TETROMINOS;
            
            for (int row = 0; row < piece[currentPiece][currentRotation].length; row++) {
                for (int col = 0; col < piece[currentPiece][currentRotation][row].length; col++) {
                    if (piece[currentPiece][currentRotation][row][col] != 0) {
                        grid[y + row][x + col] = piece[currentPiece][currentRotation][row][col];
                    }
                }
            }
            
            checkLines();
            spawnNewPiece();
        }
        
        private void checkLines() {
            int linesCleared = 0;
            
            for (int row = ROWS - 1; row >= 0; row--) {
                boolean fullLine = true;
                
                for (int col = 0; col < COLS; col++) {
                    if (grid[row][col] == 0) {
                        fullLine = false;
                        break;
                    }
                }
                
                if (fullLine) {
                    linesCleared++;
                    
                    // 清除当前行并将上面的行下移
                    for (int r = row; r > 0; r--) {
                        for (int col = 0; col < COLS; col++) {
                            grid[r][col] = grid[r - 1][col];
                        }
                    }
                    
                    // 顶部行清空
                    for (int col = 0; col < COLS; col++) {
                        grid[0][col] = 0;
                    }
                    
                    // 重新检查当前行（因为上面的行下移了）
                    row++;
                }
            }
            
            if (linesCleared > 0) {
                score += linesCleared * 100 * level;
                level = score / 1000 + 1;
                timer.setDelay(DELAY - (level - 1) * 10);
            }
        }
        
        private void moveDown() {
            if (isValidMove(1, 0, 0)) {
                y++;
            } else {
                placePiece();
            }
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }
        
        private void draw(Graphics g) {
            // 绘制网格
            for (int i = 0; i < ROWS; i++) {
                g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
            }
            
            for (int j = 0; j < COLS; j++) {
                g.drawLine(j * UNIT_SIZE, 0, j * UNIT_SIZE, SCREEN_HEIGHT);
            }
            
            // 绘制已放置的方块
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (grid[row][col] != 0) {
                        g.setColor(Color.CYAN);
                        g.fillRect(col * UNIT_SIZE, row * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                        g.setColor(Color.GRAY);
                        g.drawRect(col * UNIT_SIZE, row * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }
            
            // 绘制当前方块
            int[][][] piece = TETROMINOS;
            for (int row = 0; row < piece[currentPiece][currentRotation].length; row++) {
                for (int col = 0; col < piece[currentPiece][currentRotation][row].length; col++) {
                    if (piece[currentPiece][currentRotation][row][col] != 0) {
                        g.setColor(Color.ORANGE);
                        g.fillRect((x + col) * UNIT_SIZE, (y + row) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                        g.setColor(Color.GRAY);
                        g.drawRect((x + col) * UNIT_SIZE, (y + row) * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }
            
            // 绘制分数和等级
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("分数: " + score, 10, 20);
            g.drawString("等级: " + level, 10, 40);
            
            // 绘制游戏结束信息
            if (gameOver) {
                g.setColor(Color.RED);
                g.setFont(new Font("Arial", Font.BOLD, 30));
                FontMetrics metrics = getFontMetrics(g.getFont());
                g.drawString("游戏结束", (SCREEN_WIDTH - metrics.stringWidth("游戏结束")) / 2, SCREEN_HEIGHT / 2);
                
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 16));
                metrics = getFontMetrics(g.getFont());
                g.drawString("按任意键重新开始", (SCREEN_WIDTH - metrics.stringWidth("按任意键重新开始")) / 2, SCREEN_HEIGHT / 2 + 50);
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameOver) {
                moveDown();
            }
            repaint();
        }
        
        @Override
        public void keyPressed(KeyEvent e) {
            if (gameOver) {
                // 重新开始游戏
                initializeGrid();
                startGame();
                return;
            }
            
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT:
                    if (isValidMove(0, -1, 0)) {
                        x--;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if (isValidMove(0, 1, 0)) {
                        x++;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    moveDown();
                    break;
                case KeyEvent.VK_UP:
                    if (isValidMove(0, 0, 1)) {
                        currentRotation = (currentRotation + 1) % TETROMINOS[currentPiece].length;
                    }
                    break;
                case KeyEvent.VK_SPACE:
                    // 快速下落
                    while (isValidMove(1, 0, 0)) {
                        y++;
                    }
                    placePiece();
                    break;
            }
            repaint();
        }
        
        @Override
        public void keyReleased(KeyEvent e) {}
        
        @Override
        public void keyTyped(KeyEvent e) {}
    }
}
