package uet.oop.bomberman.entities.tile.item;

import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.graphics.Sprite;

public class LivesUp extends Item{
    public LivesUp(int x, int y, Sprite sprite) {
        super(x, y, sprite);
    }

    @Override
    public boolean collide(Entity e) {
        // TODO: xử lý Bomber ăn Item
        if(e instanceof Bomber) {
            if (!this.isRemoved()) {
                Game.playSoundEffect();
                Game.addLives(1);
            }
            remove();
            return true;
        }
        return false;
    }
}
