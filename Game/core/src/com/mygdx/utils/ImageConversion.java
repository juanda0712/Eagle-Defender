package com.mygdx.utils;


import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.opencv_core.Mat;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

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

    private BufferedImage toBufferedImage(Mat mat) {
        return Java2DFrameUtils.toBufferedImage(mat);
    }

    public static Pixmap toPixmap(Frame frame) {
        BufferedImage bufferedImage = Java2DFrameUtils.toBufferedImage(frame);
        int width = bufferedImage.getWidth();
        int height = bufferedImage.getHeight();
        ByteBuffer pixels = ByteBuffer.allocateDirect(4 * width * height);

        int[] pixelArray = new int[width * height];
        bufferedImage.getRGB(0, 0, width, height, pixelArray, 0, width);

        for (int y = height - 1; y >= 0; y--) {
            for (int x = 0; x < width; x++) {
                int pixel = pixelArray[y * width + x];
                int r = (pixel >> 16) & 0xFF;
                int g = (pixel >> 8) & 0xFF;
                int b = pixel & 0xFF;
                int a = (pixel >> 24) & 0xFF;
                pixels.put((byte) r);
                pixels.put((byte) g);
                pixels.put((byte) b);
                pixels.put((byte) a);
            }
        }

        pixels.flip();

        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        pixmap.getPixels().clear();
        pixmap.getPixels().put(pixels);
        pixmap.getPixels().flip();
        return pixmap;
    }
}
