package com.example.finanzaspro;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.application.Platform;

public class ManejadorAlertas {

    public static void showWarning(String title, String header, String content) {
        showAlert(AlertType.WARNING, title, header, content);
    }

    public static void showInformation(String title, String header, String content) {
        showAlert(AlertType.INFORMATION, title, header, content);
    }

    public static void showError(String title, String header, String content) {
        showAlert(AlertType.ERROR, title, header, content);
    }

    public static void showConfirmation(String title, String header, String content) {
        showAlert(AlertType.CONFIRMATION, title, header, content);
    }

    private static void showAlert(AlertType alertType, String title, String header, String content) {
        // Ejecutar en el hilo de la aplicación de JavaFX
        Platform.runLater(() -> {
            Alert alert = new Alert(alertType);
            alert.setTitle(title);
            alert.setHeaderText(header);
            alert.setContentText(content);

            // Obtener el stage principal si es necesario para aplicaciones que usen múltiples stages
            Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
            stage.toFront(); // Llevar la ventana de alerta al frente

            alert.showAndWait();
        });
    }
}

