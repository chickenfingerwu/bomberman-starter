package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.character.enemy.Kondoria;
import uet.oop.bomberman.level.Coordinates;
import uet.oop.bomberman.level.FileLevelLoader;
import java.util.ArrayList;

public class PathFinderAIHigh {
    FileLevelLoader _levelMap;
    Board gameBoard;
    Bomber _bomber;
    int _height;
    int _width;
    Enemy _e;
    Coordinates playerPos;
    Coordinates enenemyPos;
    Coordinates originPos;
    private boolean haveNotLoadMap = true;
    int playerX, playerY;
    ArrayList<Coordinates> path;
    Coordinates[][] map;

    public PathFinderAIHigh(Board board, Bomber bomber, Enemy e) {
        gameBoard = board;
        _bomber = bomber;
        _e = e;
        _levelMap = (FileLevelLoader) board.getLevel();
        _height = _levelMap.getHeight();
        _width = _levelMap.getWidth();
        path = new ArrayList<Coordinates>();
        loadLevel();
        try {
            playerPos = map[_bomber.getYTile()][_bomber.getXTile()];
        }
        catch (NullPointerException exception){
            playerPos = map[playerY][playerX];
        }
        originPos = map[_e.getYTile()][_e.getXTile()];
        enenemyPos = originPos;
        findPath();
        makePath();
    }

    /**
     * update path
     */

    public void update(){
        if(_bomber != null) {
            playerPos = map[_bomber.getYTile()][_bomber.getXTile()];
        }
        originPos = map[_e.getYTile()][_e.getXTile()];
        path.clear();
        findPath();
        makePath();
    }

    /**
     * load the map for the AI
     */

    public void loadLevel() {
        map = new Coordinates[_height][_width];
        for (int j = 0; j < _height; j++) {
            for (int i = 0; i < _width; i++) {
                char symbol = _levelMap.get_map()[j][i];
                switch (symbol) {
                    case '1':
                        map[j][i] = new Coordinates(i, j, true);
                        break;
                    case '#':
                        map[j][i] = new Coordinates(i, j, false);
                        break;
                    case '*':
                        if(_e instanceof Kondoria) {
                            map[j][i] = new Coordinates(i, j, true);
                        }
                        else {
                            map[j][i] = new Coordinates(i, j, false);
                        }
                        break;
                    case 'p':
                        map[j][i] = new Coordinates(i, j, true);
                        playerPos = map[j][i];
                        playerY = j;
                        playerX = i;
                        break;
                    default:
                        map[j][i] = new Coordinates(i, j, true);
                        break;
                }
            }
        }
    }

    /**
     * find the nearest point in available tiles
     * @param openList
     * @param closedList
     * @return
     */

    public Coordinates findShortestDistance(ArrayList<Coordinates> openList, ArrayList<Coordinates> closedList){
        Coordinates shortest = null;
        if(openList.size() > 0) {
            shortest = openList.get(0);
            for (int i = 0; i < openList.size(); i++) {
                if(!closedList.contains(openList.get(i))) {
                    if (openList.get(i).getPoint() < shortest.getPoint()) {
                        shortest = openList.get(i);
                    }
                }
            }
        }
        return shortest;
    }

