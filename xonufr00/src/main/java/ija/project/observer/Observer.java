/**
 * @file Observer.java
 * @author Ivan Onufriienko
 */
package ija.project.observer;

import lombok.NonNull;

/**
 * Represents an observer in the Observer design pattern.
 * Observers are notified by observables (subjects) when their state changes.
 */
public interface Observer {
    /**
     * Called by an observable to notify this observer about a state change.
     *
     * @param o The observable that has changed state.
     */
    void update(Observable o);
}
