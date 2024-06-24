/**
 * @file RobotInfoViewController.java
 * @author Ivan Onufriienko
 */
package ija.project.ui.controller;

import ija.project.model.impl.Robot;
import ija.project.observer.Observable;
import ija.project.observer.Observer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * Controller for the Robot Information View in the UI.
 * This class is responsible for managing the interaction between the user and the robot's properties
 * within the application's graphical user interface. It allows users to view and edit the properties
 * of a selected robot, such as its dimensions and position.
 */
public class RobotInfoViewController implements Observer {
    private static final org.slf4j.Logger log
            = org.slf4j.LoggerFactory.getLogger(RobotInfoViewController.class);
    /**
     * The text field for the robot's center X coordinate.
     */
    @FXML
    @NonNull
    private TextField centerX;
    
    /**
     * The text field for the robot's center Y coordinate.
     */
    @FXML
    @NonNull
    private TextField centerY;
    
    /**
     * The text field for the robot's radius.
     */
    @FXML
    @NonNull
    private TextField radius;
    
    /**
     * The text field for the robot's movement speed.
     */
    @FXML
    @NonNull
    private TextField movementSpeed;
    
    /**
     * The text field for the robot's rotation speed.
     */
    @FXML
    @NonNull
    private TextField rotationSpeed;
    
    /**
     * The text field for the robot's rotation sample.
     */
    @FXML
    @NonNull
    private TextField rotationSample;
    
    /**
     * The text field for the robot's current angle.
     */
    @FXML
    @NonNull
    private TextField angle;
    
    /**
     * The text field for the robot's arc length.
     */
    @FXML
    @NonNull
    private TextField arcLength;
    
    /**
     * The text field for the robot's arc radius.
     */
    @FXML
    @NonNull
    private TextField arcRadius;

    /**
     * The robot entity that is being displayed and edited.
     */
    private Robot robot;

    /**
     * Handles changes to the robot's center X coordinate.
     * Attempts to parse the input and update the robot's position.
     *
     * @param actionEvent The event triggered by changing the text field.
     */
    public void onCoordinateXChange(ActionEvent actionEvent) {
        try {
            robot.setCenterX(Double.parseDouble(centerX.getText()));
        } catch (Exception e) {
            log.error("Failed to set center X", e);
        }
    }

    /**
     * Handles changes to the robot's center Y coordinate.
     * Attempts to parse the input and update the robot's position.
     *
     * @param actionEvent The event triggered by changing the text field.
     */
    public void onCoordinateYChange(ActionEvent actionEvent) {
        try {
            robot.setCenterY(Double.parseDouble(centerY.getText()));
        } catch (Exception e) {
            log.error("Failed to set center Y", e);
        }
    }

    /**
     * Handles changes to the robot's radius.
     * Attempts to parse the input and update the robot's radius.
     *
     * @param actionEvent The event triggered by changing the text field.
     */
    public void onRobotRadiusChange(ActionEvent actionEvent) {
        try {
            robot.setRobotRadius(Double.parseDouble(radius.getText()));
        } catch (Exception e) {
            log.error("Failed to set radius", e);
        }
    }

    /**
     * Handles changes to the robot's movement speed.
     * Attempts to parse the input and update the robot's speed per second.
     *
     * @param inputMethodEvent The event triggered by changing the text field.
     */
    public void onMovementSpeedChange(ActionEvent inputMethodEvent) {
        try {
            robot.setSpeedPerSecond(Double.parseDouble(movementSpeed.getText()));
        } catch (Exception e) {
            log.error("Failed to set movement speed", e);
        }
    }

    /**
     * Handles changes to the robot's rotation speed.
     * Attempts to parse the input and update the robot's rotation speed in degrees per second.
     *
     * @param actionEvent The event triggered by changing the text field.
     */
    public void onRotationSpeedChange(ActionEvent actionEvent) {
        try {
            robot.setRotationSpeedPerSecInDegrees(
                    Double.parseDouble(rotationSpeed.getText()));
        } catch (Exception e) {
            log.error("Failed to set rotation speed", e);
        }
    }

    /**
     * Handles changes to the robot's rotation sample.
     * Attempts to parse the input and update the robot's rotation degree sample.
     *
     * @param actionEvent The event triggered by changing the text field.
     */
    public void onRotationSampleChange(ActionEvent actionEvent) {
        try {
            robot.setRotationDegreeSample(Double.parseDouble(rotationSample.getText()));
        } catch (Exception e) {
            log.error("Failed to set rotation", e);
        }
    }

    /**
     * Handles changes to the robot's current angle.
     * Attempts to parse the input and update the robot's current angle in degrees.
     *
     * @param actionEvent The event triggered by changing the text field.
     */
    public void onCurrentAngleChange(ActionEvent actionEvent) {
        try {
            robot.setRobotCurrentAngle(Double.parseDouble(angle.getText()));
        } catch (Exception e) {
            log.error("Failed to set current angle", e);
        }
    }

    /**
     * Handles changes to the robot's arc length.
     * Attempts to parse the input and update the robot's arc extent.
     *
     * @param actionEvent The event triggered by changing the text field.
     */
    public void onArcLengthChange(ActionEvent actionEvent) {
        try {
            robot.setArcExtent(Double.parseDouble(arcLength.getText()));
        } catch (Exception e) {
            log.error("Failed to set arc length", e);
        }
    }

    /**
     * Handles changes to the robot's arc radius.
     * Attempts to parse the input and update the robot's arc radius.
     *
     * @param actionEvent The event triggered by changing the text field.
     */
    public void onArcRadiusChange(ActionEvent actionEvent) {
        try {
            robot.setArcRadius(Double.parseDouble(arcRadius.getText()));
        } catch (Exception e) {
            log.error("Failed to set arc radius", e);
        }
    }

    /**
     * Updates the text fields to reflect the current state of the robot.
     * This method is called when the robot's observable state changes.
     *
     * @param o The observable object that has changed (in this case, the robot).
     */
    @Override
    public void update(Observable o) {

        if (robot == null) {
            log.warn("Robot is not set for {}", this.getClass().toString());
            return;
        }

        centerX.setText(Double.valueOf(robot.getCenterX()).toString());
        centerY.setText(Double.valueOf(robot.getCenterY()).toString());
        radius.setText(Double.valueOf(robot.getRobotRadius()).toString());
        arcRadius.setText(Double.valueOf(robot.getArcRadius()).toString());
        arcLength.setText(Double.valueOf(robot.getArcExtent()).toString());
        angle.setText(Double.valueOf(robot.getRobotCurrentAngle()).toString());
        movementSpeed.setText(Double.valueOf(robot.getSpeedPerSecond()).toString());
        rotationSpeed.setText(Double.valueOf(robot.getRotationSpeedPerSecInDegrees()).toString());
        rotationSample.setText(Double.valueOf(robot.getRotationDegreeSample()).toString());
    }

    /**
     * Gets the robot currently managed by this controller.
     * 
     * @return The robot.
     */
    public Robot getRobot() {
        return robot;
    }

    /**
     * Sets the robot to be displayed and edited in the view.
     *
     * @param robot The robot to be managed by this controller.
     */
    public void setRobot(Robot robot) {
        this.robot = robot;
    }
}
