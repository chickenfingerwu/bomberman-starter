package uet.oop.bomberman.entities.bomb;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;


public class FlameSegment extends AnimatedEntitiy {

	protected int _direction;
	protected boolean _last;
	public int _timeAfter = 20;
	/**
	 *
	 * @param x
	 * @param y
	 * @param direction
	 * @param last cho biết segment này là cuối cùng của Flame hay không,
	 *                segment cuối có sprite khác so với các segment còn lại
	 */
	public FlameSegment(int x, int y, int direction, boolean last) {
		_x = x;
		_y = y;
		_last = last;
		_direction = direction;

		switch (direction) {
			case 0:
				if(!last) {
					_sprite = Sprite.explosion_vertical2;
				} else {
					_sprite = Sprite.explosion_vertical_top_last2;
				}
			break;
			case 1:
				if(!last) {
					_sprite = Sprite.explosion_horizontal2;
				} else {
					_sprite = Sprite.explosion_horizontal_right_last2;
				}
				break;
			case 2:
				if(!last) {
					_sprite = Sprite.explosion_vertical2;
				} else {
					_sprite = Sprite.explosion_vertical_down_last2;
				}
				break;
			case 3: 
				if(!last) {
					_sprite = Sprite.explosion_horizontal2;
				} else {
					_sprite = Sprite.explosion_horizontal_left_last2;
				}
				break;
		}
	}
	
	@Override
	public void render(Screen screen) {

		int xt = (int)_x << 4;
		int yt = (int)_y << 4;

		switch (_direction) {
			case 0:
				if(!_last) {
					_sprite = Sprite.movingSprite(Sprite.explosion_vertical, Sprite.explosion_vertical1, Sprite.explosion_vertical2, _animate, 15);
				} else {
					_sprite = Sprite.movingSprite(Sprite.explosion_vertical_top_last, Sprite.explosion_vertical_top_last1, Sprite.explosion_vertical_top_last2, _animate, 15);
				}
				break;
			case 1:
				if(!_last) {
					_sprite = Sprite.movingSprite(Sprite.explosion_horizontal, Sprite.explosion_horizontal1, Sprite.explosion_horizontal2, _animate, 15);
				} else {
					_sprite = Sprite.movingSprite(Sprite.explosion_horizontal_right_last, Sprite.explosion_horizontal_right_last1, Sprite.explosion_horizontal_right_last2, _animate, 15);
				}
				break;
			case 2:
				if(!_last) {
					_sprite = Sprite.movingSprite(Sprite.explosion_vertical, Sprite.explosion_vertical1, Sprite.explosion_vertical2, _animate, 15);
				} else {
					_sprite = Sprite.movingSprite(Sprite.explosion_vertical_down_last, Sprite.explosion_vertical_down_last1, Sprite.explosion_vertical_down_last2, _animate, 15);
				}
				break;
			case 3:
				if(!_last) {
					_sprite = Sprite.movingSprite(Sprite.explosion_horizontal, Sprite.explosion_horizontal1, Sprite.explosion_horizontal2, _animate, 15);
				} else {
					_sprite = Sprite.movingSprite(Sprite.explosion_horizontal_left_last, Sprite.explosion_horizontal_left_last1, Sprite.explosion_horizontal_left_last2, _animate, 15);
				}
				break;
		}

		screen.renderEntity(xt, yt , this);
	}
	
	@Override
	public void update() {
		if(_timeAfter > 0){
			_timeAfter--;
		}
		animate();
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý khi FlameSegment va chạm với Character
		if(e instanceof Bomber) {
			if (Game.isFlamePassThrough()){
				//need this condition bc if bomber is moving and flamesegment is colliding,
				// 2 collide() methods are called which will substract the flamepasstime by 2 instead of 1
				//TODO:
				if(!((Bomber) e).isMoving()) {
					Game.addFlamePassTime(-1);
				}
			}
			else {
				((Bomber) e).kill();
			}
			return true;
		}
		if(e instanceof Enemy){
			((Enemy) e).kill();
			return true;
		}
		if(e instanceof Bomb){
			((Bomb) e).explode();
			return true;
		}
		return true;
	}

	public int get_timeAfter() {
		return _timeAfter;
	}

}