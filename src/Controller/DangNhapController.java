/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.sql.ResultSet;
import static java.time.Clock.system;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    Socket socket = null;
    BufferedWriter out = null;
    BufferedReader in = null;
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCgFGVfrY4jQSoZQWWygZ83roKXWD4YeT2x2p41dGkPixe73rT2IW04glagN2vgoZoHuOPqa5and6kAmK2ujmCHu6D1auJhE2tXP+yLkpSiYMQucDKmCsWMnW9XlC5K7OSL77TXXcfvTvyZcjObEz6LIBRzs6+FqpFbUO9SJEfh6wIDAQAB";
    public DangNhapController(String host, int port){
        try{
            socket = new Socket(host, port);
            System.out.println("Client connected");
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(Exception e){
            System.err.println(e);
        }
    }
    public String DangNhapController(String strTaiKhoan,String strMatKhau){
        String Result="";
        String line = "";
        try{
            //Gửi dữ liệu đi
            out.write("login");
            out.newLine();
            out.flush();
            JSONObject object= new JSONObject();
            object.put("user", strTaiKhoan);
            object.put("pass", strMatKhau);
            out.write(object.toString());
            out.newLine();
            out.flush();
            //Bắt dữ liệu về
            line = in.readLine();
            System.out.println("Server sent: " + line);
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
                        object1 = (JSONObject) parser.parse(in.readLine());
                        System.out.println(object1.get("LastName"));
                        System.out.println(object1.get("NameUser"));
                        //System.out.println(object1.get("Date_of_birth"));
                    } catch (Exception ex) {
                        Logger.getLogger(DangNhapController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    Face_Recognition face = new Face_Recognition();
                    face.Start((String) object1.get("LastName"), (String) object1.get("NameUser"), (String) object1.get("Date_of_birth"));
                    face.setVisible(true);
                    Result = "Đăng nhập thành công";
                    break;
                }
            }
            //Đóng kết nối
            in.close();
            socket.close();
        }catch(Exception e){
            System.err.println(e);
        }
        return Result;
    }
}
