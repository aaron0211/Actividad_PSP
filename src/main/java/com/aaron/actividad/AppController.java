package com.aaron.actividad;

import com.aaron.actividad.util.R;
import com.aaron.actividad.util.Tarea;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AppController {

    public TextField tfUrl;
    public Label lbInformacion;
    public ScrollPane spDescargas;
    public VBox vbDesc;

    @FXML
    public void anadir(){
        String nombre = tfUrl.getText();
        Tarea tarea = new Tarea(lbInformacion,nombre);
        tarea.start();

        try{
            FXMLLoader loader = new FXMLLoader();
            DescargaController controller = new DescargaController();
            loader.setLocation(R.getUI("descarga.fxml"));
            loader.setController(controller);
            HBox hbox = loader.load();

            spDescargas.setContent(vbDesc);
            vbDesc.getChildren().add(hbox);
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }
}
