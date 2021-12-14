package server;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.ImageIcon;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.dnn.DetectionModel;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;

public class face_detect {

    public face_detect() {

    }

    public static BufferedImage detect(Mat image) {
        MatOfRect face_detect = new MatOfRect();
        String cmlFile = "xml/haarcascade_frontalface_alt.xml";
        CascadeClassifier cc = new CascadeClassifier(cmlFile);
        cc.detectMultiScale(image, face_detect);
        System.out.println("detect: " + face_detect.toArray().length);
        for (Rect rect : face_detect.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255));
        }
        BufferedImage im = MatToBufferedImage(image);
        return im;
    }

    public static BufferedImage MatToBufferedImage(Mat frame) {
        //Mat() to BufferedImage
        int type = 0;
        if (frame.channels() == 1) {
            type = BufferedImage.TYPE_BYTE_GRAY;
        } else if (frame.channels() == 3) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        BufferedImage image = new BufferedImage(frame.width(), frame.height(), type);
        WritableRaster raster = image.getRaster();
        DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
        byte[] data = dataBuffer.getData();
        frame.get(0, 0, data);
        return image;
    }

    public BufferedImage objectDetect(Mat image) {
        BufferedImage im = MatToBufferedImage(image);;
        try {
            List<String> classes = Files.readAllLines(Paths.get("xml/coconames.txt"));
            Net net = Dnn.readNetFromDarknet("xml/yolov4.cfg", "xml/yolov4.weights");
            DetectionModel model = new DetectionModel(net);
            model.setInputParams(1 / 255.0, new Size(416, 416), new Scalar(0), true);
            MatOfInt classIds = new MatOfInt();
            MatOfFloat scores = new MatOfFloat();
            MatOfRect boxes = new MatOfRect();
            model.detect(image, classIds, scores, boxes, 0.6f, 0.4f);
            for (int i = 0; i < classIds.rows(); i++) {
                Rect box = new Rect(boxes.get(i, 0));
                Imgproc.rectangle(image, box, new Scalar(0, 255, 0), 2);
                int classId = (int) classIds.get(i, 0)[0];
                double score = scores.get(i, 0)[0];
                String text = String.format("%s: %.2f", classes.get(classId), score);
                Imgproc.putText(image, text, new Point(box.x, box.y - 5),
                        Imgproc.FONT_HERSHEY_SIMPLEX, 1, new Scalar(0, 255, 0), 2);
            }
            im = MatToBufferedImage(image);
            return im;
        } catch (IOException e) {
            // TODO Auto-generated catch block
//            e.printStackTrace();
            System.err.println("Lỗi không tìm thấy file xml/coconames.txt");
        }
        return im;
    }

}
