package uet.oop.bomberman.gui;

import uet.oop.bomberman.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Swing Frame chứa toàn bộ các component
 */
public class Frame extends JFrame {

	public GamePanel _gamepane;
	//public StartPanel _startScreen;
	private JPanel _containerpane;
	private JPanel _startScreen;
	private InfoPanel _infopanel;
	
	private Game _game;

	Image backgroundResize = null;
	BufferedImage background = null;

	public Frame() {

		_containerpane = new JPanel(new BorderLayout());
		_gamepane = new GamePanel(this);
		_infopanel = new InfoPanel(_gamepane.getGame());

		_containerpane.add(_infopanel, BorderLayout.PAGE_START);
		_containerpane.add(_gamepane, BorderLayout.PAGE_END);

		_game = _gamepane.getGame();

		add(_containerpane);

		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setLocationRelativeTo(null);
		setVisible(true);
		if(_game != null) {
			_game.start();
		}
	}


	public void setTime(int time) {
		_infopanel.setTime(time);
	}
	
	public void setPoints(int points) {
		_infopanel.setPoints(points);
	}

	public void setLives(int live){
		_infopanel.setTextLiveLabel(live);
	}
	
}
