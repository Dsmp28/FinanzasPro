package com.example.finanzaspro;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        String contenidoPresupuesto = new String(Files.readAllBytes(Paths.get("DatoPresupuesto.json")));

        if (!contenidoPresupuesto.isEmpty()) {
            MostrarFormDashBoard(stage);
            dashboardC controller = new dashboardC();
            controller.ValidarEnviarCorreo();
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
        String css = Objects.requireNonNull(this.getClass().getResource("style.css")).toExternalForm();
        scene.getStylesheets().add(css);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/finanzas.png")).toString()));
        stage.initStyle(javafx.stage.StageStyle.DECORATED);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.show();
    }

    private void MostrarFormDashBoard(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("dashboard-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 950, 550);
        stage.setTitle("FinanzasProDashBoard");
        stage.setScene(scene);
        String css = Objects.requireNonNull(this.getClass().getResource("styledashboard.css")).toExternalForm();
        scene.getStylesheets().add(css);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResource("icons/finanzas.png")).toString()));
        stage.initStyle(javafx.stage.StageStyle.DECORATED);
        stage.setResizable(false);
        stage.setMaximized(false);
        stage.show();
    }
}