package com.aaron.actividad;

import com.aaron.actividad.util.Hilo;
import com.aaron.actividad.util.R;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class SplashController {

    public ProgressBar pbProgreso;
    public Label lbProgreso;
    public ImageView ivImagen;
    private Stage pantalla;
    private Image logo;

    public SplashController(Stage pantalla){
        this.pantalla = pantalla;
    }

    public void start(){
        Hilo hilo = new Hilo(this, pbProgreso, lbProgreso);
        hilo.start();
        logo = new Image(new File("src/main/resources/images/descarga.png").toURI().toString());
        ivImagen.setImage(logo);
    }

    public void lanzarApp() throws IOException{
        FXMLLoader loader = new FXMLLoader();
        AppController controller = new AppController();
        loader.setLocation(R.getUI("gestor.fxml"));
        loader.setController(controller);
        VBox vbox = loader.load();

        Scene scene = new Scene(vbox);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle("Gestor de descargas múltiple");
        stage.show();
        pantalla.close();
    }
}
