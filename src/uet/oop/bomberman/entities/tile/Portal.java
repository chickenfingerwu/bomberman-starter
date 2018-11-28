package uet.oop.bomberman.entities.tile;

import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

import java.io.IOException;

public class Portal extends Tile {

	public Portal(int x, int y, Sprite sprite) {
		super(x, y, sprite);
	}
	
	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý khi Bomber đi vào
		if(e instanceof Bomber){
			Bomber s = (Bomber) e;
			if(s.getBoard().detectNoEnemies()){
				if(Coordinates.pixelToTile(s.getX()) == _x && Coordinates.pixelToTile(s.getY()) - 1 == _y) {
					s.getBoard().nextLevel();
				}
			}
		}
		return true;
	}

}
