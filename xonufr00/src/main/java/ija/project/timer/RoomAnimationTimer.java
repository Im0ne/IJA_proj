/**
 * @file RoomAnimationTimer.java
 * @author Ivan Onufriienko
 */
package ija.project.timer;

import ija.project.dto.RoomDTO;
import ija.project.model.PausableEntity;
import ija.project.model.impl.Room;
import ija.project.ui.controller.AppViewController;
import javafx.animation.AnimationTimer;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the animation and timing for a Room entity in a simulation environment.
 * This class extends the JavaFX AnimationTimer, providing a way to update and render
 * a simulation room at a consistent rate. It also implements the PausableEntity interface,
 * allowing the simulation to be paused and resumed.
 */
public class RoomAnimationTimer extends AnimationTimer implements PausableEntity {
    private static final org.slf4j.Logger log
            = org.slf4j.LoggerFactory.getLogger(RoomAnimationTimer.class);

    /**
     * Indicates whether the simulation is currently paused.
     */
    private boolean paused;

    /**
     * The time at which the last frame was rendered.
     */
    private long lastTimeNanos;

    /**
     * The current game time in nanoseconds.
     */
    @NonNull
    private final LongProperty gameTimeNanos;

    /**
     * The current game time in seconds.
     */
    @NonNull
    private final StringProperty gameTimeSecs;

    /**
     * The Room entity that is being animated.
     */
    @NonNull
    private Room room;

    /**
     * A list of saved states of the Room entity at different points in time.
     */
    @NonNull
    private final List<RoomDTO> states;

    /**
     * Constructs a new RoomAnimationTimer for a specified Room entity.
     *
     * @param room The Room entity to animate. Must not be null.
     */
    public RoomAnimationTimer(@NonNull Room room) {
        this.gameTimeNanos = new SimpleLongProperty();
        this.gameTimeSecs = new SimpleStringProperty();
        this.lastTimeNanos = -1;
        this.room = room;
        this.paused = true;
        this.states = new ArrayList<>();
        setGameTimeNanos(0);
    }

    /**
     * Checks whether the simulation is currently paused.
     *
     * @return True if the simulation is paused, false otherwise.
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Pauses the simulation.
     */
    public void pause() {
        paused = true;
        this.room.pause();
    }

    /**
     * Resumes the simulation.
     */
    public void resume() {
        paused = false;
        this.room.resume();
    }

    /**
     * Toggles the pause state of the simulation.
     * If the simulation is currently paused, it will be resumed; if it is active, it will be paused.
     */
    public void saveState() {
        var sec = getGameTimeNanos() / 1e9;
        if (states.size() < sec) {
            var roomDto = new RoomDTO(this.room);
            states.add(roomDto);
            log.info("State saved on {}", sec);
        }

    }

    /**
     * Sets the simulation state to a specified point in time.
     * The simulation will be set to the state saved at the specified time, if available.
     *
     * @param sec The time in seconds to which the simulation should be set.
     */
    public void setStateToSec(int sec) {
        int realSec = Math.min(sec, states.size() - 1);
        if (realSec<0 || states.size() - 1 == sec) return;

        if (realSec < sec) {
            log.info("Here I am: {}", this.getGameTimeNanos());
            setGameTimeNanos(this.getGameTimeNanos());
            return;
        }

        var newState = states.get(realSec);
        while (states.size() > realSec) {
            states.remove(states.size() - 1);
        }

        this.room = newState.buildEntity();
        setGameTimeNanos((long) (realSec * 1e9));
    }

    /**
     * Handles the animation and timing of the simulation.
     * This method is called by the JavaFX AnimationTimer at a consistent rate.
     *
     * @param now The current time in nanoseconds.
     */
    @Override
    public void handle(long now) {
        if (this.lastTimeNanos < 0) {
            this.lastTimeNanos = now;
            return;
        }

        long deltaNanos = now - lastTimeNanos;


        room.update(deltaNanos);

        if (!isPaused()) {
            setGameTimeNanos(getGameTimeNanos() + deltaNanos);
            saveState();
        }

        this.lastTimeNanos = now;
    }

    /**
     * Gets the property representing the current game time in nanoseconds.
     *
     * @return The property.
     */
    public LongProperty gameTimeNanosProperty() {
        return gameTimeNanos;
    }

    /**
     * Gets the property representing the current game time in seconds.
     *
     * @return The property.
     */
    public StringProperty gameTimeSecsProperty() {
        return gameTimeSecs;
    }

    /**
     * Gets the current game time in nanoseconds.
     *
     * @return The current game time.
     */
    public long getGameTimeNanos() {
        return gameTimeNanos.get();
    }

    /**
     * Sets the current game time in nanoseconds.
     *
     * @param nanos The new game time.
     */
    public void setGameTimeNanos(long nanos) {
        gameTimeNanos.set(nanos);
        int sec = (int) (nanos / 1e9);
        gameTimeSecs.set(Integer.valueOf(sec).toString());
        //log.info("WTF: {}", gameTimeSecs.get());
    }

    /**
     * Gets the Room entity that is being animated.
     *
     * @return The Room entity.
     */
    @NonNull
    public Room getRoom() {
        return room;
    }
}
