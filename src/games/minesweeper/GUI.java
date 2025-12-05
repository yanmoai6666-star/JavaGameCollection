package games.minesweeper;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 扫雷游戏图形界面类
 */
public class GUI {
    private Settings settings;
    private Board board;
    private JFrame mainFrame;
    private JPanel mainPanel;
    private JPanel boardPanel;
    private JButton[][] cellButtons;
    private JLabel timerLabel;
    private JLabel minesLeftLabel;
    private JButton restartButton;
    private int elapsedTime;
    private Timer timer;
    private boolean firstClick;
    
    /**
     * 构造函数
     * @param settings 游戏设置
     */
    public GUI(Settings settings) {
        this.settings = settings;
        this.board = new Board(settings);
        this.firstClick = true;
        this.elapsedTime = 0;
        
        initializeGUI();
    }
    
    /**
     * 初始化图形界面
     */
    private void initializeGUI() {
        // 创建主面板
        mainPanel = new JPanel(new BorderLayout());
        mainPanel.setPreferredSize(new Dimension(
            settings.getCols() * settings.getCellSize(),
            settings.getRows() * settings.getCellSize() + 50
        ));
        
        // 创建顶部信息面板
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 5));
        
        // 创建计时器标签
        timerLabel = new JLabel("时间: 000");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(timerLabel);
        
        // 创建重新开始按钮
        restartButton = new JButton("😊");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 20));
        restartButton.setPreferredSize(new Dimension(40, 40));
        restartButton.addActionListener(e -> restartGame());
        infoPanel.add(restartButton);
        
        // 创建剩余地雷标签
        minesLeftLabel = new JLabel("地雷: " + String.format("%03d", settings.getMines()));
        minesLeftLabel.setFont(new Font("Arial", Font.BOLD, 16));
        infoPanel.add(minesLeftLabel);
        
        mainPanel.add(infoPanel, BorderLayout.NORTH);
        
        // 创建棋盘面板
        boardPanel = new JPanel(new GridLayout(settings.getRows(), settings.getCols()));
        createCellButtons();
        mainPanel.add(boardPanel, BorderLayout.CENTER);
        
        // 创建计时器
        timer = new Timer();
    }
    
    /**
     * 创建单元格按钮
     */
    private void createCellButtons() {
        cellButtons = new JButton[settings.getRows()][settings.getCols()];
        
        for (int row = 0; row < settings.getRows(); row++) {
            for (int col = 0; col < settings.getCols(); col++) {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(settings.getCellSize(), settings.getCellSize()));
                button.setFont(new Font("Arial", Font.BOLD, 14));
                button.setFocusPainted(false);
                
                // 添加鼠标事件监听器
                int finalRow = row;
                int finalCol = col;
                
                button.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        handleCellClick(finalRow, finalCol, e.getButton());
                    }
                });
                
                cellButtons[row][col] = button;
                boardPanel.add(button);
            }
        }
    }
    
    /**
     * 处理单元格点击
     * @param row 行坐标
     * @param col 列坐标
     * @param button 鼠标按钮
     */
    private void handleCellClick(int row, int col, int button) {
        if (board.isGameOver()) {
            return;
        }
        
        // 第一次点击时放置地雷并开始计时
        if (firstClick) {
            board.placeMines(row, col);
            startTimer();
            firstClick = false;
        }
        
        // 根据鼠标按钮类型处理
        if (button == MouseEvent.BUTTON1) { // 左键点击
            boolean hitMine = board.clickCell(row, col);
            if (hitMine) {
                updateCellDisplay(row, col);
                gameOver(false);
            } else {
                updateCellDisplay(row, col);
                if (board.isGameWon()) {
                    gameOver(true);
                }
            }
        } else if (button == MouseEvent.BUTTON3) { // 右键点击
            board.flagCell(row, col);
            updateCellDisplay(row, col);
            updateMinesLeft();
        }
    }
    
    /**
     * 更新单元格显示
     * @param row 行坐标
     * @param col 列坐标
     */
    private void updateCellDisplay(int row, int col) {
        Cell cell = board.getCell(row, col);
        JButton button = cellButtons[row][col];
        
        if (cell.isRevealed()) {
            button.setEnabled(false);
            button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            
            if (cell.isMine()) {
                button.setText("💣");
                button.setBackground(Color.RED);
            } else {
                int adjacentMines = cell.getAdjacentMines();
                if (adjacentMines > 0) {
                    button.setText(String.valueOf(adjacentMines));
                    setCellTextColor(button, adjacentMines);
                }
            }
        } else {
            if (cell.isFlagged()) {
                button.setText("🚩");
            } else {
                button.setText("");
            }
        }
    }
    
    /**
     * 设置单元格文本颜色
     * @param button 按钮
     * @param adjacentMines 相邻地雷数量
     */
    private void setCellTextColor(JButton button, int adjacentMines) {
        switch (adjacentMines) {
            case 1: button.setForeground(Color.BLUE); break;
            case 2: button.setForeground(Color.GREEN.darker()); break;
            case 3: button.setForeground(Color.RED); break;
            case 4: button.setForeground(Color.MAGENTA); break;
            case 5: button.setForeground(Color.ORANGE.darker()); break;
            case 6: button.setForeground(Color.CYAN); break;
            case 7: button.setForeground(Color.BLACK); break;
            case 8: button.setForeground(Color.GRAY); break;
            default: button.setForeground(Color.BLACK);
        }
    }
    
    /**
     * 更新剩余地雷数量显示
     */
    private void updateMinesLeft() {
        int flagsPlaced = 0;
        for (int row = 0; row < settings.getRows(); row++) {
            for (int col = 0; col < settings.getCols(); col++) {
                if (board.getCell(row, col).isFlagged()) {
                    flagsPlaced++;
                }
            }
        }
        int minesLeft = settings.getMines() - flagsPlaced;
        minesLeftLabel.setText("地雷: " + String.format("%03d", minesLeft));
    }
    
    /**
     * 开始计时器
     */
    private void startTimer() {
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                elapsedTime++;
                timerLabel.setText("时间: " + String.format("%03d", elapsedTime));
                if (elapsedTime >= 999) {
                    timer.cancel();
                }
            }
        }, 1000, 1000);
    }
    
    /**
     * 停止计时器
     */
    private void stopTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }
    
    /**
     * 游戏结束
     * @param won 是否获胜
     */
    private void gameOver(boolean won) {
        stopTimer();
        
        if (won) {
            restartButton.setText("😎");
            JOptionPane.showMessageDialog(mainPanel, "恭喜！你赢了！", "游戏结束", JOptionPane.INFORMATION_MESSAGE);
        } else {
            restartButton.setText("😵");
            // 显示所有地雷
            for (int row = 0; row < settings.getRows(); row++) {
                for (int col = 0; col < settings.getCols(); col++) {
                    if (board.getCell(row, col).isMine()) {
                        updateCellDisplay(row, col);
                    }
                }
            }
            JOptionPane.showMessageDialog(mainPanel, "很遗憾！你踩到地雷了！", "游戏结束", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * 重新开始游戏
     */
    private void restartGame() {
        stopTimer();
        
        // 重置游戏状态
        firstClick = true;
        elapsedTime = 0;
        board.restartGame();
        
        // 重置界面
        timerLabel.setText("时间: 000");
        minesLeftLabel.setText("地雷: " + String.format("%03d", settings.getMines()));
        restartButton.setText("😊");
        
        // 重置所有单元格按钮
        for (int row = 0; row < settings.getRows(); row++) {
            for (int col = 0; col < settings.getCols(); col++) {
                JButton button = cellButtons[row][col];
                button.setEnabled(true);
                button.setText("");
                button.setBackground(null);
                button.setForeground(Color.BLACK);
                button.setBorder(BorderFactory.createRaisedBevelBorder());
            }
        }
    }
    
    /**
     * 获取主面板
     * @return 主面板
     */
    public JPanel getMainPanel() {
        return mainPanel;
    }
}