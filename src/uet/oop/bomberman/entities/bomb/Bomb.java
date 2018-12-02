package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

public class Bomb extends AnimatedEntitiy {

	protected double _timeToExplode = 120; //2 seconds
	public int _timeAfter = 20;
	
	protected Board _board;
	protected Flame[] _flames = new Flame[4];
	protected boolean _exploded = false;
	protected boolean _allowedToPassThru = true;
	
	public Bomb(int x, int y, Board board) {
		_x = x;
		_y = y;
		_board = board;
		_sprite = Sprite.bomb;
	}
	
	@Override
	public void update() {
		if(_timeToExplode > 0) 
			_timeToExplode--;
		else {
			if(!_exploded) 
				explode();
			else {
				updateFlames();
			}
			if(_timeAfter > 0)
				_timeAfter--;
			else {
				remove();
			}
		}
			
		animate();
	}
	
	@Override
	public void render(Screen screen) {
		if(_exploded) {
			_sprite =  Sprite.bomb_exploded2;
			renderFlames(screen);
		} else
			_sprite = Sprite.movingSprite(Sprite.bomb, Sprite.bomb_1, Sprite.bomb_2, _animate, 60);
		
		int xt = (int)_x << 4;
		int yt = (int)_y << 4;
		
		screen.renderEntity(xt, yt , this);
	}
	
	public void renderFlames(Screen screen) {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].render(screen);
		}
	}
	
	public void updateFlames() {
		for (int i = 0; i < _flames.length; i++) {
			_flames[i].update();
		}
	}

    /**
     * Xử lý Bomb nổ
     */
	protected void explode() {
		_exploded = true;
		_timeToExplode = 0;
		//_board.getGame().getAudio().playExploding();
		_board.getGame().getAudio().playExploding();
		// TODO: xử lý khi Character đứng tại vị trí Bomb
		int bombX = Coordinates.pixelToTile(_board.getBomber().getX());
		int bombY = Coordinates.pixelToTile(_board.getBomber().getY()) - 1;

		if(bombX == _x && bombY == _y){
			_board.getBomber().kill();
		}

		// TODO: tạo các Flame
		int _direction = 0;
		for(int i = 0; i < 4; i++){
			_flames[i] = new Flame((int) _x,(int) _y, _direction, (int) Game.getBombRadius(), _board);
			_direction++;
		}
	}
	
	public FlameSegment flameAt(int x, int y) {
		if(!_exploded) return null;
		
		for (int i = 0; i < _flames.length; i++) {
			if(_flames[i] == null) return null;
			FlameSegment e = _flames[i].flameSegmentAt(x, y);
			if(e != null) return e;
		}
		
		return null;
	}

	@Override
	public boolean collide(Entity e) {
        // TODO: xử lý khi Bomber đi ra sau khi vừa đặt bom (_allowedToPassThru)
        // TODO: xử lý va chạm với Flame của Bomb khác
		if(e instanceof Bomber || e instanceof Enemy){
			if(!_allowedToPassThru){
				return false;
			}
			//4 side of the bomb sprite
			int xBombLeft = Coordinates.tileToPixel(_x);
			int yBombUp = Coordinates.tileToPixel(_y);
			int xBombRight = xBombLeft + Game.TILES_SIZE;
			int yBombBottom = yBombUp + Game.TILES_SIZE;

			//4 side of bomber sprite
			int xBomberLeft = (int) e.getX();
			int yBomberUp = (int) e.getY() - Game.TILES_SIZE;
			int xBomberRight = xBomberLeft + e.getSprite().get_realWidth();
			int yBomberBottom = yBomberUp + e.getSprite().get_realHeight();

			if (xBombLeft >= xBomberRight || xBombRight <= xBomberLeft || yBombUp >= yBomberBottom || yBombBottom <= yBomberUp) {
				_allowedToPassThru = false;
			}
			return _allowedToPassThru;
		}
		if(e instanceof FlameSegment || e instanceof Flame){
			if(!_exploded){
				explode();
				return true;
			}
		}
		return false;
	}
}
