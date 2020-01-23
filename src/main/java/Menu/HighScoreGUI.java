package main.java.Menu;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import main.java.Sound;
import main.java.Window;

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
        stage.setScene(scene);
        stage.setTitle("HIGHSCORE - LISTE");
        stage.initModality(Modality.APPLICATION_MODAL);

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
                Label labelScore = new Label("Spieler " + (index + 1) + " ist auf der Highscore-Liste! SCORE: " + score);
                labelScore.setStyle("-fx-font-size: 2.2em ;");
                //labelScore.setContentDisplay(ContentDisplay.CENTER);
                labelScore.setAlignment(Pos.CENTER);
                ListView<HighScoreItem> highScoreItemListView = new ListView<>();
                highScoreItemListView.setStyle("-fx-font-size: 1.8em;");

                for(HighScoreItem item: highScores){
                    highScoreItemListView.getItems().add(item);
                }

                TextField textFieldInput = new TextField("");
                textFieldInput.setPromptText(("Geben Sie Ihren Namen ein"));
                textFieldInput.requestFocus();
                textFieldInput.setStyle("-fx-font-size: 2em ;");

                Button buttonClose = new Button("CLOSE WINDOW");
                buttonClose.setVisible(false);

                vBox.getChildren().addAll(labelScore, highScoreItemListView, textFieldInput, buttonClose);

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

                        highScoreItemListView.getItems().clear();
                        highScoreItemListView.getItems().addAll(highScores);
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

                buttonClose.setOnAction( (e) -> {
                    Sound.playMenu();
                    stage.close();
                    checkScore(scores, finalIndex + 1, false);
                });

                vBox.setAlignment(Pos.CENTER);

            } else {

                /**
                 * if playing locale then also check if the second player is on the highscore list
                 */
                if(scores.length > 1) {

                    checkScore(scores, finalIndex + 1, false);

                } else {

                    stage.show();
                    ListView<HighScoreItem> highScoreItemListView = new ListView<>();
                    highScoreItemListView.setStyle("-fx-font-size: 1.8em;");

                    for(HighScoreItem item: highScores){
                        highScoreItemListView.getItems().add(item);
                    }

                    Button buttonClose = new Button("OK");

                    buttonClose.setOnAction( (e) -> {
                        Sound.playMenu();
                        stage.close();
                    });

                    vBox.setAlignment(Pos.CENTER);
                    vBox.getChildren().addAll(highScoreItemListView, buttonClose);

                }
            }

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