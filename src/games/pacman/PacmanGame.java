package games.pacman;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

/**
 * 吃豆人游戏主类
 */
public class PacmanGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("吃豆人游戏");
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
        private static final int UNIT_SIZE = 30;
        private static final int DELAY = 100;
        
        private final int ROWS = SCREEN_HEIGHT / UNIT_SIZE;
        private final int COLS = SCREEN_WIDTH / UNIT_SIZE;
        
        private Pacman pacman;
        private ArrayList<Ghost> ghosts;
        private ArrayList<Pellet> pellets;
        private int score;
        private boolean gameOver;
        private Timer timer;
        private Random random;
        
        // 地图定义 (0: 空地, 1: 墙壁, 2: 食物)
        private final int[][] MAP = {
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1},
            {1, 2, 1, 1, 2, 1, 1, 1, 2, 2, 2, 2, 1, 1, 1, 2, 1, 1, 2, 1},
            {1, 2, 1, 1, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 1, 1, 2, 1},
            {1, 2, 2, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 2, 2, 1},
            {1, 2, 1, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 1, 2, 1},
            {1, 2, 1, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 2, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 1, 2, 2, 2, 1, 1, 2, 2, 2, 1, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 2, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 2, 1, 1, 1, 1},
            {1, 1, 1, 1, 2, 1, 2, 1, 2, 2, 2, 2, 1, 2, 1, 2, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 1, 2, 1, 1, 2, 1, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 1, 1, 2, 1, 2, 1, 1, 2, 1, 2, 1, 1, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 1, 1},
            {1, 1, 1, 1, 2, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 1, 1, 1},
            {1, 2, 2, 2, 2, 2, 2, 1, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 1},
            {1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1, 1, 1, 2, 1, 1, 2, 1},
            {1, 2, 2, 2, 2, 2, 2, 2, 2, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 1},
            {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}
        };
        
        public GamePanel() {
            random = new Random();
            this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            this.setBackground(Color.black);
            this.setFocusable(true);
            this.addKeyListener(this);
            
            initializeGame();
        }
        
        private void initializeGame() {
            // 初始化吃豆人
            pacman = new Pacman(1, 1);
            
            // 初始化幽灵
            ghosts = new ArrayList<>();
            ghosts.add(new Ghost(2, 2, Color.red));
            ghosts.add(new Ghost(2, 17, Color.pink));
            ghosts.add(new Ghost(17, 2, Color.cyan));
            ghosts.add(new Ghost(17, 17, Color.orange));
            
            // 初始化食物
            pellets = new ArrayList<>();
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (MAP[row][col] == 2) {
                        pellets.add(new Pellet(row, col));
                    }
                }
            }
            
            score = 0;
            gameOver = false;
            
            timer = new Timer(DELAY, this);
            timer.start();
        }
        
        private void movePacman() {
            int newRow = pacman.row + pacman.dy;
            int newCol = pacman.col + pacman.dx;
            
            if (isValidMove(newRow, newCol)) {
                pacman.row = newRow;
                pacman.col = newCol;
            }
        }
        
        private void moveGhosts() {
            for (Ghost ghost : ghosts) {
                // 随机移动方向
                int[] directions = {0, 1, 2, 3}; // 上, 右, 下, 左
                int direction = directions[random.nextInt(directions.length)];
                
                int newRow = ghost.row;
                int newCol = ghost.col;
                
                switch (direction) {
                    case 0: newRow--; break; // 上
                    case 1: newCol++; break; // 右
                    case 2: newRow++; break; // 下
                    case 3: newCol--; break; // 左
                }
                
                if (isValidMove(newRow, newCol)) {
                    ghost.row = newRow;
                    ghost.col = newCol;
                }
            }
        }
        
        private boolean isValidMove(int row, int col) {
            return row >= 0 && row < ROWS && col >= 0 && col < COLS && MAP[row][col] != 1;
        }
        
        private void checkCollisions() {
            // 检查吃豆人是否吃到食物
            for (int i = 0; i < pellets.size(); i++) {
                Pellet pellet = pellets.get(i);
                if (pacman.row == pellet.row && pacman.col == pellet.col) {
                    pellets.remove(i);
                    score += 10;
                    break;
                }
            }
            
            // 检查是否吃完所有食物
            if (pellets.isEmpty()) {
                gameOver = true;
                timer.stop();
            }
            
            // 检查吃豆人是否被幽灵抓到
            for (Ghost ghost : ghosts) {
                if (pacman.row == ghost.row && pacman.col == ghost.col) {
                    gameOver = true;
                    timer.stop();
                    break;
                }
            }
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            draw(g);
        }
        
        private void draw(Graphics g) {
            // 绘制墙壁
            g.setColor(Color.blue);
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (MAP[row][col] == 1) {
                        g.fillRect(col * UNIT_SIZE, row * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
                    }
                }
            }
            
            // 绘制食物
            g.setColor(Color.yellow);
            for (Pellet pellet : pellets) {
                g.fillOval(pellet.col * UNIT_SIZE + UNIT_SIZE / 3, pellet.row * UNIT_SIZE + UNIT_SIZE / 3, 
                          UNIT_SIZE / 3, UNIT_SIZE / 3);
            }
            
            // 绘制吃豆人
            g.setColor(Color.yellow);
            g.fillOval(pacman.col * UNIT_SIZE, pacman.row * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            
            // 绘制幽灵
            for (Ghost ghost : ghosts) {
                g.setColor(ghost.color);
                g.fillRect(ghost.col * UNIT_SIZE, ghost.row * UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
            }
            
            // 绘制分数
            g.setColor(Color.white);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            g.drawString("分数: " + score, 10, 25);
            
            // 绘制游戏结束信息
            if (gameOver) {
                g.setColor(Color.red);
                g.setFont(new Font("Arial", Font.BOLD, 50));
                FontMetrics metrics = getFontMetrics(g.getFont());
                if (pellets.isEmpty()) {
                    g.drawString("恭喜！你赢了！", (SCREEN_WIDTH - metrics.stringWidth("恭喜！你赢了！")) / 2, SCREEN_HEIGHT / 2);
                } else {
                    g.drawString("游戏结束", (SCREEN_WIDTH - metrics.stringWidth("游戏结束")) / 2, SCREEN_HEIGHT / 2);
                }
                
                g.setColor(Color.white);
                g.setFont(new Font("Arial", Font.BOLD, 20));
                metrics = getFontMetrics(g.getFont());
                g.drawString("按任意键重新开始", (SCREEN_WIDTH - metrics.stringWidth("按任意键重新开始")) / 2, SCREEN_HEIGHT / 2 + 50);
            }
        }
        
        @Override
        public void actionPerformed(ActionEvent e) {
            if (!gameOver) {
                movePacman();
                moveGhosts();
                checkCollisions();
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
            
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:
                    pacman.dx = 0;
                    pacman.dy = -1;
                    break;
                case KeyEvent.VK_DOWN:
                    pacman.dx = 0;
                    pacman.dy = 1;
                    break;
                case KeyEvent.VK_LEFT:
                    pacman.dx = -1;
                    pacman.dy = 0;
                    break;
                case KeyEvent.VK_RIGHT:
                    pacman.dx = 1;
                    pacman.dy = 0;
                    break;
            }
        }
        
        @Override
        public void keyReleased(KeyEvent e) {}
        
        @Override
        public void keyTyped(KeyEvent e) {}
        
        /**
         * 吃豆人类
         */
        class Pacman {
            private int row;
            private int col;
            private int dx;
            private int dy;
            
            public Pacman(int row, int col) {
                this.row = row;
                this.col = col;
                this.dx = 1; // 初始方向：右
                this.dy = 0;
            }
        }
        
        /**
         * 幽灵类
         */
        class Ghost {
            private int row;
            private int col;
            private Color color;
            
            public Ghost(int row, int col, Color color) {
                this.row = row;
                this.col = col;
                this.color = color;
            }
        }
        
        /**
         * 食物类
         */
        class Pellet {
            private int row;
            private int col;
            
            public Pellet(int row, int col) {
                this.row = row;
                this.col = col;
            }
        }
    }
}
