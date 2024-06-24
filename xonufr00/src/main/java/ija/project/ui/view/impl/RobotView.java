/**
 * @file RobotView.java
 * @author Ivan Burlustkyi
 */
package ija.project.ui.view.impl;

import ija.project.model.GameEntity;
import ija.project.model.enums.RobotMode;
import ija.project.model.impl.Robot;
import ija.project.observer.Observable;
import ija.project.observer.Observer;
import ija.project.ui.controller.AppViewController;
import ija.project.ui.view.ActivatingView;
import javafx.fxml.FXMLLoader;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Represents a view for a robot entity in the game environment.
 * This class is responsible for managing the visual representation of a robot entity
 * within the application's graphical user interface. It allows users to view and edit
 * the properties of a selected robot, such as its dimensions and position.
 */
public class RobotView implements Observer, ActivatingView {

    /**
     * The logger for the RobotView class.
     */
    private static final Logger log = LoggerFactory.getLogger(RobotView.class);

    /**
     * The color of the robot frame.
     */
    private static final Color ROBOT_FRAME_COLOR = Color.BLUE;
    
    /**
     * The color of the robot when it is rotating.
     */
    private static final Color ROTATING_COLOR = Color.PURPLE;
    
    /**
     * The color of the robot's detection arc.
     */
    private static final Color ARC_COLOR = Color.YELLOW;
    
    /**
     * The color of the robot when it is detected or colliding.
     */
    private static final Color DETECTED_OR_COLLIDING_COLOR = Color.RED;
    
    /**
     * The color of the robot frame when it is in user mode.
     */
    private static final Color USER_ROBOT_FRAME_COLOR = Color.GREEN;

    /**
     * The application view controller.
     */
    private final AppViewController appViewController;

    /**
     * The robot entity associated with this view.
     */
    @NonNull
    private final Robot robot;

    /**
     * Constructs a new RobotView with the specified application view controller and robot entity.
     *
     * @param controller The application view controller. Must not be null.
     * @param robot      The robot entity. Must not be null.
     */
    public RobotView(@NonNull AppViewController controller, @NonNull Robot robot) {
        this.appViewController = controller;
        this.robot = robot;

        configureMouseListener();
        configureKeyboardListenerForEditMode();
        configureKeyboardListenerForUserMode();
        makeViewDraggable();

        lowlight();
        update(null);
    }

    /**
     * Updates the view to reflect the state of the robot entity.
     *
     * @param o The observable object that triggered the update.
     */
    @Override
    public void update(Observable o) {
        var robotFrameColor = robot.getMode() == RobotMode.USER ? USER_ROBOT_FRAME_COLOR : ROBOT_FRAME_COLOR;
        var arcColor = ARC_COLOR;

        if (robot.hasDetected()) {
            arcColor = DETECTED_OR_COLLIDING_COLOR;
        }

        if (robot.isRotating()) {
            robotFrameColor = ROTATING_COLOR;
        }

        if (robot.isColliding() || robot.isOutOfRoom()) {
            robotFrameColor = DETECTED_OR_COLLIDING_COLOR;
        }

        this.robot.getRobotFrame().setFill(robotFrameColor);
        this.robot.getDetectionArc().setFill(arcColor);
    }

    /**
     * Activates the view, making it the active view in the application.
     */
    public void activateView() {
        this.appViewController.setActiveRobot(this);
        highlight();
        this.robot.getRobotFrame().requestFocus();
    }

    /**
     * Deactivates the view, making it no longer the active view in the application.
     */
    public void deactivateView() {
        lowlight();
        this.appViewController.setActiveRobot(null);
    }

    /**
     * Checks if the view is currently activated.
     *
     * @return true if the view is activated, otherwise false.
     */
    public boolean isActivatedView() {
        return (this.appViewController.getActiveRobot() == this);
    }

    /**
     * Highlights the robot, making it more visible in the application.
     */
    public void highlight() {
        this.robot.getRobotFrame().setStroke(Color.RED);
    }

    /**
     * Lowers the visibility of the robot in the application.
     */
    public void lowlight() {
        this.robot.getRobotFrame().setStroke(Color.BLACK);
    }

    /**
     * Retrieves the robot entity associated with this view.
     *
     * @return The robot entity.
     */
    @Override
    public GameEntity getEntity() {
        return robot;
    }

    /**
     * Retrieves the root pane of the view.
     *
     * @return The root pane of the view.
     */
    @Override
    public List<Shape> getDrawableShapes() {
        return robot.getDrawableShapes();
    }

