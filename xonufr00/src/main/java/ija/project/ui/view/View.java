/**
 * @file View.java
 * @author Ivan Burlustkyi
 */
package ija.project.ui.view;

import ija.project.model.GameEntity;
import javafx.scene.Node;
import lombok.NonNull;

import java.util.List;

/**
 * Represents a view of a game entity in the application's user interface.
 * This interface is implemented by classes that represent the visual representation of a game entity
 * within the application's user interface.
 */
public interface View extends Drawable {
    /**
     * Returns the game entity that this view is representing.
     *
     * @return The game entity that this view is representing.
     */
    GameEntity getEntity();
}
