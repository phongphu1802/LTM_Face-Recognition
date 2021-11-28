/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltm_face_recognition;

import ltm_face_recognition.Face_Recognition;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import org.json.simple.parser.ParseException;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
/**
 *
 * @author LAPTOPTOKYO
 */
public class Camera extends JFrame{
    private JLabel cameraScreen;
    private JButton btnCapture;
    private VideoCapture capture;
    private Mat image;
    private Face_Recognition fa;
    private boolean clicked = false;
    public Camera(){
        setLayout(null);    
        cameraScreen = new JLabel();
        cameraScreen.setBounds(0, 0, 640, 480);
        add(cameraScreen);
        
        btnCapture = new JButton("capture");
        btnCapture.setBounds(300, 480, 80, 40);
        add(btnCapture);
        
        btnCapture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                clicked = true;
            }
        });
        
        setSize(new Dimension(640,560));
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }
    
    public void startCamera() throws InterruptedException, ParseException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, NoSuchPaddingException, NoSuchAlgorithmException{
        capture = new VideoCapture(0);
        image = new Mat();
        byte[] imageData;
        ImageIcon icon;
        
        while(true){
            capture.read(image);      
            if(!capture.isOpened()){
                System.out.println("Error");
            }
            else {   
                    if (capture.read(image)){
                        final MatOfByte buf = new MatOfByte();
                        Imgcodecs.imencode(".jpg", image, buf);
                        imageData = buf.toArray();
                        icon = new ImageIcon(imageData);           
                        cameraScreen.setIcon(icon);
                        if(clicked){
                        	System.out.println("Click");
//                          String name = JOptionPane.showInputDialog(this,"Enter image name");
//                          if(name==null){
//                              name = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
//                          }
//                          
//                          Imgcodecs.imwrite("images/"+name+".jpg", image);
                        	try {
                                                      
    							/*Thread.sleep(3000);
    							byte[] data = new byte[1000];
    					        int count = dis.read(data);
    					        real =new byte[count+1];
    					        for(int i=1;i<=count;i++)
    					        {real[i]=data[i];}
    					        System.out.println(real.toString());
    					        Mat newmat = Imgcodecs.imdecode(new MatOfByte(real), Imgcodecs.IMREAD_UNCHANGED);	
                        		fa.loadAnh(newmat);
    	                          clicked = false;
    	                          setVisible(false);
    	                          dout.close();
    	              	        dis.close();
    	              	        socket.close();*/
                                           fa= new Face_Recognition();
                                            fa.loadAnh(image);
                                            clicked = false;
                                            setVisible(false);
    	                          dispose();  
    	                          
    						}catch(SocketTimeoutException ex){
    							System.out.println("hi");
    						} 
                        	catch (IOException e) {
    							// TODO Auto-generated catch block
    							e.printStackTrace();
    						}
                        	
                                      
                    }
                }
                
                
            }
           
            }
        }

    
//    public static void main(String args[]) {
//        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                Camera camera = new Camera();
//                
//                new Thread(new Runnable(){
//                    @Override
//                    public void run(){
//                        camera.startCamera();
//                    }
//                }).start();
//            }
//        });
//    }
}
