package uet.oop.bomberman.gui;

import uet.oop.bomberman.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Swing Panel chứa cảnh game
 */
public class GamePanel extends JPanel {


	private Game _game;

	public GamePanel(Frame frame) {

		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));

		_game = new Game(frame);
		add(_game);
		_game.setVisible(true);


		setVisible(true);
		setFocusable(true);

	}


	public Game getGame() {
		return _game;
	}
	
}
