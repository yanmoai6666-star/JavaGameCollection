package games.minesweeper;

/**
 * 扫雷游戏单元格类
 */
public class Cell {
    private int row;      // 单元格所在行
    private int col;      // 单元格所在列
    private boolean isMine;     // 是否是地雷
    private boolean isRevealed; // 是否已翻开
    private boolean isFlagged;  // 是否已标记
    private int adjacentMines;  // 相邻地雷数量
    
    /**
     * 构造函数
     * @param row 行坐标
     * @param col 列坐标
     */
    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
        this.isMine = false;
        this.isRevealed = false;
        this.isFlagged = false;
        this.adjacentMines = 0;
    }
    
    // Getters and Setters
    public int getRow() {
        return row;
    }
    
    public int getCol() {
        return col;
    }
    
    public boolean isMine() {
        return isMine;
    }
    
    public void setMine(boolean mine) {
        isMine = mine;
    }
    
    public boolean isRevealed() {
        return isRevealed;
    }
    
    public void setRevealed(boolean revealed) {
        isRevealed = revealed;
    }
    
    public boolean isFlagged() {
        return isFlagged;
    }
    
    public void setFlagged(boolean flagged) {
        isFlagged = flagged;
    }
    
    public int getAdjacentMines() {
        return adjacentMines;
    }
    
    public void setAdjacentMines(int adjacentMines) {
        this.adjacentMines = adjacentMines;
    }
    
    /**
     * 增加相邻地雷计数
     */
    public void incrementAdjacentMines() {
        adjacentMines++;
    }
    
    /**
     * 切换标记状态
     */
    public void toggleFlag() {
        isFlagged = !isFlagged;
    }
    
    @Override
    public String toString() {
        if (isMine) {
            return "M";
        } else if (adjacentMines > 0) {
            return String.valueOf(adjacentMines);
        } else {
            return "";
        }
    }
}