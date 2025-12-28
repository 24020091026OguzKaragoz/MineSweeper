package org.t3tracon.minesweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import java.util.Objects;
import java.util.Stack;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;


public class HelloController {
    @FXML
    public GridPane gridPane;
    Btn[][] board = new Btn[10][10];
    public int openBtnCount = 0;

    public void playLoseVideo() {
        try {
            String path = getClass().getResource("/lose.mp4").toExternalForm();
            Media media = new Media(path);
            MediaPlayer mediaPlayer = new MediaPlayer(media);
            MediaView mediaView = new MediaView(mediaPlayer);

            mediaView.setFitWidth(400);;
            mediaView.setPreserveRatio(true);

            StackPane root = new StackPane(mediaView);
            Scene scene = new Scene(root, 400, 200);
            Stage videoStage = new Stage();
            videoStage.setTitle("O SES NEYDİ LAN");
            videoStage.setScene(scene);

            videoStage.show();
            mediaPlayer.play();

        } catch (Exception e) {
            System.err.println("video oynatilamadi: " + e.getMessage());
        }
    }

    public void createBtn() {
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                Btn b = new Btn(row, col);
                b.setMinSize(40, 40);
                b.setMaxSize(40, 40);
                b.setPrefSize(40, 40);

                b.setOnMouseClicked(e -> {
                    // right click
                    if (e.getButton() == javafx.scene.input.MouseButton.SECONDARY) {
                        System.out.println("sag tik");
                        handleRightClick(b);
                    }
                    //left click
                    else if (e.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                        System.out.println("sol tik");
                        if (b.isMine()) {
                            playLoseVideo();
                            print();
                        } else {
                            open(b.getRow(), b.getCol());
                            if (openBtnCount == (board.length)*(board[0].length) - 10) {
                                playLoseVideo();
                                print();
                            }
                        }
                    }
                });
                board[row][col] = b;
                gridPane.add(b, col, row);
            }
        }
    }

    public void resetGame() {
        openBtnCount = 0;
        gridPane.getChildren().clear();
        board = new Btn[10][10];
        createBtn();
        generateMine();
        updateCount();
    }

    public void handleRightClick(Btn b) {
        Image flagImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/flag.png")));
        ImageView imageView = new ImageView(flagImage);
        imageView.setFitWidth(25);
        imageView.setFitHeight(25);
        imageView.setPreserveRatio(true);

        b.setGraphic(imageView);
    }

    public void generateMine() {
        int i = 0;
        while(i < 10) {
            int randRow = (int) (Math.random() * board.length);
            int randCol = (int) (Math.random() * board[0].length);

            while(board[randRow][randCol].isMine()) {
                randRow = (int) (Math.random() * board.length);
                randCol = (int) (Math.random() * board[0].length);
            }
            board[randRow][randCol].setMine(true);
            i++;
        }
    }

    public void print() {
        Image mineImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/mine.png")));
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col].isMine()) {
                    ImageView imageView = new ImageView(mineImage);
                    imageView.setFitWidth(25);
                    imageView.setFitHeight(25);
                    imageView.setPreserveRatio(true);

                    board[row][col].setGraphic(imageView);
                } else {
                    board[row][col].setText(board[row][col].getCount() + "");
                }
            }
        }
    }

    public void updateCount() {
        for (int row = 0; row < board.length; row++) {
            for (int col = 0; col < board[0].length; col++) {
                if (board[row][col].isMine()) {
                    counting(row, col);
                }
            }
        }
    }

    public void counting(int row, int col) {
        for (int i = row-1; i <= row + 1; i++) {
            for (int k = col-1; k <= col + 1; k++) {
                try {
                    int value = board[i][k].getCount();
                    board[i][k].setCount(++value);
                } catch (Exception e) {

                }
            }
        }
    }

    public void open(int r, int c) {
        if (r < 0 || r >= board.length || c < 0 || c >= board[0].length || board[r][c].getText().length() > 0 || board[r][c].isDisabled()==true) {
            return;
        } else if (board[r][c].getCount() != 0) {
            board[r][c].setText(board[r][c].getCount() + "");
            board[r][c].setDisable(true);
            openBtnCount++;
        } else {
            openBtnCount++;
            board[r][c].setDisable(true);
            open(r-1, c);
            open(r+1, c);
            open(r, c-1);
            open(r, c+1);
        }
    }

    @FXML
    public void initialize() {
        createBtn();
        generateMine();
        updateCount();
        //print();
    }
}
