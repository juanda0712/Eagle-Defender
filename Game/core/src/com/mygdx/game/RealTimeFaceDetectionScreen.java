package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
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
    private Mat referenceFaceMat;
    private Mat grayReferenceFaceMat;

    public RealTimeFaceDetectionScreen(final MainController game) {
        this.game = game;
        capture = new VideoCapture(0);
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        spriteBatch = new SpriteBatch();
        cameraTexture = new Texture(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), Pixmap.Format.RGB888);

        // Carga el archivo XML para la detección de caras
        faceCascade = new CascadeClassifier();
        String cascadeFilePath = "assets/haarcascades/haarcascade_frontalface_default.xml";
        if (faceCascade.load(cascadeFilePath)) {
            System.out.println("Archivo XML de detección de caras cargado correctamente.");
        } else {
            System.err.println("Error al cargar el archivo XML de detección de caras.");
        }


        String imagePath = "imagenPrueba.png";
        FileHandle fileHandle = Gdx.files.internal("assets/" + imagePath);
        if (fileHandle.exists()) {
            String filePath = fileHandle.file().getAbsolutePath();
            referenceFaceMat = opencv_imgcodecs.imread(filePath);
            grayReferenceFaceMat = new Mat(); // Inicializa grayReferenceFaceMat
            if (referenceFaceMat.empty()) {
                // El Mat no es válido, muestra un mensaje de error con información adicional
                Gdx.app.error("Error", "No se pudo cargar la imagen desde la ruta: " + filePath);
            } else {
                // Convierte la imagen de referencia a escala de grises
                opencv_imgproc.cvtColor(referenceFaceMat, grayReferenceFaceMat, opencv_imgproc.COLOR_RGB2GRAY);
                opencv_imgproc.equalizeHist(grayReferenceFaceMat, grayReferenceFaceMat);
                Gdx.app.log("Success", "Imagen cargada CORRECTAMENTE desde la ruta: " + filePath);
            }
        } else {
            Gdx.app.error("Error", "La imagen no se encuentra en el directorio de activos: " + imagePath);
        }
    }

    @Override
    public void show() {
        try {
            if (!capture.open(0)) {
                Gdx.app.error("RealTimeFaceDetectionScreen", "Error al abrir la cámara");
                return;
            }
            //capture.set(Videoio.CAP_PROP_ORIENTATION_AUTO, 180);
            capture.set(opencv_videoio.CAP_PROP_FRAME_HEIGHT, -720);

            int desiredWidth = Gdx.graphics.getWidth(); // Ancho deseado, igual al ancho de la pantalla
            int desiredHeight = Gdx.graphics.getHeight(); // Altura deseada, igual a la altura de la pantalla

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
        try {
            if (capture.read(frame)) {
                // Detección de caras en tiempo real
                Mat grayFrame = new Mat();
                opencv_imgproc.cvtColor(frame, grayFrame, opencv_imgproc.COLOR_RGB2GRAY);
                opencv_imgproc.equalizeHist(grayFrame, grayFrame);

                RectVector faces = new RectVector();
                faceCascade.detectMultiScale(grayFrame, faces);

                // Detección de caras en la imagen de referencia
                RectVector facesInReference = new RectVector();
                faceCascade.detectMultiScale(grayReferenceFaceMat, facesInReference);


                for (int i = 0; i < faces.size(); i++) {
                    Rect face = faces.get(i);
                    opencv_imgproc.rectangle(frame, face, new org.bytedeco.opencv.opencv_core.Scalar(0, 0, 255, 0));

                    // Obtén la región de interés (ROI) del video
                    Mat roiVideo = new Mat(grayFrame, face);

                    if (facesInReference.size() > 0) {
                        Rect faceRectInReference = facesInReference.get(0);
                        Mat roiReferenceFace = new Mat(grayReferenceFaceMat, faceRectInReference);
                        if (roiVideo.rows() != roiReferenceFace.rows() || roiVideo.cols() != roiReferenceFace.cols()) {
                            // Redimensionar una de las matrices para que coincida con las dimensiones de la otra
                            Size newSize = new Size(roiReferenceFace.cols(), roiReferenceFace.rows());
                            opencv_imgproc.resize(roiVideo, roiVideo, newSize);
                        }
                        double similarityScore = calculateSimilarity(roiReferenceFace, roiVideo);
                        if (similarityScore < 1.5E-7) { // Ajusta el umbral de similitud según tus necesidades
                            System.out.println(similarityScore);
                            Gdx.app.log("FaceDetection", "Usuario autenticado");
                        } else {
                            System.out.println(similarityScore);
                            Gdx.app.log("FaceDetection", "Autenticación fallida");
                        }
                    }

                    // Realiza la comparación solo en la ROI del video y la ROI de referencia

                    // Aquí puedes comparar similarityScore con un umbral para determinar si es un rostro autenticado o no
                }

                // Convertir el resultado a un Frame de JavaCV
                Frame processedFrame = Java2DFrameUtils.toFrame(toBufferedImage(frame));

                // Calcular la similitud entre la cara detectada y la referencia
                //double similarityScore = calculateSimilarity(referenceFaceMat, grayFrame);
                /*if (grayFrame.rows() != grayReferenceFaceMat.rows() || grayFrame.cols() != grayReferenceFaceMat.cols()) {
                    // Redimensionar una de las matrices para que coincida con las dimensiones de la otra
                    Size newSize = new Size(grayReferenceFaceMat.cols(), grayReferenceFaceMat.rows());
                    opencv_imgproc.resize(grayFrame, grayFrame, newSize);
                }*/

                //double similarityScore = calculateSimilarity(grayReferenceFaceMat, grayFrame);

                // Actualizar la textura de la cámara con el resultado procesado
                cameraTexture.draw(toPixmap(processedFrame), 0, 0);

                // Comprobar si la similitud es suficiente para autenticar al usuario
                /*if (similarityScore > 0.8) { // Ajusta el umbral de similitud según tus necesidades
                    System.out.println(similarityScore);
                    Gdx.app.log("FaceDetection", "Usuario autenticado");
                } else {
                    System.out.println(similarityScore);
                    Gdx.app.log("FaceDetection", "Autenticación fallida");
                }*/
            }
        } catch (Exception e) {
            Gdx.app.error("RealTimeFaceDetectionScreen", "Error durante la detección de caras: " + e.getMessage());
        }
        float cameraX = (Gdx.graphics.getWidth() - cameraTexture.getWidth()) / 2f;
        float cameraY = (Gdx.graphics.getHeight() - cameraTexture.getHeight()) / 2f;

        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(cameraTexture, cameraX, cameraY);
        //spriteBatch.draw(cameraTexture, -camera.viewportWidth / 2, -camera.viewportHeight / 2);
        spriteBatch.end();
    }

    private double calculateSimilarity(Mat referenceFace, Mat detectedFace) {
        // Implementa aquí la lógica para calcular la similitud entre las dos caras
        // Puedes utilizar medidas como la distancia euclidiana, SSD (Sum of Squared Differences), etc.
        Mat diff = new Mat();
        org.bytedeco.opencv.global.opencv_core.absdiff(referenceFace, detectedFace, diff);
        Scalar sum = org.bytedeco.opencv.global.opencv_core.sumElems(diff);
        double similarity = 1.0 / (1.0 + sum.get(0));
        return similarity;
        //return 0.0;
    }


    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
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
        // Convertir el Mat de OpenCV a BufferedImage
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
