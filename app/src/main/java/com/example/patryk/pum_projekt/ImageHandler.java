package com.example.patryk.pum_projekt;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;

public class ImageHandler {



    public String createDirectoryAndSaveFile(Bitmap imageToSave, String fileName) {
        String root = Environment.getExternalStorageDirectory().toString();
        String dirName = root + "/my_recipes";

        File direct = new File(dirName);

        if (!direct.exists()) {
            File recipeDirectory = new File(dirName);
            recipeDirectory.mkdirs();
        }

        File file = new File(new File(dirName), fileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(file);
            imageToSave.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file.toString();
    }
}
