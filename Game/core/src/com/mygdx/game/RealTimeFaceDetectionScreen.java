package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.Java2DFrameUtils;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.*;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_objdetect.CascadeClassifier;
import org.bytedeco.opencv.opencv_videoio.VideoCapture;
import org.bytedeco.opencv.global.opencv_videoio;


import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

public class RealTimeFaceDetectionScreen extends ScreenAdapter {
    final MainController game;
    private VideoCapture capture;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private Texture cameraTexture;
    private CascadeClassifier faceCascade;

    private Recognizer recognizer;
    private final JSONDataManager<User2> user2Manager;

    public RealTimeFaceDetectionScreen(final MainController game, JSONDataManager<User2> user2Manager) {
        this.user2Manager = user2Manager;
        recognizer = new Recognizer(user2Manager);

        this.game = game;
        capture = new VideoCapture(0);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch = new SpriteBatch();
        cameraTexture = new Texture(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGB888);

        faceCascade = new CascadeClassifier();
        String cascadeFilePath = "assets/haarcascades/haarcascade_frontalface_default.xml";
        faceCascade.load(cascadeFilePath);
    }

    @Override
    public void show() {
        try {
            if (!capture.open(0)) {
                Gdx.app.error("RealTimeFaceDetectionScreen", "Error al abrir la cámara");
                return;
            } else {
                System.out.println("ENTRO A LA CAMARA");
            }
            capture.set(opencv_videoio.CAP_PROP_ORIENTATION_AUTO, 90);
            //capture.set(opencv_videoio.CAP_PROP_FRAME_HEIGHT, -720);

            int desiredWidth = Gdx.graphics.getWidth();
            int desiredHeight = Gdx.graphics.getHeight();

            capture.set(opencv_videoio.CAP_PROP_FRAME_WIDTH, desiredWidth);
            capture.set(opencv_videoio.CAP_PROP_FRAME_HEIGHT, desiredHeight);

        } catch (Exception e) {
            Gdx.app.error("RealTimeFaceDetectionScreen", "Error al inicializar: " + e.getMessage());
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        Mat frame = new Mat();
        capture.read(frame);
        Mat grayFrame = new Mat();
        opencv_imgproc.cvtColor(frame, grayFrame, opencv_imgcodecs.IMREAD_GRAYSCALE);

        Size newSize = new Size(450, 450);
        opencv_imgproc.resize(grayFrame, grayFrame, newSize);

        //User2 user = recognizer.Predict(grayFrame);
        //System.out.println(user);

        Frame processedFrame = Java2DFrameUtils.toFrame(toBufferedImage(frame));
        cameraTexture.draw(toPixmap(processedFrame), 0, 0);


        float cameraX = (Gdx.graphics.getWidth() - cameraTexture.getWidth()) / 2f;
        float cameraY = (Gdx.graphics.getHeight() - cameraTexture.getHeight()) / 2f;

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(cameraTexture, cameraX, cameraY);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void dispose() {
        if (capture != null) {
            capture.release();
        }
        cameraTexture.dispose();
        spriteBatch.dispose();
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
