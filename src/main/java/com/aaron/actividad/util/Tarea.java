package com.aaron.actividad.util;

import javafx.application.Platform;
import javafx.scene.control.Label;

public class Tarea extends Thread{
    private Label lbInformacion;
    private String mensaje;


    public Tarea(Label lbInformacion,String mensaje){
        this.lbInformacion = lbInformacion;
        this.mensaje = mensaje;
    }

    @Override
    public void run(){
        mensaje = mensaje + " añadido correctamente";
        for(int i=0;i<=3;i++){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lbInformacion.setStyle("-fx-font-weight: bold");
            Platform.runLater(()-> lbInformacion.setText(mensaje));
        }
        mensaje="";
        Platform.runLater(()->lbInformacion.setText(mensaje));
    }
}
