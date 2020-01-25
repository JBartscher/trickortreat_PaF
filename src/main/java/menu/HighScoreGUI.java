package main.java.menu;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.sounds.Sound;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class HighScoreGUI {

    Stage stage;
    Scene scene;
    VBox vBox;

    private static ArrayList<HighScoreItem> highScores = new ArrayList<>();

    public void checkScore(int[]scores, int index, boolean readOnly) {
        if(index >= scores.length) return;
        int finalIndex = index;

        this.stage = new Stage();
        this.vBox = new VBox(10);
        this.scene = new Scene(vBox, 600, 650);

        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setTitle("HIGHSCORE - LISTE");
        stage.initModality(Modality.APPLICATION_MODAL);
        ListView<HighScoreItem> highScoreItemListView = new ListView<>();

        int score = scores[index];

        FileInputStream fis = null;
        ObjectInputStream ois = null;

        try {
            File file = new File("highscore.txt");
            if (!file.exists()) {
                file.createNewFile();
                ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(file));
                // Wichtig zur einmaligen Erstellung des Overheads, den der ObjectInputStream benötigt
                os.writeObject(new ArrayList<HighScoreItem>());
                os.flush();
                os.close();
            }

            fis = new FileInputStream(file);
            ois = new ObjectInputStream(fis);

            Object o = null;
            //System.out.println("EINLESEN! " + index);
            o = ois.readObject();
            highScores = new ArrayList<>((ArrayList<HighScoreItem>) o);

            /**
             * add highscore items when size of the current list is lower than 10 elements
             */
            while (highScores.size() < 10) {
                highScores.add(new HighScoreItem("LEER", 0));
            }

            /**
             * show the highscore gui when current player collected enough candies
             */
            if(score > highScores.get(9).getScore()) {
                stage.show();

                Label labelTitle = new Label("HIGHSCORE");
                labelTitle.setAlignment(Pos.CENTER);
                labelTitle.setTextFill(Color.WHITE);
                labelTitle.setStyle("-fx-font-size: 2.5em ;");

                Label labelScore = new Label("Spieler " + (index + 1) + " ist auf der Highscore-Liste! SCORE: " + score);
                labelScore.setTextFill(Color.WHITE);
                labelScore.setStyle("-fx-font-size: 2.0em ;");
                //labelScore.setContentDisplay(ContentDisplay.CENTER);
                labelScore.setAlignment(Pos.CENTER);
                highScoreItemListView = new ListView<>();
                highScoreItemListView.setStyle("-fx-font-size: 1.8em;");

                for(HighScoreItem item: highScores){
                    highScoreItemListView.getItems().add(item);
                }

                TextField textFieldInput = new TextField("");
                textFieldInput.setPromptText(("Geben Sie Ihren Namen ein und drücken Sie Enter"));
                textFieldInput.requestFocus();
                textFieldInput.setStyle("-fx-font-size: 2em ;");

                Button buttonClose = new Button("CLOSE WINDOW");
                buttonClose.setStyle("-fx-padding: 5 22 5 22; -fx-border-color: #e2e2e2; fx-border-width: 2; -fx-background-radius: 0;" +
                        "-fx-background-color: #1d1d1d; -fx-text-fill: #d8d8d8; -fx-background-insets: 0 0 0 0, 1, 2;");
                buttonClose.setVisible(false);

                vBox.getChildren().addAll(labelTitle, labelScore, highScoreItemListView, textFieldInput, buttonClose);

                ListView<HighScoreItem> finalHighScoreItemListView = highScoreItemListView;
                textFieldInput.setOnAction( (e) -> {
                    String eingabe = textFieldInput.getText();
                    if(eingabe.length() == 0) {
                        textFieldInput.setText("ERROR - Geben Sie einen Namen ein!");
                    } else {
                        highScores.add( new HighScoreItem(eingabe, score));
                        while(highScores.size() > 10) {
                            highScores.remove(9);
                        }

                        sort();

                        finalHighScoreItemListView.getItems().clear();
                        finalHighScoreItemListView.getItems().addAll(highScores);
                        buttonClose.setVisible(true);
                        textFieldInput.setVisible(false);
                        vBox.getChildren().remove(textFieldInput);

                        /**
                         * save the new highscore list to highscore.txt
                         */
                        try {
                            FileOutputStream fos = new FileOutputStream("highscore.txt");
                            ObjectOutputStream oos = new ObjectOutputStream(fos);
                            oos.writeObject(highScores);
                            oos.flush();
                            oos.close();
                        } catch (FileNotFoundException ex) {
                            ex.printStackTrace();
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                });

                Light.Distant light = new Light.Distant();
                light.setAzimuth(0);

                Lighting lighting = new Lighting(light);
                lighting.setSurfaceScale(5.0);
                vBox.setEffect(lighting);
                vBox.setAlignment(Pos.CENTER);
                buttonClose.setStyle("-fx-padding: 5 22 5 22; -fx-border-color: #e2e2e2; fx-border-width: 2; -fx-background-radius: 0;" +
                        "-fx-background-color: #1d1d1d; -fx-text-fill: #d8d8d8; -fx-background-insets: 0 0 0 0, 1, 2;");

                buttonClose.setOnAction( (e) -> {
                    Sound.playMenu();
                    stage.close();
                    checkScore(scores, finalIndex + 1, false);
                });


            } else {

                /**
                 * if playing locale then also check if the second player is on the highscore list
                 */
                if(scores.length > 1) {

                    checkScore(scores, finalIndex + 1, false);

                } else if(!readOnly) {

                    Label labelTitle = new Label("HIGHSCORE");
                    labelTitle.setAlignment(Pos.CENTER);
                    labelTitle.setTextFill(Color.WHITE);
                    labelTitle.setStyle("-fx-font-size: 5.0em ;");
                    labelTitle.setFont(Font.loadFont(MainMenu.class.getResource("Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 30));

                    Light.Distant light = new Light.Distant();
                    light.setAzimuth(0);

                    Lighting lighting = new Lighting(light);
                    lighting.setSurfaceScale(5.0);
                    vBox.setEffect(lighting);

                    stage.show();
                    highScoreItemListView = new ListView<>();
                    highScoreItemListView.setStyle("-fx-font-size: 1.8em;");

                    for(HighScoreItem item: highScores){
                        highScoreItemListView.getItems().add(item);
                    }

                    Button buttonClose = new Button("CLOSE");
                    buttonClose.setFont(Font.loadFont(MainMenu.class.getResource("Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 30));
                    buttonClose.setStyle("-fx-padding: 5 22 5 22; -fx-border-color: #e2e2e2; fx-border-width: 2; -fx-background-radius: 0;" +
                            "-fx-background-color: #1d1d1d; -fx-text-fill: #d8d8d8; -fx-background-insets: 0 0 0 0, 1, 2;");

                    buttonClose.setOnAction( (e) -> {
                        Sound.playMenu();
                        stage.close();
                    });

                    vBox.setAlignment(Pos.CENTER);
                    vBox.getChildren().addAll(labelTitle, highScoreItemListView, buttonClose);

                }
            }

            vBox.setBackground(new Background(new BackgroundFill(Color.rgb(10, 10, 20), CornerRadii.EMPTY, Insets.EMPTY)));



        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e);

        }
    }

    /**
     * sort the highscore items in reversed order (descending)
     */
    public void sort() {
        Collections.sort(highScores, (first, second) -> (first.getScore() < second.getScore()) ? 1 : -1);

    }

}