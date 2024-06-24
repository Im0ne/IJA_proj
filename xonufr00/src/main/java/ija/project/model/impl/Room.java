/**
 * @file Room.java
 * @author Ivan Burlustkyi
 */
package ija.project.model.impl;

import ija.project.model.GameEntity;
import javafx.scene.shape.Shape;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Represents a room environment in a simulation. This class encapsulates the properties and behaviors
 * of a room, including its dimensions, and the blocks and robots contained within it.
 * 
 * The room is responsible for managing the state of its contained entities (blocks and robots),
 * including updating their states, adding new entities, and checking for collisions or entities
 * being out of bounds.
 */
@Slf4j
public class Room extends GameEntity {
    
    /**
     * A set of blocks contained within the room.
     */
    @NonNull
    private final Set<Block> blocks;

    /**
     * A set of robots contained within the room.
     */
    @NonNull
    private final Set<Robot> robots;

    /**
     * The dimensions of the room.
     */
    private final double width, height;

    /**
     * Constructs a new Room with specified dimensions.
     *
     * @param w The width of the room.
     * @param h The height of the room.
     */
    public Room(double w, double h) {
        blocks = new HashSet<>();
        robots = new HashSet<>();
        this.width = w;
        this.height = h;
    }

    /**
     * Updates the state of all robots contained within the room.
     *
     * @param delta The time in nanoseconds since the last update.
     */
    @Override
    public void update(long delta) {
        for (var r : getRobots().stream().toList()) {
            r.update(delta);// update the state of robots
        }
    }

    /**
     * Adds a robot to the room.
     *
     * @param robot The robot to be added to the room.
     */
    public void addRobot(@NonNull Robot robot) {
        robots.add(robot);
    }

    /**
     * Adds a block to the room.
     *
     * @param block The block to be added to the room.
     */
    public void addBlock(@NonNull Block block) {
        blocks.add(block);
    }

    /**
     * Validates the state of the room, checking if any robots are colliding or out of bounds.
     *
     * @return true if the room's state is valid, otherwise false.
     */
    public boolean isInValidState() {
        for (var r : robots) {
            if (r.isColliding() || r.isOutOfRoom())
                return false;
        }
        return true;
    }

    /**
     * Checks if a given point is within the bounds of the room.
     *
     * @param x The x-coordinate of the point.
     * @param y The y-coordinate of the point.
     * @return true if the point is within the room, otherwise false.
     */
    public boolean isPointInRoom(double x, double y) {
        return !(x < 0) && !(y < 0) && !(x > width) && !(y > height);
    }

    /**
     * Retrieves a list of shapes representing the colliders (blocks and robots) within the room.
     *
     * @return A list of shapes representing the colliders in the room.
     */
    public List<Shape> getColliders() {
        return Stream.concat(
                        getRobots().stream().map(Robot::getRobotFrame),
                        getBlocks().stream().map(Block::getBlockFrame))
                .toList();
    }

    /**
     * Gets the set of blocks contained within the room.
     *
     * @return The set of blocks.
     */
    public Set<Block> getBlocks() {
        return blocks;
    }

    /**
     * Gets the set of robots contained within the room.
     *
     * @return The set of robots.
     */
    public Set<Robot> getRobots() {
        return robots;
    }

    /**
     * Gets the width of the room.
     *
     * @return The width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the room.
     *
     * @return The height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Notifies all robots and blocks in the room that their state has changed.
     */
    @Override
    public void notifyObservers() {
        for (var r : getRobots()) {
            r.notifyObservers();
        }
        for (var b : getBlocks()) {
            b.notifyObservers();
        }
    }

    /**
     * Resumes the simulation within the room, resuming all contained robots and blocks.
     */
    @Override
    public void resume() {
        for (var r : getRobots()) {
            r.resume();
        }
        for (var b : getBlocks()) {
            b.resume();
        }
        super.resume();
    }

    /**
     * Pauses the simulation within the room, pausing all contained robots and blocks.
     */
    @Override
    public void pause() {
        for (var r: getRobots()) {
            r.pause();
        }
        for (var b: getBlocks()) {
            b.pause();
        }
        super.pause();
    }

}
