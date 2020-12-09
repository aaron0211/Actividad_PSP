package com.aaron.actividad;

import com.aaron.actividad.util.Hilo;
import com.aaron.actividad.util.R;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class SplashController {

    public ProgressBar pbProgreso;
    public Label lbProgreso;
    public ImageView ivImagen;
    private Stage pantalla;

    public SplashController(Stage pantalla) throws FileNotFoundException {
        this.pantalla = pantalla;
//        FileInputStream fileInputStream = new FileInputStream("descarga.png");
//        Image image = new Image(fileInputStream);
//        ivImagen.setImage(image);
    }

    public void start() throws IOException {
        Hilo hilo = new Hilo(this, pbProgreso, lbProgreso);
        hilo.start();
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

    public void cerrarVentana(){
        //pantalla.close();
//        Stage pantalla = (Stage) this.pbProgreso.getScene().getWindow();
//        pantalla.close();
    }
}
