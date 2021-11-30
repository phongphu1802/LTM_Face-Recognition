package server;

import Controller.DangKyController;
import Controller.DangNhapController;
import ltm.server.AES;
import ltm.server.RSA;

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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    private static String privateKey = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAKAUZV+tjiNBKhlBZbKBnzeugpdYPhh5PbHanjV0aQ+LF7vetPYhbTiCVqA3a+Chmge44+prlqd3qQCYra6OYIe7oPVq4mETa1c/7IuSlKJgxC5wMqYKxYydb1eULkrs5IvvtNddx+9O/JlyM5sTPosgFHOzr4WqkVtQ71IkR+HrAgMBAAECgYAkQLo8kteP0GAyXAcmCAkA2Tql/8wASuTX9ITD4lsws/VqDKO64hMUKyBnJGX/91kkypCDNF5oCsdxZSJgV8owViYWZPnbvEcNqLtqgs7nj1UHuX9S5yYIPGN/mHL6OJJ7sosOd6rqdpg6JRRkAKUV+tmN/7Gh0+GFXM+ug6mgwQJBAO9/+CWpCAVoGxCA+YsTMb82fTOmGYMkZOAfQsvIV2v6DC8eJrSa+c0yCOTa3tirlCkhBfB08f8U2iEPS+Gu3bECQQCrG7O0gYmFL2RX1O+37ovyyHTbst4s4xbLW4jLzbSoimL235lCdIC+fllEEP96wPAiqo6dzmdH8KsGmVozsVRbAkB0ME8AZjp/9Pt8TDXD5LHzo8mlruUdnCBcIo5TMoRG2+3hRe1dHPonNCjgbdZCoyqjsWOiPfnQ2Brigvs7J4xhAkBGRiZUKC92x7QKbqXVgN9xYuq7oIanIM0nz/wq190uq0dh5Qtow7hshC/dSK3kmIEHe8z++tpoLWvQVgM538apAkBoSNfaTkDZhFavuiVl6L8cWCoDcJBItip8wKQhXwHp0O3HLg10OEd14M58ooNfpgt+8D8/8/2OOFaR0HzA+2Dm";
    private String encodeSecretKey ="";
    private RSA rsa;
    private AES aes;
    private SecretKey key;
   
    public Server(int port) {  	   	
	try {
            System.loadLibrary( Core.NATIVE_LIBRARY_NAME );
	    server = new ServerSocket(port);		
            while(true){
                System.out.println("server running.....");	
                socket = server.accept();
                rsa = new RSA();
                aes = new AES();
                while(true) {
                    String cipherText=Request();
                    encodeSecretKey=rsa.decrypt(cipherText, privateKey);
                    byte[] decodedKey = Base64.getDecoder().decode(encodeSecretKey);
                    key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
                    System.out.println(key);
                    if(key!=null) {
                        break;
                    }
                }
                switch(aes.decrypt(in.readLine(),key)){
                    case "login":{
                        System.out.println("login");
                        Login();
                        break;
                    }
                    case "register":{
                        System.out.println("register");
                        Register();
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
    
    public void Login() throws IOException{
        JSONParser parser = new JSONParser();
        JSONObject object = null;
        try {
            String rse=in.readLine();
            object = (JSONObject)parser.parse(aes.decrypt(rse,key));
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
                    Reply(aes.encrypt("1"));
                    break;
                }
                case "2":{
                    Reply(aes.encrypt("2"));
                    break;
                }
                case "3":{
                    //Trả về thông báo đăng nhập thành công
                    Reply(aes.encrypt("3"));
                    //Trả về thông tin người dùng
                    Reply(aes.encrypt(kiemtra.Select_User().toString()));
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void Register(){
        try {
            JSONParser parser = new JSONParser();
            JSONObject object = null;
            try {
                String rse=in.readLine();
                object = (JSONObject) parser.parse(aes.decrypt(rse,key));
                System.out.println(object.toString());
            } catch (ParseException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
            //Tách User Pass khỏi Json
            String user = (String) object.get("user");
            String pass = (String) object.get("pass");
            String lastName = (String) object.get("lastname");
            String firstName = (String) object.get("firstname");
            String date_Of_Birth = (String) object.get("date_of_birth");
//            System.out.println(user+"#"+pass+"#"+lastName+"#"+firstName+"#"+date_Of_Birth);
            
            DangKyController kiemtra = new DangKyController();
            switch(kiemtra.DangKyController(user, pass, lastName, firstName, date_Of_Birth)){
                case "0":{
                    Reply(aes.encrypt("0"));
                    break;
                }
                case "1":{
                    Reply(aes.encrypt("1"));
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
            String line = aes.decrypt(in.readLine(), key);
             byte[] imageAr = Base64.getDecoder().decode(line);
            bimg = ImageIO.read(new ByteArrayInputStream(imageAr));
            if(bimg!=null){
                BufferedImage mat =face_detect.detect(bufferedImageToMat(bimg));
                ImageIO.write(mat, "jpg", byteArrayOutputStream);
                if(byteArrayOutputStream!=null) { 
                	String byteImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());
                	String reply = aes.encrypt(byteImage);
                    Reply(reply);
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
    
    //Dọc key Publickey
    public String ReadPublicKey(){
        FileInputStream fileInputStream = null;
        String spilit="";
        try {
            fileInputStream = new FileInputStream("src\\testrsa\\publickey.txt");
            Scanner scanner = new Scanner(fileInputStream);
            
            while (scanner.hasNextLine()) {
                spilit+=scanner.nextLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return spilit;
    }
    
    public String ReadPrivateKey(){
        FileInputStream fileInputStream = null;
        String spilit="";
        try {
            fileInputStream = new FileInputStream("src\\testrsa\\privatekey.txt");
            Scanner scanner = new Scanner(fileInputStream);
            
            while (scanner.hasNextLine()) {
                spilit+=scanner.nextLine();
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fileInputStream.close();
            } catch (IOException ex) {
                Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return spilit;
    }
}
