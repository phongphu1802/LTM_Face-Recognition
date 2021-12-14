/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ltm_face_recognition;

import Cipher.AES;
import Cipher.RSA;
import ltm_face_recognition.Face_Recognition;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
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
    private SecretKey secretKey;
    private boolean clicked = false;
    private RSA rsa;
    private AES aes;
    private Socket socket;
    private String id="";
    private String LastName="";
    private String NameUser="";
    private String Date_of_birth="";
    
    public Camera(Socket socket,SecretKey secretKey,String id){
    	this.socket = socket;
    	this.id = id;
    	this.secretKey = secretKey;
        setLayout(null);    
        cameraScreen = new JLabel();
        cameraScreen.setBounds(0, 0, 640, 480);
        add(cameraScreen);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {         
                try {
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    writer.write(aes.encrypt("DEAD"));
                    writer.newLine();
                    writer.flush();
                    System.exit(0);
                } catch (IOException ex) {
                    Logger.getLogger(Face_Recognition.class.getName()).log(Level.SEVERE, null, ex);
                } finally {

                }
            }
        });
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
    public void Start(String id,String LastName, String NameUser, String Date_of_birth){
    	this.id = id;
        this.LastName=LastName;
        this.NameUser=NameUser;
        this.Date_of_birth=Date_of_birth;
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
//                       String name = JOptionPane.showInputDialog(this,"Enter image name");
//                       if(name==null){
//                          name = new SimpleDateFormat("yyyy-mm-dd-hh-mm-ss").format(new Date());
//                       }
//                       Imgcodecs.imwrite("images/"+name+".jpg", image);
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
                            fa= new Face_Recognition(this.socket,this.secretKey, getId());
                            fa.Start(getId(), getLastName(), getNameUser(), getDate_of_birth());
                            fa.loadAnh(image);
                            clicked = false;
                            setVisible(false);
                            dispose();        
                        }catch(SocketTimeoutException ex){
                            System.out.println("Lỗi Start camera và SocketTimeout");
                        } 
                        catch (IOException e) {
                            // TODO Auto-generated catch block
                            System.out.println("Lỗi Start camera IOException");
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
    
    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the LastName
     */
    public String getLastName() {
        return LastName;
    }

    /**
     * @param LastName the LastName to set
     */
    public void setLastName(String LastName) {
        this.LastName = LastName;
    }

    /**
     * @return the NameUser
     */
    public String getNameUser() {
        return NameUser;
    }

    /**
     * @param NameUser the NameUser to set
     */
    public void setNameUser(String NameUser) {
        this.NameUser = NameUser;
    }

    /**
     * @return the Date_of_birth
     */
    public String getDate_of_birth() {
        return Date_of_birth;
    }

    /**
     * @param Date_of_birth the Date_of_birth to set
     */
    public void setDate_of_birth(String Date_of_birth) {
        this.Date_of_birth = Date_of_birth;
    }
}