    /**
     * Configures the mouse listener for the robot view.
     * This method sets up handling for mouse click events on the robot's frame, allowing for:
     * - Single-click to toggle the activation state of the view.
     * - Double-click to toggle the robot's mode between AI and USER.
     * It also requests focus for the robot frame upon a click event.
     */
    private void configureMouseListener() {
        this.robot.getRobotFrame().addEventHandler(
                MouseEvent.MOUSE_CLICKED, e -> {
                    if (e.getClickCount() > 0) {
                        if (isActivatedView()) deactivateView(); else activateView();
                        this.robot.getRobotFrame().requestFocus();
                    }
                    if (e.getClickCount() >= 2) {
                        if (this.robot.getMode() == RobotMode.AI) {
                            this.robot.setMode(RobotMode.USER);
                        } else {
                            this.robot.setMode(RobotMode.AI);
                        }
                    }
                    log.info("Clicked the robot time = {}", e.getClickCount());
                }
        );
    }
    /**
     * Configures the keyboard listener for controlling the robot in USER mode.
     * This method sets up handling for key press events when the robot is in USER mode and not paused,
     * allowing for:
     * - LEFT arrow key to turn the robot left.
     * - RIGHT arrow key to turn the robot right.
     * - SPACE key to toggle the robot's movement.
     * It logs the pressed key and any unimplemented keyboard events.
     */
    private void configureKeyboardListenerForUserMode() {
        this.robot.getRobotFrame().addEventHandler(
                KeyEvent.KEY_PRESSED, e -> {
                    log.info("Pressed key for the robot control: {}", e.getCode());

                    if (this.robot.isPaused() || this.robot.getMode() != RobotMode.USER)
                        return;

                    switch (e.getCode()) {
                        case LEFT:
                            this.robot.turn(false);
                            break;
                        case RIGHT:
                            this.robot.turn(true);
                            break;
                        case SPACE:
                            this.robot.toggleMovement();
                            break;
                        default:
                            log.info("Not implemented keyboard event: {} ", e.getCode());
                            break;
                    }
                }
        );
    }
    
    /**
     * Configures the keyboard listener for editing the robot in EDIT mode.
     * This method sets up handling for key press events when the robot is paused and the view is activated,
     * allowing for:
     * - LEFT and RIGHT arrow keys to adjust the robot's current angle.
     * - UP and DOWN arrow keys to scale the robot's radius.
     * It logs the pressed key for editing the robot frame and arc.
     */
    private void configureKeyboardListenerForEditMode() {
        this.robot.getRobotFrame().addEventHandler(
                KeyEvent.KEY_PRESSED, e -> {
                    if (!this.robot.isPaused() || !isActivatedView()) return;
                    log.info("Pressed key edit for the robot frame: {}", e.getCode());
                    switch (e.getCode()) {
                        case LEFT:
                            this.robot.setRobotCurrentAngle(
                                    this.robot.getRobotCurrentAngle() + 5
                            );
                            break;
                        case RIGHT:
                            this.robot.setRobotCurrentAngle(
                                    this.robot.getRobotCurrentAngle() - 5
                            );
                            break;
                        case UP:
                            this.robot.setRobotRadius(
                                    this.robot.getRobotRadius() * 1.05
                            );
                            break;
                        case DOWN:
                            this.robot.setRobotRadius(
                                    this.robot.getRobotRadius() * 0.95
                            );
                            break;
                    }
                }
        );

        this.robot.getRobotFrame().addEventHandler(
                KeyEvent.KEY_PRESSED, e -> {
                    log.info("Pressed key edit for the robot arc: {}", e.getCode());
                    switch (e.getCode()) {
                        case A:
                            this.robot.setArcExtent(
                                    this.robot.getArcExtent() - 5
                            );
                            break;
                        case D:
                            this.robot.setArcExtent(
                                    this.robot.getArcExtent() + 5
                            );
                            break;
                        case W:
                            this.robot.setArcRadius(
                                    this.robot.getArcRadius() * 1.05
                            );
                            break;
                        case S:
                            this.robot.setArcRadius(
                                    this.robot.getArcRadius() * 0.95
                            );
                            break;
                    }
                }
        );
    }
    /**
     * Makes the robot view draggable.
     * This method allows the user to change the robot's position by dragging its frame with the mouse,
     * setting the robot's center X and Y coordinates to the mouse's position during the drag event.
     */
    private void makeViewDraggable() {
        robot.getRobotFrame().setOnMouseDragged(mouseEvent -> {
            robot.setCenterY(mouseEvent.getY());
            robot.setCenterX(mouseEvent.getX());
        });
    }

    /**
     * Retrieves the robot entity associated with this view.
     *
     * @return The robot entity.
     */
    public AppViewController getAppViewController() {
        return appViewController;
    }
}
