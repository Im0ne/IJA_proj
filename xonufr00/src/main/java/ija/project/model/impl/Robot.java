/**
 * @file Robot.java
 * @author Ivan Burlustkyi
 */
package ija.project.model.impl;

import ija.project.ui.view.Drawable;
import ija.project.model.GameEntity;
import ija.project.model.enums.RobotMode;
import javafx.scene.shape.*;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import javafx.scene.transform.Translate;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a robot entity within a room environment. This class encapsulates the robot's
 * behavior, including movement, rotation, and obstacle detection.
 */
@Slf4j
public class Robot extends GameEntity implements Drawable {
    
    /**
     * The mode of operation for the robot, either AI (autonomous) or USER (manual control).
     */
    @NonNull
    private RobotMode mode = RobotMode.AI;

    /**
     * The room environment in which the robot is placed.
     */
    @NonNull
    private final Room room;

    /**
     * The graphical representation of the robot.
     */
    @NonNull
    private final Ellipse robotFrame;

    /**
     * The graphical representation of the robot's detection arc.
     */
    @NonNull
    private final Arc detectionArc;

    /**
     * The speed of the robot in units per second.
     */
    private double speedPerSecond;

    /**
     * The speed of the robot's rotation in degrees per second.
     */
    private double rotationSpeedPerSecInDegrees;

    /**
     * The degree increment for each rotation sample.
     */
    private double rotationDegreeSample;

    /**
     * The current angle of the robot in degrees.
     */
    private double robotCurrentAngle;

    /**
     * The remaining degrees the robot has to rotate to reach its target angle.
     */
    private double leftToRotate;
    
    /**
     * Flag indicating whether the robot should stop moving.
     */
    private boolean stopMovement;

    /**
     * Constructs a new Robot within a specified room environment with given parameters.
     *
     * @param environment The room environment in which the robot is placed.
     * @param robotCenterX The X coordinate of the robot's center.
     * @param robotCenterY The Y coordinate of the robot's center.
     * @param robotRadius The radius of the robot.
     * @param robotAngleInDegrees The initial angle of the robot in degrees.
     * @param detectionArcRadius The radius of the robot's detection arc.
     * @param detectionArcExtent The extent of the robot's detection arc in degrees.
     * @param rotationDegreeSample The degree increment for each rotation sample.
     * @param speedPerSecond The speed of the robot in units per second.
     * @param rotationSpeedPerSecInDegrees The speed of the robot's rotation in degrees per second.
     */
    public Robot(@NonNull Room environment,
                 double robotCenterX, double robotCenterY,
                 double robotRadius, double robotAngleInDegrees,
                 double detectionArcRadius, double detectionArcExtent,
                 double rotationDegreeSample, double speedPerSecond, double rotationSpeedPerSecInDegrees) {
        this.room = environment;

        this.robotFrame = new Ellipse();
        this.detectionArc = new Arc();
        this.detectionArc.setType(ArcType.ROUND);
        this.leftToRotate = 0;

        setSpeedPerSecond(speedPerSecond);
        setRotationSpeedPerSecInDegrees(rotationSpeedPerSecInDegrees);
        setRotationDegreeSample(rotationDegreeSample);

        setArcRadius(detectionArcRadius);
        setRobotRadius(robotRadius);

        setCenterX(robotCenterX);
        setCenterY(robotCenterY);

        setArcExtent(detectionArcExtent);
        setRobotCurrentAngle(robotAngleInDegrees);

        stopMovement();
        pause();

        this.room.notifyObservers();
    }

    /**
     * Updates the robot's state based on the elapsed time since the last update.
     *
     * @param deltaNanos The elapsed time since the last update in nanoseconds.
     */
    @Override
    public void update(long deltaNanos) {
        if (isPaused()) return;

        if (isRotating()) {
            var frameDegree = Math.abs(rotationSpeedPerSecInDegrees * deltaNanos / 1e9);
            frameDegree = Math.min(Math.abs(leftToRotate), frameDegree);
            if (leftToRotate < 0) frameDegree *= -1;
            rotateOnAngle(frameDegree);
            leftToRotate -= frameDegree;
            return;
        }

        var distance = speedPerSecond * deltaNanos / 1e9;
        if (hasDetected() || !moveOnDistance(stopMovement && mode == RobotMode.USER ? 0 : distance)) {
            if (mode == RobotMode.AI) {
                turn();
                update(deltaNanos);
            }
        }
    }

