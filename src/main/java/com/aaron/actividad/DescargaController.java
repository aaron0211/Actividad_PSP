package com.aaron.actividad;

import com.aaron.actividad.util.AlertUtils;
import com.aaron.actividad.util.DownloadTask;
import javafx.concurrent.Worker;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Optional;

public class DescargaController {
    public Label lbNombre,lbPorcentaje;
    public ProgressBar pbProgreso;
    public Button btPausar,btCancelar,btEliminar, btIniciar;
    private String urlText;
    private CheckBox cbSeleccionar;
    private DownloadTask downloadTask;
    private AppController appController;
    private boolean pausado = false;
    private static final Logger logger = LogManager.getLogger(DescargaController.class);
    private String fileName;

    public DescargaController(String urlText, CheckBox cbSeleccionar, AppController appController, int contador){
        this.urlText = urlText;
        this.cbSeleccionar = cbSeleccionar;
        this.appController = appController;

        try {
            URL url = new URL(urlText);
            URLConnection con = url.openConnection();
            String fieldValue = con.getHeaderField("Content-Disposition");
            if (fieldValue != null) {
                fileName = fieldValue.substring(fieldValue.indexOf("filename=\"") + 10, fieldValue.length() - 1);
                System.out.println(fileName);
            }else {
                fileName = "Descarga"+contador;
            }
        }catch (MalformedURLException mue){
            AlertUtils.mostrarAlerta("URL mal formada");
        }catch (IOException ioe){
            AlertUtils.mostrarAlerta("Fallo al conectar la url");
        }
    }

    public void start() {
        lbNombre.setText(fileName);
        btEliminar.setDisable(true);
        btCancelar.setDisable(true);
        btIniciar.setDisable(true);
        try {
            if (cbSeleccionar.isSelected()) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Text Files", "*.txt"),
                        new FileChooser.ExtensionFilter("Image Files","*.png","*.jpg"),
                        new FileChooser.ExtensionFilter("Video Files", "*.mp4"),
                        new FileChooser.ExtensionFilter("Compressed Files", "*.zip"),
                        new FileChooser.ExtensionFilter("All Files", "*.*"));
                File file = fileChooser.showSaveDialog(btEliminar.getScene().getWindow());
                if (file == null) return;

                downloadTask = new DownloadTask(urlText, file);
            }else {
                DirectoryChooser chooser = new DirectoryChooser();
                File file = new File("src/"+fileName);
                chooser.setInitialDirectory(file);
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
            logger.error("Error de descarga"+murle.fillInStackTrace());
        }
        appController.cargarLista();
    }

    @FXML
    public void pausar(ActionEvent event){
        if (downloadTask == null){
            AlertUtils.mostrarAlerta("Inicia primero la descaga");
            return;
        }
        if(!pausado){
            downloadTask.setPausa(true);
            btPausar.setText("REANUDAR");
            pausado = !pausado;
            btCancelar.setDisable(!btCancelar.isDisable());
            btEliminar.setDisable(!btEliminar.isDisable());
        }else {
            downloadTask.setPausa(false);
            btPausar.setText("PAUSAR");
            pausado = !pausado;
            btCancelar.setDisable(!btCancelar.isDisable());
            btEliminar.setDisable(!btEliminar.isDisable());
        }
    }

    @FXML
    public void cancelar(ActionEvent event){
        if (downloadTask == null){
            AlertUtils.mostrarAlerta("Inicia primero la descaga");
            return;
        }
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Cancelar");
        confirmacion.setContentText("Si cancelas la descarga no podrás continuar descargándola, ¿estás seguro?");
        Optional<ButtonType> resp = confirmacion.showAndWait();
        if (resp.get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE)
            return;
        downloadTask.cancel();
        btPausar.setDisable(true);
        btCancelar.setDisable(true);
        btEliminar.setDisable(false);
        logger.trace("Descarga cancelada");
        appController.cargarLista();
    }

    @FXML
    public void eliminar(ActionEvent event){
        Parent pantalla = btEliminar.getParent().getParent().getParent();
        Parent descarga = btCancelar.getParent().getParent();
        VBox vBox = (VBox) pantalla;
        if (downloadTask == null){
            vBox.getChildren().remove(descarga);
            return;
        }
        if (!downloadTask.isCancelled()){
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Eliminar");
            confirmacion.setContentText("¿Estás seguro?");
            Optional<ButtonType> resp = confirmacion.showAndWait();
            if (resp.get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE)
                return;
            downloadTask.cancel();
            vBox.getChildren().remove(descarga);
        }else {
            vBox.getChildren().remove(descarga);
        }
        logger.trace("Descarga eliminada");
        appController.cargarLista();
    }

    @FXML
    public void iniciar(ActionEvent event){
        start();
        btIniciar.setDisable(true);
    }

}
