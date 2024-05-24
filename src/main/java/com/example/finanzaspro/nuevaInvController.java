package com.example.finanzaspro;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class nuevaInvController {
    @FXML
    private TextField txtMonto;

    @FXML
    private TextField txtPlazo;

    @FXML
    private TextField txtTasa;

    @FXML
    private Button txtAggInv;

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private void cerrar(){
        stage.close();
    }
}
