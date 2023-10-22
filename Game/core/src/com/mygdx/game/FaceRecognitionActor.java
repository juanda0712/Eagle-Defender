package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class FaceRecognitionActor extends Actor {
    private final MainController game;
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
    private Frame processedFrame;
    private CountersBarriers countersBarriers;

    public FaceRecognitionActor(final MainController game, JSONDataManager<User2> user2Manager) {
        this.game = game;
        this.user2Manager = user2Manager;
        int desiredWidth = 320;
        int desiredHeight = 240;

        // Configura la resolución de captura deseada (320x240)
        int captureWidth = 320;
        int captureHeight = 240;
        capture = new VideoCapture(0);
        capture.set(Videoio.CAP_PROP_FRAME_WIDTH, captureWidth);
        capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, captureHeight);

        // Configura la resolución del OrthographicCamera y cameraTexture
        camera = new OrthographicCamera(desiredWidth, desiredHeight);
        cameraTexture = new Texture(desiredWidth, desiredHeight, Pixmap.Format.RGB888);
        spriteBatch = new SpriteBatch();
        recognizer = new Recognizer(user2Manager);

        frame = new Mat();
        grayFrame = new Mat();
        processedFrame = new Frame();
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
            System.out.println(currentUser);
            game.changeScreen(new GameScreen(game, user2Manager, currentUser, countersBarriers, null));
            detectFaces = false;
            game.dispose();
        }

        if (processedFrame != null) {
            processedFrame.close();
        }
        processedFrame = Java2DFrameUtils.toFrame(toBufferedImage(frame));
        cameraTexture.draw(toPixmap(processedFrame), 0, 0);
        frame.release();
        grayFrame.release();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        float leftTableWidth = screenWidth / 2;
        float leftTableHeight = screenHeight;
        float camerax = (leftTableWidth - 320) / 2;
        float cameray = (leftTableHeight - 240) / 2;
        batch.draw(cameraTexture, camerax + 30, cameray, cameraTexture.getWidth(), cameraTexture.getHeight(), 0, 0, cameraTexture.getWidth(), cameraTexture.getHeight(), false, true);
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