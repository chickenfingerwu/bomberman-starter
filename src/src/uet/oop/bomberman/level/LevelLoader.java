package uet.oop.bomberman.level;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.exceptions.LoadLevelException;

import java.io.File;

/**
 * Load và lưu trữ thông tin bản đồ các màn chơi
 */
public abstract class LevelLoader {

	protected int _width = 20, _height = 20; // default values just for testing
	protected int _level;
	protected Board _board;
	protected static String[] levelsName = {
			"BonusStage.txt",
			"Level1.txt",
			"Level2.txt",
			"Level3.txt",
			"Level4.txt",
			"Level5.txt",
			"Level6.txt",
			"Level7.txt",
			"Level8.txt",
	};

	public LevelLoader(Board board, int level) throws LoadLevelException {
		_board = board;
		getAllLevelFromFolder();
		loadLevel(level);
	}

	private void getAllLevelFromFolder(){
		File levelFolder = new File("res/levels");
		if(levelFolder.exists()){
			File[] childrenFile = levelFolder.listFiles();
			if(childrenFile != null){
				levelsName = new String[childrenFile.length];
				int i = 0;
				for(File f : childrenFile){
					levelsName[i] = f.getName();
					i++;
				}
			}
		}
	}

	public int getLevelsLength(){ return levelsName.length; }

	public abstract void loadLevel(int level) throws LoadLevelException;

	public abstract void createEntities();

	public int getWidth() {
		return _width;
	}

	public int getHeight() {
		return _height;
	}

	public int getLevel() {
		return _level;
	}

}
