// Indicamos el paquete al que pertenece la clase
package com.example.SopaDeLetras;

// Declaramos la clase que extiende de RuntimeException
// RuntimeException es una excepción en tiempo de ejecución
public class SopaException extends RuntimeException {
    // Creamos el constructor que toma como parámetro el mensaje de la excepción
    public SopaException(String message) {
        // Añadimos el mensaje de la excepción al constructor
        super(message);
    }
}
