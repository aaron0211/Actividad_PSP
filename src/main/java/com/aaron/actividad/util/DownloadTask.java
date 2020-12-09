package com.aaron.actividad.util;

import javafx.concurrent.Task;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class DownloadTask extends Task<Void> {
    private URL tfUrl;
    private File file;

    public DownloadTask(String url, File file) throws MalformedURLException {
        this.tfUrl = new URL(url);
        this.file = file;
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

            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                updateProgress(((double)totalRead/fileSize),1);
                int porcentaje = totalRead*100/fileSize;
                updateMessage(porcentaje +" %");

                fileOutputStream.write(dataBuffer, 0, bytesRead);
                totalRead += bytesRead;

                if (isCancelled()){
                    return null;
                }
            }
            updateProgress(1,1);
            updateMessage("100 %");
        return null;
    }
}
