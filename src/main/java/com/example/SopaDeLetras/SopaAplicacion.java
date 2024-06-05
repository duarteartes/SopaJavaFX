// Indicamos el paquete al que pertenece la clase
package com.example.SopaDeLetras;

// Importamos las clases necesarias
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

// Definimos la clase que extiende la aplicación. Necesario para JavaFX
public class SopaAplicacion extends Application {

    //Llamamos a este método cuando se inicia la aplicación
    @Override
    public void start(Stage stage) {
        try {
            // Creamos este objeto para cargar los archivos fxml
            // Concretamente cargamos la primera vista de nuestro programa.
            FXMLLoader fxmlLoader = new FXMLLoader(SopaAplicacion.class.getResource("sopa-view.fxml"));
            // Creamos el objeto Scene y le damos un tamaño a la escena
            Scene scene = new Scene(fxmlLoader.load(), 400, 300);
            // Damos un título a la aplicación, le asignamos la primera escena y la mostramos
            stage.setTitle("Sopa de Letras APP");
            stage.setScene(scene);
            stage.show();
        // Utilizamos un bloque try/catch para manejar las excepciones al cargar el programa
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Iniciamos la aplicación llamando al método launch que llamará al método start
    public static void main(String[] args) {
        launch();
    }
}
