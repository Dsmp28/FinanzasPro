module com.example.finanzaspro {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.finanzaspro to javafx.fxml;
    exports com.example.finanzaspro;
}