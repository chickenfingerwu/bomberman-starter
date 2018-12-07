package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class FlamePassThroughItem extends Item {
    public FlamePassThroughItem(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý Bomber ăn Item
        if(e instanceof Bomber){
            if(!this.isRemoved()) {
                Game.playSoundEffect();
                Game.makeFlamePass();
            }
            this.remove();
        }
        return true;
    }
}
