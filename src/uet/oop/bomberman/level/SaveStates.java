package uet.oop.bomberman.level;

import java.io.File;

public class SaveStates {
    int _level;
    int _height;
    int _width;
    File _save;
    public SaveStates(int h, int w, int l, File s){
        _height = h;
        _width = w;
        _level = l;
        _save = s;
    }

    public File get_save() {
        return _save;
    }

    public int get_height() {
        return _height;
    }

    public int get_width() {
        return _width;
    }

    public int get_level() {
        return _level;
    }
}
