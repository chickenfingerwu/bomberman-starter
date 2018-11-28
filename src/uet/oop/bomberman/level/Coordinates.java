package uet.oop.bomberman.level;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;

public class Coordinates {
	public int _x;
	public int _y;

	public Coordinates(int x, int y){
		_x = x;
		_y = y;
	}

	public static int pixelToTile(double i) {
		return (int)(i / Game.TILES_SIZE);
	}
	
	public static int tileToPixel(int i) {
		return i * Game.TILES_SIZE;
	}
	
	public static int tileToPixel(double i) {
		return (int)(i * Game.TILES_SIZE);
	}

}
