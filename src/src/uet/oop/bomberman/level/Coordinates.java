package uet.oop.bomberman.level;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;

public class Coordinates {
	public int _x;
	public int _y;
	private int costToOriginPos;
	private int costToDestination;
	private int point;
	Coordinates parents;
	boolean walkable;

	public Coordinates(int x, int y){
		_x = x;
		_y = y;
	}

	public Coordinates(int x, int y, boolean walkable){
		_x = x;
		_y = y;
		this.parents = parents;
		this.walkable = walkable;
		costToOriginPos = Integer.MAX_VALUE;
		costToDestination = Integer.MAX_VALUE;
		parents = null;
		calculatePoint();
	}

	public static int pixelToTile(double i) {
		return (int)(i / Game.TILES_SIZE);
	}
	
	public static int tileToPixel(int i) {
		return i * Game.TILES_SIZE;
	}
	
	public static int tileToPixel(double i) {
		return (int)(i * Game.TILES_SIZE);
	}

	public boolean isWalkable() {
		return walkable;
	}

	public boolean isTheSamePos(Coordinates other){
		if(other._x == _x && other._y == _y){
			return true;
		}
		return false;
	}

	public int get_x() {
		return _x;
	}

	public int get_y() {
		return _y;
	}

	public int getCostToOriginPos(){
		return costToOriginPos;
	}

	public int getCostToDestination() {
		return costToDestination;
	}

	public void setCostToOriginPos(int cost){
		costToOriginPos = cost;
	}

	public void setCostToDestination(int costToDestination) {
		this.costToDestination = costToDestination;
	}

	public void calculatePoint(){
		point = costToDestination + costToOriginPos;
	}

	public void set_x(int _x) {
		this._x = _x;
	}

	public void set_y(int _y) {
		this._y = _y;
	}

	public Coordinates getParents(){
		return parents;
	}

	public void setParents(Coordinates parents){
		this.parents = parents;
	}

	public boolean theSameAs(Coordinates other){
		return _x == other._x && _y == other._y;
	}

	public int getPoint(){
		return point;
	}

	public void setWalkable(boolean walkable) {
		this.walkable = walkable;
	}
}
