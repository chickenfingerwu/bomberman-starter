package uet.oop.bomberman;

import org.w3c.dom.events.Event;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.gui.Frame;
import uet.oop.bomberman.input.Audio;
import uet.oop.bomberman.input.Keyboard;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;

/**
 * Tạo vòng lặp cho game, lưu trữ một vài tham số cấu hình toàn cục,
 * Gọi phương thức render(), update() cho tất cả các entity
 */
public class Game extends Canvas {

	public static final int TILES_SIZE = 16,
							WIDTH = TILES_SIZE * (31 / 2),
							HEIGHT = 13 * TILES_SIZE;

	public static int SCALE = 3;
	
	public static final String TITLE = "BombermanGame";

	boolean restartGame = false;
	boolean restartAllOver = false;

	private static int BOMBERLIVES = 3;
	private static final int BOMBRATE = 1;
	private static final int BOMBRADIUS = 1;
	private static final double BOMBERSPEED = 1.0;

	private static final int TIMEFORFLAMEPASS = 2;

	public static final int TIME = 200;
	public static final int POINTS = 0;
	
	protected static int SCREENDELAY = 3;

	protected static int bombRate = BOMBRATE;
	protected static int bombRadius = BOMBRADIUS;
	protected static double bomberSpeed = BOMBERSPEED;
	protected static int bomberLives =  BOMBERLIVES;
	protected static boolean flamePassThrough = false;
	private static int timeForFlamePassPowerUp = 0;

	protected int _screenDelay = SCREENDELAY;
	
	private Keyboard _input;
	private static Audio _audio;
	private boolean _running = false;
	private boolean _paused = true;

	private Board _board;
	private Screen screen;
	private Frame _frame;

	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();
	
	public Game(Frame frame) {

		_frame = frame;
		_frame.setTitle(TITLE);

		screen = new Screen(WIDTH, HEIGHT);
		_input = new Keyboard();
		_audio = new Audio();

		_board = new Board(this, _input, screen);

		addKeyListener(_input);
	}
	
	
	private void renderGame() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		
		_board.render(screen);
		
		for (int i = 0; i < pixels.length; i++) {
			pixels[i] = screen._pixels[i];
		}
		
		Graphics g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
		_board.renderMessages(g);

		g.dispose();
		bs.show();
	}
	
	private void renderScreen() {
		BufferStrategy bs = getBufferStrategy();
		if(bs == null) {
			createBufferStrategy(3);
			return;
		}
		
		screen.clear();
		
		Graphics g = bs.getDrawGraphics();
		
		_board.drawScreen(g);

		g.dispose();
		bs.show();
	}

	private void update() {
		if(flamePassThrough){
			if(timeForFlamePassPowerUp <= 0){
				flamePassThrough = false;
				timeForFlamePassPowerUp = 0;
			}
		}
		_input.update();
		_board.update();
		_audio.update();
	}

	
	public void start() {
		_running = true;
		long  lastTime = System.nanoTime();
		long timer = System.currentTimeMillis();
		final double ns = 1000000000.0 / 60.0; //nanosecond, 60 frames per second
		double delta = 0;
		int frames = 0;
		int updates = 0;
		requestFocus();
		boolean isPausing = false;
		while(_running) {
			long now = System.nanoTime();
			delta += (now - lastTime) / ns;
			lastTime = now;
			while(delta >= 1) {
				update();
				updates++;
				delta--;
			}


			if(_paused) {
				if(_screenDelay <= 0) {
					_board.setShow(-1);
					_paused = false;
				}
                if(isPausing) {
                    _board.setShow(3);
                    _audio.setMusicOff();
                }
				renderScreen();
			} else {
				renderGame();
			}


			/**
			* check if the user pressed pause
			 */

			if(_input.escape){
			    if(!isPausing){
                    pause();
                    resetScreenDelay();
                }
                else {
                    _paused = false;
                }
            }
            else {
                if(_paused && _board.getShow() != 2 && _board.getShow() != 1 && _board.getShow() != 4){
                    isPausing = true;
                }
                if(!_paused && isPausing){
                    isPausing = false;
					_audio.setMusicOn();
                }
            }

			/**
			 * check for restarting game
			 */

			if(restartGame){
				resetGameVariables();
			}
			else if(restartAllOver){
				resetGameVariables();
			}

			frames++;
			_frame.setLives(_board.getBomber().getLives());
			if(System.currentTimeMillis() - timer > 1000) {
				_frame.setTime(_board.subtractTime());
				_frame.setPoints(_board.getPoints());
				timer += 1000;
				_frame.setTitle(TITLE + " | " + updates + " rate, " + frames + " fps");
				updates = 0;
				frames = 0;
				
				if(_board.getShow() == 2)
					--_screenDelay;
			}
		}
	}

	public void resetGameVariables() {
		_paused = false;
		_board.getBomber().remove();
		_board.getBomber().setAlive();
		addBombRate(-(getBombRate() - BOMBRATE));
		addBombRadius(-(getBombRadius() - BOMBRADIUS));
		addBomberSpeed(-(getBomberSpeed() - BOMBERSPEED));
		addLives(-(getBomberLives() - BOMBERLIVES));
		flamePassThrough = false;
		timeForFlamePassPowerUp = 0;
		Game.getAudio().getCurrentMusic().stop();
		if(_board.getShow() == 1) {
			_board.loadLevel(_board.getLevel().getLevel());
			restartGame = false;
		}
		else if(_board.getShow() == 4){
			_board.loadLevel(1);
			restartAllOver = false;
		}
		_board.resetTime();
	}

	public static void playSoundEffect(){
		_audio.playPowerUp();
	}
	
	public static double getBomberSpeed() {
		return bomberSpeed;
	}
	
	public static int getBombRate() {
		return bombRate;
	}
	
	public static int getBombRadius() { return bombRadius; }

	public static int getBomberLives() { return bomberLives; }

	public static boolean isFlamePassThrough() { return flamePassThrough; }

	public static void makeFlamePass() {
		flamePassThrough = true;
		timeForFlamePassPowerUp += TIMEFORFLAMEPASS;
	}

	public static void addFlamePassTime(int i){
		timeForFlamePassPowerUp += i;
	}

	public static void addBomberSpeed(double i) { bomberSpeed += i; }

	public static void addLives(int i) { bomberLives += i; }
	
	public static void addBombRadius(int i) {
		bombRadius += i;
	}
	
	public static void addBombRate(int i) {
		bombRate += i;
	}

	public void resetScreenDelay() {
		_screenDelay = SCREENDELAY;
	}

	public Keyboard get_input() {
		return _input;
	}

	public Board getBoard() {
		return _board;
	}

	public void restartGame(){
		restartGame = true;
	}

	public void setRestartAllOver(){
		restartAllOver = true;
	}

	public boolean isPaused() {
		return _paused;
	}
	
	public void pause() {
		_paused = true;
	}

	public int getTimeforflamepass(){ return timeForFlamePassPowerUp; }

	public static Audio getAudio() { return _audio; }
}
