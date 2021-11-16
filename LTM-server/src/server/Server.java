package server;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import org.opencv.core.Core;
import org.opencv.core.CvType;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class Server {

		 public ServerSocket server;
			Socket socket;
	DataInputStream dis;
	DataOutputStream dout;
	BufferedImage bimg;
	byte[] real=null;
	int bytesRead;
	public Server(int port) {  	   	
	     	try {
                        System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	     		server = new ServerSocket(port);
			server.setSoTimeout(30000);
			System.out.println("server running.....");		
			socket = server.accept();
                       while(!server.isClosed()){
                            InputStream inputStream = socket.getInputStream();
                            OutputStream outputStream = socket.getOutputStream();
			byte[] sizeAr = new byte[4];
                        inputStream.read(sizeAr);
                        int size = ByteBuffer.wrap(sizeAr).asIntBuffer().get();
                        byte[] imageAr = new byte[size];
                        inputStream.read(imageAr);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bimg = ImageIO.read(new ByteArrayInputStream(imageAr));
                        if(bimg!=null){
                             System.out.println("in process");
                             BufferedImage mat =face_detect.detect(bufferedImageToMat(bimg));
                             ByteArrayOutputStream byteArrayOutputStream2= new ByteArrayOutputStream();
                             ImageIO.write(mat, "jpg", byteArrayOutputStream2);
                             byte[] size2 = ByteBuffer.allocate(4).putInt(byteArrayOutputStream2.size()).array();
                             outputStream.write(size2);
                             outputStream.write(byteArrayOutputStream2.toByteArray());
                             outputStream.flush();
                             //ImageIO.write(mat, "jpg", out);
                        }
			
                       }
                       } catch (IOException e) {
				e.printStackTrace();

			}catch(Exception ex) {
				ex.printStackTrace();
			}
			
	}
	
	public static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
}			
}
