/**
 * @file Drawable.java
 * @author Ivan Burlustkyi
 */
package ija.project.ui.view;

import javafx.scene.shape.Shape;

import java.util.List;

/**
 * Represents a drawable entity in the application's user interface.
 * This interface is implemented by classes that represent entities that can be drawn on the screen.
 */
public interface Drawable {
    /**
     * Returns a list of shapes that represent the drawable entity.
     * 
     * @return A list of shapes that represent the drawable entity.
     */
    List<Shape> getDrawableShapes();
}
