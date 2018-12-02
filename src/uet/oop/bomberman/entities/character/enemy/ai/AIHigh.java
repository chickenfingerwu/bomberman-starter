package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.level.Coordinates;

public class AIHigh extends AI {
    PathFinderAIHigh pathFinder;
    Bomber _bomber;
    Enemy _e;
    public AIHigh(Board board, Bomber bomber, Enemy e){
        _bomber = bomber;
        _e = e;
        pathFinder = new PathFinderAIHigh(board, _bomber, _e);
    }

    @Override
    public int calculateDirection(){
        if(_bomber == null){
            return random.nextInt(4);
        }
        pathFinder.update();
        if(pathFinder.getPath().size() > 0) {
            Coordinates newPos = pathFinder.getPath().get(pathFinder.getPath().size() - 1);
            //System.out.println("AI's pos: x = " + newPos._x + " y = " + newPos._y);
            //System.out.println("Player's pos: x = " + pathFinder.getPath().get(0)._x + " y = " + pathFinder.getPath().get(0)._y);
            if (newPos.get_x() < _e.getXTile()) {
                System.out.println("player is left");
                return 3;
            }
            if (newPos.get_x() > _e.getXTile()) {
                System.out.println("player is right");
                return 1;
            }
            if (newPos.get_y() < _e.getYTile()) {
                System.out.println("player is up");
                return 0;
            }
            if (newPos.get_y() > _e.getYTile()) {
                System.out.println("player is down");
                return 2;
            }
        }
        return random.nextInt(4);
    }
}
