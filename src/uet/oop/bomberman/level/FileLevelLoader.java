package uet.oop.bomberman.level;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.bomb.Bomb;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.*;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.destroyable.DestroyableTile;
import uet.oop.bomberman.entities.tile.item.BombItem;
import uet.oop.bomberman.entities.tile.item.FlameItem;
import uet.oop.bomberman.entities.tile.item.Item;
import uet.oop.bomberman.entities.tile.item.SpeedItem;
import uet.oop.bomberman.exceptions.LoadLevelException;
import uet.oop.bomberman.graphics.Screen;
import uet.oop.bomberman.graphics.Sprite;

import javax.swing.text.html.parser.Entity;
import java.io.*;
import java.net.URL;
import java.util.Scanner;
import java.util.StringTokenizer;

public class FileLevelLoader extends LevelLoader {

	/**
	 * Ma trận chứa thông tin bản đồ, mỗi phần tử lưu giá trị kí tự đọc được
	 * từ ma trận bản đồ trong tệp cấu hình
	 */
	private static char[][] _map;
	
	public FileLevelLoader(Board board, int level) throws LoadLevelException {
		super(board, level);
	}
	
	@Override
	public void loadLevel(int level) {
		// TODO: đọc dữ liệu từ tệp cấu hình /levels/Level{level}.txt
		// TODO: cập nhật các giá trị đọc được vào _width, _height, _level, _map
		String path = "res/levels/Level" + level +".txt";
		File filePath = new File(path);
		try {
			URL absPath = filePath.toURI().toURL();

			BufferedReader in = new BufferedReader(
					new InputStreamReader(absPath.openStream()));
			String data = in.readLine();
			String[] Int = data.split(" ");
			try {
				_level = Integer.parseInt(Int[0]);
				_height = Integer.parseInt(Int[1]);
				_width = Integer.parseInt(Int[2]);
			}
			catch (NumberFormatException e){
				System.out.println("error in converting");
				_level = level;
				_height = Integer.parseInt(Int[1]);
				_width = Integer.parseInt(Int[2]);

			}
			_map = new char[_height][_width];
			for(int i = 0; i < _height; i++){
				data = in.readLine();
				for(int j = 0; j < _width; j++){
					_map[i][j] = data.charAt(j);
				}
			}

			in.close();
		} catch (IOException e) {
			System.out.println("Error loading level ");
		}
	}

	@Override
	public void createEntities() {
		// TODO: tạo các Entity của màn chơi
		// TODO: sau khi tạo xong, gọi _board.addEntity() để thêm Entity vào game

		// TODO: phần code mẫu ở dưới để hướng dẫn cách thêm các loại Entity vào game
		// TODO: hãy xóa nó khi hoàn thành chức năng load màn chơi từ tệp cấu hình
		_board._characters.clear();
		for (int y = 0; y < _height; y++) {
			for (int x = 0; x < _width; x++) {
				Sprite sprite = Sprite.grass;
				switch (_map[y][x]){
					case '#':
						//sprite = Sprite.wall;
						_board.addEntity(x + y * _width, new Wall(x, y, Sprite.wall));
						continue;
					case '*':
						//sprite = Sprite.brick;
						_board.addEntity(x + y * _width,
								new LayeredEntity(x, y,
										new Grass(x, y, Sprite.grass),
										new Brick(x, y, Sprite.brick))
						);
						continue;
					case 'x':
						//sprite = Sprite.portal;
						_board.addEntity(x + y * _width,
								new LayeredEntity(x, y,
										new Grass(x, y, Sprite.grass),
										new Portal(x, y, Sprite.portal),
										new Brick(x, y, Sprite.brick))
						);
						continue;

					case 'p':
						_board.addCharacter( new Bomber(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
						//sprite = Sprite.player_right;
						Screen.setOffset(0, 0);
						break;
					case '1':
						//sprite = Sprite.balloom_left1;
						_board.addCharacter( new Balloon(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
						break;
					case '2':
						//sprite = Sprite.oneal_left1;
						_board.addCharacter(new Oneal(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
						break;
					case 'k':
						_board.addCharacter(new Kondoria(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
						break;
					case 'm':
						_board.addCharacter(new Minvo(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
						break;
					case 'd':
						_board.addCharacter(new Doll(Coordinates.tileToPixel(x), Coordinates.tileToPixel(y) + Game.TILES_SIZE, _board));
						break;


					case 'b':
						_board.addEntity(x + y * _width,
								new LayeredEntity(x, y,
										new Grass(x, y, Sprite.grass),
										new BombItem(x, y, Sprite.powerup_bombs),
										new Brick(x, y, Sprite.brick))
						);
						continue;
					case 'f':
						_board.addEntity(x + y * _width,
								new LayeredEntity(x, y,
										new Grass(x, y, Sprite.grass),
										new FlameItem(x, y, Sprite.powerup_flames),
										new Brick(x, y, Sprite.brick))
						);
						continue;
					case 's':
						_board.addEntity(x + y * _width,
								new LayeredEntity(x, y,
										new Grass(x, y, Sprite.grass),
										new SpeedItem(x, y, Sprite.powerup_speed),
										new Brick(x, y, Sprite.brick))
						);
						continue;
				}
				int pos = x + y * _width;
				_board.addEntity(pos, new Grass(x, y, sprite));
			}
		}
	}

	public static char[][] get_map() {
		return _map;
	}
}