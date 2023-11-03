package com.mygdx.utils;

import com.mygdx.models.SongInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class Spotify {
    public static void main(String[] args) {
        System.out.println(generateUniqueFilename("foto", "png"));
    }

    public static String generateUniqueFilename(String baseName, String extension) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
        String timestamp = dateFormat.format(new Date());
        String uniqueFilename = baseName + "_" + timestamp + "." + extension;
        return uniqueFilename;
    }
}