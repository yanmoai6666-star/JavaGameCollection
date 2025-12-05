package common;

import java.awt.*;
import java.util.Random;

/**
 * 游戏公共工具类
 * 提供各个游戏共享的功能和方法
 */
public class GameUtils {
    private static final Random random = new Random();
    
    /**
     * 生成随机整数
     * @param min 最小值（包含）
     * @param max 最大值（包含）
     * @return 随机整数
     */
    public static int getRandomInt(int min, int max) {
        return random.nextInt(max - min + 1) + min;
    }
    
    /**
     * 计算两个点之间的距离
     * @param x1 第一个点的x坐标
     * @param y1 第一个点的y坐标
     * @param x2 第二个点的x坐标
     * @param y2 第二个点的y坐标
     * @return 两点之间的距离
     */
    public static double getDistance(int x1, int y1, int x2, int y2) {
        int dx = x2 - x1;
        int dy = y2 - y1;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    /**
     * 检查点是否在矩形内
     * @param x 点的x坐标
     * @param y 点的y坐标
     * @param rect 矩形
     * @return 是否在矩形内
     */
    public static boolean isPointInRectangle(int x, int y, Rectangle rect) {
        return rect.contains(x, y);
    }
    
    /**
     * 检查点是否在圆内
     * @param x 点的x坐标
     * @param y 点的y坐标
     * @param centerX 圆心x坐标
     * @param centerY 圆心y坐标
     * @param radius 半径
     * @return 是否在圆内
     */
    public static boolean isPointInCircle(int x, int y, int centerX, int centerY, int radius) {
        return getDistance(x, y, centerX, centerY) <= radius;
    }
    
    /**
     * 限制值在指定范围内
     * @param value 要限制的值
     * @param min 最小值
     * @param max 最大值
     * @return 限制后的值
     */
    public static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * 限制值在指定范围内（双精度浮点数）
     * @param value 要限制的值
     * @param min 最小值
     * @param max 最大值
     * @return 限制后的值
     */
    public static double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
    
    /**
     * 将毫秒转换为分:秒格式
     * @param milliseconds 毫秒数
     * @return 格式化的时间字符串（MM:SS）
     */
    public static String formatTime(long milliseconds) {
        int totalSeconds = (int) (milliseconds / 1000);
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    /**
     * 生成随机颜色
     * @return 随机颜色
     */
    public static Color getRandomColor() {
        int r = random.nextInt(256);
        int g = random.nextInt(256);
        int b = random.nextInt(256);
        return new Color(r, g, b);
    }
    
    /**
     * 生成随机亮色
     * @return 随机亮色
     */
    public static Color getRandomBrightColor() {
        int r = random.nextInt(156) + 100;
        int g = random.nextInt(156) + 100;
        int b = random.nextInt(156) + 100;
        return new Color(r, g, b);
    }
    
    /**
     * 深拷贝二维整数数组
     * @param original 原始数组
     * @return 拷贝的数组
     */
    public static int[][] deepCopyIntArray(int[][] original) {
        if (original == null) {
            return null;
        }
        
        int rows = original.length;
        int cols = original[0].length;
        int[][] copy = new int[rows][cols];
        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                copy[i][j] = original[i][j];
            }
        }
        
        return copy;
    }
    
    /**
     * 检查两个矩形是否碰撞
     * @param rect1 第一个矩形
     * @param rect2 第二个矩形
     * @return 是否碰撞
     */
    public static boolean checkCollision(Rectangle rect1, Rectangle rect2) {
        return rect1.intersects(rect2);
    }
}
