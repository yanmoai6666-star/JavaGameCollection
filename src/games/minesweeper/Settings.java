package games.minesweeper;

/**
 * 扫雷游戏设置类
 */
public class Settings {
    private int rows; // 棋盘行数
    private int cols; // 棋盘列数
    private int mines; // 地雷数量
    private int cellSize; // 单元格大小（像素）
    
    /**
     * 构造函数
     * @param rows 行数
     * @param cols 列数
     * @param mines 地雷数量
     */
    public Settings(int rows, int cols, int mines) {
        this.rows = rows;
        this.cols = cols;
        this.mines = Math.min(mines, rows * cols - 1); // 确保地雷数量不超过最大可能值
        this.cellSize = 30; // 默认单元格大小
    }
    
    /**
     * 预设难度级别
     */
    public enum Difficulty {
        BEGINNER(9, 9, 10),    // 初级：9x9 10颗雷
        INTERMEDIATE(16, 16, 40), // 中级：16x16 40颗雷
        EXPERT(16, 30, 99);    // 高级：16x30 99颗雷
        
        private final int rows;
        private final int cols;
        private final int mines;
        
        Difficulty(int rows, int cols, int mines) {
            this.rows = rows;
            this.cols = cols;
            this.mines = mines;
        }
        
        public Settings toSettings() {
            return new Settings(rows, cols, mines);
        }
    }
    
    // Getters and Setters
    public int getRows() {
        return rows;
    }
    
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    public int getCols() {
        return cols;
    }
    
    public void setCols(int cols) {
        this.cols = cols;
    }
    
    public int getMines() {
        return mines;
    }
    
    public void setMines(int mines) {
        this.mines = Math.min(mines, rows * cols - 1);
    }
    
    public int getCellSize() {
        return cellSize;
    }
    
    public void setCellSize(int cellSize) {
        this.cellSize = cellSize;
    }
    
    public int getTotalCells() {
        return rows * cols;
    }
}