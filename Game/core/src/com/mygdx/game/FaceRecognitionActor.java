package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.global.opencv_videoio;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class FaceRecognitionActor extends Actor {
    private VideoCapture capture;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private Texture cameraTexture;
    private Recognizer recognizer;
    private JSONDataManager<User2> user2Manager;
    private boolean detectFaces = false;
    private Mat frame;
    private Mat grayFrame;
    private User2 currentUser;

    public FaceRecognitionActor(JSONDataManager<User2> user2Manager) {
        this.user2Manager = user2Manager;
        int desiredWidth = 640;
        int desiredHeight = 480;
        capture = new VideoCapture(0);
        camera = new OrthographicCamera(desiredWidth, desiredHeight);
        spriteBatch = new SpriteBatch();
        cameraTexture = new Texture(desiredWidth, desiredHeight, Pixmap.Format.RGB888);
        recognizer = new Recognizer(user2Manager);
    }

    @Override
    public void act(float delta) {
        if (!capture.isOpened()) {
            Gdx.app.error("RealTimeFaceDetectionActor", "Error al abrir la cámara");
            return;
        }

        capture.read(frame);

        if (detectFaces) {
            opencv_imgproc.cvtColor(frame, grayFrame, opencv_imgproc.COLOR_RGBA2GRAY);

            Size newSize = new Size(450, 450);
            opencv_imgproc.resize(grayFrame, grayFrame, newSize);

            currentUser = recognizer.Predict(grayFrame);
            detectFaces = false;
        }

        Frame processedFrame = Java2DFrameUtils.toFrame(toBufferedImage(frame));
        cameraTexture.draw(toPixmap(processedFrame), 0, 0);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float cameraX = (Gdx.graphics.getWidth() - cameraTexture.getWidth()) / 2f;
        float cameraY = (Gdx.graphics.getHeight() - cameraTexture.getHeight()) / 2f;

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(cameraTexture, cameraX, cameraY);
        spriteBatch.end();
    }

    public void startFaceDetection() {
        detectFaces = true;
    }

    public User2 getUser() {
        return currentUser;
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

