/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import Cipher.AES;
import Cipher.RSA;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.JOptionPane;
import org.json.simple.JSONObject;

/**
 *
 * @author LAPTOPTOKYO
 */
public class ThemAnhController {
    BufferedWriter out = null;
    BufferedReader in = null;
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    private RSA rsa;
    private AES aes;
    private Socket socket;
    private SecretKey secretKey;
    public void disconnectServer() throws IOException {
    	socket.close();
    }
    
    public ThemAnhController(Socket socket, SecretKey SecretKey){
        this.socket=socket;
        this.secretKey=SecretKey;
    }
    
    public void ThemAnhController(BufferedImage iCon, String iduser){
        String Result="";
        String line = ""; 
        if(socket!=null&&socket.isConnected()) {
            try {
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //L???nh th??m
                out.write(aes.encrypt("add"));
                out.newLine();
                out.flush();
                //G???i ???nh
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(iCon, "jpg", byteArrayOutputStream);//image to byte[]
		String byteImage = Base64.getEncoder().encodeToString(byteArrayOutputStream.toByteArray());//encode image to string
//                byte[] decordedValue = new BASE64Decoder().decodeBuffer(encryptedData);
//                byte[] decValue = .doFinal(decordedValue);
//                String decryptedValue = new String(decValue);
                out.write(aes.encrypt(byteImage));
                out.newLine();
                out.flush();
                //G???i m?? user
                out.write(aes.encrypt(iduser));
                out.newLine();
                out.flush();
                //B???t d??? li???u v???
                line = aes.decrypt(in.readLine(), secretKey);
                System.out.println("Server sent: " + line);
                switch(line){
                    case "0":{
                        JOptionPane.showMessageDialog(null,"Th??m ???nh kh??ng th??nh c??ng.");
                        break;
                    }
                    case "1":{
                        JOptionPane.showMessageDialog(null,"Th??m ???nh m???i v??o th??nh c??ng.");
                        break;
                    }
                    case "2":{
                        JOptionPane.showMessageDialog(null,"???nh th??m v??o kh??ng kh???p v???i ???nh ban ?????u.");
                        break;
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(ThemAnhController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }   
    }
}
