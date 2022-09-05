package net.webment;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class PrimaryController implements Initializable {
    private static int response = 1;
    private static int stable = 0;
    private static double weight = 0;


    @FXML
    private Button btn_Confirm;

    @FXML
    private Label current_Weight_Display;

    @FXML
    private Label current_Weight_Lbl;

    @FXML
    private Label pounds_Lbl;

    @FXML
    private HBox weight_Background;

    public static void setStable(int value) {
        stable = value;
    }

    public void get_Weight() {
            new Thread(() -> {
                try {
                    UsbScale scale = UsbScale.findScale();
                    assert scale != null;
                    scale.open();
                    while (response != 0) {
                        weight = scale.syncSubmit();
                        weight = (double) Math.round(weight * 10d) / 10d;
                        double finalWeight = weight;
                        Platform.runLater(() -> {
                            if (stable == -1) {
                                weight_Background.setStyle("-fx-background-color: red");
                                current_Weight_Display.setTextFill(Color.WHITE);
                                pounds_Lbl.setTextFill(Color.WHITE);
                            } else if (stable == 0) {
                                weight_Background.setStyle("-fx-background-color: white");
                                current_Weight_Display.setTextFill(Color.BLACK);
                                pounds_Lbl.setTextFill(Color.BLACK);
                            } else if (stable == 1) {
                                weight_Background.setStyle("-fx-background-color: green");
                                current_Weight_Display.setTextFill(Color.WHITE);
                                pounds_Lbl.setTextFill(Color.WHITE);
                            }
                            current_Weight_Display.setText(String.valueOf(finalWeight));
                        });
                        //here we want to be updating live a variable on the screen.
                    }
                } catch (NullPointerException e) {
                    create_Error("NullPointerException! " + e.getMessage());
                }

            }).start();

    }

    @FXML
    void on_Action_Confirm(ActionEvent event) {
        if (stable == 1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are You Sure the Package Weighs: " + weight + " Lbs?", ButtonType.YES, ButtonType.NO);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                create_File(String.valueOf(weight));
                Stage stage = (Stage) btn_Confirm.getScene().getWindow();
                // do what you have to do
                stage.close();
                System.exit(0);
            }
        } else {
            Alert error = new Alert(Alert.AlertType.ERROR, "You Cannot Confirm the Weight While until the Background is Green!");
            error.showAndWait();
        }
    }

    public static void create_File(String Weight) {
        //Use Paths.get in older JVM's
        Path f = Path.of(System.getProperty("user.home"), "Documents", "Weight.txt");
        try {
            Files.writeString(f, Weight, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            String written = Files.readString(f);
            if (written.equals(Weight)) {
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }


    public static void create_Error(String error) {
        //Use Paths.get in older JVM's
        Path f = Path.of(System.getProperty("user.home"), "Documents", "Error.txt");
        try {
            Files.writeString(f, error, StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            String written = Files.readString(f);
            if (written.equals(error)) {
                System.exit(0);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        get_Weight();
    }
}
