package uet.oop.bomberman.gui;

import uet.oop.bomberman.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class StartPanel extends JPanel {
    public StartPanel(){
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(Game.WIDTH * Game.SCALE, Game.HEIGHT * Game.SCALE));
        JPanel bg = new JPanel();
        try {
            Image background = ImageIO.read(new File("res/sprites/bomberman_logo.png"));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

}
