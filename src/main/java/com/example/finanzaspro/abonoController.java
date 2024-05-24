package com.example.finanzaspro;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class abonoController {

    @FXML
    private Button txtAbono;

    @FXML
    private TextField txtMontoAbono;
    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void cerrar(){
        stage.close();
    }
}
