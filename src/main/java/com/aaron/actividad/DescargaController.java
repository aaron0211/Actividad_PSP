package com.aaron.actividad;

import com.aaron.actividad.util.DownloadTask;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.net.MalformedURLException;

public class DescargaController {
    public Label lbNombre,lbVelocidad,lbPorcentaje;
    public ProgressBar pbProgreso;
    public Button btPausar,btCancelar,btEliminar;
    private String urlText;
    private CheckBox cbSeleccionar;
    private DownloadTask downloadTask;
    private boolean pausado = false;

    public DescargaController(String urlText, CheckBox cbSeleccionar){
        this.urlText = urlText;
        this.cbSeleccionar = cbSeleccionar;
    }

    public void start() {
        lbNombre.setText(urlText);
        btEliminar.setDisable(true);
        btCancelar.setDisable(true);
        try {
            if (cbSeleccionar.isSelected()) {
                FileChooser fileChooser = new FileChooser();
                File file = fileChooser.showSaveDialog(btEliminar.getScene().getWindow());
                if (file == null) return;

                downloadTask = new DownloadTask(urlText, file);
            }else {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setInitialFileName("C:\\Users\\aaron\\Desktop\\"+urlText);
                File file = new File(String.valueOf(fileChooser));
                downloadTask = new DownloadTask(urlText,file);
            }
            pbProgreso.progressProperty().unbind();
            pbProgreso.progressProperty().bind(downloadTask.progressProperty());

            downloadTask.stateProperty().addListener((observableValue, oldState, newState) -> {
                if (newState == Worker.State.SUCCEEDED){
                    pbProgreso.setStyle("-fx-accent: green;");
                    lbNombre.setText("Descarga Finalizada");
                    btEliminar.setDisable(false);
                    btCancelar.setDisable(true);
                    btPausar.setDisable(true);
                }
            });
            downloadTask.messageProperty().addListener((observableValue, oldValue, newValue) -> lbPorcentaje.setText(newValue));
            new Thread(downloadTask).start();
        }catch (MalformedURLException murle){
            murle.printStackTrace();
        }
        //Hilo hilo = new Hilo(pbProgreso,lbNombre,lbVelocidad, lbPorcentaje);
        //hilo.start();
    }

    @FXML
    public void pausar(ActionEvent event){
        if(!pausado){
            btPausar.setText("REANUDAR");
            pausado = !pausado;
            btCancelar.setDisable(!btCancelar.isDisable());
            btEliminar.setDisable(!btEliminar.isDisable());
        }else {
            btPausar.setText("PAUSAR");
            pausado = !pausado;
            btCancelar.setDisable(!btCancelar.isDisable());
            btEliminar.setDisable(!btEliminar.isDisable());
        }
    }

    @FXML
    public void cancelar(ActionEvent event){
        downloadTask.cancel();
        btPausar.setDisable(true);
        btCancelar.setDisable(true);
        btEliminar.setDisable(false);
    }

    @FXML
    public void eliminar(ActionEvent event){
        Stage pantalla = (Stage) this.btEliminar.getScene().getWindow();
        pantalla.close();
    }

//    public void eliminarDescarga(DescargaController controller){
//        AppController appController = new AppController();
//        System.out.println(appController.descargas);
//        System.out.println(controller.toString());
//        /*for (int i = 0;i < descargas.size();i++){
//            System.out.println(descargas.get(i));
//            if (descargas.get(i).equals(controller)){
//                vbDesc.getChildren().remove(i);
//            }
//        }*/
//    }
}
