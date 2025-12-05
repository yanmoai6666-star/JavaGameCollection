package games.connect4;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 四子连珠游戏主类
 */
public class Connect4Game {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("四子连珠游戏");
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
    static class GamePanel extends JPanel implements MouseListener, ActionListener {
        private static final int SCREEN_WIDTH = 700;
        private static final int SCREEN_HEIGHT = 600;
        private static final int ROWS = 6;
        private static final int COLS = 7;
        private static final int CIRCLE_DIAMETER = 80;
        private static final int DELAY = 200;
        
        private final int[][] board;
        private boolean player1Turn;
        private boolean gameOver;
        private int winner;
        private Timer timer;
        
        public GamePanel() {
            board = new int[ROWS][COLS];
            player1Turn = true;
            gameOver = false;
            winner = 0;
            
            this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
            this.setBackground(Color.BLUE);
            this.setFocusable(true);
            this.addMouseListener(this);
            
            initializeBoard();
            timer = new Timer(DELAY, this);
        }
        
        private void initializeBoard() {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    board[row][col] = 0;
                }
            }
        }
        
        private void dropPiece(int col) {
            if (gameOver) {
                return;
            }
            
            // 找到可以放置棋子的最低行
            for (int row = ROWS - 1; row >= 0; row--) {
                if (board[row][col] == 0) {
                    board[row][col] = player1Turn ? 1 : 2;
                    break;
                }
            }
            
            // 检查是否获胜
            if (checkWinner()) {
                gameOver = true;
                winner = player1Turn ? 1 : 2;
            } else if (checkDraw()) {
                gameOver = true;
            } else {
                // 切换玩家
                player1Turn = !player1Turn;
            }
        }
        
        private boolean checkWinner() {
            // 检查水平方向
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS - 3; col++) {
                    if (board[row][col] != 0 &&
                        board[row][col] == board[row][col + 1] &&
                        board[row][col] == board[row][col + 2] &&
                        board[row][col] == board[row][col + 3]) {
                        return true;
                    }
                }
            }
            
            // 检查垂直方向
            for (int row = 0; row < ROWS - 3; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (board[row][col] != 0 &&
                        board[row][col] == board[row + 1][col] &&
                        board[row][col] == board[row + 2][col] &&
                        board[row][col] == board[row + 3][col]) {
                        return true;
                    }
                }
            }
            
            // 检查正对角线方向
            for (int row = 0; row < ROWS - 3; row++) {
                for (int col = 0; col < COLS - 3; col++) {
                    if (board[row][col] != 0 &&
                        board[row][col] == board[row + 1][col + 1] &&
                        board[row][col] == board[row + 2][col + 2] &&
                        board[row][col] == board[row + 3][col + 3]) {
                        return true;
                    }
                }
            }
            
            // 检查反对角线方向
            for (int row = 3; row < ROWS; row++) {
                for (int col = 0; col < COLS - 3; col++) {
                    if (board[row][col] != 0 &&
                        board[row][col] == board[row - 1][col + 1] &&
                        board[row][col] == board[row - 2][col + 2] &&
                        board[row][col] == board[row - 3][col + 3]) {
                        return true;
                    }
                }
            }
            
            return false;
        }
        
        private boolean checkDraw() {
            for (int col = 0; col < COLS; col++) {
                if (board[0][col] == 0) {
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
            // 绘制棋盘格
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    // 绘制棋盘背景
                    g.setColor(Color.BLUE);
                    g.fillRect(col * 100, row * 100 + 100, 100, 100);
                    g.setColor(Color.BLACK);
                    g.drawRect(col * 100, row * 100 + 100, 100, 100);
                    
                    // 绘制棋子
                    if (board[row][col] == 1) {
                        g.setColor(Color.RED);
                        g.fillOval(col * 100 + 10, row * 100 + 110, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
                    } else if (board[row][col] == 2) {
                        g.setColor(Color.YELLOW);
                        g.fillOval(col * 100 + 10, row * 100 + 110, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
                    } else {
                        g.setColor(Color.WHITE);
                        g.fillOval(col * 100 + 10, row * 100 + 110, CIRCLE_DIAMETER, CIRCLE_DIAMETER);
                    }
                }
            }
            
            // 绘制游戏信息
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            
            if (gameOver) {
                if (winner == 1) {
                    g.drawString("红方获胜！", 250, 50);
                } else if (winner == 2) {
                    g.drawString("黄方获胜！", 250, 50);
                } else {
                    g.drawString("平局！", 280, 50);
                }
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.drawString("点击任意位置重新开始", 220, 600);
            } else {
                if (player1Turn) {
                    g.drawString("红方回合", 260, 50);
                } else {
                    g.drawString("黄方回合", 260, 50);
                }
            }
        }
        
        private void resetGame() {
            initializeBoard();
            player1Turn = true;
            gameOver = false;
            winner = 0;
        }
        
        @Override
        public void mouseClicked(MouseEvent e) {
            if (gameOver) {
                resetGame();
                repaint();
                return;
            }
            
            int col = e.getX() / 100;
            
            if (col >= 0 && col < COLS) {
                // 找到可以放置棋子的最低行
                for (int row = ROWS - 1; row >= 0; row--) {
                    if (board[row][col] == 0) {
                        board[row][col] = player1Turn ? 1 : 2;
                        
                        // 检查是否获胜或平局
                        if (checkWinner()) {
                            gameOver = true;
                            winner = player1Turn ? 1 : 2;
                        } else if (checkDraw()) {
                            gameOver = true;
                        } else {
                            player1Turn = !player1Turn;
                        }
                        
                        repaint();
                        break;
                    }
                }
            }
        }
        
        @Override
        public void mousePressed(MouseEvent e) {}
        @Override
        public void mouseReleased(MouseEvent e) {}
        @Override
        public void mouseEntered(MouseEvent e) {}
        @Override
        public void mouseExited(MouseEvent e) {}
        
        @Override
        public void actionPerformed(ActionEvent e) {
            repaint();
        }
    }
}
