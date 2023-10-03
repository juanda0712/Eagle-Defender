package com.mygdx.utils;

import com.badlogic.gdx.Gdx;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.Scalar;
import org.bytedeco.opencv.opencv_core.Size;

public class ImageComparator {

    public static double comparator(String loadedImagePath, String referencePath) {
        Mat loadedMat = opencv_imgcodecs.imread(loadedImagePath);
        String filePath = Gdx.files.local("data/imgs/" + referencePath).path();
        Mat referenceMat = opencv_imgcodecs.imread(filePath);

        Mat grayLoadedMat = new Mat();
        Mat grayReferenceMat = new Mat();

        opencv_imgproc.cvtColor(loadedMat, grayLoadedMat, opencv_imgproc.COLOR_RGB2GRAY);
        opencv_imgproc.equalizeHist(grayLoadedMat, grayLoadedMat);

        opencv_imgproc.cvtColor(referenceMat, grayReferenceMat, opencv_imgproc.COLOR_RGB2GRAY);
        opencv_imgproc.equalizeHist(grayReferenceMat, grayReferenceMat);

        if (grayLoadedMat.rows() != grayReferenceMat.rows() || grayLoadedMat.cols() != grayReferenceMat.cols()) {
            Size newSize = new Size(grayReferenceMat.cols(), grayReferenceMat.rows());
            opencv_imgproc.resize(grayLoadedMat, grayLoadedMat, newSize);
        }

        return calculateSimilarity(grayLoadedMat, grayReferenceMat);

    }

    public static double calculateSimilarity(Mat referenceFace, Mat detectedFace) {
        // Implementa aquí la lógica para calcular la similitud entre las dos caras
        // Puedes utilizar medidas como la distancia euclidiana, SSD (Sum of Squared Differences), etc.
        Mat diff = new Mat();
        org.bytedeco.opencv.global.opencv_core.absdiff(referenceFace, detectedFace, diff);
        Scalar sum = org.bytedeco.opencv.global.opencv_core.sumElems(diff);
        double similarity = 1.0 / (1.0 + sum.get(0));
        return similarity;
        //return 0.0;
    }
}
