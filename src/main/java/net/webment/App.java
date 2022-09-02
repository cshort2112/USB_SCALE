package net.webment;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * JavaFX App
 */
public class App extends Application {
    private static int response = 1;

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"));
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
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

}