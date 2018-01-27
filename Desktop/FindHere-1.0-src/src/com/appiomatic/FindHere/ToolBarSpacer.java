package com.appiomatic.FindHere;

import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/**
 *
 * @author Zunayed Hassan
 */
public class ToolBarSpacer extends HBox {
    public ToolBarSpacer() {
        HBox.setHgrow(this, Priority.ALWAYS);
    }
}
