/**
 * @file PausableEntity.java
 * @author Ivan Burlustkyi
 */
package ija.project.model;

/**
 * Defines the behavior for entities that can be paused and resumed within the game.
 * This interface provides methods to check if an entity is paused, to pause it, to resume it,
 * and to toggle its pause state.
 */
public interface PausableEntity {

    /**
     * Checks if the entity is currently paused.
     *
     * @return True if the entity is paused, false otherwise.
     */
    boolean isPaused();

    /**
     * Resumes the entity if it was paused.
     */
    void resume();

    /**
     * Pauses the entity.
     */
    void pause();

    /**
     * Toggles the pause state of the entity.
     * If the entity is currently paused, it will be resumed; if it is active, it will be paused.
     */
    default void togglePause() {
        if (isPaused()) {
            resume();
        } else {
            pause();
        }
    }
}
