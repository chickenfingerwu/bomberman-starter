package uet.oop.bomberman.entities.character;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.AnimatedEntitiy;
import uet.oop.bomberman.entities.Entity;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.bomb.Flame;
import uet.oop.bomberman.entities.bomb.FlameSegment;
import uet.oop.bomberman.entities.character.enemy.Enemy;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Tile;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;
import uet.oop.bomberman.input.Keyboard;
import uet.oop.bomberman.level.Coordinates;

import java.util.Iterator;
import java.util.List;

public class Bomber extends Character {
    private boolean _livesLost = false;
    private int timeTilNextKill = 100;
    private List<Bomb> _bombs;
    protected Keyboard _input;

    /**
     * nếu giá trị này < 0 thì cho phép đặt đối tượng Bomb tiếp theo,
     * cứ mỗi lần đặt 1 Bomb mới, giá trị này sẽ được reset về 0 và giảm dần trong mỗi lần update()
     */
    protected int _timeBetweenPutBombs = 0;

    public Bomber(int x, int y, Board board) {
        super(x, y, board);
        _bombs = _board.getBombs();
        _input = _board.getInput();
        _sprite = Sprite.player_right;
    }

    @Override
    public void update() {
        clearBombs();
        if(timeTilNextKill == 0){
            _livesLost = false;
        }
        if(timeTilNextKill > 0) {
            timeTilNextKill--;
        }
        if (!_alive) {
            afterKill();
            return;
        }

        if (_timeBetweenPutBombs < -7500) _timeBetweenPutBombs = 0;
        else _timeBetweenPutBombs--;

        animate();
        if(!_livesLost) {
            calculateMove();
        }

        detectPlaceBomb();

    }

    @Override
    public void render(Screen screen) {
        calculateXOffset();

        if(_livesLost){
            _sprite = Sprite.movingSprite(Sprite.player_dead1, Sprite.voidSprite, Sprite.player_dead1, _animate, 10);
            _moving = false;
        }
        else if (_alive)
            chooseSprite();
        else {
            _sprite = Sprite.movingSprite(Sprite.player_dead1, Sprite.player_dead2, Sprite.player_dead3, _animate, 60);
        }

        screen.renderEntity((int) _x, (int) _y - _sprite.SIZE, this);
    }

    public void calculateXOffset() {
        int xScroll = Screen.calculateXOffset(_board, this);
        Screen.setOffset(xScroll, 0);
    }

    /**
     * Kiểm tra xem có đặt được bom hay không? nếu có thì đặt bom tại vị trí hiện tại của Bomber
     */
    private void detectPlaceBomb() {
        if(_input.space){
            if(_timeBetweenPutBombs < 0 && Game.getBombRate() > 0) {
                placeBomb(Coordinates.pixelToTile(_x + _sprite.get_realWidth()/2), Coordinates.pixelToTile(_y + _sprite.get_realHeight()/2 - Game.TILES_SIZE));
                Game.addBombRate(-1);
                _timeBetweenPutBombs = 20;
            }
        }
    }

    protected void placeBomb(int x, int y) {
        _board.addBomb(new Bomb(x, y, _board));
    }

    private void clearBombs() {
        Iterator<Bomb> bs = _bombs.iterator();

        Bomb b;
        while (bs.hasNext()) {
            b = bs.next();
            if (b.isRemoved()) {
                bs.remove();
                Game.addBombRate(1);
            }
        }

    }

    public void setAlive(){
        _alive = true;
    }

    @Override
    public void kill() {
        if (!_alive) return;

        if (Game.getBomberLives() == 0) {
            _livesLost = false;
            _alive = false;
        }
        else if (Game.getBomberLives() > 0 && timeTilNextKill == 0) {
            Game.getAudio().playLifeLost();
            Game.addLives(-1);
            _livesLost = true;
            timeTilNextKill = 100;
        }
    }

    @Override
    protected void afterKill() {
        if (_timeAfter > 0) {
            --_timeAfter;
            animate();
        }
        else {
            _board.endGame();
        }
    }

