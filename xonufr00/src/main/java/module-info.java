/**
 * @file module-info.java
 * @author Ivan Burlustkyi
 */

/**
 * Defines the module for the project.
 * This module contains the main application class, the model classes, the view classes, the controller classes,
 * the timer class, and the data transfer object classes.
 */
module ija.project {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires java.desktop;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;

    opens ija.project to javafx.fxml, lombok;
    opens ija.project.ui.controller to javafx.fxml;
    opens ija.project.dto to com.fasterxml.jackson.databind, javafx.fxml;

    exports ija.project;


    exports ija.project.model;
    exports ija.project.model.impl;
    exports ija.project.model.enums;

    exports ija.project.ui.view;
    exports ija.project.ui.view.impl;

    exports ija.project.ui.controller;
    exports ija.project.timer;

    exports ija.project.dto;
}