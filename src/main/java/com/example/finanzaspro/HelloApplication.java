package com.example.finanzaspro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        double prueba = ManejadorEncriptacion.leerPresupuestoDeJSON("DatoPresupuesto.json");
        if (prueba > 0) {
            MostrarFormDashBoard(stage);
        }else {
            MostrarFormLogIn(stage);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void MostrarFormLogIn(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("inicio-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 550);
        stage.setTitle("FinanzasProLogIn");
        stage.setScene(scene);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }

    private void MostrarFormDashBoard(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 720, 550);
        stage.setTitle("FinanzasProDashBoard");
        stage.setScene(scene);
        String css = this.getClass().getResource("styledashboard.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }
}