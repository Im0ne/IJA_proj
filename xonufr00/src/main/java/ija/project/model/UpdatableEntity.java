/**
 * @file UpdatableEntity.java
 * @author Ivan Burlustkyi
 */
package ija.project.model;

/**
 * Defines the behavior for entities that can be updated over time within the game.
 * This interface provides a method to update the state of an entity based on the elapsed time.
 */
public interface UpdatableEntity {

    /**
     * Updates the state of the entity based on the elapsed time since the last update.
     *
     * @param deltaNanos The elapsed time in nanoseconds since the last update.
     */
    void update(long deltaNanos);
}
