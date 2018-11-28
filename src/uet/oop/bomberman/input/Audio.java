package uet.oop.bomberman.input;

import javafx.scene.media.AudioClip;

import javax.sound.sampled.*;
import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;

public class Audio extends JFrame {


    AudioInputStream title_screen = null;
    AudioInputStream stage_start = null;
    AudioInputStream stage_theme = null;
    AudioInputStream find_the_exit = null;
    AudioInputStream stage_complete = null;
    AudioInputStream bonus_stage = null;
    AudioInputStream invincibility = null;
    AudioInputStream life_lost = null;
    AudioInputStream game_over = null;
    AudioInputStream ending = null;
    AudioInputStream exploding = null;
    AudioInputStream powerup = null;
    AudioInputStream goofy_yell = null;

    Clip stageStart = null;
    Clip stageTheme = null;
    Clip powerUpEffect = null;
    Clip explodingEffect = null;
    Clip findTheExit = null;
    Clip titleScreen = null;
    Clip stageComplete = null;
    Clip bonusStage = null;
    Clip Invincibility = null;
    Clip lifeLost = null;
    Clip gameOver = null;
    Clip Ending = null;
    Clip goofyYell = null;

    public Audio(){
        try {
            stageTheme = AudioSystem.getClip();
            stageStart = AudioSystem.getClip();
            powerUpEffect = AudioSystem.getClip();
            explodingEffect = AudioSystem.getClip();
            findTheExit = AudioSystem.getClip();
            titleScreen = AudioSystem.getClip();
            stageComplete = AudioSystem.getClip();
            bonusStage = AudioSystem.getClip();
            Invincibility = AudioSystem.getClip();
            lifeLost = AudioSystem.getClip();
            gameOver = AudioSystem.getClip();
            Ending = AudioSystem.getClip();
            goofyYell = AudioSystem.getClip();

            goofy_yell = AudioSystem.getAudioInputStream(new File("res/audio/goofy-yell.wav"));
            title_screen = AudioSystem.getAudioInputStream(new File("res/audio/01_Title Screen.wav"));
            stage_start = AudioSystem.getAudioInputStream(new File("res/audio/02_Stage Start.wav"));
            stage_theme = AudioSystem.getAudioInputStream(new File("res/audio/level1.wav"));
            find_the_exit = AudioSystem.getAudioInputStream(new File("res/audio/04_Find The Exit.wav"));
            stage_complete = AudioSystem.getAudioInputStream(new File("res/audio/05_Stage Complete.wav"));
            bonus_stage = AudioSystem.getAudioInputStream(new File("res/audio/06_Bonus Stage.wav"));
            invincibility = AudioSystem.getAudioInputStream(new File("res/audio/07_Invincibility.wav"));
            life_lost = AudioSystem.getAudioInputStream(new File("res/audio/08_Life Lost.wav"));
            game_over = AudioSystem.getAudioInputStream(new File("res/audio/09_Game Over.wav"));
            ending = AudioSystem.getAudioInputStream(new File("res/audio/10_Ending.wav"));
            exploding = AudioSystem.getAudioInputStream(new File("res/audio/bomb.wav"));
            powerup = AudioSystem.getAudioInputStream(new File("res/audio/powerup.wav"));

            stageStart.open(stage_start);
            stageTheme.open(stage_theme);
            explodingEffect.open(exploding);
            powerUpEffect.open(powerup);
            findTheExit.open(find_the_exit);
            titleScreen.open(title_screen);
            stageComplete.open(title_screen);
            bonusStage.open(bonus_stage);
            Invincibility.open(invincibility);
            lifeLost.open(life_lost);
            gameOver.open(game_over);
            Ending.open(ending);
            goofyYell.open(goofy_yell);

            //Increase powerup sound effect by 20 decibels
            float dB = (float) (Math.log(.5D) / Math.log(10.0) * 20.0);
            FloatControl gainControlPowerUp =
                    (FloatControl) stageTheme.getControl(FloatControl.Type.MASTER_GAIN);
            gainControlPowerUp.setValue(dB);
            // Reduce stage theme by 10 decibels
            FloatControl gainControl =
                    (FloatControl) stageTheme.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-10.0f);

        }
        catch (LineUnavailableException e){
            e.getMessage();
        }
        catch (UnsupportedAudioFileException e){
            System.out.println("Unsupported format");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }


