/**
 * @file AppViewController.java
 * @author Ivan Burlustkyi
 */
package ija.project.ui.controller;

import ija.project.dto.RoomDTO;
import ija.project.model.PausableEntity;
import ija.project.timer.RoomAnimationTimer;
import ija.project.model.impl.Block;
import ija.project.model.impl.Robot;
import ija.project.model.impl.Room;
import ija.project.ui.view.View;
import ija.project.ui.view.impl.BlockView;
import ija.project.ui.view.impl.RobotView;
import ija.project.util.json.JsonSerializer;
import ija.project.util.json.RoomDTOSerializer;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.transform.Scale;
import javafx.stage.FileChooser;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

/**
 * Controller for the main application view.
 * This class manages the interactions within the main application window, including
 * pausing and resuming the simulation, saving and loading simulation states, and managing
 * the display of robots and blocks within the simulation environment.
 */
public class AppViewController implements Initializable, PausableEntity {
    private static final org.slf4j.Logger log
            = org.slf4j.LoggerFactory.getLogger(AppViewController.class);
    /**
     * The text field for the game time in seconds.
     */
    @FXML
    @NonNull
    private TextField gameTime;
    
    /**
     * The scroll pane for the simulation environment.
     */
    @FXML
    @NonNull
    private ScrollPane scrollPane;
    
    /**
     * The border pane for the main application view.
     */
    @FXML
    @NonNull
    private BorderPane borderPane;
    
    /**
     * The pane for the simulation environment.
     */
    @FXML
    @NonNull
    private Pane pane;

    /**
     * The scene for the main application view.
     */
    private Scene scene;

    /**
     * The active block entity being displayed and edited.
     */
    private BlockView activeBlock;
    
    /**
     * The active robot entity being displayed and edited.
     */
    private RobotView activeRobot;

    /**
     * The active robot information view controller.
     */
    private RobotInfoViewController activeRobotInfoViewController;
    
    /**
     * The active block information view controller.
     */
    private BlockInfoViewController activeBlockInfoViewController;

    /**
     * The room entity being displayed and animated.
     */
    private RoomAnimationTimer roomAnimationTimer;
    
    /**
     * The room entity being displayed and edited.
     */
    private Room room;

    /**
     * The pause button for the simulation.
     */
    @FXML
    @NonNull
    private Button pauseButton;

    /**
     * Closes the application.
     *
     * @param actionEvent The event that triggered the method call.
     */
    public void closeApp(ActionEvent actionEvent) {
        Platform.exit();
    }

