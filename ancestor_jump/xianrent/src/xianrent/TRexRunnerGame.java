package demo;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TRexRunnerGame extends JFrame {

    private GamePanel gamePanel;

    private static final int MIN_OBSTACLE_FREQUENCY = 5;
    private static final int MAX_OBSTACLE_FREQUENCY = 10;

    public TRexRunnerGame() {
        setTitle("T-Rex Runner");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        gamePanel = new GamePanel();
        add(gamePanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TRexRunnerGame::new);
    }

    private class GamePanel extends JPanel {

        private static final int WIDTH = 800;
        private static final int HEIGHT = 400;
        private static final int GROUND_Y = 350;
        private static int JUMP_DURATION =SpaceBarTimer.getJumpTime(); // Jump duration in milliseconds
        private static  int JUMP_HEIGHT; // Height to jump in pixels
        private static final int OBSTACLE_WIDTH = 30;
        private static final int MIN_OBSTACLE_HEIGHT = 20;
        private static final int MAX_OBSTACLE_HEIGHT = 80;
        private static final int OBSTACLE_SPEED = 4;





        

        private int playerY;
        private boolean isJumping;
        private long jumpStartTime;
        private List<Obstacle> obstacles;
        private Timer timer;
        private int score;

        private Image playerImage;
        private Image obstacleImage1;
        private Image obstacleImage2;
        private Image obstacleImage3;

        private boolean isBlueObstacleSet;
        private boolean isGreenObstacleSet;

        public GamePanel() {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setBackground(Color.PINK);
            setFocusable(true);

            try {
                // Load player and obstacle images
                playerImage = ImageIO.read(new File("C:/Users/caoan/Desktop/picture/player.png8"));
                obstacleImage1 = ImageIO.read(new File("C:/Users/caoan/Desktop/picture/obstacle1.png8"));
                obstacleImage2 = ImageIO.read(new File("C:/Users/caoan/Desktop/picture/obstacle2.png8"));
                obstacleImage3 = ImageIO.read(new File("C:/Users/caoan/Desktop/picture/obstacle3.png8"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                        jump();
                    }
                }
            });

            obstacles = new ArrayList<>();
            generateObstacle();

            timer = new Timer(10, e -> {
                update();
                repaint();
            });
            timer.start();
        }

        private void jump() {
            if (!isJumping) {
                isJumping = true;
                playerY = GROUND_Y;
                jumpStartTime = System.currentTimeMillis();
            }
        }

        private void generateObstacle() {
            Random random = new Random();
            int obstacleHeight = random.nextInt(MAX_OBSTACLE_HEIGHT - MIN_OBSTACLE_HEIGHT + 1) + MIN_OBSTACLE_HEIGHT;
            int obstacleX = WIDTH;
            int obstacleY = GROUND_Y - obstacleHeight;
            obstacles.add(new Obstacle(obstacleX, obstacleY, OBSTACLE_WIDTH, obstacleHeight, obstacleImage1));
        }

        private void update() {
            if (isJumping) {
		    if(JUMP_DURATION<300){
			JUMP_HEIGHT=50;
		}else if(JUMP_DURATION>=300 && JUMP_DURATION<400) {
			JUMP_HEIGHT=70;
		}else if(JUMP_DURATION>=400 && JUMP_DURATION<500) {
			JUMP_HEIGHT=90;
		}else if(JUMP_DURATION>=500 && JUMP_DURATION<600) {
			JUMP_HEIGHT=120;
		}else if(JUMP_DURATION>=600 && JUMP_DURATION<700) {
			JUMP_HEIGHT=160;
		}

                long currentTime = System.currentTimeMillis();
                long elapsedTime = currentTime - jumpStartTime;
                if (elapsedTime >= JUMP_DURATION) {
                    isJumping = false;
                    playerY = GROUND_Y;
                } else {
                    double progress = (double) elapsedTime / JUMP_DURATION;
                    if (progress <= 0.5) {
                        playerY = GROUND_Y - (int) (progress * 2 * JUMP_HEIGHT);
                    } else {
                        playerY = GROUND_Y - (int) ((1 - progress) * 2 * JUMP_HEIGHT);
                    }
                }
            } else {
                playerY = GROUND_Y;
            }

            for (int i = obstacles.size() - 1; i >= 0; i--) {
                Obstacle obstacle = obstacles.get(i);
                obstacle.move(getObstacleSpeed());
                if (obstacle.getX() + obstacle.getWidth() < 0) {
                    obstacles.remove(obstacle);
                    break;
                }

                if (isCollision(obstacle)) {
                    gameOver();
                    break;
                }
            }

            if (obstacles.isEmpty() || obstacles.get(obstacles.size() - 1).getX() <= WIDTH - getObstacleGap()) {
                generateObstacle();
                score++;

                if (score >= 16 && score <= 30 ) {
                    setBlueObstacleProperties();
                    isBlueObstacleSet = true;
                } else if (score >= 31 && score <= 45 ) {
                    setGreenObstacleProperties();
                    isGreenObstacleSet = true;
                }
            }

            if (score == 46) {
                JOptionPane.showMessageDialog(this, "You Win!", "Win", JOptionPane.INFORMATION_MESSAGE);
                System.exit(0);
            }
        }

        private void setBlueObstacleProperties() {
            obstacles.forEach(obstacle -> obstacle.setImage(obstacleImage2));
        }

        private void setGreenObstacleProperties() {
            obstacles.forEach(obstacle -> obstacle.setImage(obstacleImage3));
        }

        private int getObstacleGap() {
            if (score >= 30) {
                return Obstacle.getObstacleGap();
            } else if (score >= 15) {
                return 300;
            } else {
                return 350;
            }
        }

        private int getObstacleSpeed() {
            if (score >= 30) {
                return Obstacle.getObstacleSpeed();
            } else if (score >= 15) {
                return 5;
            } else {
                return 4;
            }
        }

        private boolean isCollision(Obstacle obstacle) {
            int playerLeft = 100;
            int playerRight = 100 + 50;
            int playerTop = playerY - 50;
            int playerBottom = playerY;
            int obstacleLeft = obstacle.getX();
            int obstacleRight = obstacle.getX() + obstacle.getWidth();
            int obstacleTop = obstacle.getY();
            int obstacleBottom = obstacle.getY() + obstacle.getHeight();

            if (playerRight >= obstacleLeft && playerLeft <= obstacleRight) {
                if (playerBottom >= obstacleTop && playerTop <= obstacleBottom) {
                    return true;
                }
            }

            return false;
        }

        private void gameOver() {
            timer.stop();
            JOptionPane.showMessageDialog(this, "Game Over! Score: " + score, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            drawPlayer(g);
            for (Obstacle obstacle : obstacles) {
                obstacle.draw(g);
            }
            drawScore(g);
        }

        private void drawPlayer(Graphics g) {
            g.drawImage(playerImage, 100, playerY - 50, 50, 50, null);
        }

        private void drawScore(Graphics g) {
            g.setColor(Color.BLACK);
            g.setFont(new Font(Font.MONOSPACED, Font.BOLD, 20));
            g.drawString("Score: " + score, 20, 30);
        }

        private class Obstacle {
            private int x;
            private int y;
            private int width;
            private int height;
            private static int obstacleGap = 250;
            private static int obstacleSpeed = 4;
            private static Color obstacleColor = Color.RED;
            private Image obstacleImage;

            public Obstacle(int x, int y, int width, int height, Image obstacleImage) {
                this.x = x;
                this.y = y;
                this.width = width;
                this.height = height;
                this.obstacleImage = obstacleImage;
            }

            public int getX() {
                return x;
            }

            public int getY() {
                return y;
            }

            public int getWidth() {
                return width;
            }

            public int getHeight() {
                return height;
            }

            public void move(int speed) {
                x -= speed;
            }

            public void setImage(Image image) {
                this.obstacleImage = image;
            }

            public void draw(Graphics g) {
                g.drawImage(obstacleImage, x, y, width, height, null);
            }

            public static void setObstacleGap(int gap) {
                obstacleGap = gap;
            }

            public static void setObstacleSpeed(int speed) {
                obstacleSpeed = speed;
            }

            public static void setObstacleColor(Color color) {
                obstacleColor = color;
            }

            public static int getObstacleGap() {
                return obstacleGap;
            }

            public static int getObstacleSpeed() {
                return obstacleSpeed;
            }
        }
    }
}
