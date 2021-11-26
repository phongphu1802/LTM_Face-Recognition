package server;

import Controller.DangNhapController;
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
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.opencv.core.Core;
import org.opencv.core.CvType;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

public class Server {

    public ServerSocket server;
    Socket socket;
    ConnectAPI connectAPI;
    BufferedReader in=null;
    BufferedWriter out = null;
    public Server(int port) {  	   	
	try {
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	    server = new ServerSocket(port);
            server.setSoTimeout(30000);		
                while(true){
                    System.out.println("server running.....");	
                    socket = server.accept();
                   
                    switch(Request()){
                    case "login":{
                        Login();
                        break;
                    }
                    case "Camera":{
                        face_detect();
                        break;
                    }
                }
                socket.close();
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        }		
    }
	
    public static Mat bufferedImageToMat(BufferedImage bi) {
        Mat mat = new Mat(bi.getHeight(), bi.getWidth(), CvType.CV_8UC3);
        byte[] data = ((DataBufferByte) bi.getRaster().getDataBuffer()).getData();
        mat.put(0, 0, data);
        return mat;
    }
    
    public void Login(){
        JSONParser parser = new JSONParser();
        JSONObject object = null;
        try {
            object = (JSONObject)parser.parse(Request());
        } catch (ParseException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        //Tách User Pass khỏi Json
        String User = (String) object.get("user");
        String Pass = (String) object.get("pass");
        DangNhapController kiemtra = new DangNhapController();
        try {
            switch(kiemtra.DangNhapController(User, Pass)){
                case "1":{
                    Reply("1");
                    break;
                }
                case "2":{
                    Reply("2");
                    break;
                }
                case "3":{
                    //Trả về thông báo đăng nhập thành công
                    Reply("3");
                    //Trả về thông tin người dùng
                    Reply(kiemtra.Select_User().toString());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void face_detect() throws InterruptedException, ParseException{
        System.out.println("im in face_detect");
        try {
        	ByteArrayOutputStream byteArrayOutputStream= new ByteArrayOutputStream();
            BufferedImage bimg;
            String line = in.readLine();
             byte[] imageAr = Base64.getDecoder().decode(line);
            bimg = ImageIO.read(new ByteArrayInputStream(imageAr));
            if(bimg!=null){
                BufferedImage mat =face_detect.detect(bufferedImageToMat(bimg));;
                ImageIO.write(mat, "jpg", byteArrayOutputStream);
                if(byteArrayOutputStream!=null) { 
                	String byteImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
                	 System.out.println(byteImage);
                    Reply(byteImage);
                }
               
            }
            //in.close();
           // out.close();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
           
        }
    }
    
    //Thực thi nhận request
    public String Request(){
        String request="";
        try {
        	 in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            request=in.readLine();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
        return request;
    }
    
    //Thực thi gửi request về client
    public void Reply(String stResult){
        BufferedWriter out = null;
        try {
        	 out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(stResult);
            out.newLine();
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public String Regconize() {
    	
    	return null;
    }
}
