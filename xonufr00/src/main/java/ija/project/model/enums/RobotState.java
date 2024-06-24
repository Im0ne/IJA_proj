/**
 * @file RobotState.java
 * @author Ivan Burlustkyi
 */
package ija.project.model.enums;

/**
 * Enumerates the possible states of a robot with respect to obstacle detection.
 * A robot can either detect an obstacle in its path or have a clear path ahead.
 */
public enum RobotState {
    
    /**
     * Indicates that the robot has detected an obstacle in its path.
     * The robot may need to stop or change direction to avoid a collision.
     */
    DETECTED_OBSTACLE,
    
    /**
     * Indicates that the robot has not detected any obstacles in its path.
     * The robot can continue moving forward without changing direction.
     */
    NO_OBSTACLE
}