    /**
     * find shortest path to player
     */
    public void findPath(){
        ArrayList<Coordinates> openList = new ArrayList<Coordinates>();
        ArrayList<Coordinates> closedList = new ArrayList<Coordinates>();
        openList.add(originPos);
        while (!openList.isEmpty()){
            enenemyPos = findShortestDistance(openList, closedList);
            closedList.add(enenemyPos);
            openList.remove(enenemyPos);

            if(closedList.contains(playerPos)){
                break;
            }

            //array for all adjacent tiles
            ArrayList<Coordinates> adjacentTiles = new ArrayList<Coordinates>();

            //up tile
            if(enenemyPos._y > 0) {
                Coordinates upTile = map[enenemyPos._y - 1][enenemyPos._x];
                if(upTile.isWalkable() && gameBoard.getBombAt((int) upTile.get_x(), (int) upTile.get_y()) == null) {
                    adjacentTiles.add(upTile);
                }
            }

            //right tile
            if(enenemyPos._x < _width - 1) {
                Coordinates rightTile = map[enenemyPos._y][enenemyPos._x + 1];
                if(rightTile.isWalkable() && gameBoard.getBombAt((int) rightTile.get_x(), (int) rightTile.get_y()) == null) {
                    adjacentTiles.add(rightTile);
                }
            }

            //down tile
            if(enenemyPos._y < _height - 1) {
                Coordinates downTile = map[enenemyPos._y + 1][enenemyPos._x];
                if(downTile.isWalkable() && gameBoard.getBombAt((int) downTile.get_x(), (int) downTile.get_y()) == null) {
                    adjacentTiles.add(downTile);
                }
            }

            //left tile
            if(enenemyPos._x > 0) {
                Coordinates leftTile = map[enenemyPos._y][enenemyPos._x - 1];
                if(leftTile.isWalkable() && gameBoard.getBombAt((int) leftTile.get_x(), (int) leftTile.get_y()) == null) {
                    adjacentTiles.add(leftTile);
                }
            }

            for(int i = 0; i < adjacentTiles.size(); i++){
                Coordinates adjacent = adjacentTiles.get(i);
                if(closedList.contains(adjacent)) {
                    continue;
                }

                //check for bombs and other enemies before computing point
                if(gameBoard.getBombAt(adjacent.get_x(), adjacent.get_y()) == null
                && !(gameBoard.getCharacterAt(adjacent.get_x(), adjacent.get_y()) instanceof Enemy)
                        && gameBoard.getBombAt(adjacent.get_x(), adjacent.get_y()) == null
                && ((gameBoard.getBombAt(adjacent.get_x() + Game.getBombRadius(), adjacent.get_y()) == null
                && gameBoard.getBombAt(adjacent.get_x() - Game.getBombRadius(), adjacent.get_y()) == null) &&
                        (gameBoard.getBombAt(adjacent.get_x(), adjacent.get_y() + Game.getBombRadius()) == null
                && gameBoard.getBombAt(adjacent.get_x(), adjacent.get_y() - Game.getBombRadius()) == null))) {

                    if (!openList.contains(adjacent)) {
                        //compute the adjacent square point
                        int costToOriginPos = Math.abs(adjacent._x - originPos._x) + Math.abs(adjacent._y - originPos._y);
                        int costToDestination = Math.abs(adjacent._x - playerPos._x) + Math.abs(adjacent._y - playerPos._y);
                        computePoint(adjacent, costToOriginPos, costToDestination);
                        adjacent.setParents(enenemyPos);
                        openList.add(adjacent);
                    } else {
                        //compute the adjacent square point
                        int costToOriginPos = Math.abs(adjacent._x - originPos._x) + Math.abs(adjacent._y - originPos._y);
                        int costToDestination = Math.abs(adjacent._x - playerPos._x) + Math.abs(adjacent._y - playerPos._y);
                        int temp_point = costToOriginPos + costToDestination;
                        if (temp_point < adjacent.getPoint()) {
                            computePoint(adjacent, costToOriginPos, costToDestination);
                            adjacent.setParents(enenemyPos);
                        }
                    }
                }
            }
        }
    }

    public void computePoint(Coordinates square, int costToOriginPos, int costToDestination){
        square.setCostToOriginPos(costToOriginPos);
        square.setCostToDestination(costToDestination);
        square.calculatePoint();
    }

    /**
     * backtrack the path
     */
    public void makePath(){
        while (enenemyPos != originPos){
            if(enenemyPos != null) {
                path.add(enenemyPos);
            }
            enenemyPos = enenemyPos.getParents();
        }
    }

    public ArrayList<Coordinates> getPath() {
        return path;
    }

    public void setOriginPos(Coordinates originPos) {
        this.originPos = originPos;
    }

    public void setPlayerPos(Coordinates playerPos){
        this.playerPos = playerPos;
    }

    public void setEnenemyPos(Coordinates enenemyPos) {
        this.enenemyPos = enenemyPos;
    }
}
