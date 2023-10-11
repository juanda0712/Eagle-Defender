package com.mygdx.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Array;
import com.mygdx.models.User2;
import com.mygdx.utils.JSONDataManager;
import org.bytedeco.opencv.global.opencv_core;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.global.opencv_imgproc;
import org.bytedeco.opencv.opencv_core.Mat;
import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_face.FaceRecognizer;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.DoublePointer;
import com.badlogic.gdx.files.FileHandle;
import org.bytedeco.opencv.opencv_face.FisherFaceRecognizer;
import org.bytedeco.opencv.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.opencv.opencv_face.EigenFaceRecognizer;


import java.nio.IntBuffer;


public class Recognizer {
    private final FaceRecognizer model;
    private JSONDataManager<User2> user2Manager;
    private final Array<User2> data;

    public Recognizer(JSONDataManager<User2> user2Manager) {
        this.user2Manager = user2Manager;
        model = EigenFaceRecognizer.create();
        //model = LBPHFaceRecognizer.create();
        //model = FisherFaceRecognizer.create();
        data = user2Manager.read();

        String directoryPath = "data/imgs/";

        MatVector images = new MatVector();

        int rows = data.size;
        System.out.println(rows);
        Mat labels = new Mat(rows, 1, opencv_core.CV_32SC1);
        IntBuffer labelsBuffer = labels.createBuffer();

        for (User2 user : data) {
            FileHandle fileHandle = Gdx.files.internal(directoryPath + user.getImage());
            String filePath = fileHandle.file().getAbsolutePath();
            Mat img = opencv_imgcodecs.imread(filePath, opencv_imgcodecs.IMREAD_GRAYSCALE);
            Size newSize = new Size(450, 450);
            opencv_imgproc.resize(img, img, newSize);
            images.push_back(img);
        }

        for (int i = 0; i < images.size(); i++) {
            labelsBuffer.put(i, i);
        }
        model.train(images, labels);
    }

    public User2 Predict(Mat newImg) {
        IntPointer label = new IntPointer(1);
        DoublePointer confidence = new DoublePointer(1);
        model.predict(newImg, label, confidence);
        int predictedLabel = label.get(0);

        return data.get(predictedLabel);
    }
}