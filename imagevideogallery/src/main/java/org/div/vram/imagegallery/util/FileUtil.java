package org.div.vram.imagegallery.util;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    private File path;
    private String sourcePath = "";
    private String destinationPath = "";

    private String folderName = "";
    private String fileNameWithExtention = System.currentTimeMillis() + "";
//    private boolean isSaveCache = false;

    public FileUtil sourcePath(String sourcePath) {
        this.sourcePath = sourcePath;
        return this;
    }

//    public FileUtil isSaveInCaches(boolean isSaveCache) {
//        this.isSaveCache = isSaveCache;
//        return this;
//    }

    public FileUtil folderName(String fileName) {
        this.folderName = fileName;
        return this;
    }


    public FileUtil fileName(String fileNameWithExtention) {
        this.fileNameWithExtention = fileNameWithExtention;
        return this;
    }

    public void save() throws Exception {
        if (folderName == null && sourcePath == null) {
            throw new Exception("Folder name is not found______");
        }

        File makingFile;

        if (folderName.length() > 0) {
            String externalPath = Util.EXTERNAL_PATH;

            makingFile = new File(externalPath + "/" + folderName);
            if (!makingFile.exists()) {
                makingFile.mkdirs();
            }

            String filePath = makingFile.getAbsolutePath() + "/" + fileNameWithExtention;
            Log.e("TAG_", "filePath:-> " + filePath);
            if (sourcePath.length() > 0) {
                File sourceFile = new File(sourcePath);
                if (sourceFile.exists()) {
                    FileInputStream inputStream;
                    FileOutputStream outputStream;
                    try {
                        inputStream = new FileInputStream(sourceFile.getAbsolutePath());
                        outputStream = new FileOutputStream(filePath);
                        byte[] data = new byte[1024];
                        int count = 0;
                        while ((count = inputStream.read(data)) != -1) {
                            outputStream.write(data, 0, count);
                        }
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            throw new Exception("Folder name is not found");
        }
    }
}
