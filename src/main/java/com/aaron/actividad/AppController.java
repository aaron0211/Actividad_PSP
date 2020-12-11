package com.aaron.actividad;

import com.aaron.actividad.util.R;
import com.aaron.actividad.util.Tarea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppController {

    public TextField tfUrl;
    public Label lbInformacion;
    public ScrollPane spDescargas;
    public VBox vbDesc;
    public CheckBox cbSeleccionar;
    private String urlText;
    public List<DescargaController> descargas = new ArrayList<>();

    @FXML
    public void anadir(ActionEvent event){
        urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();

        try{
            FXMLLoader loader = new FXMLLoader();
            DescargaController controller = new DescargaController(urlText,cbSeleccionar,vbDesc);
            descargas.add(controller);
            if (descargas.size()<=5) {
                loader.setLocation(R.getUI("descarga.fxml"));
                loader.setController(controller);
                VBox vbox = loader.load();

                spDescargas.setContent(vbDesc);
                vbDesc.getChildren().add(vbox);
                controller.start();
                Tarea tarea = new Tarea(lbInformacion,urlText);
                tarea.start();
            }else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Tamaño máximo");
                alert.setContentText("Sólo puedes tener 5 descargas simultáneas");
                alert.show();
            }

        }catch (IOException ioe){
            ioe.printStackTrace();
        }

    }
}
