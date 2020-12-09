package com.aaron.actividad.util;

import com.aaron.actividad.SplashController;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;

import java.io.IOException;

public class Hilo extends Thread{
    private ProgressBar pbProgreso;
    private Label lbProgreso;
    private SplashController controller;

    public Hilo(SplashController controller, ProgressBar pbProgreso,Label lbProgreso){
        this.lbProgreso = lbProgreso;
        this.pbProgreso = pbProgreso;
        this.controller = controller;
    }

    public Hilo(){

    }

    @Override
    public void run(){
        int x = (int) Math.floor(Math.random()*(50-0+25)+0);
        for(int i=1;i<=x;i++){
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int o = i;

            Platform.runLater(()-> {
                pbProgreso.setProgress((double)o/x);
                pbProgreso.setStyle("-fx-accent: red;");
                lbProgreso.setText(o*100/x+"%");
            });
        }
        Platform.runLater(()->{
            try {
                controller.lanzarApp();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
