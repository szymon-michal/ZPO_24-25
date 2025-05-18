package com.ticketing.ui;

import javafx.stage.Stage;

public abstract class BaseController {
    
    protected Stage stage;
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    public Stage getStage() {
        return stage;
    }
}