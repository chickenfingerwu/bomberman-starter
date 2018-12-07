package uet.oop.bomberman.level;

import uet.oop.bomberman.Board;
import uet.oop.bomberman.Game;
import uet.oop.bomberman.entities.LayeredEntity;
import uet.oop.bomberman.entities.character.Bomber;
import uet.oop.bomberman.entities.character.enemy.*;
import uet.oop.bomberman.entities.tile.Grass;
import uet.oop.bomberman.entities.tile.Portal;
import uet.oop.bomberman.entities.tile.Wall;
import uet.oop.bomberman.entities.tile.destroyable.Brick;
import uet.oop.bomberman.entities.tile.item.*;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;

public class SaveAndLoad {
    Board _board;
    FileLevelLoader _levelLoader;
    ArrayList<SaveStates> _saves;
    public SaveAndLoad(Board board, FileLevelLoader levelLoader){
        _board = board;
        _levelLoader = levelLoader;
        _saves = new ArrayList<>();
    }

    public void saveGame(String name){
        try {
            File saveFile = new File("res/saved levels/" + name + ".txt");
            FileWriter fileWriter;
            if(saveFile.exists()) {
                fileWriter = new FileWriter("res/saved levels/" + name + "_copy.txt");
                saveFile = new File("res/saved levels/" + name + "_copy.txt");
            }
            else {
                fileWriter = new FileWriter("res/saved levels/" + name + ".txt");
            }
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(_levelLoader.getLevel() + " " + _levelLoader.getHeight() + " " + _levelLoader.getWidth() + String.format("%n"));
            char[][] _map = new char[_levelLoader.getHeight()][_levelLoader.getWidth()];
            for(int i = 0; i < _map.length; i++){
                for(int j = 0; j < _map[0].length; j++){
                    if((_levelLoader).get_map()[i][j] == '#' || ( _levelLoader).get_map()[i][j] == '*' ||
                            ( _levelLoader).get_map()[i][j] == ' '){
                        _map[i][j] = ( _levelLoader).get_map()[i][j];
                    }
                }
            }

            //0 stands for bombs
            for(int i = 0; i < _board.getBombs().size(); i++){
                _map[(int) _board.getBombs().get(i).getY()][(int) _board.getBombs().get(i).getX()] = '0';
            }
            for(int i = 0; i < _board._entities.length; i++) {
                if (_board._entities[i] instanceof Brick) {
                    _map[_board._entities[i].getYTile()][_board._entities[i].getXTile()] = '*';
                }
                if (_board._entities[i] instanceof Grass) {
                    _map[_board._entities[i].getYTile()][_board._entities[i].getXTile()] = ' ';
                }
                if (_board._entities[i] instanceof Wall) {
                    _map[_board._entities[i].getYTile()][_board._entities[i].getXTile()] = '#';
                }
                if (_board._entities[i] instanceof LayeredEntity) {
                    int x = (int) _board._entities[i].getX();
                    int y = (int) _board._entities[i].getY();
                    if (((LayeredEntity) _board._entities[i]).getHiddenItem() instanceof FlameItem) {
                        _map[y][x] = 'f';
                    } else if (((LayeredEntity) _board._entities[i]).getHiddenItem() instanceof SpeedItem) {
                        _map[y][x] = 's';
                    } else if (((LayeredEntity) _board._entities[i]).getHiddenItem() instanceof BombItem) {
                        _map[y][x] = 'b';
                    } else if (((LayeredEntity) _board._entities[i]).getHiddenItem() instanceof FlamePassThroughItem) {
                        _map[y][x] = 'y';
                    } else if (((LayeredEntity) _board._entities[i]).getHiddenItem() instanceof LivesUp) {
                        _map[y][x] = 'l';
                    } else if (((LayeredEntity) _board._entities[i]).getHiddenItem() instanceof Portal) {
                        _map[y][x] = 'x';
                    }
                }
            }
            for(int i = 0; i < _board._characters.size(); i++){
                if(_board._characters.get(i) instanceof Balloon) {
                    _map[_board._characters.get(i).getYTile()][_board._characters.get(i).getXTile()] = '1';
                }
                if(_board._characters.get(i) instanceof Oneal) {
                    _map[_board._characters.get(i).getYTile()][_board._characters.get(i).getXTile()] = '2';
                }
                if(_board._characters.get(i) instanceof Doll) {
                    _map[_board._characters.get(i).getYTile()][_board._characters.get(i).getXTile()] = 'd';
                }
                if(_board._characters.get(i) instanceof Kondoria) {
                    _map[_board._characters.get(i).getYTile()][_board._characters.get(i).getXTile()] = 'k';
                }
                if(_board._characters.get(i) instanceof Minvo) {
                    _map[_board._characters.get(i).getYTile()][_board._characters.get(i).getXTile()] = '1';
                }
                if(_board._characters.get(i) instanceof Bomber){
                    _map[_board._characters.get(i).getYTile()][_board._characters.get(i).getXTile()] = 'p';
                }
            }

            for(int i = 0; i < _levelLoader.getHeight(); i++){
                for(int j = 0; j < _levelLoader.getWidth(); j++){
                    writer.write(_map[i][j]);
                }
                writer.write(String.format("%n"));
            }


            writer.write("BombRadius: " + Game.getBombRadius() + String.format("%n"));
            writer.write("BomberLives: " + Game.getBomberLives() + String.format("%n"));
            writer.write("BomberSpeed: " + Game.getBomberSpeed() + String.format("%n"));
            writer.write("BombRate: " + Game.getBombRate() + String.format("%n"));
            writer.write("TimeForFlamePass: " + _board.getGame().getTimeforflamepass() + String.format("%n"));
            writer.close();
            _saves.add(new SaveStates(_levelLoader.getHeight(), _levelLoader.getWidth(), _levelLoader.getLevel(), saveFile));
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadSave(String name){
        File filePath = null;
        int level = 1;
        int height = 13;
        int width = 31;
        for(int i = 0; i < _saves.size(); i++){
            if(_saves.get(i).get_save().getName().equals(name + ".txt")){
                filePath = _saves.get(i).get_save();
                level = _saves.get(i).get_level();
                height = _saves.get(i).get_height();
                width = _saves.get(i).get_width();
            }
        }
        if(filePath == null){
            filePath = new File("res/saved levels/" + name + ".txt");
        }
        try {
            URL absPath = filePath.toURI().toURL();

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(absPath.openStream()));
            String data = in.readLine();
            String[] Int = data.split(" ");
            try {
                _levelLoader._level = Integer.parseInt(Int[0]);
                _levelLoader._height = Integer.parseInt(Int[1]);
                _levelLoader._width = Integer.parseInt(Int[2]);
            }
            catch (NumberFormatException e){
                System.out.println("error in converting");
                _levelLoader._level = level;
                _levelLoader._height = Integer.parseInt(Int[1]);
                _levelLoader._width = Integer.parseInt(Int[2]);

            }
            char[][] MAP = new char[_levelLoader.getHeight()][_levelLoader.getWidth()];
            for(int i = 0; i < MAP.length; i++){
                data = in.readLine();
                for(int j = 0; j < MAP[0].length; j++){
                    MAP[i][j] = data.charAt(j);
                }
            }
            ArrayList<String> gameVariables = new ArrayList<>();
            while ((data = in.readLine()) != null){
                String[] gVariables = data.split(" ");
                gameVariables.add(gVariables[gVariables.length - 1]);
            }
            Game.addBombRadius(-(Game.getBombRadius() - Integer.parseInt(gameVariables.get(0))));
            Game.addLives(-(Game.getBomberLives() - Integer.parseInt(gameVariables.get(1))));
            Game.addBomberSpeed(-(Game.getBomberSpeed() - Double.parseDouble(gameVariables.get(2))));
            Game.addBombRate(-(Game.getBombRate() - Integer.parseInt(gameVariables.get(3))));
            Game.addFlamePassTime(-(_board.getGame().getTimeforflamepass() - Integer.parseInt(gameVariables.get(4))));
            in.close();
            _levelLoader.copyMap(MAP);
            //_levelLoader.createEntities();
            /*for(int i = 0; i < MAP.length; i++){
                for(int j = 0; j < MAP[i].length; j++){
                    System.out.print(MAP[i][j]);
                }
                System.out.println();
            }*/
        } catch (IOException e) {
            System.out.println("Error loading level ");
        }
    }
}
