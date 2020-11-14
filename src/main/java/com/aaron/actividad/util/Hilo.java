package com.aaron.actividad.util;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class Hilo extends Thread{
    private ProgressBar pbProgreso;
    private Label lbNombre;
    private Label lbVelocidad;
    public String mensaje;

    public Hilo(ProgressBar pbProgreso,Label lbNombre,Label lbVelocidad){
        this.lbNombre = lbNombre;
        this.pbProgreso = pbProgreso;
        this.lbVelocidad = lbVelocidad;
    }

    @Override
    public void run(){
        for(int i=1;i<=10;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int o = i;
            int x = (int) Math.floor(Math.random()*(100-0+1)+0);
            mensaje = String.valueOf(x);
            mensaje = mensaje+ " MB/s";
            Platform.runLater(()-> {
                pbProgreso.setProgress((double)o/10);
                lbNombre.setText("Descargando");
                lbVelocidad.setText(mensaje);
            });
        }
    }
}
