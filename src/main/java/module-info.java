module com.example.ejemplopasardatosentrecontroladores {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires java.desktop;

    opens com.example.SopaDeLetras to javafx.fxml;
    exports com.example.SopaDeLetras;
}