    /**
     * Moves the robot a specified distance in the direction it is currently facing.
     *
     * @param distance The distance to move the robot.
     * @return True if the robot moved successfully, false if it collided with an obstacle.
     */
    public boolean moveOnDistance(double distance) {

        var radians = Math.toRadians(-robotCurrentAngle);
        var incrementX = distance * Math.cos(radians);
        var incrementY = distance * Math.sin(radians);

        setCenterX(getCenterX() + incrementX);
        setCenterY(getCenterY() + incrementY);

        if (isOutOfRoom() || isColliding()) {
            setCenterX(getCenterX() - incrementX);
            setCenterY(getCenterY() - incrementY);
            this.room.notifyObservers();
            return false;
        }
        this.room.notifyObservers();
        return true;
    }

    /**
     * Rotates the robot by a specified degree sample, optionally in reverse direction.
     *
     * @param reverseSample If true, rotates the robot in the opposite direction.
     */
    public void turn(boolean reverseSample) {
        if (isRotating()) return;
        leftToRotate = this.rotationDegreeSample;
        if (reverseSample) leftToRotate *= -1;
    }

    /**
     * Rotates the robot by its predefined rotation degree sample.
     */
    public void turn() {
        turn(false);
    }

    /**
     * Rotates the robot on a specified angle in degrees.
     *
     * @param rotationAngleInDegrees The angle in degrees to rotate the robot.
     */
    private void rotateOnAngle(double rotationAngleInDegrees) {
        setRobotCurrentAngle(getRobotCurrentAngle() + rotationAngleInDegrees);
        //applyTransform(new Rotate(rotationAngleInDegrees, getCenterX(), getCenterY()));
        this.room.notifyObservers();
    }

    /**
     * Applies a transformation to the robot's graphical representation.
     *
     * @param transform The transformation to apply.
     */
    private void applyTransform(@NonNull Transform transform) {
        this.robotFrame.getTransforms().add(transform);
        this.detectionArc.getTransforms().add(transform);
    }

    /**
     * Checks if the robot is colliding with any obstacles in the room.
     *
     * @return True if the robot is colliding, false otherwise.
     */
    public boolean isColliding() {
        var shapes = room.getColliders();

        for (var s : shapes) {
            if (s == this.getRobotFrame()) continue;
            var res = (Path) Shape.intersect(s, this.getRobotFrame());
            if (!res.getElements().isEmpty()) return true;
        }
        return false;
    }

    /**
     * Checks if the robot's detection arc intersects with any obstacles in the room.
     *
     * @return True if an obstacle is detected, false otherwise.
     */
    public boolean hasDetected() {
        var shapes = room.getColliders();

        for (var s : shapes) {
            if (s == this.getRobotFrame()) continue;
            var res = (Path) Shape.intersect(s, this.getDetectionArc());
            if (!res.getElements().isEmpty()) return true;
        }
        return false;
    }

    /**
     * Checks if the robot is outside the boundaries of the room.
     *
     * @return True if the robot is out of the room, false otherwise.
     */
    public boolean isOutOfRoom() {
        return !room.isPointInRoom(
                getCenterX() + getRobotRadius(),
                getCenterY() + getRobotRadius()) ||
                !room.isPointInRoom(
                        getCenterX() - getRobotRadius(),
                        getCenterY() - getRobotRadius());
    }

    /**
     * Checks if the robot is currently rotating.
     *
     * @return True if the robot is rotating, false otherwise.
     */
    public boolean isRotating() {
        return Math.abs(leftToRotate) > 0.1;
    }

    /**
     * Gets the X coordinate of the center of the robot.
     *
     * @return The X coordinate of the robot's center.
     */
    public double getCenterX() {
        return this.robotFrame.getCenterX() + this.robotFrame.getTranslateX();
    }

    /**
     * Gets the Y coordinate of the center of the robot.
     *
     * @return The Y coordinate of the robot's center.
     */
    public double getCenterY() {
        return this.robotFrame.getCenterY() + this.robotFrame.getTranslateY();
    }

    /**
     * Gets the radius of the robot.
     *
     * @return The radius of the robot.
     */
    public double getRobotRadius() {
        return this.robotFrame.getRadiusX();
    }

    /**
     * Gets the radius of the robot's detection arc.
     *
     * @return The radius of the detection arc.
     */
    public double getArcRadius() {
        return this.detectionArc.getRadiusX();
    }

    /**
     * Gets the extent of the robot's detection arc in degrees.
     *
     * @return The extent of the detection arc in degrees.
     */
    public double getArcExtent() {
        return this.detectionArc.getLength();
    }

    /**
     * Sets the X coordinate of the center of the robot.
     *
     * @param x The new X coordinate of the robot's center.
     */
    public void setCenterX(double x) {
        this.robotFrame.setCenterX(x);
        this.detectionArc.setCenterX(x);
        this.room.notifyObservers();
    }

    /**
     * Sets the Y coordinate of the center of the robot.
     *
     * @param y The new Y coordinate of the robot's center.
     */
    public void setCenterY(double y) {
        this.robotFrame.setCenterY(y);
        this.detectionArc.setCenterY(y);
        this.room.notifyObservers();
    }

