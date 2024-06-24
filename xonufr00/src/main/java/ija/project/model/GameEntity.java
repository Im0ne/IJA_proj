/**
 * @file GameEntity.java
 * @author Ivan Burlustkyi
 */
package ija.project.model;

import ija.project.observer.Observable;
import ija.project.observer.Observer;
import lombok.NonNull;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a generic game entity that can be observed for changes, updated, and paused or resumed.
 * This abstract class provides the foundational behavior for game entities in a simulation, including
 * the ability to manage observers, pause and resume the entity's state, and notify observers of changes.
 */
@ToString
public abstract class GameEntity implements Observable, UpdatableEntity, PausableEntity {
    
    /**
     * A set of observers that are subscribed to receive updates about changes to this game entity.
     */
    private final Set<Observer> observers = new HashSet<>();
    
    /**
     * A flag indicating whether this game entity is currently paused.
     */
    private boolean pause;

    /**
     * Adds an observer to this game entity.
     * 
     * @param o The observer to be added. Must not be null.
     */
    @Override
    public void addObserver(@NonNull Observer o) {
        observers.add(o);
    }

    /**
     * Removes an observer from this game entity.
     * 
     * @param o The observer to be removed. Must not be null.
     */
    @Override
    public void removeObserver(@NonNull Observer o) {
        observers.remove(o);
    }

    /**
     * Notifies all observers that this game entity has been updated.
     */
    @Override
    public void notifyObservers() {
        for (var o : observers) {
            o.update(this);
        }
    }

    /**
     * Checks whether this game entity is currently paused.
     * 
     * @return True if the entity is paused, false otherwise.
     */
    @Override
    public boolean isPaused() {
        return pause;
    }

    /**
     * Resumes the activity of this game entity, if it was paused.
     */
    @Override
    public void resume() {
        pause = false;
    }

    /**
     * Pauses the activity of this game entity.
     */
    @Override
    public void pause() {
        pause = true;
    }
}
