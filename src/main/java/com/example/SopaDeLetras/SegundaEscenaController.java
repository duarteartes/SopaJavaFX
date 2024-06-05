// Indicamos el paquete al que pertenece la clase
package com.example.SopaDeLetras;

// Importamos las clases necesarias
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.paint.Color;
import model.PuzzelWord;
import model.WordSearch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

// Definimos el controlador que contiene la segunda vista de nuestro programa
public class SegundaEscenaController {

    // Representamos los controles de la segunda ventana
    @FXML
    Label labelData = new Label();
    @FXML
    private TextField textField;
    @FXML
    private GridPane tablero;
    @FXML
    Label numWordsLabel;
    // Creamos la instancia a la clase WordSearch
    WordSearch wordSearch;

    // Creamos este método para incializar el controlador
    void initData(String data){
        // Creamos un objeto WordSearch y lo guardamos en el archivo historial.txt
        wordSearch = new WordSearch(new ArrayList<>(List.of(data.split(" "))));
        guardarEnArchivo(wordSearch.discoverablePuzzelWords, "historial.txt");

        // Imprimimos la sopa de letras y mostramos el número de palabras que hay que adivinar
        imprimirPuzzle(wordSearch.getPuzzle());
        labelData.setText(data);
        numWordsLabel.setText("Palabras a descubir: " + wordSearch.wordsToFind());

    }

    // Creamos este método para cuando hacemos click en el botón
    @FXML
    private void handleButtonClick() {
            System.out.println("Texto ingresado: " + textField.getText());

            // Leemos el texto introducido en el campo de texto
            String palabraIntroducida = textField.getText().toUpperCase();
            boolean palabraEncontrada = wordSearch.checkWord(palabraIntroducida);

            // Tenemos este condicional si la palabra se encuentra en la sopa
            if (palabraEncontrada) {
                // Resaltamos el rojo y con el texto más grande la palabra adivinada
                PuzzelWord palabra = encontrarPalabra(palabraIntroducida);
                if (palabra != null) {
                    resaltarPalabra(palabra);
                }
            } else {
                // Si la palabra no se encuentra en la sopa, mostramos un mensaje
                mostrarMensaje("Palabra no encontrada", "La palabra '" + palabraIntroducida + "' no está en la sopa.");
            }

            // Si se adivinan todas las palabras, mostramos un mensaje y cerramos la aplicación
            if (wordSearch.gameOver()) {
                mostrarMensaje("¡Felicidades!", "Has encontrado todas las palabras. La aplicación se cerrará.");
                Platform.exit();
            }
    }

    // Creamos este método para rellenar la sopa utilizando un array 2D
    private void imprimirPuzzle(char[][] puzzle) {
        // Creamos un bucle anidado para recorrer la matriz de la sopa
        for (int row = 0; row < 10; row++) {
            for (int col = 0; col < 10; col++) {
                // Convertimos el contenido de la celda en un string
                Label label = new Label(String.valueOf(puzzle[row][col]));
                // Establecemos el tamaño del contenido de las celdas
                label.setPrefHeight(25.0);
                label.setPrefWidth(25.0);
                // Establecemos la alineación del contenido de las celdas
                label.setAlignment(Pos.CENTER);

                // Creamos una condición para verificar si la celda está descubierta
                if (wordSearch.isDiscoveredCell(row, col)) {
                    // Si es así cambiamos el color, y establecemos la fuente en negrita
                    label.setTextFill(Color.RED);
                    label.setFont(Font.font(label.getFont().getFamily(), FontWeight.BOLD, label.getFont().getSize()));
                }

                // Actualizamos la vista de la ventana con los cambios establecidos anteriormente
                tablero.add(label, col, row);
            }
        }
    }

    // Con este método resaltamos las palabras en el gridPane
    private void resaltarPalabra(PuzzelWord word) {
        // Con este bucle recorremos cada letra de la palabra y devuelve su longitud
        for (int i = 0; i < word.length(); i++) {
            // Se obtiene la posición inicial de la letra
            int row = word.getIndexRowInit();
            int col = word.getIndexColumnInit();

            // Se obtiene la posición final de la letra
            if (word.getIndexRowInit() == word.getIndexRowEnd()) {
                col += i;
            } else {
                row += i;
            }

            // Creamos esta condición si se encuentra la etiqueta en la posición actual
            Label label = obtenerEtiqueta(row, col);
            // Si es así cambiamos el color, y establecemos la fuente en negrita
            if (label != null) {
                label.setFont(Font.font("Arial", FontWeight.BOLD, label.getFont().getSize()));
                label.setTextFill(Color.RED);
            }
        }
    }

    // Creamos este método para buscar y devolver una etiqueta en la posición actual
    private Label obtenerEtiqueta(int row, int col) {
        // Utilizamos este bucle para recorrer todos los nodos de la matriz
        for (Node node : tablero.getChildren()) {
            // Obtenemos las coordenadas asociadas a cada nodo
            Integer columnIndex = GridPane.getColumnIndex(node);
            Integer rowIndex = GridPane.getRowIndex(node);

            // Comprobamos que los índices de la posición actual no sea null
            if (columnIndex != null && rowIndex != null && columnIndex.intValue() == col && rowIndex.intValue() == row) {
                // Si no es null devolvemos el nodo como etiqueta
                return (Label) node;
            }
        }
        // Si no se encuentra la etiqueta en la posición actual, devolverá null
        return null;
    }

    // Con este método buscamos una palabra en la sopa de letras
    private PuzzelWord encontrarPalabra(String word) {
        // Recorremos todas las palabras que ha introducido el usuario y se han colocado en la sopa
        for (PuzzelWord puzzleWord : wordSearch.discoverablePuzzelWords) {
            // Comprobamos si la palabra introducida es la misma que la palabra de la sopa
            if (puzzleWord.tryGuess(word)) {
                // Si es la misma devolvemos la palabra encontrada
                return puzzleWord;
            }
        }
        // Si no se encuentra la palabra en la sopa, devolverá null
        return null;
    }

    // Creamos este método para mostrar todas las ventanas de información
    // Está comentado en la clase SopaController.
    private void mostrarMensaje(String title, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    // Creamos este método para guardar las palabras introducidas por el usuario en el archivo historial.txt
    private void guardarEnArchivo(ArrayList<PuzzelWord> palabras, String filename) {
        // Obtenemos la ruta donde ejecutamos la aplicación y la almacenamos en una variable
        String currentDirectory = System.getProperty("user.dir");
        // Escribimos las palabras que forman la sopa
        // Indicamos que se tienen que agregar las palabras debajo de las de la partida anterior
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(currentDirectory + "/" + filename, true))) {
            // Utilizamos de este bucle para iterar sobre cada palabra que tenemos en el array
            for (PuzzelWord word : palabras) {
                // Se escriben todas las palabras separadas por espacios
                writer.write(word.word + " ");
            }
            // Añadimos un salto de línea para escribir cuando volvamos a iniciar una nueva partida
            writer.newLine();
        // Utilizamos un bloque try/catch para manejar las excepciones al guardar en el archivo
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
