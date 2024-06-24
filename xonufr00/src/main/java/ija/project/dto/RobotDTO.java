/**
 * @file RobotDTO.java
 * @author Ivan Onufriienko
 */
package ija.project.dto;

import ija.project.model.impl.Robot;
import ija.project.model.impl.Room;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents a data transfer object (DTO) for a Robot.
 * This class is used to transfer the state of a Robot without exposing the Robot itself.
 * It encapsulates the robot's position, movement, and detection parameters.
 */
public class RobotDTO {

    /**
     * The x and y coordinates and radius of the Robot.
     */
    private final double robotCenterX, robotCenterY, robotRadius;

    /**
     * The speed and rotation speed of the Robot.
     */
    private final double speedPerSecond, rotationSpeedPerSecInDegrees;

    /**
     * The current angle of the Robot.
     */
    private final double rotationDegreeSample, robotCurrentAngle;

    /**
     * The detection arc radius and extent of the Robot.
     */
    private final double detectionArcRadius, detectionArcExtent;

    /**
     * Default constructor initializing the RobotDTO with zero values for its coordinates and dimensions.
     */
    public RobotDTO() {
        this.robotCenterX = 0;
        this.robotCenterY = 0;
        this.robotRadius = 0;
        this.speedPerSecond = 0;
        this.rotationSpeedPerSecInDegrees = 0;
        this.rotationDegreeSample = 0;
        this.robotCurrentAngle = 0;
        this.detectionArcRadius = 0;
        this.detectionArcExtent = 0;
    }

    /**
     * Constructs a RobotDTO from a given Robot.
     * The Robot's state is used to initialize the DTO.
     *
     * @param robot The Robot from which to create the DTO. Must not be null.
     */
    public RobotDTO(@NonNull Robot robot) {
        robotCenterX = robot.getCenterX();
        robotCenterY = robot.getCenterY();
        robotRadius = robot.getRobotRadius();

        speedPerSecond = robot.getSpeedPerSecond();
        rotationSpeedPerSecInDegrees = robot.getRotationSpeedPerSecInDegrees();
        rotationDegreeSample = robot.getRotationDegreeSample();
        robotCurrentAngle = robot.getRobotCurrentAngle();

        detectionArcExtent = robot.getArcExtent();
        detectionArcRadius = robot.getArcRadius();
    }

    /**
     * Gets the X coordinate of the Robot.
     *
     * @return The X coordinate.
     */
    public double getRobotCenterX() {
        return robotCenterX;
    }

    /**
     * Gets the Y coordinate of the Robot.
     *
     * @return The Y coordinate.
     */
    public double getRobotCenterY() {
        return robotCenterY;
    }

    /**
     * Gets the radius of the Robot.
     *
     * @return The radius.
     */
    public double getRobotRadius() {
        return robotRadius;
    }

    /**
     * Gets the speed of the Robot.
     *
     * @return The speed.
     */
    public double getSpeedPerSecond() {
        return speedPerSecond;
    }

    /**
     * Gets the rotation speed of the Robot.
     *
     * @return The rotation speed.
     */
    public double getRotationSpeedPerSecInDegrees() {
        return rotationSpeedPerSecInDegrees;
    }

    /**
     * Gets the rotation degree sample of the Robot.
     *
     * @return The rotation degree sample.
     */
    public double getRotationDegreeSample() {
        return rotationDegreeSample;
    }

    /**
     * Gets the current angle of the Robot.
     *
     * @return The current angle.
     */
    public double getRobotCurrentAngle() {
        return robotCurrentAngle;
    }

    /**
     * Gets the detection arc radius of the Robot.
     *
     * @return The detection arc radius.
     */
    public double getDetectionArcRadius() {
        return detectionArcRadius;
    }

    /**
     * Gets the detection arc extent of the Robot.
     *
     * @return The detection arc extent.
     */
    public double getDetectionArcExtent() {
        return detectionArcExtent;
    }

    /**
     * Creates and returns a Robot instance based on the DTO's state.
     * The new Robot is associated with the specified Room.
     *
     * @param room The Room in which the Robot is located. Must not be null.
     * @return A new Robot instance with properties matching those of the DTO.
     */
    protected Robot buildEntity(@NonNull Room room) {
        return new Robot(room,
                this.robotCenterX, this.robotCenterY,
                this.robotRadius, this.robotCurrentAngle,
                this.detectionArcRadius, this.detectionArcExtent,
                this.rotationDegreeSample, this.speedPerSecond,
                this.rotationSpeedPerSecInDegrees);
    }
}
