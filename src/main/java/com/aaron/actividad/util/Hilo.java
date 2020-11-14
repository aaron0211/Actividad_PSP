package com.aaron.actividad.util;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class Hilo extends Thread{
    private ProgressBar pbProgreso;
    private Label lbNombre;
    private Label lbVelocidad;

    public Hilo(ProgressBar pbProgreso,Label lbNombre){
        this.lbNombre = lbNombre;
        this.pbProgreso = pbProgreso;
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
            Platform.runLater(()-> {
                pbProgreso.setProgress((double)o/10);
                lbNombre.setText("Funciona?");
                lbVelocidad.setText("0 MB/s");
            });
        }
    }
}
