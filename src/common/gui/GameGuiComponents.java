package common.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;

/**
 * 游戏公共GUI组件类
 * 提供各个游戏共享的界面元素和交互功能
 */
public class GameGuiComponents {
    
    /**
     * 创建自定义按钮
     * @param text 按钮文本
     * @param actionListener 按钮点击事件监听器
     * @return 自定义按钮
     */
    public static JButton createButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(70, 130, 180));
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createRaisedBevelBorder());
        button.setPreferredSize(new Dimension(120, 40));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (actionListener != null) {
            button.addActionListener(actionListener);
        }
        
        // 鼠标悬停效果
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(100, 149, 237));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(new Color(70, 130, 180));
            }
        });
        
        return button;
    }
    
    /**
     * 创建自定义标签
     * @param text 标签文本
     * @param fontSize 字体大小
     * @param isBold 是否加粗
     * @return 自定义标签
     */
    public static JLabel createLabel(String text, int fontSize, boolean isBold) {
        JLabel label = new JLabel(text);
        int fontStyle = isBold ? Font.BOLD : Font.PLAIN;
        label.setFont(new Font("Arial", fontStyle, fontSize));
        label.setForeground(Color.BLACK);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }
    
    /**
     * 创建游戏标题标签
     * @param title 游戏标题
     * @return 标题标签
     */
    public static JLabel createGameTitle(String title) {
        JLabel titleLabel = createLabel(title, 32, true);
        titleLabel.setForeground(new Color(70, 130, 180));
        return titleLabel;
    }
    
    /**
     * 创建游戏信息面板
     * @param gameTitle 游戏标题
     * @param infoText 游戏信息文本
     * @return 信息面板
     */
    public static JPanel createGameInfoPanel(String gameTitle, String infoText) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.LIGHT_GRAY);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        
        JLabel titleLabel = createGameTitle(gameTitle);
        JLabel infoLabel = createLabel(infoText, 14, false);
        
        panel.add(titleLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 10)));
        panel.add(infoLabel);
        
        return panel;
    }
    
    /**
     * 创建难度选择面板
     * @param buttonNames 难度按钮名称数组
     * @param listener 按钮点击事件监听器
     * @return 难度选择面板
     */
    public static JPanel createDifficultyPanel(String[] buttonNames, ActionListener listener) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        
        JLabel label = createLabel("选择难度:", 16, true);
        panel.add(label);
        
        for (String buttonName : buttonNames) {
            JButton button = createButton(buttonName, listener);
            button.setActionCommand(buttonName);
            panel.add(button);
        }
        
        return panel;
    }
    
    /**
     * 创建分数面板
     * @param initialScore 初始分数
     * @return 包含分数标签的面板
     */
    public static JPanel createScorePanel(int initialScore) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        panel.setBackground(Color.LIGHT_GRAY);
        
        JLabel scoreLabel = createLabel("分数: " + initialScore, 18, true);
        scoreLabel.setName("scoreLabel");
        panel.add(scoreLabel);
        
        return panel;
    }
    
    /**
     * 创建计时器面板
     * @return 包含计时器标签的面板
     */
    public static JPanel createTimerPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(Color.LIGHT_GRAY);
        
        JLabel timerLabel = createLabel("时间: 00:00", 18, true);
        timerLabel.setName("timerLabel");
        panel.add(timerLabel);
        
        return panel;
    }
    
    /**
     * 更新分数显示
     * @param panel 分数面板
     * @param newScore 新分数
     */
    public static void updateScore(JPanel panel, int newScore) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel && component.getName() != null && component.getName().equals("scoreLabel")) {
                ((JLabel) component).setText("分数: " + newScore);
                break;
            }
        }
    }
    
    /**
     * 更新计时器显示
     * @param panel 计时器面板
     * @param timeStr 格式化的时间字符串
     */
    public static void updateTimer(JPanel panel, String timeStr) {
        Component[] components = panel.getComponents();
        for (Component component : components) {
            if (component instanceof JLabel && component.getName() != null && component.getName().equals("timerLabel")) {
                ((JLabel) component).setText("时间: " + timeStr);
                break;
            }
        }
    }
    
    /**
     * 显示游戏结束对话框
     * @param parent 父窗口组件
     * @param title 对话框标题
     * @param message 对话框消息
     * @param score 游戏分数
     * @param restartListener 重新开始按钮监听器
     * @param quitListener 退出按钮监听器
     */
    public static void showGameOverDialog(Component parent, String title, String message, int score, 
                                         ActionListener restartListener, ActionListener quitListener) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        panel.setBackground(Color.LIGHT_GRAY);
        
        // 消息标签
        JLabel messageLabel = createLabel(message, 18, true);
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 分数标签
        JLabel scoreLabel = createLabel("最终分数: " + score, 24, true);
        scoreLabel.setForeground(new Color(255, 140, 0));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 按钮面板
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.LIGHT_GRAY);
        
        JButton restartButton = createButton("重新开始", restartListener);
        restartButton.setActionCommand("restart");
        buttonPanel.add(restartButton);
        
        JButton quitButton = createButton("退出", quitListener);
        quitButton.setActionCommand("quit");
        buttonPanel.add(quitButton);
        
        // 添加组件到面板
        panel.add(messageLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));
        panel.add(scoreLabel);
        panel.add(Box.createRigidArea(new Dimension(0, 25)));
        panel.add(buttonPanel);
        
        dialog.add(panel);
        dialog.pack();
        dialog.setLocationRelativeTo(parent);
        dialog.setVisible(true);
    }
    
    /**
     * 创建游戏控制面板
     * @param buttons 控制面板按钮数组
     * @return 控制面板
     */
    public static JPanel createControlPanel(JButton... buttons) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 10));
        panel.setBackground(Color.LIGHT_GRAY);
        
        for (JButton button : buttons) {
            panel.add(button);
        }
        
        return panel;
    }
    
    /**
     * 创建带边框的面板
     * @param title 边框标题
     * @param layout 面板布局
     * @return 带边框的面板
     */
    public static JPanel createBorderPanel(String title, LayoutManager layout) {
        JPanel panel = new JPanel(layout);
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY), 
                title, 
                javax.swing.border.TitledBorder.CENTER, 
                javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                new Font("Arial", Font.BOLD, 14)));
        panel.setBackground(Color.LIGHT_GRAY);
        return panel;
    }
    
    /**
     * 创建带有滚动条的文本区域
     * @param text 初始文本
     * @param rows 行数
     * @param columns 列数
     * @return 带有滚动条的文本区域
     */
    public static JScrollPane createTextAreaWithScroll(String text, int rows, int columns) {
        JTextArea textArea = new JTextArea(text, rows, columns);
        textArea.setFont(new Font("Arial", Font.PLAIN, 14));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setEditable(false);
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        return scrollPane;
    }
}
