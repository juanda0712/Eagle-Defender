package com.mygdx.utils;


import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

import org.bytedeco.opencv.opencv_core.Mat;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

public class ImageConversion {
    public static Pixmap matToPixmap(org.bytedeco.opencv.opencv_core.Mat mat) {
        int width = mat.cols();
        int height = mat.rows();
        int numChannels = mat.channels();
        byte[] data = new byte[width * height * numChannels];

        mat.data().get(data);

        Pixmap pixmap = new Pixmap(width, height, Format.RGBA8888);
        pixmap.getPixels().put(data, 0, data.length);
        pixmap.getPixels().rewind();

        return pixmap;
    }
}
