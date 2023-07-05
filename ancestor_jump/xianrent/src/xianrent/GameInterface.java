package demo;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameInterface {
	private static TRexRunnerGame game;
    public static void main(String[] args) {
        JFrame frame = new JFrame("游戏开始界面");
        frame.setSize(800, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建开始游戏按钮
        JButton startButton = new JButton("开始游戏");
        startButton.setBounds(125, 200, 200, 60);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 点击开始游戏按钮时触发的动作
                frame.setVisible(false); // 隐藏开始界面
                startGame(); // 进入游戏界面
            }
        });
        frame.add(startButton);

        // 创建退出游戏按钮
        JButton exitButton = new JButton("退出游戏");
        exitButton.setBounds(475, 200, 200, 60);
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // 点击退出游戏按钮时触发的动作
                int choice = JOptionPane.showConfirmDialog(null, "确定要退出游戏吗？", "确认", JOptionPane.YES_NO_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    System.exit(0); // 结束游戏进程
                }
            }
        });
        frame.add(exitButton);

        // 设置窗口居中显示
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - frame.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - frame.getHeight()) / 2);
        frame.setLocation(x, y);

        frame.setLayout(null);
        frame.setVisible(true);
    }

    
		public static void startGame() {
			game = new TRexRunnerGame();
		}
	}
