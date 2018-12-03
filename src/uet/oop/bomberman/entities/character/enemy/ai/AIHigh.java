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
        if(_bomber == null || _bomber.getLivesLost()){
            return random.nextInt(4);
        }
        pathFinder.update();
        if(pathFinder.getPath().size() > 0) {
            Coordinates newPos = pathFinder.getPath().get(pathFinder.getPath().size() - 1);
            if (newPos.get_x() < _e.getXTile()) {
                return 3;
            }
            if (newPos.get_x() > _e.getXTile()) {
                return 1;
            }
            if (newPos.get_y() < _e.getYTile()) {
                return 0;
            }
            if (newPos.get_y() > _e.getYTile()) {
                return 2;
            }
        }
        return random.nextInt(4);
    }
}
