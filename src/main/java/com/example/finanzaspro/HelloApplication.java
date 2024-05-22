package com.example.finanzaspro;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String contenidoPresupuesto = new String(Files.readAllBytes(Paths.get("DatoPresupuesto.json")));

        if (!contenidoPresupuesto.isEmpty()) {
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
        Scene scene = new Scene(fxmlLoader.load(), 950, 550);
        stage.setTitle("FinanzasProLogIn");
        stage.setScene(scene);
        String css = this.getClass().getResource("style.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }

    private void MostrarFormDashBoard(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 550);
        stage.setTitle("FinanzasProDashBoard");
        stage.setScene(scene);
        String css = this.getClass().getResource("styledashboard.css").toExternalForm();
        scene.getStylesheets().add(css);
        stage.show();
    }
}