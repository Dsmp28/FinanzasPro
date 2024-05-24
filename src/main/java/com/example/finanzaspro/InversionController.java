package com.example.finanzaspro;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class InversionController {
    @FXML
    private Button btnAgregarInversion;

    @FXML
    private void abrirVentanaAgregarInversion() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("nuevaInv-view.fxml"));
            Parent root = loader.load();
            Stage emergente = new Stage();

            emergente.initModality(Modality.APPLICATION_MODAL);
            emergente.initStyle(javafx.stage.StageStyle.UTILITY);
            emergente.setTitle("Agregar nueva Inversión");
            emergente.setScene(new Scene(root));
            nuevaInvController controller = loader.getController();
            controller.setStage(emergente);
            emergente.showAndWait();
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }
}