    /**
     * Sets the radius of the robot.
     *
     * @param radius The new radius of the robot.
     */
    public void setRobotRadius(double radius) {
        this.robotFrame.setRadiusX(radius);
        this.robotFrame.setRadiusY(radius);
        this.room.notifyObservers();
    }

    /**
     * Sets the radius of the robot's detection arc.
     *
     * @param radius The new radius of the detection arc.
     */
    public void setArcRadius(double radius) {
        this.detectionArc.setRadiusX(radius);
        this.detectionArc.setRadiusY(radius);
        this.room.notifyObservers();
    }

    /**
     * Sets the extent of the robot's detection arc in degrees.
     *
     * @param extent The new extent of the detection arc in degrees.
     */
    public void setArcExtent(double extent) {
        this.detectionArc.setLength(extent);
        this.detectionArc.setStartAngle(getRobotCurrentAngle() - extent / 2);
        this.room.notifyObservers();
    }

    /**
     * Sets the current angle of the robot in degrees.
     *
     * @param angle The new angle of the robot in degrees.
     */
    public void setRobotCurrentAngle(double angle) {
        this.robotCurrentAngle = angle;
        setArcExtent(getArcExtent());
        this.room.notifyObservers();
    }

    /**
     * Stops the robot's movement.
     * This method sets the flag to stop the robot from moving further.
     */
    public void stopMovement() {
        this.stopMovement = true;
    }

    /**
     * Starts the robot's movement.
     * This method clears the flag that stops the robot, allowing it to move again.
     */
    public void startMovement() {
        this.stopMovement = false;
    }

    /**
     * Toggles the robot's movement state.
     * If the robot is currently moving, it will stop; if it is stopped, it will start moving.
     */
    public void toggleMovement() {
        this.stopMovement = !this.stopMovement;
    }

    /**
     * Returns a list of shapes representing the robot and its detection arc.
     * This method is used for drawing the robot on the UI.
     *
     * @return A list of shapes representing the robot and its detection arc.
     */
    @Override
    public List<Shape> getDrawableShapes() {
        return new ArrayList<>(List.of(getRobotFrame(), getDetectionArc()));
    }

    /**
     * Gets the graphical representation of the robot.
     *
     * @return The Ellipse shape representing the robot.
     */
    public Ellipse getRobotFrame() {
        return robotFrame;
    }

    /**
     * Gets the graphical representation of the robot's detection arc.
     *
     * @return The Arc shape representing the robot's detection arc.
     */
    public Arc getDetectionArc() {
        return detectionArc;
    }

    /**
     * Gets the current mode of operation of the robot.
     *
     * @return The mode of operation, either AI (autonomous) or USER (manual control).
     */
    public RobotMode getMode() {
        return mode;
    }

    /**
     * Gets the room environment in which the robot is placed.
     *
     * @return The room environment.
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Gets the speed of the robot in units per second.
     *
     * @return The speed of the robot.
     */
    public double getSpeedPerSecond() {
        return speedPerSecond;
    }

    /**
     * Gets the speed of the robot's rotation in degrees per second.
     *
     * @return The rotation speed of the robot.
     */
    public double getRotationSpeedPerSecInDegrees() {
        return rotationSpeedPerSecInDegrees;
    }

    /**
     * Gets the degree increment for each rotation sample.
     *
     * @return The rotation degree sample.
     */
    public double getRotationDegreeSample() {
        return rotationDegreeSample;
    }

    /**
     * Gets the current angle of the robot in degrees.
     *
     * @return The current angle of the robot.
     */
    public double getRobotCurrentAngle() {
        return robotCurrentAngle;
    }

    /**
     * Sets the mode of operation for the robot.
     *
     * @param mode The new mode of operation.
     */
    public void setMode(RobotMode mode) {
        this.mode = mode;
        this.room.notifyObservers();
    }

    /**
     * Sets the speed of the robot in units per second.
     *
     * @param speedPerSecond The new speed of the robot.
     */
    public void setSpeedPerSecond(double speedPerSecond) {
        this.speedPerSecond = speedPerSecond;
    }

    /**
     * Sets the speed of the robot's rotation in degrees per second.
     *
     * @param rotationSpeedPerSecInDegrees The new rotation speed of the robot.
     */
    public void setRotationSpeedPerSecInDegrees(double rotationSpeedPerSecInDegrees) {
        this.rotationSpeedPerSecInDegrees = rotationSpeedPerSecInDegrees;
    }

    /**
     * Sets the degree increment for each rotation sample.
     *
     * @param rotationDegreeSample The new rotation degree sample.
     */
    public void setRotationDegreeSample(double rotationDegreeSample) {
        this.rotationDegreeSample = rotationDegreeSample;
    }
}