    @Override
    public Coordinates getFarRight(){
        return new Coordinates((int) _x + _sprite.get_realWidth(), (int) _y - Game.TILES_SIZE);
    }

    @Override
    public Coordinates getBottomLeft(){
        return new Coordinates( (int) _x, (int) _y);
    }

    @Override
    protected void calculateMove() {
        int xa = 0;
        int ya = 0;
        if(_input.down){
            ya++;
            _moving = true;
        }
        else if(_input.left){
            xa--;
            _moving = true;
        }
        else if(_input.right){
            xa++;
            _moving = true;
        }
        else if(_input.up){
            ya--;
            _moving = true;
        }
        else{
            _moving = false;
        }
        if(_moving){
            move(xa * Game.getBomberSpeed(), ya * Game.getBomberSpeed());
        }
    }

    @Override
    public boolean canMove(double xa, double ya) {
        Entity[] e = new Entity[4];
        e[0] = _board.getEntity(Coordinates.pixelToTile(_x + xa), Coordinates.pixelToTile(_y + ya) - 1, this); // e[0]: upper left corner
        e[1] = _board.getEntity(Coordinates.pixelToTile(_x + xa + _sprite.get_realWidth()), Coordinates.pixelToTile(_y + ya) - 1, this); // e[1]: upper right corner
        e[2] = _board.getEntity(Coordinates.pixelToTile(_x + xa), Coordinates.pixelToTile(_y + ya - 1), this); // e[2]: lower left corner
        e[3] = _board.getEntity(Coordinates.pixelToTile(_x + xa + _sprite.get_realWidth()), Coordinates.pixelToTile(_y + ya - 1), this); // e[3]: lower right corner

        /*if(!e[0].collide(this) || !e[1].collide(this) || !e[2].collide(this) || !e[3].collide(this)){
            return false;
        }*/
        if (ya < 0) { // Up
            if (!e[0].collide(this) && !e[1].collide(this)) // Both corner is blocked
                return false;
            else if (!e[0].collide(this) && e[1].collide(this)) { // Only upper left is blocked
                if (Coordinates.tileToPixel(e[0].getX()) + Game.TILES_SIZE - _x <= Sprite.player_up.get_realWidth() / 2 + 2)
                    move(1, 0);
                else
                    return false;
            }
            else if (e[0].collide(this) && !e[1].collide(this)) { // Only upper right is blocked
                if (_x + Sprite.player_up.get_realWidth() - Coordinates.tileToPixel(e[1].getX()) <= Sprite.player_up.get_realWidth() / 2 + 2)
                    move(-1, 0);
                else
                    return false;
            }
        }
        else if (ya > 0) { // Down
            if (!e[2].collide(this) && !e[3].collide(this)) // Both corner is blocked
                return false;
            else if (!e[2].collide(this) && e[3].collide(this)) { // Only lower left is blocked
                if (Coordinates.tileToPixel(e[2].getX()) + Game.TILES_SIZE - _x <= Sprite.player_down.get_realWidth() / 2 + 2)
                    move(1, 0);
                else
                    return false;
            }
            else if (e[2].collide(this) && !e[3].collide(this)) { // Only lower right is blocked
                if (_x + Sprite.player_down.get_realHeight() - Coordinates.tileToPixel(e[3].getX()) <= Sprite.player_down.get_realWidth() / 2 + 2)
                    move(-1, 0);
                else
                    return false;
            }
        }
        else if (xa > 0) { // Right
            if (!e[1].collide(this) && !e[3].collide(this))
                return false;
            else if (!e[1].collide(this) && e[3].collide(this)) { // Only upper right is blocked
                if (Coordinates.tileToPixel(e[1].getY() + 1) + Game.TILES_SIZE - _y <= Sprite.player_right.get_realHeight() / 2 + 2)
                    move(0, 1);
                else
                    return false;
            }
            else if (e[1].collide(this) && !e[3].collide(this)) { // Only lower right is blocked
                if (_y + Sprite.player_right.get_realHeight() - Coordinates.tileToPixel(e[3].getY() + 1) <= Sprite.player_right.get_realHeight() / 2 + 2)
                    move(0, -1);
                else
                    return false;
            }
        }
        else { // Left
            if (!e[0].collide(this) && !e[2].collide(this))
                return false;
            else if (!e[0].collide(this) && e[2].collide(this)) { // Only upper left is blocked
                if (Coordinates.tileToPixel(e[0].getY() + 1) + Game.TILES_SIZE - _y <= Sprite.player_left.get_realHeight() / 2 + 2)
                    move(0, 1);
                else
                    return false;
            }
            else if (e[0].collide(this) && !e[2].collide(this)) { // Only lower left is blocked
                if (_y + Sprite.player_right.get_realHeight() - Coordinates.tileToPixel(e[2].getY() + 1) <= Sprite.player_left.get_realHeight() / 2 + 2)
                    move(0, -1);
                else
                    return false;
            }
        }

        return true;
    }

