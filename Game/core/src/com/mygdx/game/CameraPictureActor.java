package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.models.User2;
import com.mygdx.models.CountersBarriers;
import com.mygdx.utils.JSONDataManager;
import com.mygdx.utils.Recognizer;
import com.mygdx.utils.SpotifyAuthenticator;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.concurrent.atomic.AtomicReference;

public class CameraPictureActor extends Actor {
    private final MainController game;
    private final Register register;

    private VideoCapture capture;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private Texture cameraTexture;
    private Recognizer recognizer;
    private JSONDataManager<User2> user2Manager;
    private boolean takePicture = false;
    private Mat nativeFrame;
    private Mat displayFrame;
    private Mat picture;
    private Frame processedFrame;
    private final AtomicReference<SpotifyAuthenticator> spotifyReference = new AtomicReference<>(null);

    public CameraPictureActor(final MainController game, JSONDataManager<User2> user2Manager, final Register register) {
        this.game = game;
        this.register = register;
        this.user2Manager = user2Manager;
        int desiredWidth = 320;
        int desiredHeight = 240;

        // Configura la resolución de captura deseada (320x240)
        capture = new VideoCapture(0); // Abre la cámara con índice 0 (cámara predeterminada)
        camera = new OrthographicCamera(desiredWidth, desiredHeight);
        cameraTexture = new Texture(desiredWidth, desiredHeight, Pixmap.Format.RGB888);
        spriteBatch = new SpriteBatch();
        recognizer = new Recognizer(user2Manager);

        nativeFrame = new Mat();
        displayFrame = new Mat();
        processedFrame = new Frame();
    }

    @Override
    public void act(float delta) {
        if (!capture.isOpened()) {
            Gdx.app.error("CameraPictureActor", "Error al abrir la cámara");
            return;
        }

        capture.read(nativeFrame);
        displayFrame = nativeFrame.clone();
        Size newSizeFrame = new Size(320, 240);
        opencv_imgproc.resize(displayFrame, displayFrame, newSizeFrame);
        if (takePicture) {
            register.setPicture(nativeFrame.clone());
            takePicture = false;
        }
        if (processedFrame != null) {
            processedFrame.close();
        }
        processedFrame = Java2DFrameUtils.toFrame(toBufferedImage(displayFrame));
        cameraTexture.draw(toPixmap(processedFrame), 0, 0);
        displayFrame.release();
        nativeFrame.release();
    }

    public void takePicture() {
        takePicture = true;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float leftTableWidth = screenWidth / 2;
        float leftTableHeight = screenHeight;
        float camerax = (leftTableWidth / 12);

        float cameray = (float) (leftTableHeight / 1.4);

        batch.draw(cameraTexture, camerax + 30, cameray, cameraTexture.getWidth(), cameraTexture.getHeight(), 0, 0, cameraTexture.getWidth(), cameraTexture.getHeight(), false, true);
    }

    private BufferedImage toBufferedImage(Mat mat) {
        return Java2DFrameUtils.toBufferedImage(mat);
    }

    private Pixmap toPixmap(Frame frame) {
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