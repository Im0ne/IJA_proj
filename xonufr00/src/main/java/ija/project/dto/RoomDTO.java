/**
 * @file RoomDTO.java
 * @author Ivan Onufriienko
 */
package ija.project.dto;

import ija.project.model.impl.Room;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a data transfer object (DTO) for a Room.
 * This class is used to transfer the state of a Room without exposing the Room itself.
 * It encapsulates the dimensions of the Room and lists of Blocks and Robots in the Room.
 */
public class RoomDTO implements DtoToEntityBuilder<Room> {

    /**
     * Dimensions of the Room.
     */
    private final double width, height;

    /**
     * List of Blocks in the Room.
     */
    @NonNull
    private final List<BlockDTO> blocks;

    /**
     * List of Robots in the Room.
     */
    @NonNull
    private final List<RobotDTO> robots;

    /**
     * Default constructor initializing the RoomDTO with zero values for its dimensions and empty lists of Blocks and Robots.
     */
    public RoomDTO() {
        this.width = 0.0;
        this.height = 0.0;
        this.blocks = new ArrayList<>();
        this.robots = new ArrayList<>();
    }

     /**
     * Constructs a RoomDTO from a given Room.
     * The Room's dimensions, blocks, and robots are used to initialize the DTO.
     *
     * @param room The Room from which to create the DTO. Must not be null.
     */
    public RoomDTO(@NonNull Room room) {
        this.width = room.getWidth();
        this.height = room.getHeight();
        this.blocks = room.getBlocks().stream().map(BlockDTO::new).collect(Collectors.toList());
        this.robots = room.getRobots().stream().map(RobotDTO::new).collect(Collectors.toList());
    }

    /**
     * Gets the width of the Room.
     *
     * @return The width.
     */
    public double getWidth() {
        return width;
    }

    /**
     * Gets the height of the Room.
     *
     * @return The height.
     */
    public double getHeight() {
        return height;
    }

    /**
     * Gets the list of Blocks in the Room.
     *
     * @return The list of Blocks.
     */
    public List<BlockDTO> getBlocks() {
        return blocks;
    }
    
    /**
     * Gets the list of Robots in the Room.
     *
     * @return The list of Robots.
     */
    public List<RobotDTO> getRobots() {
        return robots;
    }

    /**
     * Creates and returns a Room instance based on the DTO's state.
     * The new Room contains blocks and robots as specified by the DTO.
     *
     * @return A new Room instance with properties matching those of the DTO.
     */
    @NonNull
    public Room buildEntity() {
        var room = new Room(this.width, this.height);
        for (var bDto : this.blocks) {
            room.addBlock(bDto.buildBlock(room));
        }
        for (var rDto : this.robots) {
            room.addRobot(rDto.buildEntity(room));
        }
        return room;
    }
}
