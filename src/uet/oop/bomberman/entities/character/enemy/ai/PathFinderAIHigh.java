package uet.oop.bomberman.entities.character.enemy.ai;

import uet.oop.bomberman.Board;
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
        originPos = map[_e.getYTile()][_e.getXTile()];
        enenemyPos = originPos;
        playerPos = map[_bomber.getYTile()][_bomber.getXTile()];
        findPath();
        makePath();
    }

    private boolean playerChangedPosition(){
        return (_bomber.getXTile() != playerPos.get_x() || _bomber.getYTile() - 1 != playerPos.get_y());
    }

    public void update(){
        if(_bomber != null) {
            playerPos = map[_bomber.getYTile()][_bomber.getXTile()];
            originPos = map[_e.getYTile()][_e.getXTile()];
            path.clear();
            findPath();
            makePath();
        }
    }

    public void loadLevel() {
        map = new Coordinates[_height][_width];
        for (int j = 0; j < _height; j++) {
            for (int i = 0; i < _width; i++) {
                char symbol = _levelMap.get_map()[j][i];
                switch (symbol) {
                    case 'p':
                        map[j][i] = new Coordinates(i, j, true);
                        playerPos = map[j][i];
                        break;
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
                    default:
                        map[j][i] = new Coordinates(i, j, true);
                        break;
                }
            }
        }
    }

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

            //array for all adjacent tile
            ArrayList<Coordinates> adjacentTiles = new ArrayList<Coordinates>();

            //up tile
            if(enenemyPos._y > 0) {
                Coordinates upTile = map[enenemyPos._y - 1][enenemyPos._x];
                if(upTile.isWalkable() && gameBoard.getBombAt(enenemyPos._x, enenemyPos._y - 1) == null) {
                    adjacentTiles.add(upTile);
                }
            }

            //right tile
            if(enenemyPos._x < _width - 1) {
                Coordinates rightTile = map[enenemyPos._y][enenemyPos._x + 1];
                if(rightTile.isWalkable() && gameBoard.getBombAt(enenemyPos._x + 1, enenemyPos._y) == null) {
                    adjacentTiles.add(rightTile);
                }
            }

            //down tile
            if(enenemyPos._y < _height - 1) {
                Coordinates downTile = map[enenemyPos._y + 1][enenemyPos._x];
                if(downTile.isWalkable() && gameBoard.getBombAt(enenemyPos._x, enenemyPos._y + 1) == null) {
                    adjacentTiles.add(downTile);
                }
            }

            //left tile
            if(enenemyPos._x > 0) {
                Coordinates leftTile = map[enenemyPos._y][enenemyPos._x - 1];
                if(leftTile.isWalkable() && gameBoard.getBombAt(enenemyPos._x - 1, enenemyPos._y) == null) {
                    adjacentTiles.add(leftTile);
                }
            }

            for(int i = 0; i < adjacentTiles.size(); i++){
                Coordinates adjacent = adjacentTiles.get(i);
                if(closedList.contains(adjacent)) {
                    continue;
                }
                if(!openList.contains(adjacent)) {
                    int costToOriginPos = Math.abs(adjacent._x - originPos._x) + Math.abs(adjacent._y - originPos._y);
                    int costToDestination = Math.abs(adjacent._x - playerPos._x) + Math.abs(adjacent._y - playerPos._y);
                    adjacent.setCostToOriginPos(costToOriginPos);
                    adjacent.setCostToDestination(costToDestination);
                    adjacent.setParents(enenemyPos);
                    adjacent.calculatePoint();
                    openList.add(adjacent);
                }
                else {
                    int costToOriginPos = Math.abs(adjacent._x - originPos._x) + Math.abs(adjacent._y - originPos._y);
                    int costToDestination = Math.abs(adjacent._x - playerPos._x) + Math.abs(adjacent._y - playerPos._y);
                    int temp_point = costToOriginPos + costToDestination;
                    if(temp_point < adjacent.getPoint()){
                        adjacent.setCostToOriginPos(costToOriginPos);
                        adjacent.setCostToDestination(costToDestination);
                        adjacent.setParents(enenemyPos);
                        adjacent.calculatePoint();
                    }
                }
            }
        }
    }
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
