package com.aaron.actividad;

import com.aaron.actividad.util.Hilo;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class DescargaController {
    public Label lbNombre;
    public ProgressBar pbProgreso;

    public DescargaController(String nombre){
        System.out.println(nombre);
        Hilo hilo = new Hilo(pbProgreso,lbNombre);
        hilo.start();
    }

    public DescargaController(){
        Hilo hilo = new Hilo(pbProgreso,lbNombre);
        hilo.start();
    }
}
