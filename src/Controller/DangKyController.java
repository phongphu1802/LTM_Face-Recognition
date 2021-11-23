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
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
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
    Socket socket = null;
    BufferedWriter out = null;
    BufferedReader in = null;
    
    public DangKyController(String host, int port){
        try{
            socket = new Socket(host, port);
            out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }catch(Exception e){
            System.err.println(e);
        }
    }
    public String DangKyController(String strTaiKhoan,String strMatKhau, String strNhapLaiMatKhau, String strHo, String strTen, String strNgaySinh){
        String Result="";
        String line = "";
        //Kiểm tra dữ liệu rỗng
        if(!strTaiKhoan.equals("")&&!strMatKhau.equals("")&&!strNhapLaiMatKhau.equals("")&&!strHo.equals("")&&!strTen.equals("")&&!strNgaySinh.equals("")){
            //Kiểm tra mật khẩu nhập lại
            if(strMatKhau.equals(strNhapLaiMatKhau)){
                //Kiểm tra họ không được có số
                if(Pattern.matches("[a-zA-Z]+", strHo)){
                    //Kiểm tra tên không được có số
                    if(Pattern.matches("[a-zA-Z]+", strTen)){
                        try{
                            //Gửi dữ liệu đi
                            out.write("register");
                            out.newLine();
                            out.flush();
                            JSONObject object= new JSONObject();
                            object.put("user", strTaiKhoan);
                            object.put("pass", strMatKhau);
                            object.put("lastname", strHo);
                            object.put("firstname", strTen);
                            object.put("date_of_birth", strNgaySinh);
                            System.out.println(object);
                            out.write(object.toString());
                            out.newLine();
                            out.flush();
                            //Bắt dữ liệu về
                            line = in.readLine();
                            System.out.println("Server sent: " + line);
                            switch(line){
                                case "0":{
                                    JOptionPane.showMessageDialog(null,"Tài khoản "+strTaiKhoan+" đăng ký thành công.");
                                    Result = "Tài khoản đăng ký thành công.";
                                    break;
                                }
                                case "1":{
                                    JOptionPane.showMessageDialog(null,"Tài khoản "+strTaiKhoan+" đăng ký đã có người dùng vui lòng chọn tên tài khoản khác.");
                                    Result = "Tài khoản "+strTaiKhoan+" đăng ký đã có người dùng vui lòng chọn tên tài khoản khác.";
                                    break;
                                }
                            }
                            //Đóng kết nối
                            in.close();
                            socket.close();
                        }catch(Exception e){
                            System.err.println(e);
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
}
