/**
 * @file Observable.java
 * @author Ivan Onufriienko
 */
package ija.project.observer;

/**
 * Defines the behavior for observable entities within the game.
 * This interface allows entities to be observed by other objects, typically for the purpose of
 * reacting to changes in the observable entity's state.
 */
public interface Observable {

    /**
     * Adds an observer to this observable entity.
     * The observer will be notified of changes to the entity's state.
     *
     * @param o The observer to add.
     */
    void addObserver(Observer o);

    /**
     * Removes an observer from this observable entity.
     * The observer will no longer be notified of changes to the entity's state.
     *
     * @param o The observer to remove.
     */
    void removeObserver(Observer o);

    /**
     * Notifies all registered observers of a change in the entity's state.
     */
    void notifyObservers();
}
