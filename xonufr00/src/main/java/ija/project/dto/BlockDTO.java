/**
 * @file BlockDTO.java
 * @author Ivan Onufriienko
 */
package ija.project.dto;

import ija.project.model.impl.Block;
import ija.project.model.impl.Room;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

/**
 * Represents a data transfer object (DTO) for a Block.
 * This class is used to transfer the state of a Block without exposing the Block itself.
 * It encapsulates the position and dimensions of a Block.
 */
public class BlockDTO {

    /**
     * The x and y coordinates and dimensions of the Block.
     */
    private final double x, y, width, height;

    /**
     * Default constructor initializing the BlockDTO with zero values for its coordinates and dimensions.
     */
    public BlockDTO() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    /**
     * Constructor initializing the BlockDTO with the given Block.
     * @param block The Block to initialize the BlockDTO with.
     */
    public BlockDTO(@NonNull Block block) {
        this.width = block.getBlockFrame().getWidth();
        this.height = block.getBlockFrame().getHeight();
        this.x = block.getBlockFrame().getX();
        this.y = block.getBlockFrame().getY();
    }

    /**
     * Gets the X coordinate of the Block.
     *
     * @return The X coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the Y coordinate of the Block.
     *
     * @return The Y coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the width of the Block.
     *
     * @return The width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the Block.
     *
     * @return The height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Creates and returns a Block instance based on the DTO's state.
     * The new Block is associated with the specified Room.
     *
     * @param room The Room in which the Block is located. Must not be null.
     * @return A new Block instance with properties matching those of the DTO.
     */
    protected Block buildBlock(@NonNull Room room) {
        return new Block(room, x, y, width, height);
    }
}
