package com.aaron.actividad;

import com.aaron.actividad.util.AlertUtils;
import com.aaron.actividad.util.R;
import com.aaron.actividad.util.Tarea;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import java.io.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AppController {

    public TextField tfUrl,tfMax;
    public Label lbInformacion;
    public ScrollPane spDescargas;
    public VBox vbDesc;
    public CheckBox cbSeleccionar;
    public TextArea taHistorial;
    private String urlText;
    private int maxDescarga = 0;
    public List<DescargaController> descargas = new ArrayList<>();
    private int contador = 0;

    @FXML
    public void descMaxima(ActionEvent event){
        if (esNumerico(tfMax.getText())){
            maxDescarga = Integer.parseInt(tfMax.getText());
        }else {
            AlertUtils.mostrarAlerta("No es introducido un número");
        }
        tfMax.setText("");
        tfMax.setPromptText(String.valueOf(maxDescarga));
    }

    private Boolean esNumerico(String cadena){
        try {
            Integer.parseInt(cadena);
            return true;
        }catch (NumberFormatException nfe){
            return false;
        }
    }

    @FXML
    public void anadir(ActionEvent event){
        urlText = tfUrl.getText();
        tfUrl.clear();
        tfUrl.requestFocus();
        descarga(urlText);
    }

    public void descarga(String urlText){
        contador++;
        try{
            FXMLLoader loader = new FXMLLoader();
            DescargaController controller = new DescargaController(urlText,cbSeleccionar,vbDesc,this,contador);
            descargas.add(controller);
            loader.setLocation(R.getUI("descarga.fxml"));
            loader.setController(controller);
            VBox vbox = loader.load();

            spDescargas.setContent(vbDesc);
            vbDesc.getChildren().add(vbox);
            if (maxDescarga<=0){
                maxDescarga = 5;
            }
            if (descargas.size() <= maxDescarga) {

                controller.start();
                Tarea tarea = new Tarea(lbInformacion,urlText);
                tarea.start();
                cargarLista();
            }
        }catch (IOException ioe){
            ioe.printStackTrace();
        }
    }

    @FXML
    public void importar(ActionEvent event){
        try {
            FileChooser fileChooser = new FileChooser();
            File importar = fileChooser.showOpenDialog(null);

            BufferedReader br = new BufferedReader(new FileReader(importar));
            String linea = br.readLine();

            while (linea != null){
                descarga(linea);
                linea = br.readLine();
            }
        }catch (IOException ioe){
            AlertUtils.mostrarAlerta("Error al importar datos");
        }
    }

    public void cargarLista() {
        taHistorial.setText("");
        try {
            File log = new File("multidescarga.log");
            BufferedReader br = new BufferedReader(new FileReader(log));
            String linea = br.readLine();

            while (linea!=null){
                taHistorial.appendText(linea);
                taHistorial.appendText("\n");
                linea = br.readLine();
            }
        }catch (IOException fnfe){
            AlertUtils.mostrarAlerta("Error al cargar lista");
        }
    }
}