    public void playTitleScreen(){
        stageStart.stop();
        stageStart.setFramePosition(0);
        stageStart.start();
    }
    public void playStageStart(){
        stageStart.stop();
        stageStart.setFramePosition(0);
        stageStart.start();

    }

    public void loadStageTheme(int level){
        try {
            stage_theme = AudioSystem.getAudioInputStream(new File("res/audio/level" + level + ".wav"));
            stageTheme.close();
            stageTheme.open(stage_theme);
            System.out.println("loaded stage " + level + "'s theme");
        }
        catch (LineUnavailableException e){
            e.printStackTrace();
        }
        catch (UnsupportedAudioFileException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void playStageTheme(){
        stageTheme.loop(Clip.LOOP_CONTINUOUSLY);
        /*try{
            if(stageTheme!=null){
                new Thread(){
                    public void run(){
                        synchronized (stageTheme){
                            stageTheme.stop();
                            stageTheme.setFramePosition(0);
                            stageTheme.start();
                        }
                    }
                }.start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/
    }
    public void playFindTheExit(){
        try {
            Clip audio = AudioSystem.getClip();
            audio.open(find_the_exit);
            while (true){
                audio.loop(10000);
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (LineUnavailableException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void playStageComplete(){
        try {
            Clip audio = AudioSystem.getClip();
            audio.open(stage_complete);
            while (true){
                audio.loop(10000);
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (LineUnavailableException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void playBonusStage(){
        try {
            Clip audio = AudioSystem.getClip();
            audio.open(bonus_stage);
            while (true){
                audio.loop(10000);
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (LineUnavailableException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void playInvincibility(){
        try {
            Clip audio = AudioSystem.getClip();
            audio.open(invincibility);
            while (true){
                audio.loop(10000);
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (LineUnavailableException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void playLifeLost(){
        lifeLost.stop();
        lifeLost.setFramePosition(0);
        lifeLost.start();
    }
    public void playGameOver(){
        gameOver.stop();
        gameOver.setFramePosition(0);
        gameOver.start();
    }
    public void playEnding(){
        try {
            Clip audio = AudioSystem.getClip();
            audio.open(ending);
            while (true){
                audio.loop(10000);
            }
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
        catch (LineUnavailableException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
    public void playExploding(){
        explodingEffect.stop();
        explodingEffect.setFramePosition(0);
        explodingEffect.start();
    }
    public void playPowerUp(){
        powerUpEffect.stop();
        powerUpEffect.setFramePosition(0);
        powerUpEffect.start();
    }

    public void playGoofyYell(){
        try {
            AudioInputStream gY = AudioSystem.getAudioInputStream(new File("res/audio/goofy-yell.wav"));
            Clip gy = AudioSystem.getClip();
            gy.open(gY);
            gy.start();
        }
        catch (UnsupportedAudioFileException e){
            e.printStackTrace();
        }
        catch (LineUnavailableException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public Clip getStageStart(){
        return stageStart;
    }

    public Clip getStageTheme(){
        return stageTheme;
    }

    public Clip getBonusStage() {
        return bonusStage;
    }

    public Clip getExplodingEffect() {
        return explodingEffect;
    }

    public Clip getFindTheExit() {
        return findTheExit;
    }

    public Clip getGameOver() {
        return gameOver;
    }

    public Clip getLifeLost() {
        return lifeLost;
    }

    public Clip getPowerUpEffect() {
        return powerUpEffect;
    }

    public Clip getStageComplete() {
        return stageComplete;
    }

    public Clip getTitleScreen() {
        return titleScreen;
    }

    protected void update(){

    }
}
