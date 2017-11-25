/**
 * Create by hieuduong on 11/24/17
 * CSC-201 Unit5 Take Home Test
 * 16.23
 */

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import javafx.util.Duration;


public class Main extends Application {

    private TextField animationSpeedInput = new TextField();
    private TextField imageFilePrefixInput = new TextField();
    private TextField numberOfimageInput = new TextField();
    private TextField audioFileURLInput = new TextField();
    private Label status;
    private ImageView imageView;
    protected Media media;
    protected MediaPlayer mediaPlayer;
    private Timeline animation;
    private static int ImageOrder = 0;

    @Override
    public void start(Stage primaryStage) {

        // Create a pane
        BorderPane pane = new BorderPane();

        // Place nodes in the pane
        pane.setTop(getHBox());
        pane.setCenter(getMiddleHBox());
        pane.setBottom(getGridPane());

        // Create a scene and place it in the stage
        Scene scene = new Scene(pane, 700, 450);
        primaryStage.setTitle("16.23"); // Set the stage title
        primaryStage.setScene(scene); // Place the scene in the stage
        primaryStage.show(); // Display the stage

    }

    /**
     * Create Hbox to hold the control button
     * @return
     */
    private HBox getHBox() {
        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(15, 15, 15, 15));
        hBox.setStyle("-fx-background-color: silver");
        /**
         * Add Start Button to start the animation
         */
        Button startBtn = new Button("Start Animation");

        hBox.getChildren().add(startBtn);
        hBox.setAlignment(Pos.CENTER);

        /**
         * Assign action to button
         * Either lamba or regular action event should be fine
         */
        /*
        startBtn.setOnAction(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent event) {
                checkInputfields();
            }
        });
        */

        startBtn.setOnAction(e -> checkInputfields());
        return hBox;
    }

    /**
     * Create the second Hbox to hold the image
     * @return
     */
    private HBox getMiddleHBox(){
        HBox hBox = new HBox(15);
        hBox.setPadding(new Insets(15, 15, 15, 15));

        imageView = new ImageView();
        imageView.setFitHeight(150);
        hBox.getChildren().addAll(imageView);
        hBox.setAlignment(Pos.CENTER);
        return hBox;
    }

    /**
     * Create a gridpane to hold textfields
     * @return
     */
    private GridPane getGridPane(){

        //Create a new gridPane to hold the textfields
        animationSpeedInput.setPrefWidth(400);
        imageFilePrefixInput.setPrefWidth(400);
        numberOfimageInput.setPrefWidth(400);
        audioFileURLInput.setPrefWidth(400);
        status = new Label();

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.add(new Label("Enter information for animation:"), 0, 0);
        gridPane.add(new Label("Animation speed in miliseconds:"), 0, 1);
        gridPane.add(animationSpeedInput, 1, 1);
        gridPane.add(new Label("Image the prefix:"), 0, 2);
        gridPane.add(imageFilePrefixInput, 1, 2);
        gridPane.add(new Label("Number of image:"), 0, 3);
        gridPane.add(numberOfimageInput, 1, 3);
        gridPane.add(new Label("Audio file URL:"), 0, 4);
        gridPane.add(audioFileURLInput, 1, 4);
        gridPane.add(new Label("Status"), 0, 5);
        gridPane.add(status, 1, 5);

        /**
         * Prefilled the inputs, only for testing purpose.
         */
        animationSpeedInput.setText("500");
        imageFilePrefixInput.setText("L");
        numberOfimageInput.setText("5");
        audioFileURLInput.setText("http://cs.armstrong.edu/liang/common/audio/anthem/anthem2.mp3");
        //Set alignments
        gridPane.setAlignment(Pos.CENTER);

        return gridPane;
    }

    /**
     * Check inputs method
     * This method will check the input and make sure they are filled with correct format
     */
    private void checkInputfields() {

        boolean mediaURLIsValid = false;
        boolean animationSpeedIsValid = false;
        boolean imagePrefixIsValid = false;
        boolean numberOfimageIsValid = false;

        /**
         * Check image speed input
         */
        int animationSpeed = 0;
        try{
            animationSpeed = Integer.parseInt(animationSpeedInput.getText().toString());
            animationSpeedIsValid = true;
        }
        catch(Exception ex){
            showAlert("Input is missing or invalid. Please enter integer only!");
            animationSpeedInput.requestFocus();
        }

        /**
         * Check image prefix input
         */
        String imagePrefix = imageFilePrefixInput.getText();
        if(imagePrefix.isEmpty() || imagePrefix.equals("")){
            showAlert("Image file prefix is missing");
            imageFilePrefixInput.requestFocus();
        }
        else{
            imagePrefixIsValid = true;
        }

        /**
         * Check number of image input
         */
        int numberOfImage = 0;
        try{
            numberOfImage = Integer.parseInt(numberOfimageInput.getText().toString());
            numberOfimageIsValid = true;
        }
        catch (Exception ex){
            showAlert("Input is missing or invalid. Please enter integer only!");
        }

        /**
         * Check media URL input
         */
        String mediaURL = audioFileURLInput.getText();
        if(mediaURL.isEmpty() || mediaURL.equals("")){
            showAlert("Media URL is missing!");
            audioFileURLInput.requestFocus();
        }
        else {
           mediaURLIsValid = true;
        }

        /**
         * If all of the inputs are valid, start the animation method
         */
        if(mediaURLIsValid && animationSpeedIsValid && imagePrefixIsValid && numberOfimageIsValid){
            startAnimation(mediaURL, animationSpeed, imagePrefix, numberOfImage);
        }
    }

    /**
     * Start the animation method
     * @param mediaURL
     * @param animationSpeed
     * @param imagePrefix
     * @param numberOfImage
     */
    private void startAnimation(String mediaURL, int animationSpeed, String imagePrefix, int numberOfImage) {

        media = new Media(mediaURL);
        mediaPlayer = new MediaPlayer(media);
        //Stop current player first, then start
        //This will prevent the music from overlapping each other
        mediaPlayer.stop();
        mediaPlayer.play();

        animation = new Timeline(new KeyFrame(Duration.millis(animationSpeed), e -> getImages(imagePrefix, numberOfImage)));
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play(); // Start animation
        status.setText("Playing...");
    }

    /**
     * Get Images method. This method will display the gif files
     * @param imagePrefix
     * @param numberOfImage
     */
    private void getImages(String imagePrefix, int numberOfImage) {

        //Create a loop to reset the image order when it is equals the number of image
        ImageOrder +=1;
        if(ImageOrder <=numberOfImage){
            String imagePath = "";
            imagePath = "image/"+imagePrefix + ImageOrder +".gif";
            imageView.setImage(new Image(imagePath));
        }
        else{
            ImageOrder = 0;
        }
    }

    /**
     * Show alert method
     * @param msg
     */
    private void showAlert(String msg){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error!");
        alert.setHeaderText(null);
        alert.setContentText(msg);

        alert.showAndWait();
    }
}