    /**
     * Saves the current state of the room to a JSON file.
     *
     * @param actionEvent The event that triggered the method call.
     */
    public void saveStateToJson(ActionEvent actionEvent) {
        pause();
        if (scene == null || scene.getWindow() == null) {
            log.error("Could not save file, no scene set");
            return;
        }
        if (room == null) {
            log.error("Nothing to save, no room set");
            return;
        }

        var filter = new FileChooser.ExtensionFilter("Json file", "*.json");
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Save robot json file");
        fileChooser.getExtensionFilters().add(filter);
        var selectedFile = fileChooser.showSaveDialog(scene.getWindow());

        if (selectedFile != null) {
            log.info("File path: {}", selectedFile.getAbsolutePath());
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(selectedFile.getAbsolutePath()))) {
                JsonSerializer<RoomDTO> serializer = new RoomDTOSerializer();
                writer.write(serializer.serializeObject(new RoomDTO(this.room)));
            } catch (IOException e) {
                log.error("Failed to save file {}", e.getMessage());
            }
        }
    }

    /**
     * Loads the state of the room from a JSON file.
     *
     * @param actionEvent The event that triggered the method call.
     */
    public void loadStateFromJson(ActionEvent actionEvent) {
        pause();
        if (scene == null || scene.getWindow() == null) {
            log.error("Could not load file, no scene set");
            return;
        }

        var filter = new FileChooser.ExtensionFilter("Json file", "*.json");
        var fileChooser = new FileChooser();
        fileChooser.setTitle("Load robot json file");
        fileChooser.getExtensionFilters().add(filter);
        var selectedFile = fileChooser.showOpenDialog(scene.getWindow());

        if (selectedFile != null) {
            try {
                var content = Files.readString(Path.of(selectedFile.getAbsolutePath()));
                JsonSerializer<RoomDTO> serializer = new RoomDTOSerializer();
                setRoom(serializer.deserilizeObject(content).buildEntity());
                log.info("Room successfully loaded");
            } catch (IOException e) {
                log.error("Error happened: {}", e.getMessage());
            }
        }
    }

    /**
     * Shows help information.
     *
     * @param actionEvent The event that triggered the method call.
     */
    public void showHelp(ActionEvent actionEvent) {
        //TODO
        // popup window with help info ...
        // or just send to url with git project
    }

    /**
     * Toggles the pause state of the simulation.
     *
     * @param mouseEvent The event that triggered the method call.
     */
    public void togglePause(MouseEvent mouseEvent) {
        if (isPaused()) {
            resume();
        } else {
            pause();
        }
    }

    /**
     * Pauses the simulation.
     */
    public void pause() {
        if (isPaused()) return;
        roomAnimationTimer.pause();
        pauseButton.setText("▷");
        gameTime.setEditable(true);
    }

    /**
     * Resumes the simulation.
     */
    public void resume() {
        if (!isPaused()) return;
        if (!room.isInValidState()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Bad room state");
            alert.setContentText(
                    """
                                Some robots in the room have bad state
                                Problem: some robots are colliding or out of room
                                Hint: pay attention to red circles (they must not be red)
                            """);
            alert.showAndWait();
            return;
        }
        roomAnimationTimer.resume();
        pauseButton.setText("||");
        gameTime.setEditable(false);
    }

    /**
     * Checks if the simulation is paused.
     *
     * @return True if the simulation is paused, false otherwise.
     */
    public boolean isPaused() {
        return roomAnimationTimer.isPaused();
    }

    /**
     * Adds a default robot to the simulation.
     *
     * @param mouseEvent The event that triggered the method call.
     */
    public void addDefaultRobot(MouseEvent mouseEvent) {
        pause();
        var magicValue = (getRoom().getWidth() + getRoom().getHeight()) / 2;
        var robot = new Robot(room,
                getRoom().getWidth() / 2, getRoom().getHeight() / 2,
                magicValue * 0.05, 0,
                magicValue * 0.08, 60, 12,
                magicValue / 10, 180);
        room.addRobot(robot);
        addRobotToPane(robot);
    }

    /**
     * Adds a default block to the simulation.
     *
     * @param mouseEvent The event that triggered the method call.
     */
    public void addDefaultBlock(MouseEvent mouseEvent) {
        pause();
        var magicValue = (getRoom().getWidth() + getRoom().getHeight()) / 2;
        var block = new Block(room,
                getRoom().getWidth() / 2, getRoom().getHeight() / 2,
                magicValue * 0.05, magicValue * 0.05);
        room.addBlock(block);
        addBlockToPane(block);
    }

    /**
     * Initializes the controller.
     *
     * @param location  The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param resources The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setRoom(new Room(800, 600));

        makePositiveIntField(gameTime);
        gameTime.textProperty().bindBidirectional(roomAnimationTimer.gameTimeSecsProperty());

        roomAnimationTimer.gameTimeNanosProperty().addListener((obs, oldVal, newVal) -> {
            if (this.roomAnimationTimer.getRoom() == getRoom()) return;
            setRoom(this.roomAnimationTimer.getRoom());
        });

        gameTime.setOnAction(
                e -> {
                        try {
                            log.info("Written info into field {}", gameTime.getText());
                            roomAnimationTimer.setStateToSec(Integer.parseInt(gameTime.getText()));
                        } catch (Exception exception) {
                            log.error("Exception happened: {}", exception.getMessage());
                        }
                }
        );

        scrollPane.viewportBoundsProperty().addListener(
                (obs, oldVal, newVal) -> {
                    if (room == null) return;
                    pane.getTransforms().setAll(new Scale(
                            newVal.getWidth() / room.getWidth(),
                            newVal.getHeight() / room.getHeight()
                    ));
                }
        );

    }

    /**
     * Gets the active block entity being displayed and edited.
     *
     * @return The active block entity being displayed and edited.
     */
    public BlockView getActiveBlock() {
        return activeBlock;
    }

    /**
     * Gets the active robot entity being displayed and edited.
     *
     * @return The active robot entity being displayed and edited.
     */
    public RobotView getActiveRobot() {
        return activeRobot;
    }

    /**
     * Resets the active view.
     */
    public void resetActiveView() {
        if (getActiveBlock() != null) {
            activeBlock.deactivateView();
        }

        if (getActiveRobot() != null) {
            activeRobot.deactivateView();
        }

        if (this.activeBlockInfoViewController != null) {
            this.activeBlockInfoViewController.getBlock()
                    .removeObserver(this.activeBlockInfoViewController);
        }

        if (this.activeRobotInfoViewController != null) {
            this.activeRobotInfoViewController.getRobot()
                    .removeObserver(this.activeRobotInfoViewController);
        }

        this.activeRobot = null;
        this.activeBlock = null;
        this.activeBlockInfoViewController = null;
        this.activeRobotInfoViewController = null;

        this.borderPane.setRight(null);
    }

    /**
     * Gets the room entity being displayed and animated.
     *
     * @return The room entity being displayed and animated.
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Sets the active block entity being displayed and edited.
     *
     * @param activeBlock The active block entity being displayed and edited.
     */
    public void setActiveBlock(BlockView activeBlock) {
        if (activeBlock != null) resetActiveView();
        this.activeBlock = activeBlock;

        if (activeBlock == null) {
            this.borderPane.setRight(null);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(View.class.getResource("block-info-view.fxml"));
            var pane = new BorderPane(loader.load());

            this.activeBlockInfoViewController = loader.getController();
            this.activeBlockInfoViewController.setBlock((Block)activeBlock.getEntity());
            activeBlock.getEntity().addObserver(this.activeBlockInfoViewController);
            this.activeBlockInfoViewController.update(null);

            this.borderPane.setRight(pane);
        } catch (Exception e) {
            log.error("Failed to load more info for block... {}", e.getMessage());
        }
    }

    /**
     * Sets the active robot entity being displayed and edited.
     *
     * @param activeRobot The active robot entity being displayed and edited.
     */
    public void setActiveRobot(RobotView activeRobot) {
        if (activeRobot != null) resetActiveView();
        this.activeRobot = activeRobot;

        if (activeRobot == null) {
            this.borderPane.setRight(null);
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(View.class.getResource("robot-info-view.fxml"));
            var pane = new BorderPane(loader.load());

            this.activeRobotInfoViewController = loader.getController();
            this.activeRobotInfoViewController.setRobot((Robot)activeRobot.getEntity());
            activeRobot.getEntity().addObserver(this.activeRobotInfoViewController);
            this.activeRobotInfoViewController.update(null);

            this.borderPane.setRight(pane);
        } catch (Exception e) {
            log.error("Failed to load more info for robot... {}", e.getMessage());
        }
    }

    /**
     * Sets the room entity being displayed and animated.
     *
     * @param room The room entity being displayed and animated.
     */
    public void setRoom(Room room) {
        this.room = room;
        if (roomAnimationTimer != null) roomAnimationTimer.stop();
        pane.getChildren().clear();

        if (room == null) return;

        roomAnimationTimer = new RoomAnimationTimer(room);
        roomAnimationTimer.start();

        addRoomToPane(room);
    }

    /**
     * Gets the border pane for the main application view.
     *
     * @return The border pane for the main application view.
     */
    public BorderPane getBorderPane() {
        return borderPane;
    }

    /**
     * Gets the scene for the main application view.
     * 
     * @return The scene for the main application view.
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * Sets the scene for the main application view.
     *
     * @param scene The scene for the main application view.
     */
    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Gets the scroll pane for the simulation environment.
     *
     * @return The scroll pane for the simulation environment.
     */
    private void addRobotToPane(@NonNull Robot robot) {
        var robotView = new RobotView(this, robot);
        this.pane.getChildren().addAll(robotView.getDrawableShapes());
        robot.addObserver(robotView);
    }

    /**
     * Adds a block entity to the simulation environment.
     *
     * @param block The block entity to be added.
     */
    private void addBlockToPane(@NonNull Block block) {
        var blockView = new BlockView(this, block);
        this.pane.getChildren().addAll(blockView.getDrawableShapes());
        block.addObserver(blockView);
    }

    /**
     * Adds a room entity to the simulation environment.
     *
     * @param room The room entity to be added.
     */
    private void addRoomToPane(@NonNull Room room) {
        for (var r : room.getRobots()) {
            addRobotToPane(r);
        }

        for (var b : room.getBlocks()) {
            addBlockToPane(b);
        }
    }

    /**
     * Makes a text field accept only positive integers.
     *
     * @param textField The text field to be modified.
     */
    private void makePositiveIntField(@NonNull TextField textField) {
        UnaryOperator<TextFormatter.Change> integerFilter = change -> {
            String newText = change.getControlNewText();
            if (newText.matches("\\d*")) {
                return change; // Keep the change
            }
            return null; // Reject the change
        };

        TextFormatter<Integer> textFormatter = new TextFormatter<>(integerFilter);
        textField.setTextFormatter(textFormatter);
    }

    /**
     * Gets the active block information view controller.
     *
     * @return The active block information view controller.
     */
    public BlockInfoViewController getActiveBlockInfoViewController() {
        return activeBlockInfoViewController;
    }

    /**
     * Sets the active block information view controller.
     *
     * @param activeBlockInfoViewController The active block information view controller.
     */
    public void setActiveBlockInfoViewController(BlockInfoViewController activeBlockInfoViewController) {
        this.activeBlockInfoViewController = activeBlockInfoViewController;
    }

    /**
     * Gets the active robot information view controller.
     *
     * @return The active robot information view controller.
     */
    public RobotInfoViewController getActiveRobotInfoViewController() {
        return activeRobotInfoViewController;
    }

    /**
     * Sets the active robot information view controller.
     *
     * @param activeRobotInfoViewController The active robot information view controller.
     */
    public void setActiveRobotInfoViewController(RobotInfoViewController activeRobotInfoViewController) {
        this.activeRobotInfoViewController = activeRobotInfoViewController;
    }
}
