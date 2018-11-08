package Polandball_pliki.Others;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa odpowiedzialna za informacje wyswietlane podczas gry
 */
public class ButtonLabel extends JButton {

    /**
     * Kontruktor klasy ButtonLabel, okreslajacy parametry komponentow zawierajacych informacje o stanie rozgrywki
     */
    public ButtonLabel(String text) {
        this.setText(text);
        this.setFont(new Font("Serif", Font.PLAIN, 30));
        this.setFocusPainted(false);
        this.setMargin(new Insets(0, 0, 0, 0));
        this.setContentAreaFilled(false);
        this.setBorderPainted(false);
        this.setOpaque(false);
    }
}
