package Polandball_pliki.Others;

import Polandball_pliki.Frame.GameOver;
import Polandball_pliki.Panel.PanelInfoOne;


import static Polandball_pliki.Others.GetConstans.LevelTime;
import static Polandball_pliki.Panel.PanelBoard.PauseActive;

/**
 * Klasa odpowiedzialna za czas gry
 */
public class GameTime implements Runnable{

    /**
     * Metoda zwracaja obiekt klasy Gametime
     * @return obiekt klasy GameTime
     */
    public GameTime getGameTime(){
        return this;
    }

    /**
     * Metoda watku odliczajaca czas do konca poziomu
     */
    @Override
    public void run() {
        try {
            int i = LevelTime;
            while (i+1 >= 0 ) {
                synchronized (this) {
                    Thread.sleep(1000);//odliczamy co 1 sekunde
                    if(PauseActive==false){
                        //super dziwna formula, jak to dziala? trzeba spytac wrozki
                        if ((LevelTime - (Math.floor(LevelTime / 60)) * 60) >= 10) {
                            PanelInfoOne.TimeLabel2.setText(Integer.toString((int) Math.floor(LevelTime / 60)) + ":"
                                    + Integer.toString((int) (LevelTime - (Math.floor(LevelTime / 60)) * 60)));
                            i--;//petelka sie konczy na 0
                        } else if ((LevelTime - (Math.floor(LevelTime / 60)) * 60) < 10) {
                            PanelInfoOne.TimeLabel2.setText(Integer.toString((int) Math.floor(LevelTime / 60)) + ":0"
                                    + Integer.toString((int) (LevelTime - (Math.floor(LevelTime / 60)) * 60)));
                            i--;//petelka sie konczy na 0
                        }
                        if(LevelTime==0){//sprawdzenie czy skonczyl sie czas
                            GameOver gameover = new GameOver();
                            gameover.setVisible(true);
                            PauseActive=true;
                        }
                        LevelTime--;//zmniejszenie czasu o 1 sekunde
                    }
                }
            }

        } catch(Exception e){System.out.println(e+"Blad klasy GameTime");
        }
    }
}
