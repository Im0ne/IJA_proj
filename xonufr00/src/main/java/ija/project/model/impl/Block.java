/**
 * @file Block.java
 * @author Ivan Burlustkyi
 */
package ija.project.model.impl;

import ija.project.ui.view.Drawable;
import ija.project.model.GameEntity;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Represents a block within a room in the game environment.
 * Blocks are static obstacles that can be placed in a room. They are drawable entities
 * and can be interacted with by other game entities.
 */
@Slf4j
public class Block extends GameEntity implements Drawable {
    /**
     * The room in which the block is placed.
     */
    @NonNull
    private final Room room;
    
    /**
     * The dimensions and position of the block.
     */
    @NonNull
    private final Rectangle blockFrame;

    /**
     * Constructs a new Block within a specified room with given dimensions and position.
     *
     * @param room   The room in which the block is placed. Must not be null.
     * @param x      The X coordinate of the block's top-left corner.
     * @param y      The Y coordinate of the block's top-left corner.
     * @param width  The width of the block.
     * @param height The height of the block.
     */
    public Block(@NonNull Room room,
                    double x, double y,
                    double width, double height
                 ) {
        this.room = room;
        this.blockFrame = new Rectangle();

        setLeftTopX(x);
        setLeftTopY(y);
        setHeight(height);
        setWidth(width);

        this.room.notifyObservers();
        pause();
    }

    /**
     * Updates the state of the block.
     *
     * @param deltaNanos The time passed since the last update.
     */
    @Override
    public void update(long deltaNanos) {
        if (isPaused()) return;
    }

    /**
     * Gets the list of shapes that represent the block.
     *
     * @return The list of shapes.
     */
    @Override
    public List<Shape> getDrawableShapes() {
        return List.of(blockFrame);
    }

    /**
     * Gets the room in which the block is placed.
     *
     * @return The room.
     */
    public Room getRoom() {
        return room;
    }

    /**
     * Gets the frame of the block.
     *
     * @return The frame.
     */
    public Rectangle getBlockFrame() {
        return blockFrame;
    }

    /**
     * Gets the X coordinate of the block's top-left corner.
     *
     * @return The X coordinate.
     */
    public double getLeftTopX() {
        return this.blockFrame.getX();
    }

    /**
     * Sets the X coordinate of the block's top-left corner.
     *
     * @param x The X coordinate.
     */
    public void setLeftTopX(double x) {
        if (getLeftTopX() == x) return;
        this.blockFrame.setX(x);
        notifyObservers();
    }

    /**
     * Gets the Y coordinate of the block's top-left corner.
     *
     * @return The Y coordinate.
     */
    public double getLeftTopY() {
        return this.blockFrame.getY();
    }

    /**
     * Sets the Y coordinate of the block's top-left corner.
     *
     * @param y The Y coordinate.
     */
    public void setLeftTopY(double y) {
        if (getLeftTopY() == y) return;
        this.blockFrame.setY(y);
        notifyObservers();
    }

    /**
     * Gets the width of the block.
     *
     * @return The width.
     */
    public double getWidth() {
        return this.blockFrame.getWidth();
    }

    /**
     * Sets the width of the block.
     *
     * @param width The width.
     */
    public void setWidth(double width) {
        if (getWidth() == width) return;
        this.blockFrame.setWidth(width);
        notifyObservers();
    }

    /**
     * Gets the height of the block.
     *
     * @return The height.
     */
    public double getHeight() {
        return this.blockFrame.getHeight();
    }

    /**
     * Sets the height of the block.
     *
     * @param height The height.
     */
    public void setHeight(double height) {
        if (getHeight() == height) return;
        this.blockFrame.setHeight(height);
        notifyObservers();
    }
}
