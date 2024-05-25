package com.example.finanzaspro;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class estadisticasController implements Initializable {
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    public void handleExportar(ActionEvent event){
        List<String> choices = List.of("Descargar", "Enviar por correo");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Descargar", choices);
        dialog.setTitle("Exportar Datos");
        dialog.setHeaderText("Elige una opción");
        dialog.setContentText("Seleccione:");
        dialog.setGraphic(null);

        Stage alertStage = (Stage) dialog.getDialogPane().getScene().getWindow();
        alertStage.getIcons().add(new Image(getClass().getResource("icons/cuota.png").toString()));
        dialog.getDialogPane().getStylesheets().add(getClass().getResource("cssestadisticas.css").toExternalForm());
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(selected -> {
            if (selected.equals("Descargar")) {
                //código de descarga xd
            } else {
                //código de enviar por correo
            }
        });
    }
}
