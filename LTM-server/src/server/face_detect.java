package server;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;

import javax.swing.ImageIcon;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
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
		System.out.println("detect: "+face_detect.toArray().length);
		for(Rect rect: face_detect.toArray()) {
			Imgproc.rectangle(image,new Point(rect.x,rect.y),new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,0,255) ); 
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
}



