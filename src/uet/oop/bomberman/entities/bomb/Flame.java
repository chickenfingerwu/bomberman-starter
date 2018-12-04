package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.enemy.Balloon;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.destroyable.DestroyableTile;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.level.Coordinates;

public class Flame extends Entity {

	protected Board _board;
	protected int _direction;
	private int _radius;
	protected int xOrigin, yOrigin;
	protected FlameSegment[] _flameSegments = new FlameSegment[0];

	/**
	 *
	 * @param x hoành độ bắt đầu của Flame
	 * @param y tung độ bắt đầu của Flame
	 * @param direction là hướng của Flame
	 * @param radius độ dài cực đại của Flame
	 */
	public Flame(int x, int y, int direction, int radius, Board board) {
		xOrigin = x;
		yOrigin = y;
		_x = x;
		_y = y;
		_direction = direction;
		_radius = radius;
		_board = board;
		createFlameSegments();
	}

	/**
	 * Tạo các FlameSegment, mỗi segment ứng một đơn vị độ dài
	 */
	private void createFlameSegments() {
		/**
		 * tính toán độ dài Flame, tương ứng với số lượng segment
		 */
		_flameSegments = new FlameSegment[calculatePermitedDistance()];

		/**
		 * biến last dùng để đánh dấu cho segment cuối cùng
		 */
		boolean last = false;
		if(_flameSegments.length == 0){
			last = true;
			return;
		}
		int xt = 0;
		int yt = 0;
		switch (_direction){
			case 0:
				yt = -1;
				break;
			case 1:
				xt = 1;
				break;
			case 2:
				yt = 1;
				break;
			case 3:
				xt = -1;
				break;
		}
		// TODO: tạo các segment dưới đây
		int copy_xOrigin = xOrigin;
		int copy_yOrigin = yOrigin;

		for(int i = 0; i < _flameSegments.length; i++){
			if(i == _flameSegments.length - 1){
				last = true;
			}
			_flameSegments[i] = new FlameSegment(copy_xOrigin+=xt, copy_yOrigin+=yt, _direction, last);
		}
	}

	/**
	 * Tính toán độ dài của Flame, nếu gặp vật cản là Brick/Wall, độ dài sẽ bị cắt ngắn
	 * @return
	 */
	private int calculatePermitedDistance() {
		// TODO: thực hiện tính toán độ dài của Flame
		int xEntity = xOrigin;
		int yEntity = yOrigin;
		Entity a = null;
		int testLengthX = 0;
		int testLengthY = 0;
		int testLength = 0;
		switch (_direction){
			case 0:
				testLengthY = -1;
				break;
			case 1:
				testLengthX = 1;
				break;
			case 2:
				testLengthY = 1;
				break;
			case 3:
				testLengthX = -1;
				break;
		}
		while (testLength < _radius)
		{
			if((xEntity + testLengthX < _board.getWidth() && xEntity + testLengthX >= 0)
					&& (yEntity + testLengthY < _board.getHeight() - 1 && yEntity + testLengthY >= 0)) {
				a = _board.getEntityAt(xEntity += testLengthX, yEntity += testLengthY);
				if(a instanceof LayeredEntity){
					Entity s = ((LayeredEntity) a).getTopEntity();
					if(s instanceof Grass){
						testLength++;
						continue;
					}
					if(s instanceof DestroyableTile) {
						((DestroyableTile) s).destroy();
						return testLength;
					}
				}
				if(!(a instanceof Grass)){
					return testLength;
				}
			}
			else {
				return testLength;
			}
			testLength++;
		}
		return testLength;
	}
	
	public FlameSegment flameSegmentAt(int x, int y) {
		for (int i = 0; i < _flameSegments.length; i++) {
			if(_flameSegments[i].getX() == x && _flameSegments[i].getY() == y)
				return _flameSegments[i];
		}
		return null;
	}

	@Override
	public void update() {
		//check original flame for characters or bomber or bombs
		Character character = _board.getCharacterAt((int) this.getX(), (int) this.getY());
		Bomb bomb = _board.getBombAt(this.getX(), this.getY());
		if(character != null) {
			if (this.getBottomLeft()._x <= character.getFarRight()._x ||
					this.getBottomLeft()._y <= character.getFarRight()._y ||
					this.getFarRight()._x >= character.getBottomLeft()._x ||
					this.getFarRight()._y >= character.getBottomLeft()._y) {
				//check if bomber can pass through flame
				if(character instanceof Bomber) {
					if(!Game.isFlamePassThrough()) {
						character.kill();
					}
					else {
						Game.addFlamePassTime(-1);
					}
				}
				else {
					character.kill();
				}
			}
		}
		if(bomb != null){
			if(!bomb._exploded) {
				bomb.explode();
			}
		}

		//check flame segments for characters
		for(int i = 0; i < _flameSegments.length; i++) {
			Entity flame = _flameSegments[i];
			character = _board.getCharacterAt((int) flame.getX(), (int) flame.getY());
			bomb = _board.getBombAt(flame.getX(), flame.getY());
			if(bomb != null){
				if(!bomb._exploded) {
					bomb.explode();
				}
			}
			if (character != null) {

				if (flame.getBottomLeft()._x <= character.getFarRight()._x ||
						flame.getBottomLeft()._y <= character.getFarRight()._y ||
						flame.getFarRight()._x >= character.getBottomLeft()._x ||
						flame.getFarRight()._y >= character.getBottomLeft()._y) {
					character.kill();
				}
			}
		}
	}
	
	@Override
	public void render(Screen screen) {
		for (int i = 0; i < _flameSegments.length; i++) {
			_flameSegments[i].render(screen);
		}
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý va chạm với Bomber, Enemy. Chú ý đối tượng này có vị trí chính là vị trí của Bomb đã nổ
		if(e instanceof Character){
			//check if bomber can pass through flame
			if(e instanceof Bomber){
				if (!Game.isFlamePassThrough()){
					((Bomber) e).kill();
					return true;
				}
				else {
					Game.addFlamePassTime(-1);
				}
			}
			((Character) e).kill();
			return true;
		}
		if(e instanceof Bomb){
			((Bomb) e).explode();
			return true;
		}
		return false;
	}
}
