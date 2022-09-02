package net.webment;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import static net.webment.App.create_Error;

public class PrimaryController implements Initializable {


    private static int response = 1;


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


    private void get_Weight() {

        new Thread(() -> {
            try {
                UsbScale scale = UsbScale.findScale();
                assert scale != null;
                scale.open();
                double weight = 0;
                System.out.println("Test");
                while (response != 0) {
                    weight = scale.syncSubmit();
                    weight = (double) Math.round(weight * 10d) / 10d;
                    double finalWeight = weight;
                    Platform.runLater(() -> {
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

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        get_Weight();
    }
}
