package uet.oop.bomberman.gui;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.input.Audio;

import javax.imageio.ImageIO;
import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Swing Panel hiển thị thông tin thời gian, điểm mà người chơi đạt được
 */
public class InfoPanel extends JPanel {
	Audio gameAudio;

	private BufferedImage heart;
	private ImageIcon heartIcon;
	private Image heartResize;

	final Image	speakerResize;
	final Image	speakerOffResize;
	final ImageIcon sResizeIcon;
	final ImageIcon sOffResizeIcon;

	BufferedImage speaker;
	BufferedImage speaker_off;

	private JLabel timeLabel;
	private JLabel pointsLabel;
	private JLabel speakerLabel;
	private JLabel LivesLabel;

	public InfoPanel(Game game) {
		setLayout(new GridLayout());
		try {
			speaker = ImageIO.read(new File("res/sprites/speaker.png"));
			speaker_off = ImageIO.read(new File("res/sprites/speaker_off.png"));
		}
		catch (IOException e){
			e.printStackTrace();
		}

		gameAudio = game.getAudio();



		try {
			heart = ImageIO.read(new File("res/sprites/heart.png"));
		}
		catch (IOException e){
			e.printStackTrace();
		}

		heartResize = heart.getScaledInstance(20, 20, Image.SCALE_SMOOTH);
		heartIcon = new ImageIcon(heartResize);
		LivesLabel = new JLabel(new ImageIcon(heartResize));

		speakerResize = speaker.getScaledInstance(16, 16, Image.SCALE_SMOOTH);
		speakerOffResize = speaker_off.getScaledInstance(16, 16, Image.SCALE_SMOOTH);

		speakerLabel = new JLabel(new ImageIcon(speakerResize));
		speakerLabel.setHorizontalAlignment(JLabel.CENTER);
		sResizeIcon = new ImageIcon(speakerResize);
		sOffResizeIcon = new ImageIcon(speakerOffResize);

		speakerLabel.addMouseListener(new MouseListener() {
			boolean clicked = false;
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!clicked) {
					speakerLabel.setIcon(sOffResizeIcon);
					gameAudio.setMusicOff();
					clicked = true;
				}
				else {
					speakerLabel.setIcon(sResizeIcon);
					gameAudio.setMusicOn();
					clicked = false;
				}
			}

			@Override
			public void mousePressed(MouseEvent e) {

			}

			@Override
			public void mouseReleased(MouseEvent e) {

			}

			@Override
			public void mouseEntered(MouseEvent e) {

			}

			@Override
			public void mouseExited(MouseEvent e) {

			}
		});

		timeLabel = new JLabel("Time: " + game.getBoard().getTime());
		timeLabel.setForeground(Color.white);
		timeLabel.setHorizontalAlignment(JLabel.CENTER);
		
		pointsLabel = new JLabel("Points: " + game.getBoard().getPoints());
		pointsLabel.setForeground(Color.white);
		pointsLabel.setHorizontalAlignment(JLabel.CENTER);
		
		add(timeLabel);
		add(pointsLabel);
		add(speakerLabel);
		add(LivesLabel);

		setBackground(Color.black);
		setPreferredSize(new Dimension(0, 40));
	}

	public void setTime(int t) {
		timeLabel.setText("Time: " + t);
	}

	public void setPoints(int t) {
		pointsLabel.setText("Score: " + t);
	}

}
