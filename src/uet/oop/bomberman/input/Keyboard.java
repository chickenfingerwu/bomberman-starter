package uet.oop.bomberman.input;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Tiếp nhận và xử lý các sự kiện nhập từ bàn phím
 */
public class Keyboard implements KeyListener {

	private boolean aKeyIsPressed;
	private boolean[] keys = new boolean[120]; //120 is enough to this game
	public boolean up, down, left, right, space, enter;
	
	public void update() {
		up = keys[KeyEvent.VK_UP] || keys[KeyEvent.VK_W];
		down = keys[KeyEvent.VK_DOWN] || keys[KeyEvent.VK_S];
		left = keys[KeyEvent.VK_LEFT] || keys[KeyEvent.VK_A];
		right = keys[KeyEvent.VK_RIGHT] || keys[KeyEvent.VK_D];
		space = keys[KeyEvent.VK_SPACE] || keys[KeyEvent.VK_X];
		enter = keys[KeyEvent.VK_ENTER];
		/*KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

			@Override
			public boolean dispatchKeyEvent(KeyEvent ke) {
				synchronized (Keyboard.class) {
					switch (ke.getID()) {
						case KeyEvent.KEY_PRESSED:
							if ((ke.getKeyCode() == KeyEvent.VK_W) || (ke.getKeyCode() == KeyEvent.VK_UP)) {

							}
							break;

						case KeyEvent.KEY_RELEASED:
							if (ke.getKeyCode() == KeyEvent.VK_W) {
								wPressed = false;
							}
							break;
					}
					return false;
				}
			}
		});
	}*/
	}

	public boolean isKeyPressed(int keyEvent){
		return keys[keyEvent];
	}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
		
	}

}
