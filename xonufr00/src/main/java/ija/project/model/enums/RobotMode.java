/**
 * @file RobotMode.java
 * @author Ivan Burlustkyi
 */
package ija.project.model.enums;

/**
 * Enumerates the possible modes of operation for a robot.
 * Robots can operate autonomously (AI) or be controlled by a user (USER).
 */
public enum RobotMode {
    /**
     * Represents the autonomous operation mode of the robot.
     * In this mode, the robot makes decisions and moves based on its AI algorithms.
     */
    AI,
    
    /**
     * Represents the user-controlled operation mode of the robot.
     * In this mode, the robot's movements and actions are directly controlled by the user.
     */
    USER
}
