# JavaGameCollection

JavaGameCollection 是一个用 Java 编写的游戏集合仓库，包含了多种经典和现代的小游戏。本仓库旨在提供高质量的 Java 游戏示例代码，方便学习和使用。

## 项目特点

- **多种游戏类型**：包含休闲益智、动作冒险、策略等多种类型的游戏
- **完整的游戏实现**：每个游戏都有完整的源代码和资源文件
- **可复用的组件**：提供了公共工具类和 GUI 组件，方便扩展和开发新游戏
- **良好的代码结构**：遵循面向对象设计原则，代码结构清晰，易于维护
- **跨平台支持**：使用 Java Swing 编写，支持 Windows、Mac 和 Linux 系统

## 游戏列表

### 休闲益智类

- **扫雷游戏 (Minesweeper)**：经典的扫雷游戏，支持自定义难度和计时器功能
- **四子连珠 (Connect4)**：经典的四子连珠游戏，支持双人对战

### 动作类

- **贪吃蛇 (Snake)**：经典的贪吃蛇游戏，支持键盘控制和分数记录
- **吃豆人 (Pacman)**：经典的吃豆人游戏，包含多个关卡和敌人

### 策略类

- **俄罗斯方块 (Tetris)**：经典的俄罗斯方块游戏，支持键盘控制和难度递增

### 街机类

- **打砖块 (Breakout)**：经典的打砖块游戏，包含多种砖块类型和关卡

## 目录结构

```
JavaGameCollection/
├── src/
│   ├── common/              # 公共工具类
│   │   ├── GameUtils.java
│   │   └── gui/             # 公共GUI组件
│   │       └── GameGuiComponents.java
│   ├── games/               # 游戏实现
│   │   ├── breakout/        # 打砖块游戏
│   │   │   └── BreakoutGame.java
│   │   ├── connect4/        # 四子连珠游戏
│   │   │   └── Connect4Game.java
│   │   ├── minesweeper/     # 扫雷游戏
│   │   │   ├── Board.java
│   │   │   ├── Cell.java
│   │   │   ├── GUI.java
│   │   │   ├── MinesweeperGame.java
│   │   │   └── Settings.java
│   │   ├── pacman/          # 吃豆人游戏
│   │   │   └── PacmanGame.java
│   │   ├── snake/           # 贪吃蛇游戏
│   │   │   └── SnakeGame.java
│   │   └── tetris/          # 俄罗斯方块游戏
│   │       └── TetrisGame.java
│   └── resources/           # 游戏资源文件
├── docs/                    # 项目文档
│   ├── screenshots/         # 游戏截图
│   └── user_manual.md       # 用户手册
├── lib/                     # 第三方库
├── README.md                # 项目说明文档
├── .gitignore               # Git忽略文件
└── build.xml                # Ant构建脚本
```

## 环境要求

- Java JDK 8 或更高版本
- Ant 构建工具（可选）

## 如何运行

### 使用命令行运行

1. 编译项目：
   ```bash
   javac -d bin src/**/*.java
   ```

2. 运行特定游戏（以扫雷为例）：
   ```bash
   java -cp bin games.minesweeper.MinesweeperGame
   ```

### 使用构建工具（Ant）

1. 编译项目：
   ```bash
   ant compile
   ```

2. 运行特定游戏：
   ```bash
   ant run-minesweeper
   ```

## 游戏玩法

### 扫雷游戏
- 点击格子翻开，如果是地雷游戏结束
- 数字表示周围8个格子中的地雷数量
- 右键点击标记地雷
- 目标：找出所有非地雷格子

### 贪吃蛇游戏
- 使用方向键控制蛇的移动方向
- 吃到食物蛇会变长
- 撞到墙壁或自己的身体游戏结束
- 目标：获得尽可能高的分数

### 俄罗斯方块游戏
- 使用左右方向键移动方块
- 向下方向键加速下落
- 上方向键旋转方块
- 目标：消除尽可能多的行数

### 吃豆人游戏
- 使用方向键控制吃豆人的移动方向
- 吃到所有豆子游戏通关
- 躲避幽灵，吃到特殊豆子可以暂时吃掉幽灵
- 目标：在规定时间内完成所有关卡

### 打砖块游戏
- 使用左右方向键控制挡板移动
- 弹球击碎所有砖块游戏通关
- 接住掉落的道具可以获得额外能力
- 目标：在弹球掉落前击碎所有砖块

### 四子连珠游戏
- 玩家轮流在7列中选择一列放置棋子
- 棋子会落在所选列的最底部
- 先将4个相同颜色的棋子连成一条线（横、竖、对角线）的玩家获胜
- 目标：先连成4子的玩家获胜

## 开发说明

### 项目结构

项目采用模块化设计，每个游戏都是独立的模块，共享公共工具类和GUI组件。公共组件位于 `src/common` 目录下，包含：

- `GameUtils.java`：提供游戏常用的工具方法
- `GameGuiComponents.java`：提供统一的GUI组件和交互功能

### 添加新游戏

要添加新游戏，只需在 `src/games` 目录下创建一个新的子目录，然后实现游戏的主类和相关组件。建议遵循以下步骤：

1. 创建游戏目录：`src/games/your_game_name/`
2. 实现游戏主类（包含main方法）
3. 实现游戏逻辑和GUI界面
4. 使用公共工具类和GUI组件
5. 更新README.md文件中的游戏列表

## 贡献

欢迎提交问题和代码贡献！如果您想添加新游戏或改进现有游戏，请遵循以下步骤：

1. Fork 本仓库
2. 创建新的分支
3. 实现您的功能或修复
4. 提交Pull Request

## 许可证

本项目采用 MIT 许可证，详见 LICENSE 文件。

## 联系方式

如有问题或建议，请通过以下方式联系：

- Email: example@example.com
- GitHub Issues: https://github.com/yourusername/JavaGameCollection/issues

## 致谢

感谢所有为这个项目做出贡献的开发者！

---

**JavaGameCollection - 让游戏开发变得更简单！**
