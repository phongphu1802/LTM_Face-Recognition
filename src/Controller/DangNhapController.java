/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Cipher.AES;
import Cipher.RSA;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import static java.time.Clock.system;
import java.util.ArrayList;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import ltm_face_recognition.Face_Recognition;


/**
 *
 * @author LAPTOPTOKYO
 */
public class DangNhapController {
    BufferedWriter out = null;
    BufferedReader in = null;
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    private RSA rsa;
    private AES aes;
    private Socket socket=null;
    private SecretKey secretKey;
    
    public Socket ConnectServer(String host,int port) {
    	try {
            socket = new Socket(host,port);
            rsa = new RSA();
            aes = new AES();
            return socket;
	}catch(UnknownHostException ex) {
            ex.printStackTrace();
	}catch (IOException e) {
            // TODO: handle exception
            JOptionPane.showMessageDialog(null, "Server is busy right now");
	}
    	return null;
    }
    public void disconnectServer() throws IOException {
    	socket.close();
    }
    
    public DangNhapController(){
//        try{
//            socket = ConnectServer(host, port);  
//        }catch(Exception e){
//            System.err.println(e);
//        }
    }
    public String DangNhapController(String host, int port, String strTaiKhoan,String strMatKhau){
        socket = ConnectServer(host, port);
        String Result="";
        String line = "";
        try{
            if(socket!=null&&socket.isConnected()) {
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        	//encrypt
        	secretKey = aes.generatorKey();
//        	System.out.println(secretKey);
        	String cipherText1 = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        	String RSAEncrypt = Base64.getEncoder().encodeToString(rsa.encrypt(cipherText1, publicKey));
        	//send secret key
        	out.write(RSAEncrypt);
        	out.newLine();
        	out.flush();
                //Gửi dữ liệu đi
                out.write(aes.encrypt("login"));
                out.newLine();
                out.flush();
                JSONObject object= new JSONObject();
                object.put("user", strTaiKhoan);
                object.put("pass", strMatKhau);
                out.write(aes.encrypt(object.toString()));
                out.newLine();
                out.flush();
                System.out.println("Đã gửi");
                //Bắt dữ liệu về
                String RS= in.readLine();
                line = aes.decrypt(RS, secretKey);
//                System.out.println("Server sent: " + line+"\n"+RS);
                switch(line){
                    case "1":{
                        JOptionPane.showMessageDialog(null,"Tài khoản "+strTaiKhoan+" không tồn tại.");
                        Result = "Tài khoản "+strTaiKhoan+" không tồn tại.";
                        break;
                    }
                    case "2":{
                        JOptionPane.showMessageDialog(null,"Sai mật khẩu.");
                        Result = "Sai mật khẩu";
                        break;
                    }
                    case "3":{
                        JSONObject object1=null;
                        JSONParser parser = new JSONParser();
                        try {
                            object1 = (JSONObject) parser.parse(aes.decrypt(in.readLine(), secretKey));
                        } catch (Exception ex) {
                            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        Face_Recognition face = new Face_Recognition(socket,secretKey,(String)object1.get("userID"));
                        face.Start((String)object1.get("userID"),(String) object1.get("LastName"), (String) object1.get("NameUser"), (String) object1.get("Date_of_birth"));
                        face.setVisible(true);
                        Result = "Đăng nhập thành công";
                        break;
                    }
                }
            }
           // disconnectServer();
        }catch(Exception e){
            System.err.println(e);
        }
        return Result;
    }
}
