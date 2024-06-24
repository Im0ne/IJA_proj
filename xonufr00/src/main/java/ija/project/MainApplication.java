/**
 * @file MainApplication.java
 * @author Ivan Onufriienko
 */
package ija.project;

import ija.project.timer.RoomAnimationTimer;
import ija.project.model.impl.Room;
import ija.project.ui.controller.AppViewController;
import ija.project.ui.view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

/**
 * Main class for the JavaFX application.
 * This class extends the Application class from JavaFX and serves as the main entry point for the application.
 * It initializes the primary stage and sets up the initial scene for the application using FXML.
 */
public class MainApplication extends Application {
    private static final org.slf4j.Logger log
            = org.slf4j.LoggerFactory.getLogger(AppViewController.class);
    /**
     * Starts the primary stage of the application.
     * This method is called after the application has been initialized and is ready to start.
     * It loads the application's main view from an FXML file and sets it as the scene of the primary stage.
     *
     * @param stage The primary stage for this application, onto which the application scene can be set.
     *              The primary stage will be embedded in the platform's default browser or as a standalone
     *              application window depending on the platform and how the application is launched.
     * @throws Exception if the FXML file cannot be loaded.
     */
    @Override
    public void start(Stage stage) throws Exception {
        log.info("Path: {}", View.class.getResource("robot-info-view.fxml"));
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("app-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setScene(scene);
        stage.show();
    }


    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned, and after the system is ready for
     * the application to begin running.
     *
     * @param args the command line arguments passed to the application.
     *             An application may get these parameters using the getParameters() method.
     */
    public static void main(String[] args) {
        //Process args init the state of out app
        launch();
    }
}