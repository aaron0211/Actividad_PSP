package com.aaron.actividad;

import com.aaron.actividad.util.Hilo;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class DescargaController {
    public Label lbNombre,lbVelocidad;
    public ProgressBar pbProgreso;

    public void pasarParametros(String nombre){
        lbNombre.setText(nombre);
        Hilo hilo = new Hilo(pbProgreso,lbNombre,lbVelocidad);
        hilo.start();
    }
}
