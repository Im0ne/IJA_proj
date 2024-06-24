/**
 * @file RoomView.java
 * @author Ivan Burlustkyi
 */
package ija.project.ui.view.impl;

import ija.project.model.GameEntity;
import ija.project.model.impl.Room;
import ija.project.observer.Observable;
import ija.project.observer.Observer;
import ija.project.ui.controller.AppViewController;
import ija.project.ui.view.View;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Shape;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Represents the visual representation of a Room entity in the application's user interface.
 * This class is responsible for managing how a Room entity is displayed within the application,
 * including the rendering of its shape and handling updates to its state. It implements the Observer
 * interface to react to changes in the Room entity and the View interface to provide a consistent
 * method of displaying the entity.
 */
@Slf4j
public class RoomView implements Observer, View {

    /**
     * The Room entity this view is responsible for displaying.
     */
    @NonNull
    private final Room room;

    /**
     * The application's main controller which this view interacts with for handling user actions
     * and application state changes.
     */
    @NonNull
    private final AppViewController controller;

    /**
     * Constructs a new RoomView with the specified controller and room.
     * 
     * @param controller The main application controller.
     * @param room The Room entity to be displayed by this view.
     */
    public RoomView(@NonNull AppViewController controller, @NonNull Room room) {
        this.controller = controller;
        this.room = room;
    }

    /**
     * Updates the view based on changes to the observed Room entity.
     * This method is called when the Room entity, which this view is observing, notifies its observers
     * of a change in its state.
     * 
     * @param o The observable object that has changed.
     */
    @Override
    public void update(@NonNull Observable o) {

    }

    /**
     * Returns the Room entity associated with this view.
     * 
     * @return The Room entity this view is displaying.
     */
    @Override
    public GameEntity getEntity() {
        return room;
    }

    /**
     * Returns a list of drawable shapes that represent the Room entity in the UI.
     * This method is used to retrieve the visual components that make up the Room entity
     * for rendering in the application's user interface.
     * 
     * @return A list of Shape objects that visually represent the Room.
     */
    @Override
    public List<Shape> getDrawableShapes() {
        return List.of();
    }
}
