/**
 * @file BlockInfoViewController.java
 * @author Ivan Onufriienko
 */
package ija.project.ui.controller;

import ija.project.model.GameEntity;
import ija.project.model.impl.Block;
import ija.project.observer.Observable;
import ija.project.observer.Observer;
import ija.project.ui.view.ActivatingView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.shape.Shape;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller for the Block Information View in the UI.
 * This class is responsible for managing the interaction between the user and the block's properties
 * within the application's graphical user interface. It allows users to view and edit the properties
 * of a selected block, such as its dimensions and position.
 */
public class BlockInfoViewController implements Observer {
    private static final org.slf4j.Logger log
            = org.slf4j.LoggerFactory.getLogger(BlockInfoViewController.class);

    /**
     * The text field for the block's width.
     */
    @FXML
    @NonNull
    private TextField width;

    /**
     * The text field for the block's top-left Y coordinate.
     */
    @FXML
    @NonNull
    private TextField leftTopY;

    /**
     * The text field for the block's top-left X coordinate.
     */
    @FXML
    @NonNull
    private TextField leftTopX;

    /**
     * The text field for the block's height.
     */
    @FXML
    @NonNull
    private TextField height;

    /**
     * The block entity that is being displayed and edited.
     */
    private Block block;

    /**
     * Handles changes to the X coordinate of the block's top-left corner.
     * Attempts to parse the input and update the block's position.
     *
     * @param inputMethodEvent The event triggered by changing the text field.
     */
    public void onCoordinateXChnage(ActionEvent inputMethodEvent) {
        try {
            block.setLeftTopX(Double.parseDouble(leftTopX.getText()));
        } catch (Exception e) {
            log.error("Failed to set left top X", e);
        }
    }

    /**
     * Handles changes to the Y coordinate of the block's top-left corner.
     * Attempts to parse the input and update the block's position.
     *
     * @param inputMethodEvent The event triggered by changing the text field.
     */
    public void onCoordinateYChnage(ActionEvent inputMethodEvent) {
        try {
            block.setLeftTopY(Double.parseDouble(leftTopY.getText()));
        } catch (Exception e) {
            log.error("Failed to set left top Y", e);
        }
    }

    /**
     * Handles changes to the width of the block.
     * Attempts to parse the input and update the block's width.
     *
     * @param inputMethodEvent The event triggered by changing the text field.
     */
    public void onWidthChange(ActionEvent inputMethodEvent) {
        try {
            block.setWidth(Double.parseDouble(width.getText()));
        } catch (Exception e) {
            log.error("Failed to set width", e);
        }
    }

    /**
     * Handles changes to the height of the block.
     * Attempts to parse the input and update the block's height.
     *
     * @param inputMethodEvent The event triggered by changing the text field.
     */
    public void onHeightChange(ActionEvent inputMethodEvent) {
        try {
            block.setHeight(Double.parseDouble(height.getText()));
        } catch (Exception e) {
            log.error("Failed to set height", e);
        }
    }

    /**
     * Updates the text fields to reflect the current state of the block.
     * This method is called when the block's observable state changes.
     *
     * @param o The observable object that has changed (in this case, the block).
     */
    @Override
    public void update(Observable o) {

        if (block == null) {
            log.warn("Block is not set for {}", this.getClass().toString());
            return;
        }

        width.setText(Double.valueOf(block.getWidth()).toString());
        height.setText(Double.valueOf(block.getHeight()).toString());
        leftTopX.setText(Double.valueOf(block.getLeftTopX()).toString());
        leftTopY.setText(Double.valueOf(block.getLeftTopY()).toString());
    }

    /**
     * Sets the block to be displayed and edited in the view.
     *
     * @param block The block to be managed by this controller.
     */
    public void setBlock(Block block) {
        this.block = block;
    }

    /**
     * Gets the block currently managed by this controller.
     *
     * @return The block being displayed and edited.
     */
    public Block getBlock() {
        return block;
    }
}
