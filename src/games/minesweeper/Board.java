package games.minesweeper;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;

/**
 * 扫雷游戏棋盘类
 */
public class Board {
    private Cell[][] cells;
    private Settings settings;
    private int rows;
    private int cols;
    private int mines;
    private int revealedCells;
    private boolean gameOver;
    private boolean gameWon;
    
    /**
     * 构造函数
     * @param settings 游戏设置
     */
    public Board(Settings settings) {
        this.settings = settings;
        this.rows = settings.getRows();
        this.cols = settings.getCols();
        this.mines = settings.getMines();
        this.cells = new Cell[rows][cols];
        this.revealedCells = 0;
        this.gameOver = false;
        this.gameWon = false;
        
        initializeBoard();
    }
    
    /**
     * 初始化棋盘
     */
    private void initializeBoard() {
        // 创建所有单元格
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                cells[row][col] = new Cell(row, col);
            }
        }
    }
    
    /**
     * 放置地雷
     * @param firstClickRow 玩家第一次点击的行
     * @param firstClickCol 玩家第一次点击的列
     */
    public void placeMines(int firstClickRow, int firstClickCol) {
        Random random = new Random();
        int minesPlaced = 0;
        
        // 创建一个排除第一次点击位置及其相邻位置的列表
        List<Integer> excludedPositions = new ArrayList<>();
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int r = firstClickRow + dr;
                int c = firstClickCol + dc;
                if (isValidPosition(r, c)) {
                    excludedPositions.add(r * cols + c);
                }
            }
        }
        
        // 随机放置地雷
        while (minesPlaced < mines) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            int position = r * cols + c;
            
            if (!cells[r][c].isMine() && !excludedPositions.contains(position)) {
                cells[r][c].setMine(true);
                minesPlaced++;
            }
        }
        
        // 计算每个单元格的相邻地雷数量
        calculateAdjacentMines();
    }
    
    /**
     * 计算每个单元格的相邻地雷数量
     */
    private void calculateAdjacentMines() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (!cells[row][col].isMine()) {
                    int count = 0;
                    for (int dr = -1; dr <= 1; dr++) {
                        for (int dc = -1; dc <= 1; dc++) {
                            int r = row + dr;
                            int c = col + dc;
                            if (isValidPosition(r, c) && cells[r][c].isMine()) {
                                count++;
                            }
                        }
                    }
                    cells[row][col].setAdjacentMines(count);
                }
            }
        }
    }
    
    /**
     * 检查位置是否有效
     * @param row 行坐标
     * @param col 列坐标
     * @return 是否有效
     */
    private boolean isValidPosition(int row, int col) {
        return row >= 0 && row < rows && col >= 0 && col < cols;
    }
    
    /**
     * 处理玩家点击单元格
     * @param row 行坐标
     * @param col 列坐标
     * @return 是否点击了地雷
     */
    public boolean clickCell(int row, int col) {
        if (!isValidPosition(row, col) || gameOver || gameWon) {
            return false;
        }
        
        Cell cell = cells[row][col];
        if (cell.isRevealed() || cell.isFlagged()) {
            return false;
        }
        
        // 如果是第一次点击，放置地雷
        if (revealedCells == 0) {
            placeMines(row, col);
        }
        
        // 翻开单元格
        cell.setRevealed(true);
        revealedCells++;
        
        // 如果点击了地雷，游戏结束
        if (cell.isMine()) {
            gameOver = true;
            revealAllMines();
            return true;
        }
        
        // 如果是空白单元格，递归翻开相邻单元格
        if (cell.getAdjacentMines() == 0) {
            revealAdjacentCells(row, col);
        }
        
        // 检查是否获胜
        checkWin();
        
        return false;
    }
    
    /**
     * 递归翻开相邻单元格
     * @param row 行坐标
     * @param col 列坐标
     */
    private void revealAdjacentCells(int row, int col) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int r = row + dr;
                int c = col + dc;
                if (isValidPosition(r, c)) {
                    Cell adjacentCell = cells[r][c];
                    if (!adjacentCell.isRevealed() && !adjacentCell.isFlagged() && !adjacentCell.isMine()) {
                        adjacentCell.setRevealed(true);
                        revealedCells++;
                        if (adjacentCell.getAdjacentMines() == 0) {
                            revealAdjacentCells(r, c);
                        }
                    }
                }
            }
        }
    }
    
    /**
     * 标记单元格
     * @param row 行坐标
     * @param col 列坐标
     */
    public void flagCell(int row, int col) {
        if (!isValidPosition(row, col) || gameOver || gameWon) {
            return;
        }
        
        Cell cell = cells[row][col];
        if (!cell.isRevealed()) {
            cell.toggleFlag();
        }
    }
    
    /**
     * 翻开所有地雷
     */
    private void revealAllMines() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                if (cells[row][col].isMine()) {
                    cells[row][col].setRevealed(true);
                }
            }
        }
    }
    
    /**
     * 检查是否获胜
     */
    private void checkWin() {
        // 获胜条件：所有非地雷单元格都已翻开
        if (revealedCells == rows * cols - mines) {
            gameWon = true;
            gameOver = true;
            // 标记所有地雷
            for (int row = 0; row < rows; row++) {
                for (int col = 0; col < cols; col++) {
                    if (cells[row][col].isMine()) {
                        cells[row][col].setFlagged(true);
                    }
                }
            }
        }
    }
    
    /**
     * 重新开始游戏
     */
    public void restartGame() {
        initializeBoard();
        revealedCells = 0;
        gameOver = false;
        gameWon = false;
    }
    
    // Getters
    public Cell[][] getCells() {
        return cells;
    }
    
    public Cell getCell(int row, int col) {
        if (isValidPosition(row, col)) {
            return cells[row][col];
        }
        return null;
    }
    
    public int getRows() {
        return rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public int getMines() {
        return mines;
    }
    
    public int getRevealedCells() {
        return revealedCells;
    }
    
    public boolean isGameOver() {
        return gameOver;
    }
    
    public boolean isGameWon() {
        return gameWon;
    }
    
    public Settings getSettings() {
        return settings;
    }
}