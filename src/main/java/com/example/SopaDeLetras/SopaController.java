// Indicamos el paquete al que pertenece la clase
package com.example.SopaDeLetras;

// Importamos las clases necesarias
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

// Definimos el controlador que contiene la primera vista de nuestro programa
public class SopaController {
    // Declaramos este campo que lo iniciará el cargador de FXML
    @FXML
    private TextField textData;

    // Creamos este método asociado al botón de ¡Comencemos!
    // Se llamará a este método en el momento en el que hagamos click en el botón
    @FXML
    protected void onHelloButtonClick() {
        try {
            // Cargamos la segunda escena de nuestra aplicación
            FXMLLoader loader = new FXMLLoader(SopaAplicacion.class.getResource("segundaEscena.fxml"));
            //Cargamos la raíz del archivo FXML
            Parent root = loader.load();
            // Obtenemos el controlador de la segunda escena
            SegundaEscenaController controller = loader.getController();

            // Obtenemos el texto introducido por el usuario
            // Para conocer el final de cada palabra utilizamos espacios
            String[] palabras = textData.getText().split(" ");
            // Comprobamos que el número de palabras introducido sea 5 o menos
            if (palabras.length > 5) {
                // Si escribe más de 5 palabras, lanzamos una excepción personalizada
                throw new SopaException("¡ERROR! Sólo se admiten 5 palabras");
            }
            // Llamamos al método que inicializa la segunda escena
            // Le pasamos el texto introducido por el usuario
            controller.initData(textData.getText());
            //Obtenemos la ventana actual
            Stage stage = (Stage) textData.getScene().getWindow();
            // Creamos una nueva escena con la raíz del segundo archivo FXML
            Scene scene = new Scene(root);
            // Añadimos el estilo CSS
            scene.getStylesheets().add(getClass().getResource("style.css").toExternalForm());
            // Mostramos la nueva escena
            stage.setScene(scene);
        // Utilizamos un bloque try/catch para manejar las excepciones al ejecutar el programa
        } catch (SopaException e) {
            mostrarMensaje("Error", e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Creamos un método para mostrar las ventanas de información (alerts)
    private void mostrarMensaje(String title, String content) {
        // Creamos un objeto para mostrar la ventana de información
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        // Establecemos el título de la ventana
        alert.setTitle(title);
        // Decimos que la ventana no tenga un encabezado
        alert.setHeaderText(null);
        // Establecemos el mensaje de la ventana
        alert.setContentText(content);
        // Mostramos la ventana hasta que se presiona el botón de cerrar
        alert.showAndWait();
    }
}
