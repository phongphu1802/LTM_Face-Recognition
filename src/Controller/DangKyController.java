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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javax.crypto.SecretKey;
import javax.swing.JOptionPane;
import ltm_face_recognition.Face_Recognition;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * @author LAPTOPTOKYO
 */
public class DangKyController {
    BufferedWriter out = null;
    BufferedReader in = null;
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    private RSA rsa;
    private AES aes;
    private Socket socket;
    private SecretKey secretKey;
    
    public Socket ConnectServer(String host,int port) {
    	try {
            setSocket(new Socket(host,port));
            rsa = new RSA();
            aes = new AES();
            return getSocket();
	}catch(UnknownHostException ex) {
            ex.printStackTrace();
	}catch (IOException e) {
            // TODO: handle exception
            JOptionPane.showMessageDialog(null, "Server is busy right now");
	}
    	return null;
    }
    public void disconnectServer() throws IOException {
    	getSocket().close();
    }
    
    public DangKyController(){

    }
    
    public String DangKyController(String host, int port,String strTaiKhoan,String strMatKhau, String strNhapLaiMatKhau, String strHo, String strTen, String strNgaySinh){
        String Result="";
        String line = "";
        //Kiểm tra dữ liệu rỗng
        if(!strTaiKhoan.equals("")&&!strMatKhau.equals("")&&!strNhapLaiMatKhau.equals("")&&!strHo.equals("")&&!strTen.equals("")&&!strNgaySinh.equals("")){
            //Kiểm tra mật khẩu nhập lại
            if(strMatKhau.equals(strNhapLaiMatKhau)){
                //Kiểm tra họ không được có số
                if(Pattern.matches("^\\D+$", strHo)){
                    //Kiểm tra tên không được có số
                    if(Pattern.matches("^\\D+$", strTen)){
                        if(calculateAge(strNgaySinh)>18&&calculateAge(strNgaySinh)<100){
                            try{
                                setSocket(ConnectServer(host, port));  
                                if(getSocket()!=null&&getSocket().isConnected()) {
                                    out = new BufferedWriter(new OutputStreamWriter(getSocket().getOutputStream()));
                                    in = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
                                    //encrypt
                                    setSecretKey(aes.generatorKey());
                                    String cipherText1 = Base64.getEncoder().encodeToString(getSecretKey().getEncoded());
                                    String RSAEncrypt = Base64.getEncoder().encodeToString(rsa.encrypt(cipherText1, publicKey));
                                    //send secret key
                                    out.write(RSAEncrypt);
                                    out.newLine();
                                    out.flush();
                                    //Gửi dữ liệu đi
                                    out.write(aes.encrypt("register"));
                                    out.newLine();
                                    out.flush();
                                    JSONObject object= new JSONObject();
                                    object.put("user", strTaiKhoan);
                                    object.put("pass", strMatKhau);
                                    object.put("lastname", strHo);
                                    object.put("firstname", strTen);
                                    object.put("date_of_birth", strNgaySinh+"T08:51:10.1225942+07:00");
                                    System.out.println(object);
                                    out.write(aes.encrypt(object.toString()));
                                    out.newLine();
                                    out.flush();
                                    //Bắt dữ liệu về
                                    line = aes.decrypt(in.readLine(), getSecretKey());
                                    System.out.println("Server sent: " + line);
                                    switch(line){
                                        case "0":{
                                            JOptionPane.showMessageDialog(null,"Tài khoản "+strTaiKhoan+" đăng ký thành công.");
                                            Result = "Tài khoản đăng ký thành công.";
                                            out.write(aes.encrypt("DEAD"));
                                            out.newLine();
                                            out.flush();
                                            break;
                                        }
                                        case "1":{
                                            JOptionPane.showMessageDialog(null,"Tài khoản "+strTaiKhoan+" đăng ký đã có người dùng vui lòng chọn tên tài khoản khác.");
                                            Result = "Tài khoản "+strTaiKhoan+" đăng ký đã có người dùng vui lòng chọn tên tài khoản khác.";
                                            break;
                                        }
                                    }
                                }
                                //Đóng kết nối
                                disconnectServer();
                            }catch(Exception e){
                                System.err.println(e);
                            }
                        }else{
                            JOptionPane.showMessageDialog(null,"Tuổi phải lớn hơn 18 và bé hơn 100!!!");
                            Result="Tuổi phải lớn hơn 18 và bé hơn 100.";
                        }
                    }else{
                        JOptionPane.showMessageDialog(null,"Tên không được có số!!!");
                        Result="Tên không được có số.";
                    }
                }else{
                    JOptionPane.showMessageDialog(null,"Họ không được có số!!!");
                    Result="Họ không được có số.";
                }
            }else{
                JOptionPane.showMessageDialog(null,"Mật khẩu nhập lại không giống nhau!!!");
                Result="Mật khẩu nhập lại không giống nhau.";
            }
        }else{
            JOptionPane.showMessageDialog(null,"Dữ liệu không được rỗng!!!");
            Result="Dữ liệu không được rỗng.";
        }
        return Result;
    }
    
    /**
     * @return the socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     * @param socket the socket to set
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * @return the secretKey
     */
    public SecretKey getSecretKey() {
        return secretKey;
    }

    /**
     * @param secretKey the secretKey to set
     */
    public void setSecretKey(SecretKey secretKey) {
        this.secretKey = secretKey;
    }
    //Thực thi gửi request về client
    public void Reply(String stResult){
        try {
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            out.write(stResult);
            out.newLine();
            out.flush();
        } catch (IOException ex) {
            Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public String Encrypt(String stResult){
        return aes.encrypt(stResult);
    }
    
    public static long calculateAge(String birthDate) {
        LocalDate localDate1 = LocalDate.parse(birthDate,DateTimeFormatter.ISO_LOCAL_DATE);
        LocalDate localDate2 = LocalDate.now();
        long years = java.time.temporal.ChronoUnit.YEARS.between( localDate1 , localDate2 );
        return years;
    }
}
