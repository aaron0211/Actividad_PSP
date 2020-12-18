package com.aaron.actividad.util;

import com.aaron.actividad.DescargaController;
import javafx.concurrent.Task;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends Task<Void> {
    private URL tfUrl;
    private File file;
    private boolean pausa;
    private static final Logger logger = LogManager.getLogger(DescargaController.class);

    public DownloadTask(String url, File file) throws MalformedURLException {
        this.tfUrl = new URL(url);
        this.file = file;
        logger.trace("Descarga Iniciada");
    }

    public boolean isPausa() {
        return pausa;
    }

    public void setPausa(boolean pausa) {
        this.pausa = pausa;
    }

    @Override
    protected Void call() throws Exception {
        URLConnection urlConnection = tfUrl.openConnection();
        int fileSize = urlConnection.getContentLength();
        BufferedInputStream in = new BufferedInputStream(tfUrl.openStream());
        FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte dataBuffer[] = new byte[1024];
            int bytesRead;
            int totalRead = 0;
            int anterior = 0;

            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                if (pausa){
                    Thread.sleep(1000);
                    continue;
                }
                updateProgress(((double)totalRead/fileSize),1);
                long tiempo = System.currentTimeMillis();
                if (tiempo%100 == 0){
                    double velocidad = (double)(totalRead - anterior)/1048576;
                    anterior = totalRead;
                    if (velocidad>0.5) {
                        double porcentaje = ((double)totalRead*100/fileSize);
                        updateMessage("Descargado "+totalRead/1048576+"/"+fileSize/1048576+"MB  "+String.format("%.1f", porcentaje) +" %"+"      Velocidad: "+String.format("%.1f",velocidad)+" MB/s");
                    }
                }

                fileOutputStream.write(dataBuffer, 0, bytesRead);
                totalRead += bytesRead;

                if (isCancelled()){
                    logger.trace("Descarga Cancelada");
                    return null;
                }

            }
            in.close();
            fileOutputStream.close();
            updateProgress(1,1);
            updateMessage("100 %");
            logger.trace("Descarga Finalizada");
        return null;
    }
}
