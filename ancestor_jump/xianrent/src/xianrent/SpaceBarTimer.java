package xianrent;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;

public class SpaceBarTimer {
    private static int jumpTime;



    public static void setJumpTime(long startTime) {
        long endTime = System.currentTimeMillis();
        int duration = (int) (endTime - startTime);
        if (duration < 100) {
            duration = 100;
        } else if (duration > 700) {
            duration = 700;
        }
        jumpTime = duration;
 
    }

    public static int getJumpTime() {
        return jumpTime;
    }

    public static void main(String[] args) {

        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent e) {
                if (e.getID() == KeyEvent.KEY_PRESSED && e.getKeyCode() == KeyEvent.VK_SPACE) {
                    long startTime = System.currentTimeMillis();
                    setJumpTime(startTime);
                }
                return false;
            }
        });
    }


}