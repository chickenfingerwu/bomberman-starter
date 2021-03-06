package uet.oop.bomberman.entities;

import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.destroyable.DestroyableTile;
import uet.oop.bomberman.graphics.Screen;

import java.util.LinkedList;

/**
 * Chứa và quản lý nhiều Entity tại cùng một vị trí
 * Ví dụ: tại vị trí dấu Item, có 3 Entity [Grass, Item, Brick]
 */
public class LayeredEntity extends Entity {
	
	protected LinkedList<Entity> _entities = new LinkedList<>();
	
	public LayeredEntity(int x, int y, Entity ... entities) {
		_x = x;
		_y = y;
		
		for (int i = 0; i < entities.length; i++) {
			_entities.add(entities[i]); 
			
			if(i > 1) {
				if(entities[i] instanceof DestroyableTile)
					((DestroyableTile)entities[i]).addBelowSprite(entities[i-1].getSprite());
			}
		}
	}
	
	@Override
	public void update() {
		clearRemoved();
		getTopEntity().update();
	}
	
	@Override
	public void render(Screen screen) {
		getTopEntity().render(screen);
	}
	
	public Entity getTopEntity() {
		
		return _entities.getLast();
	}
	
	private void clearRemoved() {
		Entity top  = getTopEntity();
		
		if(top.isRemoved())  {
			_entities.removeLast();
		}
	}

	public Entity getHiddenItem(){
		Entity e = getTopEntity();
		for(int i = 0; i < _entities.size(); i++){
			if(!(_entities.get(i) instanceof Grass) && !(_entities.get(i) instanceof Brick) && _entities.get(i) != null){
				e = _entities.get(i);
			}
		}
		return e;
	}
	
	public void addBeforeTop(Entity e) {
		_entities.add(_entities.size() - 1, e);
	}
	
	@Override
	public boolean collide(Entity e) {
		// TODO: lấy entity trên cùng ra để xử lý va chạm
		if(e instanceof Bomber || e instanceof Enemy){
			return getTopEntity().collide(e);
		}
		if(e instanceof Flame){
			return getTopEntity().collide(e);
		}
		return false;
	}

}
