/**
 * @file BlockView.java
 * @author Ivan Burlustkyi
 */
package ija.project.ui.view.impl;

import ija.project.model.GameEntity;
import ija.project.model.impl.Block;
import ija.project.observer.Observable;
import ija.project.observer.Observer;
import ija.project.ui.controller.AppViewController;
import ija.project.ui.controller.BlockInfoViewController;
import ija.project.ui.view.ActivatingView;
import ija.project.ui.view.View;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Represents a view for a block entity in the game environment.
 * This class is responsible for managing the visual representation of a block entity
 * within the application's graphical user interface. It allows users to view and edit
 * the properties of a selected block, such as its dimensions and position.
 */
public class BlockView implements Observer, ActivatingView {
    private static final org.slf4j.Logger log
            = org.slf4j.LoggerFactory.getLogger(BlockView.class);
    /**
     * The color of the block.
     */
    private final static Color BLOCK_COLOR = Color.GRAY;

    /**
     * The application view controller.
     */
    @NonNull
    private final AppViewController appViewController;

    /**
     * The block entity associated with this view.
     */
    @NonNull
    private final Block block;

    /**
     * Constructs a new BlockView with the specified application view controller and block entity.
     *
     * @param appViewController The application view controller. Must not be null.
     * @param block             The block entity. Must not be null.
     */
    public BlockView(
            @NonNull AppViewController appViewController,
            @NonNull Block block) {
        this.appViewController = appViewController;
        this.block = block;

        configureMouseListener();
        configureKeyboardListener();
        makeDraggable();


        lowlight();
        update(null);
    }

    /**
     * Updates the view to reflect the state of the block entity.
     *
     * @param o The observable object that triggered the update.
     */
    @Override
    public void update(Observable o) {
        this.block.getBlockFrame().setFill(BLOCK_COLOR);
    }

    /**
     * Activates the view, making it the active block in the application.
     */
    public void activateView() {
        this.appViewController.setActiveBlock(this);
        highlight();
        this.block.getBlockFrame().requestFocus();
    }

    /**
     * Deactivates the view, making it no longer the active block in the application.
     */
    public void deactivateView() {
        lowlight();
        this.appViewController.setActiveBlock(null);
    }

    /**
     * Checks if the view is currently activated.
     *
     * @return True if the view is activated, false otherwise.
     */
    public boolean isActivatedView() {
        return (this.appViewController.getActiveBlock() == this);
    }

    /**
     * Highlights the block, making it more visible in the application.
     */
    public void highlight() {
        this.block.getBlockFrame().setStroke(Color.RED);
    }

    /**
     * Lowers the block's visibility, making it less prominent in the application.
     */
    public void lowlight() {
        this.block.getBlockFrame().setStroke(Color.BLACK);
    }

    /**
     * Configures the mouse listener for the block entity.
     */
    private void configureMouseListener() {
        block.getBlockFrame().addEventHandler(
                MouseEvent.MOUSE_CLICKED, e -> {
                    if (e.getClickCount() > 0) {
                        if (isActivatedView()) deactivateView(); else activateView();
                    }
                    log.info("Clicked the block time = {}", e.getClickCount());
                }
        );
    }

    /**
     * Makes the block draggable within the application.
     */
    private void makeDraggable() {
        block.getBlockFrame().setOnMouseDragged(mouseEvent -> {
            block.setLeftTopY(mouseEvent.getY() - block.getHeight() / 2);
            block.setLeftTopX(mouseEvent.getX() - block.getWidth() / 2);
        });
    }

    /**
     * Configures the keyboard listener for the block entity.
     */
    private void configureKeyboardListener() {
        block.getBlockFrame().addEventHandler(
                KeyEvent.KEY_PRESSED, e -> {
                    log.info("Pressed key edit for the block frame: {}", e.getCode());
                    log.info("Test block {} {}", this.block.isPaused(), isActivatedView());
                    if (!this.block.isPaused() || !isActivatedView()) return;;
                    switch (e.getCode()) {
                        case LEFT:
                            block.setWidth(
                                    block.getWidth() * 0.95
                            );
                            break;
                        case RIGHT:
                            block.setWidth(
                                    block.getWidth() * 1.05
                            );
                            break;
                        case UP:
                            block.setHeight(
                                    block.getHeight() * 1.05
                            );
                            break;
                        case DOWN:
                            block.setHeight(
                                    block.getHeight() * 0.95
                            );
                            break;
                    }
                }
        );
    }

    /**
     * Gets the block entity associated with this view.
     *
     * @return The block entity.
     */
    @Override
    public GameEntity getEntity() {
        return block;
    }

    /**
     * Gets the list of shapes that represent the block entity.
     *
     * @return The list of shapes.
     */
    @Override
    public List<Shape> getDrawableShapes() {
        return this.block.getDrawableShapes();
    }
}
