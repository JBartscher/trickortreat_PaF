package main.java.Menu;

import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class MenuTitle extends Pane {

    private Text text;
    private int size = 48;

    public MenuTitle(String name, int size) {
        this.size = size;
        String spread = "";
        for (char c : name.toCharArray()) {
            spread += c + " ";
        }


        text = new Text(spread);
        text.setFont(Font.loadFont(MainMenu.class.getResource("Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), size));
        text.setFill(Color.WHITE);
        text.setEffect(new DropShadow(30, Color.BLACK));

        getChildren().addAll(text);
    }

    public double getTitleWidth() {
        return text.getLayoutBounds().getWidth();
    }

    public double getTitleHeight() {
        return text.getLayoutBounds().getHeight();
    }

    public Text getText() {
        return text;
    }

    public void setText(Text text) {
        this.text = text;
    }
}
