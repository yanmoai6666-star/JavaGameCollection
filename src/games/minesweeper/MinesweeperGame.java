package games.minesweeper;

import javax.swing.*;
import java.awt.*;

/**
 * 扫雷游戏主类
 */
public class MinesweeperGame {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // 创建游戏设置
            Settings settings = new Settings(16, 30, 99); // 标准模式：16行30列99颗雷
            
            // 创建主窗口
            JFrame frame = new JFrame("扫雷游戏");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            
            // 创建游戏面板
            GUI gui = new GUI(settings);
            frame.add(gui.getMainPanel());
            
            // 设置窗口大小并居中显示
            frame.pack();
            frame.setLocationRelativeTo(null);
            
            // 显示窗口
            frame.setVisible(true);
        });
    }
}