    @Override
    public void move(double xa, double ya) {
        if(ya > 0){
            _direction = 2;
        }
        else if(xa < 0){
            _direction = 3;
        }
        else if(xa > 0){
            _direction = 1;
        }
        else if(ya < 0){
            _direction = 0;
        }
        if (canMove(0, ya)) {
            _y += ya;
        }
        if (canMove(xa, 0)) { // Allow sliding like NES version
            _x += xa;
        }
    }

    @Override
    public boolean collide(Entity e) {

        if(e instanceof Flame || e instanceof FlameSegment) {
            kill();
            return true;
        }
        else if(e instanceof Enemy) {
            kill();
            return true;
        }
        return false;
    }

    public Board getBoard(){
        return _board;
    }

    private void chooseSprite() {
        if(Game.isFlamePassThrough()){
            switch (_direction) {
                case 0:
                    _sprite = Sprite.player_up_flamepass;
                    if (_moving) {
                        _sprite = Sprite.movingSprite(Sprite.player_up_flamepass_1, Sprite.player_up_flamepass_2, _animate, 20);
                    }
                    break;
                case 1:
                    _sprite = Sprite.player_right_flamepass;
                    if (_moving) {
                        _sprite = Sprite.movingSprite(Sprite.player_right_flamepass_1, Sprite.player_right_flamepass_2, _animate, 20);
                    }
                    break;
                case 2:
                    _sprite = Sprite.player_down_flamepass;
                    if (_moving) {
                        _sprite = Sprite.movingSprite(Sprite.player_down_flamepass_1, Sprite.player_down_flamepass_2, _animate, 20);
                    }
                    break;
                case 3:
                    _sprite = Sprite.player_left_flamepass;
                    if (_moving) {
                        _sprite = Sprite.movingSprite(Sprite.player_left_flamepass_1, Sprite.player_left_flamepass_2, _animate, 20);
                    }
                    break;
                default:
                    _sprite = Sprite.player_right_flamepass;
                    if (_moving) {
                        _sprite = Sprite.movingSprite(Sprite.player_right_flamepass_1, Sprite.player_right_flamepass_2, _animate, 20);
                    }
                    break;
            }
            return;
        }
        switch (_direction) {
            case 0:
                _sprite = Sprite.player_up;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_up_1, Sprite.player_up_2, _animate, 20);
                }
                break;
            case 1:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
            case 2:
                _sprite = Sprite.player_down;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_down_1, Sprite.player_down_2, _animate, 20);
                }
                break;
            case 3:
                _sprite = Sprite.player_left;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_left_1, Sprite.player_left_2, _animate, 20);
                }
                break;
            default:
                _sprite = Sprite.player_right;
                if (_moving) {
                    _sprite = Sprite.movingSprite(Sprite.player_right_1, Sprite.player_right_2, _animate, 20);
                }
                break;
        }
    }

    public boolean getAlive(){
        return _alive;
    }
    public Sprite getSprite(){
        return _sprite;
    }

    public boolean getLivesLost(){
        return _livesLost;
    }

    public int getLives(){
        return Game.getBomberLives();
    }

    public boolean isMoving(){
        return _moving;
    }
}
