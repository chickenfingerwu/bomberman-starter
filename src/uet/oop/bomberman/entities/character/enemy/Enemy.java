package uet.oop.bomberman.entities.character.enemy;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.Message;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.Character;
import uet.oop.bomberman.entities.character.enemy.ai.AI;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.level.Coordinates;

import java.awt.*;

public abstract class Enemy extends Character {

	protected int _points;
	
	protected double _speed;
	protected AI _ai;

	protected final double MAX_STEPS;
	protected final double rest;
	protected double _steps;
	
	protected int _finalAnimation = 30;
	protected Sprite _deadSprite;
	
	public Enemy(int x, int y, Board board, Sprite dead, double speed, int points) {
		super(x, y, board);
		_points = points;
		_speed = speed;

		MAX_STEPS = Game.TILES_SIZE / _speed;
		rest = (MAX_STEPS - (int) MAX_STEPS) / MAX_STEPS;
		_steps = MAX_STEPS;
		_timeAfter = 20;
		_deadSprite = dead;
	}
	
	@Override
	public void update() {
		animate();
		
		if(!_alive) {
			afterKill();
			return;
		}
		
		if(_alive)
			calculateMove();
	}
	
	@Override
	public void render(Screen screen) {
		
		if(_alive)
			chooseSprite();
		else {
			if(_timeAfter > 0) {
				_sprite = _deadSprite;
				_animate = 0;
			} else {
				_sprite = Sprite.movingSprite(Sprite.mob_dead1, Sprite.mob_dead2, Sprite.mob_dead3, _animate, 60);
			}
				
		}
			
		screen.renderEntity((int)_x, (int)_y - _sprite.SIZE, this);
	}
	
	@Override
	public void calculateMove() {
		// TODO: Tính toán hướng đi và di chuyển Enemy theo _ai và cập nhật giá trị cho _direction
		// TODO: sử dụng canMove() để kiểm tra xem có thể di chuyển tới điểm đã tính toán hay không
		// TODO: sử dụng move() để di chuyển
		// TODO: nhớ cập nhật lại giá trị cờ _moving khi thay đổi trạng thái di chuyển
		int xa = 0;
		int ya = 0;
		if(_steps <= 0){
			_steps = MAX_STEPS;
			_direction = _ai.calculateDirection();
		}
		switch (_direction){
			case 0:
				ya--;
				break;
			case 1:
				xa++;
				break;
			case 2:
				ya++;
				break;
			case 3:
				xa--;
				break;
		}
		if(canMove(xa , ya)){
			move(xa * _speed, ya *_speed);
			_moving = true;
			_steps--;
		}
		else {

			_steps = 0;
			_moving = false;
		}
	}
	
	@Override
	public void move(double xa, double ya) {
		if(canMove(xa, ya)){
			_x+=xa;
			_y+=ya;
		}
		else{
			if(xa!=0) {
				xa--;
			}
			if(ya!=0){
				ya--;
			}
			if(canMove(xa, ya)) {
				_x += xa;
				_y += ya;
			}
		}
	}
	
	@Override
	public boolean canMove(double x, double y) {
		// TODO: kiểm tra có đối tượng tại vị trí chuẩn bị di chuyển đến và có thể di chuyển tới đó hay không
		//System.out.println("direction: " + _direction + " speed: " + _speed);
		Entity e[] = new Entity[4];

		e[0] = _board.getEntity(Coordinates.pixelToTile(_x + x), Coordinates.pixelToTile(_y + y) - 1, this);
		e[1] = _board.getEntity(Coordinates.pixelToTile(_x +_sprite.get_realWidth() + x - 1), Coordinates.pixelToTile(_y + y) - 1, this);
		e[2] = _board.getEntity(Coordinates.pixelToTile(_x + x), Coordinates.pixelToTile(_y + y + _sprite.get_realHeight() - 1) - 1, this);
		e[3] = _board.getEntity(Coordinates.pixelToTile(_x + x + _sprite.get_realWidth() - 1), Coordinates.pixelToTile(_y + y + _sprite.get_realHeight() - 1) - 1, this);

		if(!e[0].collide(this ) || !e[1].collide(this) || !e[2].collide(this) || !e[3].collide(this)){
			_ai.calculateDirection();
			return false;
		}
		return true;
	}

	@Override
	public boolean collide(Entity e) {
		// TODO: xử lý va chạm với Flame
		// TODO: xử lý va chạm với Bomber
		if(e instanceof Flame || e instanceof FlameSegment) {
			kill();
			return false;
		}

		if(e instanceof Bomber) {
			((Bomber) e).kill();
			return true;
		}

		if(e instanceof Enemy){
			return false;
		}
		return true;
	}
	
	@Override
	public void kill() {
		if(!_alive) return;
		_alive = false;
		
		_board.addPoints(_points);

		Message msg = new Message("+" + _points, getXMessage(), getYMessage(), 2, Color.white, 14);
		_board.addMessage(msg);
	}
	
	
	@Override
	protected void afterKill() {
		if(_timeAfter > 0) --_timeAfter;
		else {
			if(_finalAnimation > 0) --_finalAnimation;
			else
				remove();
		}
	}


	public double getSpeed(){
		return _speed;
	}

	protected abstract void chooseSprite();
}
