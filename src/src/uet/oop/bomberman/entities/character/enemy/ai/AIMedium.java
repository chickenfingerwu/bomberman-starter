package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;

public class AIMedium extends AI {
	Board _board;
	Bomber _bomber;
	Enemy _e;
	
	public AIMedium(Board board, Bomber bomber, Enemy e) {
		_board = board;
		_bomber = bomber;
		_e = e;
	}
	@Override
	public int calculateDirection() {
		//board might not have bomber yet after initializing the ai
		if(_bomber == null) {
			_bomber = _board.getBomber();
		}
		//if bomber is still null then just go random
		if(_bomber == null || _bomber.getLivesLost())
			return random.nextInt(4);

		int vertical = random.nextInt(2);

		if(vertical == 1) {
			int v = calculateRowDirection();
			if(v != -1)
				return v;
			else
				return calculateColDirection();

		} else {
			int h = calculateColDirection();

			if(h != -1)
				return h;
			else
				return calculateRowDirection();
		}

	}

	protected int calculateColDirection() {
		if(_bomber.getXTile() < _e.getXTile())
			return 3;
		else if(_bomber.getXTile() > _e.getXTile())
			return 1;

		return -1;
	}

	protected int calculateRowDirection() {
		if(_bomber.getYTile() < _e.getYTile())
			return 0;
		else if(_bomber.getYTile() > _e.getYTile())
			return 2;
		return -1;
	}

}
