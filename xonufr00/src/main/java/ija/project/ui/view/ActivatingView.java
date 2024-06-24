/**
 * @file ActivatingView.java
 * @author Ivan Burlustkyi
 */
package ija.project.ui.view;

/**
 * Represents a view that can be activated to show additional information.
 * This interface provides methods to activate and deactivate the view, as well as check if the view is currently activated.
 */
public interface ActivatingView extends View {
    /**
     * Activates current view, gives more info about view
     */
    void activateView();

    /**
     * Deactivates view, hides additional info
     */
    void deactivateView();

    /**
     * Checks if current view is activated
     * 
     * @return true if current view is showing additional info, otherwise false
     */
    boolean isActivatedView();
}
